package uk.ac.stir.cs.homer.serviceDatabase.queryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.stir.cs.homer.serviceDatabase.objects.Environ;


public class QueryBuilder {
	
	private static final String ACTION_ENVIRON_EFFECT = "ActionEnvironEffect";
	private static final String ACTION_ENVIRON_EFFECT_dot_SYSTEM_DEVICE_TYPE_ID = "ActionEnvironEffect.systemDeviceType";
	private static final String ACTION_ENVIRON_EFFECT_dot_USER_DEVICE_TYPE_ID = "ActionEnvironEffect.userDeviceType";
	private static final String ACTION_ENVIRON_EFFECT_dot_ACTION_ID = "ActionEnvironEffect.action";
	
	private static final String SYSTEM_DEVICE_TYPE = "SystemDeviceType";
	private static final String SYSTEM_DEVICE_TYPE_dot_ID = "SystemDeviceType.id";
	private static final String SYSTEM_DEVICE_TYPE_dot_NAME = "SystemDeviceType.name";
	
	private static final String SYSTEM_DEVICE = "SystemDevice";
	private static final String SYSTEM_DEVICE_dot_ID = "SystemDevice.id";
	private static final String SYSTEM_DEVICE_dot_TYPE = "SystemDevice.type";
	private static final String SYSTEM_DEVICE_dot_PARENT = "SystemDevice.parent";

	private static final String USER_DEVICE_TYPE = "UserDeviceType";
	private static final String USER_DEVICE_TYPE_dot_ID = "UserDeviceType.id";
	private static final String USER_DEVICE_TYPE_dot_NAME = "UserDeviceType.name";
	
	private static final String USER_DEVICE = "UserDevice";
	private static final String USER_DEVICE_dot_ID = "UserDevice.id";
	private static final String USER_DEVICE_dot_NAME = "UserDevice.name";
	private static final String USER_DEVICE_dot_LOCATION = "UserDevice.location";
	private static final String USER_DEVICE_dot_TYPE = "UserDevice.type";
	private static final String USER_DEVICE_dot_CURRENTSTATE = "UserDevice.currentState";
	private static final String USER_DEVICE_dot_CURRENTSTATEPARAMETERS = "UserDevice.currentStateParameters";
	
	private static final String LOCATION_CONTEXT = "LocationContext";
	private static final String LOCATION_CONTEXT_dot_ID = "LocationContext.id";
	private static final String LOCATION_CONTEXT_dot_NAME = "LocationContext.name";
	
	private static final String LOCATION = "Location";
	private static final String LOCATION_dot_ID = "Location.id";
	private static final String LOCATION_dot_NAME = "Location.name";
	private static final String LOCATION_dot_CONTEXT = "Location.context";

	private static final String TRIGGER = "Trigger";
	private static final String TRIGGER_dot_ID = "Trigger.id";
	private static final String TRIGGER_dot_DESCRIPTION = "Trigger.description";
	private static final String TRIGGER_dot_FOR_TYPE = "Trigger.forType";
	
	private static final String CONDITION = "Condition";
	private static final String CONDITION_dot_ID = "Condition.id";
	private static final String CONDITION_dot_DESCRIPTION = "Condition.description";
	private static final String CONDITION_dot_FOR_TYPE = "Condition.forType";
	
	private static final String ACTION = "Action";
	private static final String ACTION_dot_ID = "Action.id";
	private static final String ACTION_dot_DESCRIPTION = "Action.description";
	private static final String ACTION_dot_FOR_TYPE = "Action.forType";
	
