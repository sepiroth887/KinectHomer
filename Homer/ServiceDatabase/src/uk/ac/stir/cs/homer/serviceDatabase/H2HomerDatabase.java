package uk.ac.stir.cs.homer.serviceDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.ConnectionPoolDataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.serviceDatabase.objects.Action;
import uk.ac.stir.cs.homer.serviceDatabase.objects.ActionEnvironEffect;
import uk.ac.stir.cs.homer.serviceDatabase.objects.ActionEnvironEffects;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Condition;
import uk.ac.stir.cs.homer.serviceDatabase.objects.DBObject;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Environ;
import uk.ac.stir.cs.homer.serviceDatabase.objects.EnvironEffect;
import uk.ac.stir.cs.homer.serviceDatabase.objects.IDsForSystemDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.IDsForUserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Location;
import uk.ac.stir.cs.homer.serviceDatabase.objects.LocationContext;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Log;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Trigger;
import uk.ac.stir.cs.homer.serviceDatabase.objects.User;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.BuiltQuery;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.LoggerQueryObject;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryBuilder;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;

public class H2HomerDatabase implements HomerDatabase {

	private static final String DB_PATH = "uk.ac.stir.cs.homer.database.dbpath";
	private static final String QUERY_PATH = "uk.ac.stir.cs.homer.database.querypath";
	
	private static final boolean ADD_FULL_SAMPLE_INFORMATION = Boolean.valueOf("uk.ac.stir.cs.homer.database.initalise.addFullInformation");
	private static final boolean ADD_BASIC_SAMPLE_INFORMATION = Boolean.valueOf("uk.ac.stir.cs.homer.database.initalise.addBasicInformation");
	
	
	private static final Logger logger = LoggerFactory.getLogger(H2HomerDatabase.class);
	private static final int MAX_CONNECTIONS = 16;
	private static final boolean addImages = true;
	
	private int currentImageIndex=0;
	
	private MiniConnectionPoolManager connPool;

	private QueryBuilder queryBuilder = new QueryBuilder();
	
	//******** CORE FEATURES ****************************
	public H2HomerDatabase() throws IOException {
		ConnectionPoolDataSource dataSource = createDataSource();
		connPool = new MiniConnectionPoolManager(dataSource,MAX_CONNECTIONS);
		
		try {
			configureDatabase();
			//printDatabase();
		} catch (SQLException e) {
			try {
                connPool.dispose();
            } catch (SQLException e1) {
                logger.error("Failed to dispose database pool when recovering from configuring database", e1);
            }

            IOException wrapper = new IOException("Failed to configure database properly");
            wrapper.initCause(e);
            throw wrapper;
		} catch(RuntimeException e2)
		{
			e2.printStackTrace();

		}
	}

	public void shutdown() {
		try {
			connPool.dispose();
		} catch (SQLException e) {
			logger.error("Failed to tear down connection pool", e);
		}
	}
	
	protected ConnectionPoolDataSource createDataSource() {
		if(!loadDbDriver("org.h2.Driver")) {
			throw new RuntimeException("Unable to loadDB driver: org.h2.Driver");
		}
		JdbcDataSource source = new JdbcDataSource();
		source.setURL("jdbc:h2:" + getDbPath());
		//System.err.println(source.getURL());
		return source;
	}

	private String getDbPath() {
		String dbPath = System.getProperty(DB_PATH, ".");
		File dbFile = new File(dbPath);
		try
		{
			return dbFile.getCanonicalPath();
		} catch (IOException e)
		{
			return dbFile.getAbsolutePath();
		}
	}
	
