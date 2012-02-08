package uk.ac.stir.cs.homer.componentVisonicSensors;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

import javax.comm.SerialPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.componentVisonicSensors.utils.EventObservers;
import uk.ac.stir.cs.homer.componentVisonicSensors.utils.ReceiverSerialPort;
import uk.ac.stir.cs.homer.serviceCOMMs.CommAccess;




public class VisonicSensorListener 
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String portName, portEntity;

	private ReceiverSerialPort receiverPort;
	private SerialPort serialPort;

	private EventObservers eventObservers;
	private Map<String, Observer> observers = new HashMap<String, Observer>();
	
	public VisonicSensorListener() throws Exception
	{
		connect();
	}

	
	public void connect() throws Exception
	{
		portName = System.getProperty("uk.ac.stir.cs.homer.componentVisonicSensors.port");
		
		if (portName == null)
		{
			logger.error("'uk.ac.stir.cs.homer.componentVisonicSensors.port' property needed in order to communicate with rfxcom device. Set it in the systems property.");
			throw new Exception("Port property unset.");
		}
		
		eventObservers = new EventObservers();
		receiverPort = new ReceiverSerialPort(eventObservers);

		serialPort = new CommAccess().openPort(portName).getPort();
		
		if (!receiverPort.initialise(portEntity, serialPort)) {
			receiverPort.close();
			logger.error("Could not initialise port - try restarting RFXCOMDriver");
		}
		else
			logger.info("Started Visonic Port " + portName);

	}

	
	public void disconnect()
	{
		if (serialPort != null)
			receiverPort.close();
		logger.info("Stopped Port " + portName);
	}
	
	public void addSensorListener(Observer o)
	{
		eventObservers.addNewObserver(o);
	}
	public void addTemperatureListener(Observer o)
	{
		receiverPort.addTemperatureListener(o);
	}
	public void removeTemperatueListener(String systemDeviceID)
	{
		receiverPort.removeTemperatureListener(observers.get(systemDeviceID));
	}
	
	public void addHumidityListener(Observer o)
	{
		receiverPort.addHumidityListener(o);
	}
	public void removeHumidityListener(String systemDeviceID)
	{
		receiverPort.removeHumidityListener(observers.get(systemDeviceID));
	}
	
	public void addUnrecognisedSensorListener(String systemDeviceID, Observer o)
	{
		observers.put(systemDeviceID, o);
		receiverPort.addUnrecocnisedDeviceListener(o);
	}
	
	public double getCurrentTemperature()
	{
		return receiverPort.getCurrentTemperature();
	}




}
