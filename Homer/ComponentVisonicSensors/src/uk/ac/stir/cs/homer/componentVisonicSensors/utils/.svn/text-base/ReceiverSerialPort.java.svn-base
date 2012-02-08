package uk.ac.stir.cs.homer.componentVisonicSensors.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.TooManyListenersException;

import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
  Serial porting handling for RFXCOMDriver.

  @author	Feng Wang, Kenneth J. Turner
  @version	1.0: 25/02/09 (FW)
  <br/>		1.1: 06/06/09 (KJT)
 */

public class ReceiverSerialPort implements SerialPortEventListener {

	/** BTHR918(N) sensor id (indoor temperature-humidity-pressure) */
	private final static String typeBTHR918 = "5A6D";

	/** THGR228(N) sensor id (indoor/outdoor temperature-humidity) */
	private final static String typeTHGR228 = "1A2D";

	/** THGR918(N) sensor id (outdoor temperature-humidity) */
	private final static String typeTHGR918 = "1A3D";

	private final static Logger logger = LoggerFactory.getLogger(ReceiverSerialPort.class);
	
	/** Delay after opening port and before writing to it (seconds) */
	private final static int openDelay = 2;

	/** Maximum number of attempts for a read operation */
	private final static int retryLimit = 3;

	private String portName;

	private SerialPort serialPort = null;

	private InputStream inputStream;

	private OutputStream outputStream;

	private EventObservers eventObservers;

	private Hashtable<String, String> sensorHistory = new Hashtable<String, String>();

	
	private HashSet<Observer> temperatureObservers = new HashSet<Observer>();
	private HashSet<Observer> humidityObservers = new HashSet<Observer>();
	private HashSet<Observer> unrecocnisedDeviceObservers = new HashSet<Observer>();
	
	private double prevTemperature, currentTemperature = 0;
	private int prevHumidity, currentHumidity = 0;
	
	/**
	    Verify if Oregon Scientific sensor frame has a valid checksum.

	    @param readBuffer	receive buffer
	    @return		true if checksum is valid
	 */

	public static boolean checksumOregon(StringBuffer readBuffer) {
		int checksumIndex = readBuffer.length() - 2;
		int checksumActual = readBuffer.charAt(checksumIndex);
		int checksumComputed = -10;
		for (int i = 0; i < checksumIndex; i++) {
			int value = readBuffer.charAt(i);
			checksumComputed += (value >> 4) + (value & 0XF);
		}
		// if (checksumComputed != checksumActual)
		//   Activator.logError("ReceiverSerialPort.checksumOregon: " +
		//     "message ignored - checksum invalid");
		return(checksumComputed == checksumActual);
	}

	
	
	
	
	/**
    Constructor for the ReceiverSerialPort object

    @param port		serial port
    @param mapping		sensor mapping 
    @param eventService	OSGi event service
	 */
	public ReceiverSerialPort(EventObservers eventObservers) {
		this.eventObservers = eventObservers;
	}


	/**
    Initialise the serial port used by the RFXCOM receiver.

    @param portEntity	entity to own input events
    @param port		serial port
    @return		true if initialisation of the serial port succeeds
	 */

