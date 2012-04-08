package com.sepiroth.ooha.kinect;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.HomerComponent;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasConditions;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasTriggers;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.encoding.IDUtil;
import uk.ac.stir.cs.homer.homerFrameworkAPI.homerObjects.SystemDeviceType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Condition;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Trigger;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.types.BStr;
import com.sepiroth.ooha.kinect.gesture.Gesture;
import com.sepiroth.ooha.kinect.gesture.GestureListModel;

public class KinectSensorComponent implements HomerComponent, WhichHasTriggers, WhichHasConditions{
	

	private static final Logger logger = LoggerFactory.getLogger(KinectSensorComponent.class);
	private KinectSensorListener kinectSensorListener;
	static final String KINECT_SENSOR  = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "KINECT SENSOR");
	
	public static final String PRESENCE_DETECTED = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "PRESENCE_DETECTED");
	public static final String PRESENCE_LOST = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "PRESENCE_LOST");
	public static final String USER_DETECTED = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "USER_DETECTED");
	public static final String USER_LOST = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "USER_LOST");
	
	public static final String HAS_USER = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "sees a user");
	public static final String HAS_SKELETON = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "sees a skeleton");
	public static final String HAS_NO_USER = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "sees no user");
	public static final String HAS_NO_SKELETON = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "sees no skeleton");
	
	private KinectUI ui;
	private final HomerDatabase database;
	private String sysDeviceID;
	private GestureListModel gestureModel;
	private DefaultListModel<String> userModel;
	
	static{
		System.setProperty("java.library.path",System.getProperty("java.library.path")+";F:\\KinectHomer\\Kinect\\libs\\comfyJ\\bin");
	}
	
	public KinectSensorComponent(HomerDatabase db){
		
		this.database = db;
		
		try {
			kinectSensorListener = new KinectSensorListener(this);
		} catch (Exception ex){
			logger.error("Could not create Kinect sensor listener : "+ex.getMessage());
		}
	
		
	}
	
	public void disconnect(){
		kinectSensorListener.disconnect();
	}
	
	public void setUI(KinectUI ui){
		this.ui = ui;
	}

	@Override
	public List<Trigger> getTriggers(String systemDeviceTypeID) {
		List<Trigger> triggers = new ArrayList<Trigger>();
		
		triggers.add(new Trigger(KinectSensorComponent.PRESENCE_DETECTED, "presence detected", Trigger.DEFAULT_PRESENCE_DETECTED_IMAGE, Trigger.DEFAULT_PRESENCE_LOST_IMAGE));
		triggers.add(new Trigger(KinectSensorComponent.PRESENCE_LOST, "presence lost", Trigger.DEFAULT_PRESENCE_LOST_IMAGE, Trigger.DEFAULT_PRESENCE_DETECTED_IMAGE));

		triggers.add(new Trigger(KinectSensorComponent.USER_DETECTED, "user detected", Trigger.DEFAULT_USER_DETECTED_IMAGE, Trigger.DEFAULT_USER_LOST_IMAGE));
		triggers.add(new Trigger(KinectSensorComponent.USER_LOST, "user lost", Trigger.DEFAULT_USER_LOST_IMAGE, Trigger.DEFAULT_USER_DETECTED_IMAGE));

		return triggers;
	}

	@Override
	public SystemDeviceType[] getSystemDeviceTypeData() {
		return new SystemDeviceType[] { 
				new SystemDeviceType(KINECT_SENSOR, "Kinect Sensor", null)};
	}

	
	public String getSysDeviceID(){
		return sysDeviceID;
	}
	
	@Override
	public void registerComponentInstance(String systemDeviceTypeID,
			String sysDeviceID, String[] parameters) {
		logger.info("New Kinect sensor registered: " + systemDeviceTypeID + " with code: " + sysDeviceID);
		this.sysDeviceID = sysDeviceID;
		
	}

	@Override
	public void editComponentInstance(String systemDeviceTypeID,
			String sysDeviceID, String[] newParameters, String[] oldParameters) {
		logger.info("Kinect sensor settings changed: " + sysDeviceID);
		
		this.sysDeviceID = sysDeviceID;
		
	}

	@Override
	public void deleteComponentInstance(String systemDeviceTypeID,
			String sysDeviceID, String[] parameters) {
		kinectSensorListener.disconnect();
	}

	public void recordGesture(String string, String context) {
		kinectSensorListener.recordGesture(string, context);
	}

	public void recognizeGesture(String i) {
		kinectSensorListener.recognizeGesture(new BStr(i));
		
	}

	public void setTracking(int skelID) {
		kinectSensorListener.setTracking(new Int32(skelID));
	}

	public void storeGestures() {
		kinectSensorListener.storeGestures();
	}
	
	public String[] loadGestures(){
		if(kinectSensorListener!= null)
			return kinectSensorListener.loadGestures();
		return null;
	}

	public void updateGestureModel() {
		ui.updateGestureModel();		
	}
	
	public HomerDatabase getDatabase(){
		return database;
	}
	
	public GestureListModel getGestureModel() {
		if(gestureModel == null){
			String[] gestures = this.loadGestures();
			if(gestures != null){
				this.gestureModel = new GestureListModel(gestures);
			}else{
				this.gestureModel = new GestureListModel(new String[]{"No gestures;available"});
			}
			
		}
		
		return gestureModel;
	}
	
	public DefaultListModel<String> getUserModel(){
		if(userModel == null){
			String[] users = kinectSensorListener.loadUsers();
			
			if(users != null){
				userModel = new DefaultListModel<String>();
				for(String user : users){
					userModel.addElement(user);
				}
			}else{
				userModel = new DefaultListModel<String>();
				userModel.add(0,"No users found!");
			}
		}
		
		return userModel;
	}

	public String[] getObjects() {
		return kinectSensorListener.getObjects();
	}

	public void setHandConfig(boolean selected) {
		kinectSensorListener.setHandConfig(selected);
	}

	public void loadGestureBindings() {
		
	}

	public void storeGestureBindings() {
		for(Gesture g : (Gesture[])gestureModel.toArray()){
			g.getName();
		}
			
	}

	public void trainUser(String userName) {
		kinectSensorListener.trainUser(userName);
	}

	public void removeUser(String userName) {
		kinectSensorListener.removeUser(userName);
	}

	public void createNewObject(String objName) {
		kinectSensorListener.createNewObject(objName);
	}

	@Override
	public List<Condition> getConditions(String systemDeviceTypeID) {
		// TODO Auto-generated method stub
		List<Condition> conditions = new ArrayList<Condition>();
	
		conditions.add(new Condition(HAS_NO_USER,"no user","",null,USER_LOST,USER_LOST));
		conditions.add(new Condition(HAS_USER,"user present","",null,USER_DETECTED,USER_DETECTED));
		
		return conditions;
	}

	@Override
	public boolean checkCondition(String sysDeviceTypeID, String sysDeviceID,
			String conditionID, String[] parameters) {
		// TODO Auto-generated method stub
		return false;
	}
}