	private static final Map<StringPair,String> tableRelations;
	private static final Map<StringPair,List<String>> tableLinks;
	private static final Map<String, String> orderBys;

	
	static {
		HashMap<StringPair,String> relations = new HashMap<StringPair, String>();
		addRelation(relations, TRIGGER, 			SYSTEM_DEVICE_TYPE, TRIGGER_dot_FOR_TYPE + " = " + SYSTEM_DEVICE_TYPE_dot_ID);
		addRelation(relations, CONDITION, 			SYSTEM_DEVICE_TYPE, CONDITION_dot_FOR_TYPE + " = " + SYSTEM_DEVICE_TYPE_dot_ID);
		addRelation(relations, ACTION,		 		SYSTEM_DEVICE_TYPE, ACTION_dot_FOR_TYPE + " = " + SYSTEM_DEVICE_TYPE_dot_ID);
		addRelation(relations, SYSTEM_DEVICE_TYPE, 	SYSTEM_DEVICE, 		SYSTEM_DEVICE_TYPE_dot_ID + " = " + SYSTEM_DEVICE_dot_TYPE);
		addRelation(relations, SYSTEM_DEVICE, 		USER_DEVICE, 		SYSTEM_DEVICE_dot_PARENT + " = " + USER_DEVICE_dot_ID);
		addRelation(relations, USER_DEVICE_TYPE, 	USER_DEVICE, 		USER_DEVICE_TYPE_dot_ID + " = " + USER_DEVICE_dot_TYPE);
		addRelation(relations, USER_DEVICE, 		LOCATION, 			USER_DEVICE_dot_LOCATION + " = " + LOCATION_dot_ID);
		addRelation(relations, LOCATION, 			LOCATION_CONTEXT, 	LOCATION_dot_CONTEXT + " = " + LOCATION_CONTEXT_dot_ID);
		addRelation(relations, ACTION, 				ACTION_ENVIRON_EFFECT, 	ACTION_dot_ID + " = " + ACTION_ENVIRON_EFFECT_dot_ACTION_ID);
		addRelation(relations, USER_DEVICE_TYPE, 	ACTION_ENVIRON_EFFECT, 	USER_DEVICE_TYPE_dot_ID + " = " + ACTION_ENVIRON_EFFECT_dot_USER_DEVICE_TYPE_ID);
		addRelation(relations, SYSTEM_DEVICE_TYPE, 	ACTION_ENVIRON_EFFECT, 	SYSTEM_DEVICE_TYPE_dot_ID + " = " + ACTION_ENVIRON_EFFECT_dot_SYSTEM_DEVICE_TYPE_ID);
		
		HashMap<StringPair,List<String>> links = new HashMap<StringPair, List<String>>();
		
		addLink(links, LOCATION, USER_DEVICE_TYPE, 	list(LOCATION, USER_DEVICE, USER_DEVICE_TYPE));
		addLink(links, LOCATION, SYSTEM_DEVICE, 	list(LOCATION, USER_DEVICE, SYSTEM_DEVICE));
		addLink(links, LOCATION, SYSTEM_DEVICE_TYPE, list(LOCATION, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE));
		addLink(links, LOCATION, TRIGGER, 			list(LOCATION, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, TRIGGER));
		addLink(links, LOCATION, CONDITION, 		list(LOCATION, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, CONDITION));
		addLink(links, LOCATION, ACTION, 			list(LOCATION, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, ACTION));
		addLink(links, LOCATION, ACTION_ENVIRON_EFFECT, 			list(LOCATION, USER_DEVICE, USER_DEVICE_TYPE, ACTION_ENVIRON_EFFECT));
		
		addLink(links, LOCATION_CONTEXT, USER_DEVICE, 		list(LOCATION_CONTEXT, LOCATION, USER_DEVICE));
		addLink(links, LOCATION_CONTEXT, USER_DEVICE_TYPE, 	list(LOCATION_CONTEXT, LOCATION, USER_DEVICE, USER_DEVICE_TYPE));
		addLink(links, LOCATION_CONTEXT, SYSTEM_DEVICE, 	list(LOCATION_CONTEXT, LOCATION, USER_DEVICE, SYSTEM_DEVICE));
		addLink(links, LOCATION_CONTEXT, SYSTEM_DEVICE_TYPE, list(LOCATION_CONTEXT, LOCATION, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE));
		addLink(links, LOCATION_CONTEXT, TRIGGER, 			list(LOCATION_CONTEXT, LOCATION, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, TRIGGER));
		addLink(links, LOCATION_CONTEXT, CONDITION, 		list(LOCATION_CONTEXT, LOCATION, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, CONDITION));
		addLink(links, LOCATION_CONTEXT, ACTION, 			list(LOCATION_CONTEXT, LOCATION, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, ACTION));
		addLink(links, LOCATION_CONTEXT, ACTION_ENVIRON_EFFECT, 			list(LOCATION_CONTEXT, LOCATION, USER_DEVICE, USER_DEVICE_TYPE, ACTION_ENVIRON_EFFECT));
		
		addLink(links, USER_DEVICE, SYSTEM_DEVICE_TYPE, list(USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE));
		addLink(links, USER_DEVICE, TRIGGER, 			list(USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, TRIGGER));
		addLink(links, USER_DEVICE, CONDITION, 			list(USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, CONDITION));
		addLink(links, USER_DEVICE, ACTION, 			list(USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, ACTION));
		addLink(links, USER_DEVICE, ACTION_ENVIRON_EFFECT, 			list(USER_DEVICE, USER_DEVICE_TYPE, ACTION_ENVIRON_EFFECT));

		addLink(links, USER_DEVICE_TYPE, SYSTEM_DEVICE, 		list(USER_DEVICE_TYPE, USER_DEVICE, SYSTEM_DEVICE));
		addLink(links, USER_DEVICE_TYPE, SYSTEM_DEVICE_TYPE, 	list(USER_DEVICE_TYPE, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE));
		addLink(links, USER_DEVICE_TYPE, TRIGGER, 				list(USER_DEVICE_TYPE, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, TRIGGER));
		addLink(links, USER_DEVICE_TYPE, CONDITION, 			list(USER_DEVICE_TYPE, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, CONDITION));
		addLink(links, USER_DEVICE_TYPE, ACTION, 				list(USER_DEVICE_TYPE, USER_DEVICE, SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, ACTION));
		addLink(links, USER_DEVICE_TYPE, ACTION_ENVIRON_EFFECT, 			list(USER_DEVICE_TYPE, ACTION_ENVIRON_EFFECT));

		addLink(links, SYSTEM_DEVICE, TRIGGER, 				list(SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, TRIGGER));
		addLink(links, SYSTEM_DEVICE, CONDITION, 			list(SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, CONDITION));
		addLink(links, SYSTEM_DEVICE, ACTION, 				list(SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, ACTION));
		addLink(links, SYSTEM_DEVICE, ACTION_ENVIRON_EFFECT, 			list(SYSTEM_DEVICE, SYSTEM_DEVICE_TYPE, ACTION_ENVIRON_EFFECT));

		addLink(links, ACTION, ACTION_ENVIRON_EFFECT, 			list(ACTION, ACTION_ENVIRON_EFFECT));

		
		tableRelations = Collections.unmodifiableMap(relations);
		tableLinks = Collections.unmodifiableMap(links);
		
		orderBys = new HashMap<String, String>();
		orderBys.put(LOCATION_CONTEXT, LOCATION_CONTEXT_dot_NAME);
		orderBys.put(LOCATION, LOCATION_dot_NAME);
		orderBys.put(USER_DEVICE, USER_DEVICE_dot_NAME);
		orderBys.put(USER_DEVICE_TYPE, USER_DEVICE_TYPE_dot_NAME);
		orderBys.put(SYSTEM_DEVICE_TYPE, SYSTEM_DEVICE_TYPE_dot_NAME);
		orderBys.put(TRIGGER, TRIGGER_dot_DESCRIPTION);
		orderBys.put(CONDITION, CONDITION_dot_DESCRIPTION);
		orderBys.put(ACTION, ACTION_dot_DESCRIPTION);
	}
	