	public boolean initialise(String entity, SerialPort port) {
		serialPort = port;
		boolean result = false;

		try {
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(38400,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			serialPort.enableReceiveTimeout(1000);
		}
		catch (IOException e1) {
			System.err.println("I/O error on port " + portName + " - " + e1);
			// e`.printStackTrace();
			serialPort.close();
			return result;
		}
		catch (IllegalStateException e2) {
			System.err.println(
					"port " + portName + " already closed - restart CommAccess");
			// e2.printStackTrace();
			return result;
		}
		catch (TooManyListenersException e2) {
			System.err.println("port " + portName + " has too many listeners");
			// e2.printStackTrace();
			return result;
		}
		catch (UnsupportedCommOperationException e4) {
			System.err.println("port " + portName + " unsupported operation");
			// e4.printStackTrace();
			return result;
		}

		serialPort.setDTR(false);
		serialPort.setRTS(false);

		try {
			initialisePort();

			// set receiver mode to be variable length
			byte[] variableLengthModeBytes = new byte[] { (byte) 0xF0, (byte) 0x2C };
			byte[] ackBytes = new byte[1];

			int tries = 0;
			while (!result && tries < retryLimit) {
				tries++;
				outputStream.write(variableLengthModeBytes);
				inputStream.read(ackBytes);
				if (ackBytes[0] == 0x2C)
					result = true;
				// else
				//   Activator.logError(
				//     "retrying after incorrect ack from port " + portName);
			}
			if (!result)
				System.err.println("incorrect ack from port " + portName +
				" - is this port connected to an RFXCOM receiver?");
		}
		catch (IOException e) {
			System.err.println("I/O error on port " + portName + " - " + e);
			// e.printStackTrace();
		}

		return result;
	}

	/**
    Close the serial port streams and remove the listener.
	 */

	public void close() {
		if (serialPort != null && inputStream != null && outputStream != null) {
			try {
				inputStream.close();
				outputStream.close();
			}
			catch (IOException e) {
				System.err.println("I/O error on port " + portName + " - " + e);
				// e.printStackTrace();
			}
			try {
				serialPort.removeEventListener();
			}
			catch (IllegalStateException e) {
				System.err.println("port " + portName + " already closed");
			}
		}
	}

	/**
    Initialise port, reading and discarding any data from the port.
	 */

	public void initialisePort() {
		try {
			Thread.sleep(1000 * openDelay);
		}
		catch (InterruptedException exception) {
			// ignore exception
		}
		try {
			if (inputStream.available() > 0) {
				// Activator.logNote("discarding old data from port " + portName);
				byte[] discard = new byte[1];
				while (inputStream.available() > 0)
					inputStream.read(discard);
			}
		}
		catch (IOException exception) {
			System.err.println(
					"ReceiverSerialPort.initialisePort: I/O exception " + exception);
		}
	}

	/**
    Handle serial port event.

    @param event  Parameter
	 */

	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			StringBuffer readBuffer = new StringBuffer();

			try {
				int lengthInBits = inputStream.read();
				if (lengthInBits != -1) {
					lengthInBits = lengthInBits & 0x7F;
					int remains = lengthInBits % 8;
					int lengthInBytes = lengthInBits / 8;
					if (remains != 0)
						lengthInBytes = lengthInBytes + 1;
					for (int i = 0; i < lengthInBytes; i++)
						readBuffer.append((char) inputStream.read());
					if (readBuffer.length() >= 5) {
						if (((readBuffer.charAt(2)) ^
								(readBuffer.charAt(3))) == 0xFF) {	// Visonic sensor
							receivedVisonic(lengthInBytes, readBuffer);
						}
						else											// Oregon sensor
							receivedOregon(lengthInBytes, readBuffer);	// handle received data

					}
					else
						System.err.println("insufficient bytes read");

				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private void receivedOregon(int byteLength, StringBuffer readBuffer)
	{
		String sensorId =
			byteToHex(readBuffer.charAt(0)) +
			byteToHex(readBuffer.charAt(1));
		if (sensorId.equals(typeBTHR918) ||
				sensorId.equals(typeTHGR228) ||
				sensorId.equals(typeTHGR918)) {
			if (byteLength >= 10 && checksumOregon(readBuffer)) { // valid frame?
				int channel = readBuffer.charAt(2);
				channel = (channel >> 4) & 0X7;
				//String messageCode = Integer.toString(channel);

				int value4 = readBuffer.charAt(4);
				int value5 = readBuffer.charAt(5);
				int value6 = readBuffer.charAt(6);
				int value7 = readBuffer.charAt(7);

				String temperature = (value6 & 0X8) == 1 ? "-" : "";
				temperature +=
					intToString(value5 >> 4) +
					intToString(value5 & 0XF) + "." +
					intToString(value4 >> 4);

				String humidity = "";
				humidity +=
					intToString(value7 & 0XF) +
					intToString(value6 >> 4);

				//			System.out.println("Sensor ID: " + sensorId);
				//			System.out.println("messageCode: " + messageCode);
				//			System.out.println("temperature: " + temperature);
				//			System.out.println("humidity: " + humidity);

				//temperatureListener.receivedOregon(temperature, humidity);
				double newTempReading = Double.parseDouble(temperature);
				if (hasTemperatureChanged(newTempReading))
				{
					Iterator<Observer> it = temperatureObservers.iterator();
					while (it.hasNext())
					{
						it.next().update(new Observable(), new TemperatureReading(sensorId, prevTemperature, Double.parseDouble(temperature), prevHumidity, Integer.parseInt(humidity)));
					}	
				}
				
				int newHumidityReading = Integer.parseInt(humidity);
				if (hasHumidityChanged(newHumidityReading))
				{
					Iterator<Observer> it = humidityObservers.iterator();
					while (it.hasNext())
					{
						it.next().update(new Observable(), new TemperatureReading(sensorId, prevTemperature, Double.parseDouble(temperature), prevHumidity, Integer.parseInt(humidity)));
					}	
				}
			}
		}
	}
	public void addUnrecocnisedDeviceListener(Observer o)
	{
		unrecocnisedDeviceObservers.add(o);
	}
	public void addTemperatureListener(Observer o)
	{
		temperatureObservers.add(o);
	}
	public void addHumidityListener(Observer o)
	{
		humidityObservers.add(o);
	}
	public void removeTemperatureListener(Observer o) {
		temperatureObservers.remove(o);
	}	
	public void removeHumidityListener(Observer o) {
		humidityObservers.remove(o);
	}
	public double getCurrentTemperature()
	{
		return currentTemperature;	
	}
	private boolean hasTemperatureChanged(double newTempReading)
	{
		if (Math.abs(newTempReading) == Math.abs(currentTemperature)) return false;
		
		prevTemperature = currentTemperature;
		currentTemperature = newTempReading;
		return true;
	}
	private boolean hasHumidityChanged(int newHumiReading)
	{
		if (newHumiReading == currentHumidity) return false;
		
		prevHumidity = currentHumidity;
		currentHumidity = newHumiReading;
		return true;
	}
	private void receivedVisonic(int lengthInBytes, StringBuffer readBuffer)
	{
		String sensorId =
			byteToHex(readBuffer.charAt(0)) +
			byteToHex(readBuffer.charAt(1)) +
			byteToHex(readBuffer.charAt(4));
		String messageCode = byteToHex(readBuffer.charAt(2));
		//System.out.println("input from sensor " + sensorId +
		//		" with code " + messageCode);

		SensorParameters sensorParameter = new SensorParameters(sensorId, messageCode);
		
		// check if alert/normal code is a repeat
		boolean validSignal = true;
		if (sensorParameter.isIgnored())
		{
			validSignal = false;	
		}
		if (messageCode.equals("04") || messageCode.equals("84")) {
			if (messageCode.equals(sensorHistory.get(sensorId))) {
				validSignal = false;
			}
		}
		if (validSignal) {
			if (sensorParameter.sensorParameterKnown()) 
			{
				logger.debug("Known Sensor Received: sensor id: " + sensorId + " " + "code:" + messageCode);
				eventObservers.update(new Observable(), sensorParameter);
				sensorHistory.put(sensorId, messageCode);
			}
			else
			{
				logger.info("Unrecognised sensor signal: sensor id: " + sensorId + " " + "code:" + messageCode);
				
				Iterator<Observer> it = unrecocnisedDeviceObservers.iterator();
				while (it.hasNext())
				{
					it.next().update(new Observable(), sensorParameter);
				}	
			}
		}
		else
		{
			logger.debug("Ignoring signal: sensor id:" + sensorId + " " + "code:" + messageCode);
		}
	}

	/**
    Convert an integer value 0..9 to a one-character decimal string.

    @param i	integer to convert
    @return	converted character
	 */

	public static String intToString(int i) {
		char ch = (0 <= i && i <= 9) ? (char) ('0' + i) : '?';
		return(Character.toString(ch));
	}


	/**
    Convert a byte to a hex string.

    @param data  the byte to convert
    @return      String the converted byte
	 */

	public static String byteToHex(char data) {
		StringBuffer buf = new StringBuffer();
		buf.append(toHexChar((data >>> 4) & 0x0F));
		buf.append(toHexChar(data & 0x0F));
		return buf.toString();
	}

	/**
    Convert an int to a hex char.

    @param i  the int to convert
    @return   char the converted char
	 */

	public static char toHexChar(int i) {
		if ((0 <= i) && (i <= 9))
			return (char) ('0' + i);
		else
			return (char) ('A' + (i - 10));
	}	

}

