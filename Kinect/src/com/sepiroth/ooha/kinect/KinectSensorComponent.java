package com.sepiroth.ooha.kinect;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.HomerComponent;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasTriggers;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.encoding.IDUtil;
import uk.ac.stir.cs.homer.homerFrameworkAPI.homerObjects.SystemDeviceType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Trigger;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;


import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.types.BStr;
import com.sepiroth.ooha.kinect.gesture.GestureListModel;

public class KinectSensorComponent implements HomerComponent, WhichHasTriggers{
	

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private KinectSensorListener kinectSensorListener;
	static final String KINECT_SENSOR  = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "KINECT SENSOR");
	
	static final String PRESENCE_DETECTED = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "PRESENCE_DETECTED");
	static final String PRESENCE_LOST = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "PRESENCE_LOST");
	static final String USER_DETECTED = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "USER_DETECTED");
	static final String USER_LOST = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "USER_LOST");
	
	private KinectUI ui;
	private final HomerDatabase database;
	private String sysDeviceID;
	private GestureListModel gestureModel;
	
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
		this.sysDeviceID = systemDeviceTypeID;
		
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
		// TODO Auto-generated method stub
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
		return kinectSensorListener.loadGestures();
	}

	public void updateGestureModel() {
		ui.updateGestureModel();
	}
	
	public HomerDatabase getDatabase(){
		return database;
	}
	
	public GestureListModel getGestureModel() {
		if(gestureModel == null){
			this.gestureModel = new GestureListModel(this.loadGestures());
		}
		
		return gestureModel;
	}
}