	private static List<String> list(String... args) {
		return Arrays.asList(args);
	}
	
	private static void addRelation(Map<StringPair,String> map, String a, String b, String relation) {
		map.put(new StringPair(a,b), relation);
	}
	private static void addLink(Map<StringPair,List<String>> map, String a, String b, List<String> relation) {
		map.put(new StringPair(a,b), relation);
	}
	

	private void append(StringBuffer buffer, String separator, Collection<String> strs) {
		Iterator<String> iter = strs.iterator();
		while(iter.hasNext()) {
			buffer.append(iter.next());
			if(iter.hasNext()) {
				buffer.append(separator);
			}
		}
	}

	private Set<String>[] getTablesRelationsConditionsAndArguments(String table, QueryObject qObj) {
		Set<String> tables = new LinkedHashSet<String>();
		Set<String> joins = new LinkedHashSet<String>();
		Set<String> conditions = new LinkedHashSet<String>();
		Set<String> arguments = new LinkedHashSet<String>();
		
		if(qObj.getEnvironID() != null) {
			Set<String>[] tablesAndJoins = getTablesAndJoins(table, ACTION_ENVIRON_EFFECT);
			tables.addAll(tablesAndJoins[0]);
			joins.addAll(tablesAndJoins[1]);
		}
		
		if(qObj.getUserDeviceID() != null) {
			Set<String>[] tablesAndJoins = getTablesAndJoins(table, USER_DEVICE);
			tables.addAll(tablesAndJoins[0]);
			joins.addAll(tablesAndJoins[1]);
			
			if (!qObj.canBeAnyUserDevice())
			{
				conditions.add(USER_DEVICE_dot_ID + " = ?");
				arguments.add(qObj.getUserDeviceID());
			}
		}
		
		if(qObj.getCurrentState() != null) {
			Set<String>[] tablesAndJoins = getTablesAndJoins(table, USER_DEVICE);
			tables.addAll(tablesAndJoins[0]);
			joins.addAll(tablesAndJoins[1]);
			
			if (!qObj.canBeAnyUserDevice())
			{
				conditions.add(USER_DEVICE_dot_CURRENTSTATE + " = ?");
				arguments.add(qObj.getCurrentState());
			}
		}
		
		if(qObj.getCurrentStateParameters() != null) {
			Set<String>[] tablesAndJoins = getTablesAndJoins(table, USER_DEVICE);
			tables.addAll(tablesAndJoins[0]);
			joins.addAll(tablesAndJoins[1]);
			
			if (!qObj.canBeAnyUserDevice())
			{
				conditions.add(USER_DEVICE_dot_CURRENTSTATEPARAMETERS + " = ?");
				arguments.add(qObj.getCurrentStateParameters());
			}
		}
		if(qObj.getUserDeviceTypeID() != null) {
			Set<String>[] tablesAndJoins = getTablesAndJoins(table, USER_DEVICE_TYPE);
			tables.addAll(tablesAndJoins[0]);
			joins.addAll(tablesAndJoins[1]);
			
			if (!qObj.canBeAnyUserDeviceType())
			{
				conditions.add(USER_DEVICE_TYPE_dot_ID + " = ?");
				arguments.add(qObj.getUserDeviceTypeID());
			}
		}
		
		if(qObj.getLocationContextID() != null) {
			Set<String>[] tablesAndJoins = getTablesAndJoins(table, LOCATION_CONTEXT);
			tables.addAll(tablesAndJoins[0]);
			joins.addAll(tablesAndJoins[1]);
			
			if (!qObj.canBeAnyLocationContext())
			{
				conditions.add(LOCATION_CONTEXT_dot_ID + " = ?");
				arguments.add(qObj.getLocationContextID());
			}
		}
		
		if(qObj.getLocationID() != null) {
			Set<String>[] tablesAndJoins = getTablesAndJoins(table, LOCATION);
			tables.addAll(tablesAndJoins[0]);
			joins.addAll(tablesAndJoins[1]);
			
			if (!qObj.canBeAnyLocation())
			{
				conditions.add(LOCATION_dot_ID + " = ?");
				arguments.add(qObj.getLocationID());
			}
		}
		
		if(qObj.getSystemDeviceID() != null) {
			Set<String>[] tablesAndJoins = getTablesAndJoins(table, SYSTEM_DEVICE);
			tables.addAll(tablesAndJoins[0]);
			joins.addAll(tablesAndJoins[1]);
			
			if (!qObj.canBeAnySystemDevice())
			{
				conditions.add(SYSTEM_DEVICE_dot_ID + " = ?");
				arguments.add(qObj.getSystemDeviceID());
			}
		}
		if(qObj.getSystemDeviceTypeID() != null) {
			Set<String>[] tablesAndJoins = getTablesAndJoins(table, SYSTEM_DEVICE_TYPE);
			tables.addAll(tablesAndJoins[0]);
			joins.addAll(tablesAndJoins[1]);
			
			if (!qObj.canBeAnySystemDeviceType())
			{
				conditions.add(SYSTEM_DEVICE_TYPE_dot_ID + " = ?");
				arguments.add(qObj.getSystemDeviceTypeID());
			}
		}
		if (qObj.getTriggerID() != null)
		{
			if (qObj.mustHaveTriggers())
			{
				Set<String>[] tablesAndJoins = getTablesAndJoins(table, SYSTEM_DEVICE_TYPE);
				tables.add(TRIGGER);
				tables.addAll(tablesAndJoins[0]);
				joins.addAll(tablesAndJoins[1]);
			}
			else
			{
				Set<String>[] tablesAndJoins = getTablesAndJoins(table, TRIGGER);
				tables.addAll(tablesAndJoins[0]);
				joins.addAll(tablesAndJoins[1]);
				conditions.add(TRIGGER_dot_ID + " = ?");
				arguments.add(qObj.getTriggerID());
			}
		}
		if (qObj.getConditionID() != null)
		{
			if (qObj.mustHaveConditions())
			{
				Set<String>[] tablesAndJoins = getTablesAndJoins(table, SYSTEM_DEVICE_TYPE);
				tables.add(CONDITION);
				tables.addAll(tablesAndJoins[0]);
				joins.addAll(tablesAndJoins[1]);
			}
			else
			{
				Set<String>[] tablesAndJoins = getTablesAndJoins(table, CONDITION);
				tables.addAll(tablesAndJoins[0]);
				joins.addAll(tablesAndJoins[1]);
				conditions.add(CONDITION_dot_ID + " = ?");
				arguments.add(qObj.getConditionID());
			}
		}
		if (qObj.getActionID() != null)
		{
			if (qObj.mustHaveActions())
			{
				Set<String>[] tablesAndJoins = getTablesAndJoins(table, SYSTEM_DEVICE_TYPE);
				tables.addAll(tablesAndJoins[0]);
				tables.add(ACTION);
				joins.addAll(tablesAndJoins[1]);
			}
			else
			{
				Set<String>[] tablesAndJoins = getTablesAndJoins(table, ACTION);
				tables.addAll(tablesAndJoins[0]);
				joins.addAll(tablesAndJoins[1]);
				conditions.add(ACTION_dot_ID + " = ?");
				arguments.add(qObj.getActionID());
			}
		}
	
		return new Set[] {tables, joins,  conditions, arguments};
	}
	