	protected boolean loadDbDriver(String dbDriver) {
		try {
			Class.forName(dbDriver).newInstance();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void configureDatabase() throws SQLException {
		Connection conn = null;
		boolean successful = false;
		try {
			
			conn = getConnection();
			//executeCreate(conn, "DROP TABLE Policy");
			executeCreate(conn, getCreateSql("Resources"));
			executeCreate(conn, Environ.SQL_CREATE);
			executeCreate(conn, LocationContext.SQL_CREATE);
			executeCreate(conn, Location.SQL_CREATE);
			executeCreate(conn, UserDeviceType.SQL_CREATE);
			executeCreate(conn, SystemDeviceType.SQL_CREATE);
			executeCreate(conn, getCreateSql("UserTypeToSystemType"));
			executeCreate(conn, Action.SQL_CREATE);
			executeCreate(conn, Condition.SQL_CREATE);
			executeCreate(conn, Trigger.SQL_CREATE);
			executeCreate(conn, UserDevice.SQL_CREATE);
			executeCreate(conn, SystemDevice.SQL_CREATE);
			executeCreate(conn, Policy.SQL_CREATE);
			executeCreate(conn, Log.SQL_CREATE);
			executeCreate(conn, ActionEnvironEffect.SQL_CREATE);
			executeCreate(conn, User.SQL_CREATE);
			
			if (addImages)
			{
				if (executeQueryForStringSet("SELECT id FROM Resources WHERE id=1").length == 0)
				{
					for (File f: new File(System.getProperty("uk.ac.stir.cs.homer.resources.images")).listFiles())
					{
						if (f.getName().toLowerCase().endsWith("png") && f.exists())
						{
							String wholeFilePath = f.getAbsolutePath();
						
							executeSingleInsert(getAddDataSql("addResourceImage"), wholeFilePath.substring(wholeFilePath.lastIndexOf(File.separator) + File.separator.length(), wholeFilePath.lastIndexOf(".png")), wholeFilePath);
							logger.info("Successfully added image to database: " + wholeFilePath );
						}
					}
				}
			}
			String id = executeQueryForSingleString("SELECT MAX(id) FROM Resources");
			currentImageIndex = id==null? 0 : Integer.parseInt(id);
			
			if (ADD_BASIC_SAMPLE_INFORMATION || ADD_FULL_SAMPLE_INFORMATION)
			{
				// check to see if the basic data is not there
				if (executeQueryForStringSet("SELECT LOCATION.id FROM LOCATION").length == 0)
				{
					// must have the basic data before you can add the full data
					executeCreate(conn, getSetupSQL("basicTemplate"));	

					if (ADD_FULL_SAMPLE_INFORMATION)
					{	
						executeCreate(conn, getSetupSQL("fullTemplate"));
					}
				}
				
				// check to see if the environ data is not there
				if (executeQueryForStringSet("SELECT Environ.id FROM Environ").length == 0)
				{
					executeCreate(conn, getSetupSQL("environs"));
				}
			}
			conn.commit();
			
			successful = true;
		} catch (Exception e) {
			throw new RuntimeException("Failed to configure database!", e);
		} finally {
			closeResources(conn, !successful, null);
		}
	}
	private int saveSubImages(File[] files)
	{
		int totalSavedMessages = 0;
		
		for (File f : files)
		{
			if (f.isDirectory())
			{
				totalSavedMessages += saveSubImages(f.listFiles());
			}
			else if (f.isFile() && f.getName().toLowerCase().endsWith(".png"))
			{
				addImage(f);
				totalSavedMessages++;
			}
		}
		return totalSavedMessages;
	}
	private boolean testDatabaseSetUp(Connection conn) {
		try {
			return executeQueryForSingleString(getTestTableSQL("QueryTable"))!=null;
		} catch (SQLException e) {
			return false;
		}
	}

	private Connection getConnection() throws SQLException {
		Connection connection = connPool.getConnection();
		connection.setAutoCommit(false);
		return connection;
	}
	//*****************************************************
	
	
	//**************** SQL Files **************************
	private String getCreateSql(String queryFileName) {
		return getSql("create_table_files/" + queryFileName + "_create");
	}
	private String getQuerySql(String queryFileName) {
		return getSql("query_db_files/" + queryFileName);
	}
	private String getAddDataSql(String queryFileName) {
		return getSql("add_data/" + queryFileName);
	}
	private String getSetupSQL(String queryFileName) {
		return getSql("setup_data/" + queryFileName);
	}
	private String getTestTableSQL(String queryFileName) {
		return getSql("test_table/" + queryFileName);
	}
	
	private String getSql(String queryFileName) {
		String queryFilePath = System.getProperty(QUERY_PATH) + queryFileName + ".sql";
		InputStream qStream;
		
		try {
			qStream = new FileInputStream(new File(queryFilePath));
		} catch (FileNotFoundException e1) {
			throw new RuntimeException("Query file " + queryFilePath + " does not exist!");
		}
		
		try {
			InputStreamReader reader = new InputStreamReader(qStream, "UTF-8");
			
			final char[] buffer = new char[1024];
			StringBuilder out = new StringBuilder();
			int read;
			while((read = reader.read(buffer, 0, buffer.length)) >= 0) {
			  if (read>0) {
			    out.append(buffer, 0, read);
			  }
			}
			
			reader.close();
			return out.toString();
		} catch(IOException e) {
			throw new RuntimeException("Unable to read internal query file!");
		}
	}
	//*****************************************************
	
	//********* Database amendments ***********************
	private void executeCreate(Connection conn, String sql) throws SQLException {
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} finally {
			closeStatement(st);
		}
	}
	
	/**
	 * Attempts to close the common resources used in most DB accesses. If any
	 * exceptions occur, they are logged and ignored.
	 * 
	 * @param st
	 *            if true, an attempt will be made to roll back the connection.
	 */
	private void closeResources(Connection conn, boolean rollBack, ResultSet rs,
			Statement... statements) {
		
		closeResultSet(rs);
		
		if (statements != null) {
			for (Statement st : statements) {
				closeStatement(st);
			}
		}

		if (conn != null) {

			if (rollBack) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					logger.error( "Failed to roll back connection", e);
				}
			}

			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Failed to close connection\n", e);
			}
		}
	}

	private void closeStatement(Statement st) {
		if (st == null)
			return;

		try {
			st.close();
		} catch (SQLException e) {
			logger.error("Failed to close statement", e);
		}
	}

	private void closeResultSet(ResultSet rs) {
		if (rs == null)
			return;

		try {
			rs.close();
		} catch (SQLException e) {
			logger.error("Failed to close result set", e);
		}
	}

	//*******************************************************
	
	//********LOGS********************************
	public String saveLog(Log log)
	{
		try {
			executeSingleInsert(Log.SQL_INSERT, log.getTimestamp(), log.getUserDeviceID(), log.getUserDeviceTypeID(), log.getSystemDeviceId(), log.getSystemDeviceTypeID(), 
					log.getLocationID(), log.getLocationContextId(), log.getTriggerID(), log.getConditionID(), log.getActionID(), log.getParameters(), log.getUserFriendly(), log.getDisplay());
			logger.trace("New Log added to database: " + log.getUserFriendly());
			return executeQueryForSingleString("SELECT MAX(id) FROM Logs;");
		} catch (SQLException e) {
			logger.error("Error adding Log to database: " + log.getUserFriendly(), e);
		}
		return null;
	}
	public Log[] getLogs(LoggerQueryObject queryObj)
	{
		ArrayList<Log> stuff = executeQueryForObjectsFromDatabase(queryBuilder.logQuery(queryObj), Log.class);
		return stuff.toArray(new Log[stuff.size()]);
	}
	//*********************************************
	
	//********** ADD ******************************************
	public String addEnviron(String name, String type, boolean report)
	{
		try {
			String id = executeQueryForSingleString(getQuerySql("getEnvironForName"), name);
			if (id!=null)
				return id;
			
			executeSingleInsert(Environ.SQL_INSERT, name, type, Boolean.toString(report));
			return executeQueryForSingleString(getQuerySql("getEnvironForName"), name);
		} catch (SQLException e) {
			logger.error("Failed to add new environ "+name+":" + e);
		}
		return "";
	}
	public void addActionEnvironEffects(ActionEnvironEffects actionEnvironEffect)
	{
		ActionEnvironEffect[] existingActionEnvironEffects = getActionEnvironEffects(new QueryObject().userDeviceType(actionEnvironEffect.getUserDeviceTypeID()).systemDeviceType(actionEnvironEffect.getSystemDeviceTypeID()));
		ActionEnvironEffect existingActionEnvironEffect = existingActionEnvironEffects.length>0? existingActionEnvironEffects[0] : null;
		
		if (actionEnvironEffect.getActionIDs().length  == 0)
		{
			if (existingActionEnvironEffect!=null)
			{
				if (!existingActionEnvironEffect.isEmpty())
				{
					HOMER_USE_ONLY_deleteActionEnvironEffects(actionEnvironEffect.getUserDeviceTypeID(), actionEnvironEffect.getSystemDeviceTypeID());
					addActionEnvironEffect(actionEnvironEffect.getUserDeviceTypeID(), actionEnvironEffect.getSystemDeviceTypeID(), "*", "*", "*");
				}
			}
			else
			{
				addActionEnvironEffect(actionEnvironEffect.getUserDeviceTypeID(), actionEnvironEffect.getSystemDeviceTypeID(), "*", "h", "*");
			}
		}
		else
		{
			if (existingActionEnvironEffect!=null)
			{
				HOMER_USE_ONLY_deleteActionEnvironEffects(actionEnvironEffect.getUserDeviceTypeID(), actionEnvironEffect.getSystemDeviceTypeID());				
			}

			for (String actionID : actionEnvironEffect.getActionIDs())
			{
				
				Map<Environ, EnvironEffect> environEffectsForAction = actionEnvironEffect.getEnvironEffectsForAction(actionID);
				for (Environ environ : environEffectsForAction.keySet())
				{
					addActionEnvironEffect(actionEnvironEffect.getUserDeviceTypeID(), actionEnvironEffect.getSystemDeviceTypeID(), 
							actionID, environ.getId(), environEffectsForAction.get(environ).name());
				}
			}
		}
	}
	
	public void addActionEnvironEffect(String userDeviceTypeID, String systemDeviceTypeID, String actionID, String environID, String effect)
	{
		try {
			executeSingleInsert(ActionEnvironEffect.SQL_INSERT, userDeviceTypeID, systemDeviceTypeID, actionID, environID, effect);
		} catch (SQLException e) {
			logger.error("Failed to add new action environ effect:" + e);
			e.printStackTrace();
		}
	}
	
	
	public void addAction(String sysDeviceTypeId, String actionId,
			String description, String jsonParameterData, String image) {
		try {
			executeSingleInsert(Action.SQL_INSERT, actionId, description,jsonParameterData, sysDeviceTypeId, image);
			logger.info("New Action added to database with ID: " + actionId +", description: " + description +", systemDeviceTypeID: " + sysDeviceTypeId + ", parameterData: " + jsonParameterData + " and image: " +image);
		} catch (SQLException e) {
			updateAction(new Action(actionId, description, jsonParameterData, sysDeviceTypeId, image));
			logger.info("Action already exists in database, so updated it with ID: " + actionId +", description: " + description +", systemDeviceTypeID: " + sysDeviceTypeId + ", parameterData: " + jsonParameterData + " and image: " +image);
		}
	}
	public void addCondition(String sysDeviceTypeId, String conditionId,
			String description, String jsonParameterData, String image, String actionIDresultingInThisState) {
		try {
			executeSingleInsert(Condition.SQL_INSERT, conditionId, description, jsonParameterData, sysDeviceTypeId, image, actionIDresultingInThisState);
			logger.info("New Condition added to database with ID: " + conditionId +" , description: " + description + ", parameterData: " + jsonParameterData + " and image: " +image);
		} catch (SQLException e) {
			updateCondition(new Condition(conditionId, description, jsonParameterData, sysDeviceTypeId, image, actionIDresultingInThisState));
			logger.info("Condition: " + description + " already exists in the database, updated instead.");
		}
	}
	public void addTrigger(String sysDeviceTypeId, String triggerId,
			String description, String jsonParameterData, String beforeImage, String afterImage) {
		try {
			executeSingleInsert(Trigger.SQL_INSERT, triggerId, description, jsonParameterData, sysDeviceTypeId, beforeImage, afterImage);
			logger.info("New Trigger added to database with ID: " + triggerId +" , description: " + description + ", parameterData: " + jsonParameterData + ", before image: " +beforeImage +" and after image: "+ afterImage);
		} catch (SQLException e) {
			updateTrigger(new Trigger(triggerId, description, jsonParameterData, sysDeviceTypeId, beforeImage, afterImage));
			logger.info("Trigger: " + description + " already exists in the database, updated instead.");	
		}
	}
	public String addUserDevice(String userDeviceName, String locationID, String userDeviceTypeID, String image, String defaultState)
	{
		try {
			executeSingleInsert(UserDevice.SQL_INSERT, userDeviceName, locationID, userDeviceTypeID, image, defaultState);
			return executeQueryForSingleString(getQuerySql("getLatestUserDeviceID"));
		} catch (SQLException e) {
			logger.error("Failed to add and find new user device:" + e);
		}
		return "";
	}
	public String addUserDeviceType(String userDeviceTypeName, String image, String parentID)
	{
		try {
			String id = executeQueryForSingleString(getQuerySql("getUserDeviceTypeIDWithNameAndImage"), userDeviceTypeName, image);
			if (id!=null)
				return id;
				
			executeSingleInsert(UserDeviceType.SQL_INSERT, userDeviceTypeName, image, parentID);
			return executeQueryForSingleString(getQuerySql("getUserDeviceTypeIDWithNameAndImage"), userDeviceTypeName, image);
		} catch (SQLException e) {
			logger.error("Failed to add new user device type:" + e);
		}
		return "";
	}
	public String addSystemDevice(String jsonConfig, String systemDeviceTypeID, String userDeviceID)
	{
		try {
			executeSingleInsert(SystemDevice.SQL_INSERT, jsonConfig, systemDeviceTypeID, userDeviceID);
			logger.info("New System Device added to database with type ID: " + systemDeviceTypeID +" and userDeviceID: " +userDeviceID);
		
			/*if (resources != null)
			{
				for (SystemDeviceResource resource : resources)
				{
					addSystemDeviceResource(resource);
				}
			}*/
			
			return executeQueryForSingleString(getQuerySql("getLatestSystemDeviceID"));
		} catch (SQLException e) {
			logger.error("Failed to add and get new user device:" + e);
		}
		return "";
	}
	
	public void addSystemDeviceType(String sysDeviceTypeId, String displayName, String jsonConfigData) {
		try {
			executeSingleInsert(SystemDeviceType.SQL_INSERT, sysDeviceTypeId, displayName, jsonConfigData);
			logger.info("New System Device Type added to database with ID: " + sysDeviceTypeId +", displayName: " +displayName+" and jsonConfigData: " +jsonConfigData);
		
			/*if (resources != null)
			{
				for (SystemDeviceTypeResource resource : resources)
				{
					addSystemDeviceTypeResource(resource);
				}
			}*/
			
		} catch (SQLException e) {
			try
			{
				executeSingleInsert(SystemDeviceType.SQL_UPDATE, displayName, jsonConfigData, sysDeviceTypeId);
				logger.info("DeviceType: " + displayName + "already exists in the database, updated details in case.");
			} catch (SQLException e1)
			{
				logger.error("DeviceType: " + displayName + "already exists in the database and failed to update details.", e1);
			}		
		}
	}
	
	public void addUserTypeToSystemType(String userDeviceTypeID, String systemDeviceTypeID)
	{
		try {
			executeSingleInsert(getAddDataSql("addUserTypeToSystemType"), userDeviceTypeID, systemDeviceTypeID);
		} catch (SQLException e) {
			//means that the relationship already exists.
		}
	}
	public String addLocationContext(String name, String image)
	{
		try {
			String id = executeQueryForSingleString(LocationContext.SQL_GET_ID, name);
			if (id!=null)
				return id;
				
			executeSingleInsert(LocationContext.SQL_INSERT, name, image);
			return executeQueryForSingleString(LocationContext.SQL_GET_ID, name);
		} catch (SQLException e) {
			logger.error("Failed to add new location context:" + e);
		}
		return "";
	}
	public String addLocation(String name, String locationContextID, String image)
	{
		try {
			String id = executeQueryForSingleString(Location.SQL_GET_ID, name, locationContextID, image);
			if (id != null) //if the location with those details does not exist, then add it
				return id;
				
			executeSingleInsert(Location.SQL_INSERT, name, locationContextID, image);	
			return executeQueryForSingleString(Location.SQL_GET_ID, name, locationContextID, image);
		} catch (SQLException e) {
			logger.error("Failed to add new location:" + e);
		}
		return "";
	}
	
	public String addImage(File imageFile)
	{
		try {
			if (imageFile.getName().toLowerCase().endsWith("png") && imageFile.exists())
			{
				currentImageIndex++;
				String id = Integer.toString(currentImageIndex);
				saveFile(imageFile, id);
				executeSingleInsert(getAddDataSql("addResourceImage"), id, imageFile.getAbsolutePath());
				logger.info("Successfully added image to database: " + imageFile.getAbsolutePath() );
				return id;
			}
		} catch (SQLException e) {
			logger.error("Unable to save image with path: " + imageFile.getAbsolutePath(), e);
		}
		return "error";
	}

	private ExecutorService s = Executors.newSingleThreadExecutor();
	private void saveFile(final File imageFile, final String newFileName)
	{
		s.execute(new Runnable(){
			public void run() {
				try {
					FileInputStream originalStream = new FileInputStream(new File(imageFile.getAbsolutePath()));
					File newFile = new File(System.getProperty("uk.ac.stir.cs.homer.resources.images") + newFileName + ".png");
					//if (!newFile.exists())
					{
						FileOutputStream newFileStream = new FileOutputStream(newFile);
						byte[] buf = new byte[1024];
						int numRead = 0;
						while((numRead = originalStream.read(buf)) >= 0) {
							newFileStream.write(buf, 0, numRead);
						}
						newFileStream.close();
						originalStream.close();
						logger.info("Successfully saved image to homer image store: " + imageFile.getAbsolutePath() +" to new file: " + newFile.getAbsolutePath());

					}
				} catch (IOException e) {
					logger.error("Unable to save a copy of the image just copied to the database to the homer image store: " + imageFile.getAbsolutePath());
				}
			}
			
		});
	}
	//*******************************************************
	
	
	//********** UPDATE ******************************************
	public void updateUserDeviceTypeDetails(UserDeviceType ud)
	{
		try {
			executeSingleInsert(UserDeviceType.SQL_UPDATE, ud.getName(), ud.getImage(), ud.getParent(), ud.getId());
		} catch (SQLException e) {
			logger.error("Failed to update user device details." + e);
		}
	}
	public void updateUserDeviceDetails(UserDevice ud)
	{
		try {
			executeSingleInsert(UserDevice.SQL_UPDATE, ud.getName(), ud.getLocationID(), ud.getTypeID(), ud.getImage(), ud.getDefaultState(), ud.getCurrentState(), ud.getCurrentStateParameters(), ud.getId());
		} catch (SQLException e) {
			logger.error("Failed to update user device details." + e);
		}
	}
	public void updateSystemDeviceDetails(SystemDevice sd)
	{
		try {
			executeSingleInsert(SystemDevice.SQL_UPDATE, sd.getJsonConfigData(), sd.getSystemDeviceTypeID(), sd.getUserDeviceID(), sd.getId());
		} catch (SQLException e) {
			logger.error("Failed to update user system device details." + e);
		}
	}
	public void updateCondition(Condition c) {
		try {
			executeSingleInsert(Condition.SQL_UPDATE, c.getDescription(), c.getJsonParamData(), c.getSystemDeviceType(), c.getImage(), c.getTriggerIDsAndOrActionIDsResultingInThisState(), c.getId());
		} catch (SQLException e) {
			logger.error("Failed to update condition." + e);
			logger.error("SQL: " + Condition.SQL_UPDATE);
		}
	}
	public void updateAction(Action a) {
		try {
			executeSingleInsert(Action.SQL_UPDATE, a.getDescription(), a.getJsonParamData(), a.getSystemDeviceType(), a.getImage(), a.getId());
		} catch (SQLException e) {
			logger.error("Failed to update action."+ e);
		}
	}
	public void updateTrigger(Trigger t) {
		try {
			executeSingleInsert(Trigger.SQL_UPDATE, t.getDescription(), t.getJsonParamData(), t.getSystemDeviceType(), t.getBeforeImage(), t.getAfterImage(), t.getId());
		} catch (SQLException e) {
			logger.error("Failed to update trigger."+ e);
		}
	}
	public void updateLocationContextDetails(LocationContext locationContext)
	{
		try {
			executeSingleInsert(LocationContext.SQL_UPDATE, locationContext.getName(), locationContext.getImage(), locationContext.getId());
		} catch (SQLException e) {
			logger.error("Failed to update location context name.", e);
		}
	}
	public void updateLocationDetails(Location l)
	{
		try {
			executeSingleInsert(Location.SQL_UPDATE, l.getName(), l.getLocationContextID(), l.getImage(), l.getId());
		} catch (SQLException e) {
			logger.error("Failed to update location.", e);
		}
	}
	public void updateSysDeviceTypeDisplayName(String sysDeviceTypeId, String displayName) {
		try {
			executeSingleInsert(SystemDeviceType.SQL_UPDATE_NAME, displayName, sysDeviceTypeId);
		} catch (SQLException e) {
			logger.error("Failed to update system device type name", e);
		}
	}
	
	public void updateDeviceState(String userDeviceID, String stateID, String parameters)
	{
		try {
			executeSingleInsert(UserDevice.SQL_UPDATE_STATE, stateID, parameters, userDeviceID);		
		} catch (Exception e) {
			logger.error("Failed to update a device state for userdeviceid:" + userDeviceID, e);
		}
	}
	
	@Override
	public void updatePolicy(Policy policy)
	{
		try {
			executeSingleInsert(Policy.SQL_UPDATE, policy.getName(), policy.getAuthor(), policy.getDateCreatedInMillisecs(), policy.getDateLastEditedInMillisecs(), Boolean.toString(policy.isEnabled()), policy.getWhenClauseAsJSON(), policy.getDoClauseAsJSON(), policy.getID());		
		} catch (Exception e) {
			logger.error("Failed to update the policy:" + policy.getName(), e);
		}
	}
	
	//********************************************************
	
	
	//******************* GETS *******************************
	public ActionEnvironEffect[] getActionEnvironEffects(QueryObject qryObj)
	{
		BuiltQuery actionEnvEffQuery = queryBuilder.actionEnvironEffectQuery(qryObj);
		ArrayList<ActionEnvironEffect> stuff = executeQueryForObjectsFromDatabase(actionEnvEffQuery, ActionEnvironEffect.class);
		if (stuff.size()==1)	//want to remove the non specifics, so the times when the user has claimed a certain user and system device
								//doesnt affect the environment then we put in stars for the action id to know that this combination doesnt
								//effect the environment and we dont need to reask the user for information.
		{
			if (stuff.get(0).isEmpty()) 
			{
				stuff.remove(0);
			}
		}
		return stuff.toArray(new ActionEnvironEffect[stuff.size()]);
	}
	
	public Environ[] getAllEnirons()
	{
		ArrayList<Environ> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM Environ ORDER BY Environ.name ASC", Environ.class);
		return stuff.toArray(new Environ[stuff.size()]);
	}
	
	public Environ getEnvironWithName(String name)
	{
		ArrayList<Environ> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM Environ WHERE name = '" + name + "'", Environ.class);
		return stuff.get(0);
	}
	
	public Environ getEnvironWithID(String id) 
	{
		ArrayList<Environ> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM Environ WHERE id = '" + id + "'", Environ.class);
		return stuff.get(0);
	}	
	
	public Trigger[] getAllTriggers()
	{
		ArrayList<Trigger> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM Trigger ORDER BY Trigger.description ASC", Trigger.class);
		return stuff.toArray(new Trigger[stuff.size()]);
	}
	public Trigger[] getTriggers(QueryObject qryObj)
	{
		BuiltQuery triggerQuery = queryBuilder.triggerQuery(qryObj);
		ArrayList<Trigger> stuff = executeQueryForObjectsFromDatabase(triggerQuery, Trigger.class);
		return stuff.toArray(new Trigger[stuff.size()]);
	}

	public Condition[] getAllConditions()
	{
		ArrayList<Condition> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM Condition ORDER BY Condition.description ASC", Condition.class);
		return stuff.toArray(new Condition[stuff.size()]);
	}
	public Condition[] getConditions(QueryObject qryObj)
	{
		ArrayList<Condition> stuff = executeQueryForObjectsFromDatabase(queryBuilder.conditionQuery(qryObj), Condition.class);
		return stuff.toArray(new Condition[stuff.size()]);
	}

	public Action[] getAllActions()
	{
		ArrayList<Action> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM Action ORDER BY Action.description ASC", Action.class);
		return stuff.toArray(new Action[stuff.size()]);
	}
	public Action[] getActions(QueryObject qryObj)
	{
		ArrayList<Action> stuff = executeQueryForObjectsFromDatabase(queryBuilder.actionQuery(qryObj), Action.class);
		return stuff.toArray(new Action[stuff.size()]);
	}
	
	public Location[] getAllLocations()
	{
		ArrayList<Location> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM Location ORDER BY Location.name", Location.class);
		return stuff.toArray(new Location[stuff.size()]);
	}
	public Location[] getLocations(QueryObject qryObj)
	{
		ArrayList<Location> stuff = executeQueryForObjectsFromDatabase(queryBuilder.locationQuery(qryObj), Location.class);
		return stuff.toArray(new Location[stuff.size()]);
	}
	
	public LocationContext[] getAllLocationContexts()
	{
		ArrayList<LocationContext> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM LocationContext ORDER BY LocationContext.name", LocationContext.class);
		return stuff.toArray(new LocationContext[stuff.size()]);
	}
	public LocationContext[] getLocationContexts(QueryObject qryObj)
	{
		ArrayList<LocationContext> stuff = executeQueryForObjectsFromDatabase(queryBuilder.locationContextQuery(qryObj), LocationContext.class);
		return stuff.toArray(new LocationContext[stuff.size()]);
	}
	
	public UserDevice[] getAllUserDevices()
	{
		ArrayList<UserDevice> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM UserDevice ORDER BY UserDevice.name ASC", UserDevice.class);
		return stuff.toArray(new UserDevice[stuff.size()]);
	}
	public UserDevice[] getUserDevices(QueryObject qryObj)
	{
		ArrayList<UserDevice> stuff = executeQueryForObjectsFromDatabase(queryBuilder.userDeviceQuery(qryObj), UserDevice.class);
		return stuff.toArray(new UserDevice[stuff.size()]);
	}
	
	public UserDeviceType[] getAllUserDeviceTypes()
	{
		ArrayList<UserDeviceType> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM UserDeviceType ORDER BY UserDeviceType.name ASC", UserDeviceType.class);
		return stuff.toArray(new UserDeviceType[stuff.size()]);
	}
	public UserDeviceType[] getUserDeviceTypes(QueryObject qryObj)
	{
		ArrayList<UserDeviceType> stuff = executeQueryForObjectsFromDatabase(queryBuilder.userDeviceTypeQuery(qryObj), UserDeviceType.class);
		return stuff.toArray(new UserDeviceType[stuff.size()]);
	}
	
	public SystemDevice[] getAllSystemDevices()
	{
		ArrayList<SystemDevice> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM SystemDevice", SystemDevice.class);
		return stuff.toArray(new SystemDevice[stuff.size()]);
	}
	public SystemDevice[] getSystemDevices(QueryObject qryObj)
	{
		ArrayList<SystemDevice> stuff = executeQueryForObjectsFromDatabase(queryBuilder.systemDeviceQuery(qryObj), SystemDevice.class);
		return stuff.toArray(new SystemDevice[stuff.size()]);
	}
	
	public SystemDeviceType[] getAllSystemDeviceTypes()
	{
		ArrayList<SystemDeviceType> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM SystemDeviceType ORDER BY SystemDeviceType.name ASC", SystemDeviceType.class);
		return stuff.toArray(new SystemDeviceType[stuff.size()]);
	}
	
	public SystemDeviceType[] getSystemDeviceTypes(QueryObject qryObj)
	{
		ArrayList<SystemDeviceType> stuff = executeQueryForObjectsFromDatabase(queryBuilder.systemDeviceTypeQuery(qryObj), SystemDeviceType.class);
		return stuff.toArray(new SystemDeviceType[stuff.size()]);
	}

	public IDsForSystemDevice getDetailsAboutSysDeviceID(String sysDeviceID) {
		IDsForSystemDevice ids = new IDsForSystemDevice();
		ids.setSysDeviceID(sysDeviceID);
		
		String[] idsFromDB = executeQueryForStringSet(getQuerySql("getIDsAboutSysDeviceID"), sysDeviceID);
		
		ids.setSysDeviceTypeID(idsFromDB[0]);
		ids.setUserDeviceID(idsFromDB[1]);
		ids.setUserDeviceTypeID(idsFromDB[2]);
		ids.setLocationID(idsFromDB[3]);
		ids.setLocationContextID(idsFromDB[4]);
		return ids;
	}
	public IDsForUserDevice getDetailsAboutUserDeviceID(String userDeviceID)
	{
		IDsForUserDevice ids = new IDsForUserDevice();
		ids.setUserDeviceID(userDeviceID);
		
		Set<String[]> idsFromDB = executeQueryForStringSetList(getQuerySql("getIDsAboutUserDeviceID"), userDeviceID);
		
		for (String[] eachSysDev: idsFromDB)
		{
			ids.addSysDeviceTypeID(eachSysDev[1], eachSysDev[0]);
			ids.addSysDeviceID(eachSysDev[1]);
			ids.setUserDeviceTypeID(eachSysDev[2]);
			ids.setLocationID(eachSysDev[3]);
			ids.setLocationContextID(eachSysDev[4]);
		}
		return ids;
	}
	
	public InputStream getImage(String imageName)
	{
		 InputStream[] images = executeQueryForFileList("SELECT resource FROM Resources WHERE id=?", imageName);
		 if (images.length > 0) return images[0];
		 else return null;
	}
	//*******************************************************************
	
	
	//****** DELETE *******************************
	public void HOMER_USE_ONLY_deleteActionEnvironEffects(String userDeviceID, String systemDeviceID)
	{
		try {
			executeSingleInsert(ActionEnvironEffect.SQL_DELETE, userDeviceID, systemDeviceID);
		} catch (SQLException e) {
			logger.error("Failed to delete action environ effect: " + e);
		}	
	}
	public SystemDevice[] HOMER_USE_ONLY_deleteLocationContext(String locationContextID)
	{
		try {
			ArrayList<SystemDevice> systemDevicesNowDeleted = executeQueryForObjectsFromDatabase(queryBuilder.systemDeviceQuery(new QueryObject().locationContext(locationContextID)), SystemDevice.class);
			executeSingleInsert(LocationContext.SQL_DELETE, locationContextID);
			return systemDevicesNowDeleted.toArray(new SystemDevice[systemDevicesNowDeleted.size()]);
		} catch (SQLException e) {
			logger.error("Failed to delete location context: " + e);
		}	
		return new SystemDevice[]{};
	}
	public SystemDevice[] HOMER_USE_ONLY_deleteLocation(String locationID)
	{
		try {
			ArrayList<SystemDevice> systemDevicesNowDeleted = executeQueryForObjectsFromDatabase(queryBuilder.systemDeviceQuery(new QueryObject().location(locationID)), SystemDevice.class);
			executeSingleInsert(Location.SQL_DELETE, locationID);
			return systemDevicesNowDeleted.toArray(new SystemDevice[systemDevicesNowDeleted.size()]);
		} catch (SQLException e) {
			logger.error("Failed to delete location: " + e);
		}	
		return new SystemDevice[]{};	
	}
	public SystemDevice[] HOMER_USE_ONLY_deleteUserDevice(String userDeviceID)
	{
		try {
			ArrayList<SystemDevice> systemDevicesNowDeleted = executeQueryForObjectsFromDatabase(queryBuilder.systemDeviceQuery(new QueryObject().userDevice(userDeviceID)), SystemDevice.class);
			executeSingleInsert(UserDevice.SQL_DELETE, userDeviceID);
			return systemDevicesNowDeleted.toArray(new SystemDevice[systemDevicesNowDeleted.size()]);
		} catch (SQLException e) {
			logger.error("Failed to delete user device: " + e);
		}	
		return new SystemDevice[]{};	
	}
	public SystemDevice[] HOMER_USE_ONLY_deleteUserDeviceType(String userDeviceTypeID)
	{
		try {
			ArrayList<SystemDevice> systemDevicesNowDeleted = executeQueryForObjectsFromDatabase(queryBuilder.systemDeviceQuery(new QueryObject().userDeviceType(userDeviceTypeID)), SystemDevice.class);
			executeSingleInsert(UserDeviceType.SQL_DELETE, userDeviceTypeID);
			return systemDevicesNowDeleted.toArray(new SystemDevice[systemDevicesNowDeleted.size()]);
		} catch (SQLException e) {
			logger.error("Failed to delete user device type: " + e);
		}	
		return new SystemDevice[]{};	
	}
	public SystemDevice[] HOMER_USE_ONLY_deleteSystemDeviceType(String id)
	{
		try {
			ArrayList<SystemDevice> systemDevicesNowDeleted = executeQueryForObjectsFromDatabase(queryBuilder.systemDeviceQuery(new QueryObject().systemDeviceType(id)), SystemDevice.class);
			executeSingleInsert(SystemDeviceType.SQL_DELETE, id);
			return systemDevicesNowDeleted.toArray(new SystemDevice[systemDevicesNowDeleted.size()]);
		} catch (SQLException e) {
			logger.error("Failed to delete system device type: " + e);
		}	
		return new SystemDevice[]{};
	}
	public void HOMER_USE_ONLY_deleteSystemDevice(String id)
	{
		try {
			executeSingleInsert(SystemDevice.SQL_DELETE, id);
		} catch (SQLException e) {
			logger.error("Failed to delete system device: " + e);
		}
	}
	public void deletePolicy(Policy policy)
	{
		try {
			executeSingleInsert(Policy.SQL_DELETE, policy.getID());
		} catch (Exception e) {
			logger.error("Failed to delete the policy:" + policy.getName(), e);
		}
	}
	//****************************************
	
	
	//******POLICIES*******************************
	public Policy[] getPolicies()
	{
		ArrayList<Policy> stuff = executeQueryForObjectsFromDatabase("SELECT * FROM Policy", Policy.class);
		return stuff.toArray(new Policy[stuff.size()]);
	}
	
	public void savePolicy(Policy p)
	{
		try {
			executeSingleInsert(Policy.SQL_INSERT, p.getName(), p.getAuthor(), p.getDateCreatedInMillisecs(), p.getDateLastEditedInMillisecs(), Boolean.toString(p.isEnabled()), p.getWhenClauseAsJSON(), p.getDoClauseAsJSON());
			logger.debug("New Policy added to database: " + p.getName());
		} catch (SQLException e) {
			logger.error("Error adding new policy to database: " + p.getName(), e);
		}
	}
	//****************************************
	
	//******************* Database calls *******************************
	private <t extends DBObject> ArrayList<t> executeQueryForObjectsFromDatabase(BuiltQuery builtQuery, Class<t> stuffClass) 
	{
		return executeQueryForObjectsFromDatabaseNow(builtQuery.queryStr, stuffClass, builtQuery.args);
	}
	
	private <t extends DBObject> ArrayList<t> executeQueryForObjectsFromDatabase(String querySql, Class<t> stuffClass, String... parameters) {
		if (parameters != null && parameters.length>0)
		{
			List<String> list = new ArrayList<String>();
			for (String p: parameters) { list.add(p); }
			return executeQueryForObjectsFromDatabaseNow(querySql, stuffClass, list);
		}
		else
		{
			return executeQueryForObjectsFromDatabaseNow(querySql, stuffClass, null);
		}
	}
	
	private <t extends DBObject> ArrayList<t> executeQueryForObjectsFromDatabaseNow(String querySql, Class<t> stuffClass, List<String> args) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet results = null;
		ArrayList<t> objects = new ArrayList<t>();
		
		try {
			conn = getConnection();
			st = conn.prepareStatement(querySql);
			
			if(args != null) {
				for(int i=0; i < args.size(); i++) {
					st.setString(i+1, args.get(i));
				}
			}
			
			results = st.executeQuery();
	
			if (results != null)
			{
				while (results.next())
				{
					t newObject = stuffClass.newInstance();
					String[] s = new String[newObject.getConstructorSize()];
					for(int i=0; i < s.length; i++) {
						try
						{
							s[i] = results.getString(i+1);
						} catch (SQLException e1) {break;}
					}
					newObject.init(s);
					objects.add(newObject);
				}
			}
		} catch(SQLException e) {
			throw new RuntimeException("SQL error when trying to obtain info from database: "+ e);
		} catch (InstantiationException e) {
			throw new RuntimeException("Security violation?", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Security violation?", e);
		} finally {
			closeResources(conn, false, results, st);
		}
		return objects;
	}
	
	/*private void executeSingleInsert(String query, String id, File file) throws SQLException, FileNotFoundException {
		Connection conn = null;
		boolean rollback = true;
		PreparedStatement st = null;
		try {
			conn = getConnection();
			st = conn.prepareStatement(query);
			
			FileInputStream inputStream = new FileInputStream(file);
			st.setBinaryStream(1, inputStream);
			
			if(st.executeUpdate() != 0) {
				conn.commit();
				rollback = false;
			}
			inputStream.close();
		} catch (IOException e) {
			logger.error("Problem trying to close the file just saved to the database: " + file.getAbsolutePath());
		} 
		finally {
			closeResources(conn, rollback, null, st);
		}
	}*/
	
	private void executeSingleInsert(String query, String... parameters) throws SQLException {
		Connection conn = null;
		boolean rollback = true;
		PreparedStatement st = null;
		try {
			conn = getConnection();
			st = conn.prepareStatement(query);
			
			if(parameters != null) {
				for(int i=0; i < parameters.length; i++) {
					st.setString(i+1, parameters[i]);
				}
			}
			
			if(st.executeUpdate() != 0) {
				conn.commit();
				rollback = false;
			}
		} 
		finally {
			closeResources(conn, rollback, null, st);
		}
	}

	
	private String[] executeQueryForStringSet(String query, String...parameters)
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet results = null;
		String[] setResults;
		
		try {
			conn = getConnection();
			st = conn.prepareStatement(query);
			
			if(parameters != null) {
				for(int i=0; i < parameters.length; i++) {
					st.setString(i+1, parameters[i]);
				}
			}
			
			results = st.executeQuery();
			
			if(results.next()) {
				setResults = new String[results.getMetaData().getColumnCount()];
				for (int i=0; i<results.getMetaData().getColumnCount(); i++)
				{
					setResults[i]= (results.getString(i+1));
				}
			}
			else
			{
				setResults = new String[0];
			}
			return setResults;
		} catch(SQLException e) {
			throw new RuntimeException("OMG!", e);
		} finally {
			closeResources(conn, false, results, st);
		}
	}
	
	private InputStream[] executeQueryForFileList(String query, String...parameters)
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet results = null;
		ArrayList<InputStream> setResults;
		
		try {
			conn = getConnection();
			st = conn.prepareStatement(query);
			
			if(parameters != null) {
				for(int i=0; i < parameters.length; i++) {
					st.setString(i+1, parameters[i]);
				}
			}
			
			results = st.executeQuery();
			
			setResults = new ArrayList<InputStream>();
			
			while(results.next()) {
				
				setResults.add(results.getBlob(1).getBinaryStream());
			}
			return setResults.toArray(new InputStream[0]);
		} catch(SQLException e) {
			throw new RuntimeException("OMG!", e);
		} finally {
			closeResources(conn, false, results, st);
		}
	}
	
	private Set<String[]> executeQueryForStringSetList(String query, String...parameters)
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet results = null;
	
		HashSet<String[]> allSetsOfResults = new HashSet<String[]>();
		
		try {
			conn = getConnection();
			st = conn.prepareStatement(query);
			
			if(parameters != null) {
				for(int i=0; i < parameters.length; i++) {
					st.setString(i+1, parameters[i]);
				}
			}
			
			results = st.executeQuery();
			
			while(results.next()) {
				String[] setResults = new String[results.getMetaData().getColumnCount()];
				for (int i=0; i<results.getMetaData().getColumnCount(); i++)
				{
					setResults[i]= (results.getString(i+1));
				}
				allSetsOfResults.add(setResults);
			}
			
			return allSetsOfResults;
		} catch(SQLException e) {
			throw new RuntimeException("OMG!", e);
		} finally {
			closeResources(conn, false, results, st);
		}
	}
	
	public String executeQueryForSingleString(String query, String...parameters) throws SQLException
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			st = conn.prepareStatement(query);
			
			if(parameters != null) {
				for(int i=0; i < parameters.length; i++) {
					st.setString(i+1, parameters[i]);
				}
			}
			
			results = st.executeQuery();
			
			if(results.next()) {
				return results.getString(1);
			} else {
				return null;
			}
		} finally {
			closeResources(conn, false, results, st);
		}
	}
	
	
	private static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

	@Override
	public void addUser(String name) {
		try {
			this.executeSingleInsert(User.SQL_INSERT,name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void updateUser(int id, String name) {
		try {
			this.executeSingleInsert(User.SQL_UPDATE,name,""+id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getUserName(int id) {
		try {
			return this.executeQueryForSingleString(User.SQL_SELECT,"name",""+id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<Integer, String> getAllUsers() {
		Set<String[]> resultSet = this.executeQueryForStringSetList(User.SQL_SELECT, "id,name");
		Map<Integer,String> returnSet = new HashMap<Integer,String>();
		
		Iterator<String[]> it = resultSet.iterator();
		
		while(it.hasNext()){
			String[] result = it.next();
			
			int key = Integer.getInteger(result[0]);
			String value = result[1];
			
			returnSet.put(key,value);
			
		}
		
		return returnSet;
	}

	@Override
	public void deleteUser(int id) {
		try {
			this.executeSingleInsert(User.SQL_DELETE,""+id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
