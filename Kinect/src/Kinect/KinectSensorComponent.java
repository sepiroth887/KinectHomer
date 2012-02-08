package Kinect;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.types.BStr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.HomerComponent;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasConditions;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasTriggers;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.encoding.IDUtil;
import uk.ac.stir.cs.homer.homerFrameworkAPI.homerObjects.SystemDeviceType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Condition;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Trigger;

import java.util.List;

public class KinectSensorComponent implements HomerComponent, WhichHasTriggers, WhichHasConditions {
	

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private KinectSensorListener kinectSensorListener;
	static final String KINECT_SENSOR  = IDUtil.getUniqueIdentifier(KinectSensorComponent.class, "KINECT SENSOR");

	
	public KinectSensorComponent(){
		
		try {
			kinectSensorListener = new KinectSensorListener();
		} catch (Exception ex){
			logger.error("Could not create Kinect sensor listener : "+ex.getMessage());
		}

	}
	
	public void disconnect(){
		kinectSensorListener.disconnect();
	}

	@Override
	public List<Condition> getConditions(String systemDeviceTypeID) {
		return null;
	}

	@Override
	public boolean checkCondition(String sysDeviceTypeID, String sysDeviceID,
			String conditionID, String[] parameters) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Trigger> getTriggers(String systemDeviceTypeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemDeviceType[] getSystemDeviceTypeData() {
		return new SystemDeviceType[] { 
				new SystemDeviceType(KINECT_SENSOR, "Kinect Sensor", null)};
	}

	private String sysDeviceID;
	
	@Override
	public void registerComponentInstance(String systemDeviceTypeID,
			String sysDeviceID, String[] parameters) {
		logger.info("New Kinect sensor registered: " + sysDeviceID + " with code: " + parameters[0]);
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
	
	
}