	private Set<String>[] getTablesAndJoins(String from, String to) {
		Set<String> tables = new LinkedHashSet<String>();
		Set<String> joins = new LinkedHashSet<String>();
		
		if (from.equals(to))
		{
			tables.add(from);
		}
		else
		{
			List<String> links = tableLinks.get(new StringPair(from, to));
			
			if (links==null)	//tables are right next to each other
			{
				tables.add(from);
				joins.add(tableRelations.get(new StringPair(from, to)));
				tables.add(to);
			}
			else	//there is at least one table between from and to
			{
				tables.add(links.get(0));
				for (int i=1; i<links.size(); i++)
				{
					tables.add(links.get(i));
					joins.add(tableRelations.get(new StringPair(links.get(i-1), links.get(i))));
				}
			}
		}
		
		return new Set[] {tables, joins};
	}

	public BuiltQuery actionEnvironEffectQuery(QueryObject qObj) 
	{
		return createQueryForTable(qObj, ACTION_ENVIRON_EFFECT);
	}
	public BuiltQuery locationQuery(QueryObject qObj) 
	{
		return createQueryForTable(qObj, LOCATION);
	}
	public BuiltQuery locationContextQuery(QueryObject qObj) 
	{
		return createQueryForTable(qObj, LOCATION_CONTEXT);
	}
	public BuiltQuery userDeviceQuery(QueryObject qObj)
	{
		return createQueryForTable(qObj, USER_DEVICE);
	}
	public BuiltQuery userDeviceTypeQuery(QueryObject qObj)
	{
		return createQueryForTable(qObj, USER_DEVICE_TYPE);
	}
	public BuiltQuery systemDeviceTypeQuery(QueryObject qObj)
	{
		return createQueryForTable(qObj, SYSTEM_DEVICE_TYPE);
	}
	public BuiltQuery systemDeviceQuery(QueryObject qObj)
	{
		return createQueryForTable(qObj, SYSTEM_DEVICE);
	}
	public BuiltQuery triggerQuery(QueryObject qObj)
	{
		return createQueryForTable(qObj, TRIGGER);
	}
	public BuiltQuery conditionQuery(QueryObject qObj)
	{
		return createQueryForTable(qObj, CONDITION);
	}
	public BuiltQuery actionQuery(QueryObject qObj)
	{
		return createQueryForTable(qObj, ACTION);
	}
	private BuiltQuery createQueryForTable(QueryObject qObj, String table)
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		query.append(" DISTINCT ");
		query.append(table + ".* FROM ");
		
