package Kinect;

import kinect.kinectcom.server.IUserEventsServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

public class UserEventsHandler extends IUserEventsServer {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private KinectSensorListener kinectListener;
	
	public UserEventsHandler(CoClassMetaInfo classImpl) {
		super(classImpl);
		
		
		// TODO Auto-generated constructor stub
	}
	
	public void onPresenceDetected(Int32 skelID){
		logger.info("User presence detected with userID: "+skelID);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kinectListener.setTracking(skelID);
	}
	
	public void onPresenceLost(Int32 skelID){
		logger.info("User presence lost with userID: "+skelID);
		
	}
	
	public void onGestureRecognitionCompleted(BStr gestureName){
		if(!gestureName.toString().contains(("__UNKNOWN"))){
			logger.info("Gesture recognition result: "+gestureName);
		}else{
			logger.info("Gesture recognition timeout. Please complete in under 3s");
		}	
	}
	
	public void onGestureRecordCompleted(BStr gestureName, Int32 ctxt){
		logger.info("Gesture recording result: "+gestureName+", context: "+ctxt);
	}
	
	public void onRecordingCountDownEvent(Int32 time){
		logger.info("Recording countdown event received: "+time);
	}

	public void onContextSelected(BStr ctxt){
		if(!ctxt.toString().equals("__NOCONTEXT")){
			System.out.println("Context "+ctxt+" selected. Recognition starting.");
			kinectListener.recognizeGesture(ctxt);
		}else{
			System.out.println("Context reset");
		}
		
		
	}
	
	public void onAddonGestureValueChange(Variant value){
		System.out.println("Value recieved: "+value.getFltVal());
	}
	
	public void onVoiceCommandDetected(BStr command){
		
	}
	public void setKinectListener(KinectSensorListener kinectSensorListener) {
		this.kinectListener = kinectSensorListener;
	}
}
