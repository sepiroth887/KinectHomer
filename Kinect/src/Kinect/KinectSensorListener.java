package Kinect;

import kinect.kinectcom.Device;
import kinect.kinectcom.IUserEvents;
import kinect.kinectcom.impl._DeviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.StringArray;
import com.jniwrapper.win32.automation.AutomationException;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.ExcepInfo;
import com.jniwrapper.win32.automation.types.SafeArray;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.IErrorInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;

public class KinectSensorListener {
	
	private _DeviceImpl comDevice;
	private IDispatchImpl userEventsHandlerDP;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private KinectSensorComponent kinectComponent ;
	
	public KinectSensorListener(KinectSensorComponent kinectComponent) throws Exception{
		
		this.kinectComponent = kinectComponent;
		connect();
	}
	
	public String getSysDeviceID(){
		return kinectComponent.getSysDeviceID();
	}
	
	private void connect() throws Exception{
		ComFunctions.coInitialize();

		comDevice = Device.create(ClsCtx.INPROC_SERVER);
		System.out.println("Handler attached");
		try { 
			comDevice.init();
		} catch (AutomationException e) { 
			ExcepInfo info = e.getExceptionInformation(); 
			String description = info.getBstrDescription();
			throw new Exception("Cannot initialize Kinect sensor: "+info+" : "+description);
		}
		
		try{
			attachToListeners(comDevice);
		}catch(Exception e){
			throw new Exception("Attaching listeners failed: "+e.getMessage());
		}
		
	}
	
	public void disconnect(){
		
		comDevice.uninit();
		
		ComFunctions.coUninitialize();
		
		
	}
	
	public void recordGesture(String gestureName, String ctxt){
		comDevice.recordGesture(new BStr(gestureName),new BStr(ctxt));
	}
	
	public void recognizeGesture(BStr ctxt){
		comDevice.recognizeGesture(ctxt);
	}
	
	private UserEventsHandler userEventsHandler;
	
	private void attachToListeners(_DeviceImpl device) throws Exception{
		
		try{
		IClassFactoryServer server = new IClassFactoryServer(UserEventsHandler.class);
		
		server.registerInterface(IDispatch.class,new IDispatchVTBL(server));
		server.registerInterface(IUserEvents.class,new IDispatchVTBL(server));
		server.setDefaultInterface(IDispatch.class);
		IClassFactory classFactory = server.createIClassFactory();
		userEventsHandlerDP = new IDispatchImpl();
		classFactory.createInstance(null, userEventsHandlerDP.getIID(),userEventsHandlerDP);
		userEventsHandler = (UserEventsHandler)server.getInstances().pop();
		userEventsHandler.setKinectListener(this);
		
		IConnectionPointContainer cpc = new IConnectionPointContainerImpl(device);
		IConnectionPoint cp = cpc.findConnectionPoint(new IID(IUserEvents.INTERFACE_IDENTIFIER));

		cp.advise(userEventsHandlerDP);
		
		}catch(ComException e){
			IErrorInfo info = e.getErrorInfo(); 
			String description = info.getDescription(null).toString();
			throw new Exception("Cannot initialize Kinect sensor: "+info+" : "+description);
		}
	}

	public void setTracking(Int32 skelID) {
		comDevice.startTracking(skelID);
	}

	public void stopRecognition() {
		comDevice.stopGestureRecognition();
	}

	public void setContext(BStr ctxt) {
		comDevice.setContext(ctxt);
	}

	public void storeGestures() {
		comDevice.storeGestures();
	}
	
	public String[] loadGestures(){
		String gestures = comDevice.loadGestures().toString();
		
		String[] gestureArray = gestures.split("\n");
		
		return gestureArray;
	}

	public void updateGestureModel() {
		kinectComponent.updateGestureModel();
	}
	
}