		Set<String>[] data = getTablesRelationsConditionsAndArguments(table, qObj);
		Set<String> tables = data[0];
		Set<String> links = data[1];
		Set<String> makeConditions = data[2];
		Set<String> args = data[3];
			
		if (tables.size()==0) tables.add(table);
		append(query, ", ", tables);
		
		if (links.size()>0 || makeConditions.size()>0)
		{
			query.append(" WHERE ");
			append(query, " AND ", links);
			
			if (links.size()>0 && makeConditions.size()>0)
			{
				query.append(" AND ");	
			}
			append(query, " AND ", makeConditions);
		}
		if (qObj.mustHaveTriggers() || qObj.mustHaveConditions() || qObj.mustHaveActions())
		{
			query.append(" AND ");
			
			int i = (qObj.mustHaveTriggers()? 1: 0) + (qObj.mustHaveConditions()? 1: 0) + (qObj.mustHaveActions()? 1: 0);
			if (i == 1)
			{
				if (qObj.mustHaveTriggers()) query.append(TRIGGER_dot_FOR_TYPE + " = " + SYSTEM_DEVICE_TYPE_dot_ID );	
				if (qObj.mustHaveConditions()) query.append(CONDITION_dot_FOR_TYPE + " = " + SYSTEM_DEVICE_TYPE_dot_ID );	;	
				if (qObj.mustHaveActions()) query.append(ACTION_dot_FOR_TYPE + " = " + SYSTEM_DEVICE_TYPE_dot_ID );		
			}
			else
			{
				query.append(" (");
				if (qObj.mustHaveTriggers()) query.append(TRIGGER_dot_FOR_TYPE + " = " + SYSTEM_DEVICE_TYPE_dot_ID + " OR ");	
				if (qObj.mustHaveConditions()) query.append(CONDITION_dot_FOR_TYPE + " = " + SYSTEM_DEVICE_TYPE_dot_ID );	
				if (qObj.mustHaveActions()) 
				{
					if (qObj.mustHaveConditions()) query.append(" OR ");
					query.append(ACTION_dot_FOR_TYPE + " = " + SYSTEM_DEVICE_TYPE_dot_ID );	
				}
				query.append(")");
				
				
			}
		}
		String orderBy = orderBys.get(table);
		if (orderBy != null)
		{
			//System.out.println("Table name: " + table + " orderBy: "+ orderBy);
			query.append(" ORDER BY " + orderBy + " ASC");
		}
		return new BuiltQuery(query.toString(), new ArrayList<String>(args));
	}

	
	
	public BuiltQuery logQuery(LoggerQueryObject queryObj) {
		StringBuffer query = new StringBuffer();
		ArrayList<String> arguements = new ArrayList<String>();
		ArrayList<String> whenConditions = new ArrayList<String>();
		query.append("SELECT * FROM Logs ");
		
		if (queryObj.getId() != null)
		{
			whenConditions.add(" id = ? ");
			arguements.add(queryObj.getId());
		}
		else
		{
			if (queryObj.getUserDeviceID() != null)
			{
				whenConditions.add(" userDeviceID = ? ");
				arguements.add(queryObj.getUserDeviceID());
			}
			if (queryObj.getUserDeviceTypeID() != null)
			{
				whenConditions.add(" userDeviceTypeID = ? ");
				arguements.add(queryObj.getUserDeviceTypeID());
			}
			if (queryObj.getSystemDeviceID() != null)
			{
				whenConditions.add(" systemDeviceID = ? ");
				arguements.add(queryObj.getSystemDeviceID());
			}
			if (queryObj.getSystemDeviceTypeID() != null)
			{
				whenConditions.add(" systemDeviceTypeID = ? ");
				arguements.add(queryObj.getSystemDeviceTypeID());
			}
			if (queryObj.getLocationID() != null)
			{
				whenConditions.add(" locationID = ? ");
				arguements.add(queryObj.getLocationID());
			}
			if (queryObj.getLocationContextID() != null)
			{
				whenConditions.add(" locationContextID = ? ");
				arguements.add(queryObj.getLocationContextID());
			}
			if (queryObj.getTriggerID() != null)
			{
				whenConditions.add(" triggerID = ? ");
				arguements.add(queryObj.getTriggerID());
			}
			if (queryObj.getConditionID() != null)
			{
				whenConditions.add(" conditionID = ? ");
				arguements.add(queryObj.getConditionID());
			}
			if (queryObj.getActionID() != null)
			{
				whenConditions.add(" actionID = ? ");
				arguements.add(queryObj.getActionID());
			}
			if (queryObj.getParameters() != null)
			{
				whenConditions.add(" parameters = ? ");
				arguements.add(queryObj.getParameters());
			}
			if (queryObj.getDisplay() != null)
			{
				whenConditions.add(" display = ? ");
				arguements.add(queryObj.getDisplay());
			}
		}
		
		if (queryObj.getEarliestDate() != null)
		{	
			whenConditions.add(" timestamp >= ? ");
			arguements.add(queryObj.getEarliestDate());
		}
		if (queryObj.getLatestDate() != null)
		{
			whenConditions.add(" timestamp <= ? ");
			arguements.add(queryObj.getLatestDate());
		}
		
		if (whenConditions.size() > 0)
		{
			query.append("WHERE");
			
			int conditionIndex = 1;
			for (String condition: whenConditions)
			{
				query.append(condition);
				
				if (whenConditions.size() != conditionIndex) query.append("AND");
				conditionIndex++;
			}
		}
		
		query.append(" ORDER BY id DESC");
		
		if (queryObj.getMaxNumEntries()!=null)
		{
			query.append(" LIMIT ? ");
			arguements.add(queryObj.getMaxNumEntries());
		}		
		return new BuiltQuery(query.toString(), arguements);

	}

	
	
}
