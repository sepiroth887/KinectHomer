package uk.ac.stir.cs.homer.componentX10;

import java.util.HashMap;
import java.util.Map;

import javax.comm.SerialPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.componentX10.utils.X10ListenerImpl;
import uk.ac.stir.cs.homer.componentX10.utils.stirlingAdaptor.KensX10Actions;
import uk.ac.stir.cs.homer.componentX10.utils.stirlingAdaptor.X10EventNotifier;
import uk.ac.stir.cs.homer.componentX10.utils.stirlingAdaptor.X10TransmitterCM12Impl;
import uk.ac.stir.cs.homer.componentX10.utils.stirlingAdaptor.X10TransmitterService;
import uk.ac.stir.cs.homer.serviceCOMMs.CommAccess;
import uk.ac.stir.cs.homer.serviceCOMMs.SerialService;

public class X10Commander 
{
	private SerialService serialService;
	private X10ListenerImpl x10listener;
	private X10TransmitterService transmitterService;
	private Map<String, String> x10DevicesHouseCodes;
	private Map<String, String> x10DeviceStates;
	private KensX10Actions x10Actions;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public X10Commander() {
		x10DevicesHouseCodes = new HashMap<String, String>();
		x10DeviceStates = new HashMap<String, String>();
		if (connect())
			x10Actions = new KensX10Actions(transmitterService);
	}
	public boolean connect()
	{	
		x10listener = new X10ListenerImpl();
		SerialPort sp = getSerialPort();
		if (sp!=null)
		{
			transmitterService = new X10TransmitterCM12Impl(sp, new X10EventNotifier(x10listener));
			return true;
		}
		return false;
	}
	public SerialPort getSerialPort()
	{
		if (serialService==null)
		{
			serialService = new CommAccess().openPort(X10Component.COMMPORT);
		}
		try
		{
			return serialService.getPort();
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}
	
	public void disconnect()
	{
		transmitterService.closeTransmitterGateway();
	}
	public void addNewX10Device(String systemDeviceID, String x10Code)
	{
		x10DevicesHouseCodes.put(systemDeviceID, x10Code);
	}
	public void updateX10Device(String systemDeviceID, String x10Code)
	{
		addNewX10Device(systemDeviceID, x10Code);
	}
	public void removeX10Device(String systemDeviceID) {
		x10DevicesHouseCodes.remove(systemDeviceID);
	}
	public void setState(String systemDeviceID, String state)
	{
		x10DeviceStates.put(systemDeviceID, state);
	}
	public void turnOn(String systemDeviceID)
	{
		x10Actions.handleEvent("on", x10DevicesHouseCodes.get(systemDeviceID), "");
		logger.info("Turning on x10 code: " + x10DevicesHouseCodes.get(systemDeviceID));
	}
	public void turnOff(String systemDeviceID)
	{
		x10Actions.handleEvent("off",(x10DevicesHouseCodes.get(systemDeviceID)), "");
		logger.info("Turning off x10 code: " + x10DevicesHouseCodes.get(systemDeviceID));
	}
	public void dim(String systemDeviceID, String dimLevel) {
		x10Actions.handleEvent("dim", (x10DevicesHouseCodes.get(systemDeviceID)), dimLevel);
		logger.info("Dimming x10 code: " +x10DevicesHouseCodes.get(systemDeviceID) + " to level: " + dimLevel);
	}
}
