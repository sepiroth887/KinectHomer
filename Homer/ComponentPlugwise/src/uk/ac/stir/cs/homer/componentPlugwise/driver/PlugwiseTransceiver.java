package uk.ac.stir.cs.homer.componentPlugwise.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.componentPlugwise.Activator;
import uk.ac.stir.cs.homer.componentPlugwise.Circle;
import uk.ac.stir.cs.homer.serviceCOMMs.CommAccess;
import uk.ac.stir.cs.homer.serviceCOMMs.SerialService;

/**
 * This class supports Plugwise Circles connected via a Stick. The Plugwise
 * network should (perhaps) have first been set up using the Source. The
 * following limitations or uncertainties apply:
 * 
 * <ul>
 * 
 * <li>
 * Various aspects of the protocol and its format are not yet known (to me or
 * others).</li>
 * 
 * <li>
 * Sequence numbers from the Circle+/Circle are not used. Instead commands are
 * sent one at a time, waiting for the response.</li>
 * 
 * <li>
 * It is unclear whether it is sufficient to periodically set the Circle+ clock,
 * or whether all Circle clocks need to be set. This is presumably OK since the
 * Plugwise documentation says that it is the Circle+ that has the clock. It
 * appears that Circles synchronise clocks with the Circle+ on the hour. It
 * appears that clocks follow UTC, and have to be read or written by adjusting
 * local time.</li>
 * 
 * <li>
 * A Circle sometimes refuses to respond to a valid request for a power buffer,
 * as if the current buffer were locked. This problem goes away after the next
 * buffer is written on the following hour. This may be related to using the
 * Source in between runs of this bundle. The Source successfully reads the
 * buffer even though the bundle does not.</li>
 * 
 * </ul>
 * 
 * <p>
 * Circles are periodically polled to perform the following tasks:
 * </p>
 * 
 * <ul>
 * 
 * <li>
 * The Circle+ clock is set every 24 hours to the PC clock, starting immediately
 * after the bundle is started.</li>
 * 
 * <li>
 * The Circle energy consumption for the past hour is retrieved at 1 minute past
 * the first hour after the bundle is started.</li>
 * 
 * <li>
 * The Circle instantaneous power consumption is retrieved at the given interval
 * in minutes, starting at this interval after the bundle is started.</li>
 * 
 * </ul>
 * 
 * <p>
 * The following kinds of Accent events are supported:
 * </p>
 * 
 * <ul>
 * 
 * <li>
 * <var>device_in(energy,lamp,lounge,14:00:00,70)</var> means 70 watt-hours have
 * been consumed by the lounge lamp in the hour starting at 2PM. The total of
 * all energy readings has the form <var>device_in(energy,,,14:00:00,130)</var>.
 * </li>
 * 
 * <li>
 * <var>device_in(power,lamp,lounge,,110)</var> 110 watts are currently being
 * consumed by the lounge lamp. The total of all power readings has the form
 * <var>device_in(power,,,,650)</var>.</li>
 * 
 * <li>
 * <var>device_out(on,lamp,lounge)</var> switches the lounge lamp on, while
 * "device_out(off,lamp,lounge)" switches it off. Any message type apart from
 * <var>on</var> and <var>off</var> is an error. If the entity name/instance are
 * not defined in the mapping given by the property files, the event is ignored.
 * </li>
 * 
 * </ul>
 * 
 * <p>
 * The protocol implementation was inspired by information from <a
 * href="http://www.maartendamen.com/category/plugwise-unleashed/">Maarten
 * Damen</a> and <a
 * href="http://roheve.wordpress.com/2011/04/24/plugwise-protocol-analyse/"
 * >Roheve</a>. Inspiration was also drawn from code by <a
 * href="https://github.com/gonium/libplugwise">Gonium</a> and by <a
 * href="https://bitbucket.org/hadara/python-plugwise/wiki/Home">Sven Petai</a>.
 * The CRC calculation was adapted from code by <a
 * href="http://www.domoticaforum.eu/viewtopic.php?f=39&t=5803">Tiz</a>.
 * </p>
 * 
 * @author Kenneth J. Turner
 * @version 1.0: 17/05/11 (KJT)
 */
public class PlugwiseTransceiver extends Observable
{

	/* ****************************** Constants ******************************* */

	/** Plugwise Stick bit rate (bps) */
	private final static int BIT_RATE = 115200;

	/** CRC polynomial (x16 + x12 + x5 + 1) */
	private static int CRC_POLYNOMIAL = 0x1021;

	/** Whether to produce debug output for error situations */
	private static boolean ERROR_DEBUG = true;

	/* ************************* Protocol Constants *************************** */

	/** Circle+ general response code (hex */
	private final static String GENERAL_RESPONSE = "0000";

	/** Circle clock get request code (hex) */
	private final static String CLOCK_GET_REQUEST = "003E";

	/** Circle clock get response code (hex) */
	private final static String CLOCK_GET_RESPONSE = "003F";

	/** Circle clock set request code (hex) */
	private final static String CLOCK_SET_REQUEST = "0016";

	/** Circle+ clock set response code (hex) */
	private final static String CLOCK_SET_RESPONSE = GENERAL_RESPONSE;

	/** Circle calibration request code (hex) */
	private final static String DEVICE_CALIBRATION_REQUEST = "0026";

	/** Circle calibration response code (hex) */
	private final static String DEVICE_CALIBRATION_RESPONSE = "0027";

	/** Circle device information request code (hex) */
	private final static String DEVICE_INFORMATION_REQUEST = "0023";

	/** Circle device information response code (hex) */
	private final static String DEVICE_INFORMATION_RESPONSE = "0024";

	/** Circle power buffer request code (hex) */
	private final static String POWER_BUFFER_REQUEST = "0048";

	/** Circle power buffer response code (hex) */
	private final static String POWER_BUFFER_RESPONSE = "0049";

	/** Circle power change (i.e. on/off) request code (hex) */
	private final static String POWER_CHANGE_REQUEST = "0017";

	/** Circle+ power change (i.e. on/off) response code (hex) */
	private final static String POWER_CHANGE_RESPONSE = GENERAL_RESPONSE;

	/** Circle power information request code (hex) */
	private final static String POWER_INFORMATION_REQUEST = "0012";

	/** Circle power information response code (hex) */
	private final static String POWER_INFORMATION_RESPONSE = "0013";

	/** Circle+ acknowledgement code (hex) */
	private final static String PROTOCOL_ACK = "00C1";

	/** Plugwise protocol header code (hex) */
	private final static String PROTOCOL_HEADER = "\u0005\u0005\u0003\u0003";

	/** Plugwise protocol trailer code (hex) */
	private final static String PROTOCOL_TRAILER = "\r\n";

	/** Amount by which to multiple pulse count to get watts */
	private final static float PULSE_FACTOR = 2.1324759f;

	/** Delay before expecting a protocol response (seconds) */
	private final static int RECEIVE_DELAY = 1;

	/** Delay (seconds) before assuming no further response data will arrive */
	private final static int RECEIVE_TIMEOUT = 2;

	/** Stick initialisation request code (hex) */
	private final static String STICK_INITIALISE_REQUEST = "000A";

	/** Stick initialisation response code (hex) */
	private final static String STICK_INITIALISE_RESPONSE = "0011";

	/** Circle+ address (second part only), fixed for all plugwise modules */
	private final static String CONTROLLER_ADDRESS = "000D6F0000";	
	
	private final static Logger logger = LoggerFactory.getLogger(PlugwiseTransceiver.class);

	/* ****************************** Variables ******************************* */

	/** Default calibration if not available */
	private Calibration calibrationDefault = new Calibration(1.0f, 0.0f, 0.0f,0.0f);

	/** Clock offset for zone and daylight saving time (minutes) */
	private int clockOffset;

	/** CRC table */
	private int[] crcTable;
	
	/** Circle+ address (second part only) */
	private String controllerAddress;

	/** Interval (minutes) between polls for recent energy consumption */
	private int energyInterval;

	/** Plugwise input stream */
	private InputStream inputStream;

	/** Plugwise output stream */
	private OutputStream outputStream;

	/** Plugwise port name */
	private String portName;

	/** Interval (minutes) between polls for instantaneous power consumption */
	private int powerInterval; 

	/** The number of attempts allowed at sending a message */
	private int retryLimit;

	/** Plugwise serial port */
	private SerialPort serialPort = null;

	
	private Set<Circle> circles;

	private ScheduledExecutorService pollingService;
	

	/* ******************************* Methods ******************************** */

	public PlugwiseTransceiver()
	{
		if (System.getProperty(Activator.PORT_NAME_PROPERTY_NAME) == null || System.getProperty(Activator.ENERGY_INTERVAL_PROPERTY_NAME)== null
				|| System.getProperty(Activator.POWER_INTERVAL_PROPERTY_NAME) == null || System.getProperty(Activator.RETRY_LIMIT_PROPRETY_NAME) == null)
		{
			logger.error("PLEASE ENSUSRE THE FOLLOWING PROPERTIES ARE SET:");
			logger.error("\tuk.ac.stir.cs.homer.componentPlugwise.port");
			logger.error("\tuk.ac.stir.cs.homer.componentPlugwise.retryLimit");
			logger.error("\tuk.ac.stir.cs.homer.componentPlugwise.powerIntervalPollSecs");
			logger.error("\tuk.ac.stir.cs.homer.componentPlugwise.energyIntervalPollMins");
			return;
		}
		this.portName = System.getProperty(Activator.PORT_NAME_PROPERTY_NAME);
		this.energyInterval = Integer.parseInt(System.getProperty(Activator.ENERGY_INTERVAL_PROPERTY_NAME));
		this.powerInterval = Integer.parseInt(System.getProperty(Activator.POWER_INTERVAL_PROPERTY_NAME));
		this.retryLimit = Integer.parseInt(System.getProperty(Activator.RETRY_LIMIT_PROPRETY_NAME));
		
		this.crcTable = getCRCTable();
		
		this.circles = new HashSet<Circle>();
		
		Calendar calendar = Calendar.getInstance(); // get current date/time
		this.clockOffset = (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000);
		
		if (initialisePort())
		{
			if (initialisePlugwise())
			{
				startPollingAllCircles();
			}
		}
	}
	
	public void addCircle(Circle circle)
	{
		calculateCalibration(circle);
		this.circles.add(circle);
		if (circle.isPlus())
		{
			controllerAddress = circle.getAddress();
		}
	}
	
	public void removeCircle(Circle circle)
	{
		this.circles.remove(circle);
	}
	
	/**
	 * Initialise the serial port used by the Plugwise receiver.
	 * 
	 * @param portEntity
	 *            entity to own input events
	 * @param serialPort
	 *            serial port
	 * @return true if initialisation of the serial port succeeds
	 */
	private boolean initialisePort()
	{
		SerialService port = new CommAccess().openPort(portName);
		if (port == null) return false;
		
		this.serialPort = port.getPort();
		boolean result = false;

		try
		{
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
			serialPort.setSerialPortParams(BIT_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.enableReceiveTimeout(1000 * RECEIVE_TIMEOUT);
			result = true;
		} catch (IOException exception1)
		{
			logger.error("initialise", "I/O error on port '" + portName
					+ "' - " , exception1);
			serialPort.close();
			return (result);
		} catch (IllegalStateException exception2)
		{
			logger.error("initialise", "port '" + portName
					+ "' already closed - restart CommAccess");
			return (result);
		} catch (UnsupportedCommOperationException exception4)
		{
			logger.error("initialise", "port '" + portName
					+ "' unsupported operation");
			return (result);
		}

		serialPort.setDTR(false);
		serialPort.setRTS(false);
		return (result);
	}

	/** Close the serial port streams and remove the listener. */
	public void close()
	{
		if (serialPort != null && inputStream != null && outputStream != null)
		{
			try
			{
				inputStream.close();
				outputStream.close();
			} catch (IOException exception)
			{
				logger.error("close", "I/O error on port '" + portName
						+ "' - " , exception);
			}
			try
			{
				serialPort.removeEventListener();
			} catch (IllegalStateException exception)
			{
				logger.error("close", "port " + portName
						+ " already closed");
			}
		}
		pollingService.shutdownNow();
	}

	/**
	 * Check received data, returning the payload if the response is valid (or
	 * null if not). Global "circleAddress" is set to the address of the
	 * responding Circle.
	 * 
	 * @param command
	 *            command for which a response is expected
	 * @param buffer
	 *            string of characters
	 * @return payload for valid response (null if invalid)
	 */
	private String checkData(String command, String buffer)
	{
		// System.err.println("buffer <" + buffer + ">"); // TEMP
		// for (int i = 0; i < buffer.length(); i++)
		// System.err.print(String.format("%04X ", ((int) buffer.charAt(i))));
		// System.err.println();
		String response = null; // initialise response
		String code = GENERAL_RESPONSE; // set Circle+ response code
		String header = PROTOCOL_HEADER + code; // set response header
		int position1 = buffer.indexOf(header); // check if response found
		// System.err.println("code1 <" + code + "> position1 <" + position1 +
		// ">"); // TEMP
		if (position1 != -1)
		{ // response header found?
			code = // get acknowledgement code
			buffer.substring(position1 + 12, position1 + 16);
			// System.err.println("code2 <" + code + ">"); // TEMP
			if (code.equals(PROTOCOL_ACK))
			{ // Circle+ acknowledgement?
				code = responseCode(command); // set command response code
				header = PROTOCOL_HEADER + code; // set response header
				if (!code.equals(GENERAL_RESPONSE)) // not general response
													// code?
					position1 = // check after Circle+ response
					buffer.indexOf(header, position1 + 20);
				// System.err.println("code3 <" + code + "> position1 <" +
				// position1 + ">"); // TEMP
				if (position1 != -1)
				{ // response header found?
					position1 += PROTOCOL_HEADER.length();// move past header to
															// content
					int position2 = // get trailer start
					buffer.indexOf(PROTOCOL_TRAILER, position1);
					if (position2 != -1)
					{ // trailer found?
						position2 -= 4; // move to before CRC
						String crcExpected = // get expected CRC
						getCRCString(buffer.substring(position1, position2));
						String message = // get message
						buffer.substring(position1, position2);
						position1 += code.length(); // move to payload start
						String content = // get payload
						buffer.substring(position1, position2);
						String crcActual = // get actual CRC
						buffer.substring(position2, position2 + 4);
						if (crcExpected.equals(crcActual))
						{// CRCs match?
							response = content; // store response
								logger.trace( // log received message
										"checkData", "receiving '"
												+ formatBuffer(message) + "'");
						} else  // CRC mismatch, debug?
							logger.trace(
											"checkData",
											"protocol response to "
													+ commandName(command)
													+ " ignored due to invalid checksum");
					} else if (ERROR_DEBUG) // trailer not found, debug?
						logger.debug("checkData", "protocol response to "
								+ commandName(command) + "lacks trailer");
				} else if (ERROR_DEBUG) // no response header, debug?
					logger.debug("checkData",
							"protocol response header for "
									+ commandName(command) + " not received");
			} else if (ERROR_DEBUG) // no Circle+ ack, debug?
				logger.debug("checkData", "protocol response to "
						+ commandName(command) + " is negative");
		} else if (ERROR_DEBUG) // no response header, debug?
			logger.debug("checkData", "protocol response header for "
					+ commandName(command) + " not received");
		return (response); // return response
	}

	/**
	 * Return a user-friendly name for a command code.
	 * 
	 * @param command
	 *            command code
	 * @return user-friendly name for command
	 */
	private String commandName(String command)
	{
		String name = "?";
		if (command.equals(POWER_BUFFER_REQUEST)) 
			name = "power buffer request"; 
		else if (command.equals( DEVICE_CALIBRATION_REQUEST))
			name = "calibration request"; 
		else if (command.equals(DEVICE_INFORMATION_REQUEST))
			name = "device information request"; 
		else if (command.equals(CLOCK_GET_REQUEST))
			name = "clock get request";
		else if (command.equals(CLOCK_SET_REQUEST))
			name = "clock set request"; 
		else if (command.equals(POWER_CHANGE_REQUEST))
			name = "power change request"; 
		else if (command.equals(POWER_INFORMATION_REQUEST))
			name = "power information request"; 
		else if (command.equals(STICK_INITIALISE_REQUEST))
			name = "stick initialisation request";
		return (name); 
	}

	/**
	 * Convert a string of characters into a string of hex digits in groups of
	 * four.
	 * 
	 * @param buffer
	 *            string of characters
	 * @return string of hex digits
	 */
	private String formatBuffer(String buffer)
	{
		String hexString = "";
		if (buffer != null)
		{
			for (int i = 0; i < buffer.length(); i++)
			{
				if (i > 0 && i % 4 == 0)
					hexString += " ";
				hexString += buffer.charAt(i);
			}
		}
		return (hexString);
	}

	/**
	 * Return CRC for a hext string as a hex string.
	 * 
	 * @param buffer
	 *            send/receive buffer
	 * @return CRC as hex string
	 */
	private String getCRCString(String buffer)
	{
		int work = 0x0000;
		byte[] bytes = buffer.getBytes();
		// loop, calculating CRC for each byte of the string
		for (byte b : bytes)
			// xor next byte with high byte so far to look up CRC table
			// xor that with low byte so far and mask back to 16 bits
			work = (crcTable[(b ^ (work >>> 8)) & 0xff] ^ (work << 8)) & 0xffff;
		return (Integer.toHexString(work).toUpperCase());
	}

	/**
	 * Return the CRC table.
	 * 
	 * @return CRC table
	 */
	private int[] getCRCTable()
	{
		int[] crcTable = new int[256];
		for (int i = 0; i < 256; i++)
		{
			int fcs = 0;
			int d = i << 8;
			for (int k = 0; k < 8; k++)
			{
				if (((fcs ^ d) & 0x8000) != 0)
					fcs = (fcs << 1) ^ CRC_POLYNOMIAL;
				else
					fcs = (fcs << 1);
				d <<= 1;
				fcs &= 0xffff;
			}
			crcTable[i] = fcs;
		}
		return (crcTable);
	}

	/**
	 * Return a float from the given hex digit string.
	 * 
	 * @param buffer
	 *            receive buffer
	 * @param start
	 *            start of float
	 * @param length
	 *            length of float (8 hex digits)
	 * @return converted float
	 */
	private float getFloat(String buffer, int start, int length)
	{
		float number = 0.0f;
		if (length == 8)
		{
			int floatBits = getInt(buffer, start, length);
			number = Float.intBitsToFloat(floatBits);
		} else
			logger.error("getFloat", "float length for '" + length
					+ "' not 8");
		return (number);
	}

	/**
	 * Return an integer from the given hex digit string.
	 * 
	 * @param buffer
	 *            receive buffer
	 * @param start
	 *            start of integer
	 * @param length
	 *            length of integer (1 to 8 hex digits)
	 * @return converted integer
	 */
	private int getInt(String buffer, int start, int length)
	{
		int number = 0;
		if (1 <= length && length <= 8)
		{
			String numberString = buffer.substring(start, start + length);
			try
			{
				long numberLong = // parse as long for 16 bits
				Long.parseLong(numberString, 16);
				number = (int) (numberLong & 0XFFFFFFFF);
			} catch (NumberFormatException exception)
			{
				logger.error("getInt", "incorrect format for integer '"
						+ numberString + "'");
			}
		} else
			logger.error("getInt", "integer length for '" + length
					+ "' not 1 to 8");
		return (number);
	}

	/**
	 * Return power consumption in watts for the given Circle calibration and
	 * pulse count measured over some period.
	 * 
	 * @param calibration
	 *            Circle calibration settings
	 * @param pulses
	 *            pulse count over period
	 * @param period
	 *            period in seconds
	 * @return corrected pulse count
	 */
	private float getPower(Calibration calibration, int pulses, int period)
	{
		float power = 0.0f; // assume no power consumed
		if (period > 0)
		{ // non-zero period?
			if (pulses != 0 && // meaningful pulse count?
					pulses != 65535 && pulses != -1)
			{
				float gainA = calibration.getGainA(); // get Circle gains and
														// offsets
				float gainB = calibration.getGainB();
				float offsetNoise = calibration.getOffsetNoise();
				float offsetTotal = calibration.getOffsetTotal();
				float pulsesAverage = // get pulses per second
				((float) pulses) / period;
				float pulsesOffsetNoise = // correct pulses for noise
				pulsesAverage + offsetNoise;
				float pulsesCorrected = // correct pulses for gains
				pulsesOffsetNoise * pulsesOffsetNoise * gainB
						+ pulsesOffsetNoise * gainA + offsetTotal;
				power = PULSE_FACTOR * pulsesCorrected; // get power in watts
			}
		} else
			// zero period
			logger.error("getPower", "period '" + period
					+ "' must be positive");
		return (power);
	}

	/**
	 * Return a time HH:MM:SS from the given minutes relative to midnight of the
	 * start of the current month.
	 * 
	 * @param minutes
	 *            minutes relative to month start
	 * @return converted time (HH:MM:SS)
	 */
	private String getTime(int minutes)
	{
		minutes = minutes % (24 * 60); // get minutes from day start
		int hour = minutes / 60; // get hours
		int minute = minutes % 60; // get minutes from hour start
		String time = String.format("%02d:%02d:00", hour, minute);
		return (time);
	}

	/**
	 * Initialise port and Stick.
	 * 
	 * @return true if initialisation port and Plugwise Stick succeeds
	 */

	private boolean initialisePlugwise()
	{
		boolean online = false; // assume offline
		try
		{ // try to communicate
			if (inputStream.available() > 0)
			{ // left-over input?
					logger.debug("initialisePort",
							"discarding old data from port '" + portName + "'");
				while (inputStream.available() > 0)
					// while left-over data
					inputStream.read(); // read and ignore it
			}
			online = requestInitialisation(); // initialise Stick
			if (!online)
				logger.error("initialisePort", "Plugwise network offline");
		} catch (IOException exception)
		{
			logger.error("initialisePort", "I/O exception - " , exception);
		}
		return online; // return online result
	}

	/**
	 * Poll all Circles for the given task type, posting events for energy/power
	 * consumption:
	 * 
	 * <ul>
	 * 
	 * <li>The Circle clock is set to the current PC time.</li>
	 * 
	 * <li>
	 * The Circle energy consumption for the past hour is retrieved. An energy
	 * event trigger is then sent with parameters such as
	 * "energy,lamp,lounge,14:00:00,70", meaning 70 watt-hours consumed by the
	 * lounge lamp in the hour leading up to 3PM. The total of all energy
	 * readings is then sent in the form "energy,,,14:00:00,130".</li>
	 * 
	 * <li>
	 * The Circle instantaneous power consumption is retrieved. A power event
	 * trigger is then sent with parameters such as ""power,lamp,lounge,,110",
	 * meaning 110 watts are currently being consumed by the lounge lamp. The
	 * total of all power readings is then sent in the form "power,,,,250".</li>
	 * 
	 * </ul>
	 * 
	 * @param Task taskType
	 */
	public int getEnergy(Circle circle)
	{
		return requestEnergy(circle).getEnergy();
	}
	
	public int getPower(Circle circle)
	{
		return requestPower(circle);
	}


	/**
	 * Receive data in response to a command, returning the payload if the
	 * response is valid (or null if not).
	 * 
	 * @param command
	 *            command for which a response is expected
	 * @return payload for valid response (null if invalid)
	 */
	private String receiveData(String command)
	{
		String response = null; // initialise response
		try
		{
			Vector<Byte> bytes = new Vector<Byte>(); // initialise byte list
			int byteLength = 0; // initialise byte count
			while (inputStream.available() != 0)
			{ // data available?
				byte b = (byte) inputStream.read(); // read bytes repeatedly
				byteLength++; // increment byte count
				bytes.add(b); // append byte value
			}
			if (byteLength != 0)
			{ // data read?
				StringBuffer buffer = // initialise buffer
				new StringBuffer(byteLength);
				for (int i = 0; i < byteLength; i++)
				{ // convert bytes to characters
					byte b = bytes.get(i); // get byte
					buffer.append((char) b); // append as character
				}
				response = // get response (if response OK)
				checkData(command, buffer.toString());
			} else  // nothing read, debug?
				logger.debug("receiveData", "protocol response to "
						+ commandName(command) + " not received");
		} catch (IOException exception)
		{
			logger.error("receiveData",
					"I/O exception reading response to " + commandName(command)
							+ " - " , exception);
		}
		return (response); // return response
	}

	/**
	 * Request Circle calibration, returning calibration settings from the
	 * response (null if calibration not retrieved).
	 * 
	 * @param circleAddress
	 *            MAC address of Circle (second part only)
	 * @return Circle calibration settings
	 */
	private Calibration requestCalibration(String circleAddress)
	{
		Calibration calibration = null; // initialise no calibration
		String response = // get calibration response
		sendCommand(DEVICE_CALIBRATION_REQUEST, circleAddress, null);
		if (response != null)
		{ // response received?
			float gainA = getFloat(response, 20, 8); // get gains and offsets
			float gainB = getFloat(response, 28, 8);
			float offsetNoise = getFloat(response, 44, 8);
			float offsetTotal = getFloat(response, 36, 8);
			calibration = // create calibration
			new Calibration(gainA, gainB, offsetNoise, offsetTotal);
		}
		if (calibration == null) calibration = calibrationDefault;
		return (calibration); // return calibration
	}

	/**
	 * Request a change in the power setting of a Circle, switching it on or
	 * off.
	 * 
	 * @param circleAddress
	 *            circle address
	 * @param power
	 *            true/false to switch on/off
	 */
	public boolean changeState(Circle circle, boolean power)
	{
		String parameters = power ? "01" : "00"; // set power parameter
		String response = // get change response
		sendCommand(POWER_CHANGE_REQUEST, circle.getAddress(), parameters);
		if (response == null) // no response received?
		{
			logger.debug("requestChange", "could not change power state for " + circle.getAddress());
			return false;
		}
		return true;
	}

	/**
	 * Request Circle energy, returning the power consumption for the last hour
	 * (time of reading and energy in watt-hours, null if energy not obtained).
	 * 
	 * @param circleAddress
	 *            MAC address of Circle (second part only)
	 * @return energy consumption reading/energy for last hour
	 */
	private Energy requestEnergy(Circle circle)
	{
		Energy energy = null; // initialise no energy reading
		int logAddress = // get current log address
		requestInformation(circle.getAddress());
		logAddress -= 8; // go to last filled buffer
		if (logAddress != 0)
		{ // log address obtained?
			String logAddressString = // get log address in hex
			String.format("%08X", logAddress);
			Calibration calibration = circle.getCalibration();
			if (calibration != null)
			{ // calibration obtained?
				String response = // get power buffer response
				sendCommand(POWER_BUFFER_REQUEST, circle.getAddress(),
						logAddressString);
				if (response != null)
				{ // response received?
					// int year = getInt(response, 20, 2) + 2000; // get year
					// int month = getInt(response, 22, 2); // get month
					int minutes = getInt(response, 24, 4);// get minutes from
															// month start
					String time = getTime(minutes); // get time from minutes
					int pulses = // get pulses for last buffer
					getInt(response, 28, 8);
					int energyConsumption = // get power for last power
					Math.round(getPower(calibration, pulses, 3600));
					energy = new Energy(time, energyConsumption);
					logger.debug("circle " + circle.getAddress(), "time "
										+ time + " energyConsumption "
										+ energyConsumption);
				}
			}
		}
		return (energy); // return energy reading
	}

	/**
	 * Request Circle information, returning current log address from the
	 * response (0 if device information not retrieved).
	 * 
	 * @param circleAddress
	 *            MAC address of Circle (second part only)
	 * @return Circle current log address
	 */
	private int requestInformation(String circleAddress)
	{
		int logAddress = 0; // initialise no log address
		String response = // get information response
		sendCommand(DEVICE_INFORMATION_REQUEST, circleAddress, null);
		if (response != null) // response received?
			logAddress = getInt(response, 28, 8); // get last log address
		return (logAddress); // return log address
	}

	/**
	 * Request Stick initialisation, returning network status from the response
	 * (false if status not obtained).
	 * 
	 * @return true/false if network is/is not online
	 */
	private boolean requestInitialisation()
	{
		boolean networkStatus = false; // initialise network offline
		String response = // get initialisation response
		sendCommand(STICK_INITIALISE_REQUEST, null, null);
		if (response != null) // response received?
			networkStatus = // get network status
			getInt(response, 22, 2) == 1;
		return (networkStatus); // return network status
	}

	/**
	 * Request Circle power, returning the 8-second instantaneous power
	 * consumption (watts, -1 if power not obtained).
	 * 
	 * @param circle MAC address of Circle (second part only)
	 * @return instantaneous power consumption (watts)
	 */
	private int requestPower(Circle circle)
	{
		float powerConsumption = -1.0f; // initialise no power
		Calibration calibration = circle.getCalibration();
		if (calibration != null)
		{ // calibration obtained?
			String response = // get power response
			sendCommand(POWER_INFORMATION_REQUEST, circle.getAddress(), null);
			if (response != null)
			{ // response received?
				// use "getInt(response, 20, 4)" for 1-second power string
				int pulseCount = // get 8-second pulse count
				getInt(response, 24, 4);
				powerConsumption = // get 8-second power
				getPower(calibration, pulseCount, 8);
			}
		}
		return (Math.round(powerConsumption)); // return power consumption
	}

	/**
	 * Request setting of a Circle(+) clock to the current time and log address
	 * to a new value (use null to leave log buffers alone).
	 * 
	 * @param circleAddress
	 *            circle address
	 * @param logAddress
	 *            log address (null to leave alone)
	 */
	private void requestSetting(String circleAddress, String logAddress)
	{
		Calendar calendar = Calendar.getInstance(); // get current date/time
		calendar.add(Calendar.MINUTE, -clockOffset);// less clock offset in
													// minutes
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1; // Jan supplied as 0 not 1
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		// get day number in week and minutes since the start of the month
		int weekDay = // Java 1-7 = Plugwise 0-6
		calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int minutes = 24 * 60 * (day - 1) + 60 * hour + minute;
		String date = // set Plugwise date
		String.format("%02X%02X%04X", year - 2000, month, minutes);
		if (logAddress == null) // no log address?
			logAddress = "FFFFFFFF"; // set no change to log buffers
		String time = // set Plugwise time
		String.format("%02X%02X%02X%02X", hour, minute, second, weekDay);
		String parameters = date + logAddress + time;
		String response = // get clock setting response
		sendCommand(CLOCK_SET_REQUEST, circleAddress, parameters);
		if (response == null) // no response received?
			logger.debug("requestSetting", "could not set clock for "
						+ circleAddress);
	}

	/**
	 * Return the response code for the given command code.
	 * 
	 * @param command
	 *            command code
	 * @return response code
	 */
	private String responseCode(String command)
	{
		String response = GENERAL_RESPONSE; // initialise default code
		if (command.equals(POWER_BUFFER_REQUEST))
			response = POWER_BUFFER_RESPONSE;
		else if (command.equals(DEVICE_CALIBRATION_REQUEST))
			response = DEVICE_CALIBRATION_RESPONSE;
		else if (command.equals(CLOCK_GET_REQUEST))
			response = CLOCK_GET_RESPONSE;
		else if (command.equals(CLOCK_SET_REQUEST))
			response = CLOCK_SET_RESPONSE;
		else if (command.equals(DEVICE_INFORMATION_REQUEST))
			response = DEVICE_INFORMATION_RESPONSE;
		else if (command.equals(POWER_CHANGE_REQUEST))
			response = POWER_CHANGE_RESPONSE;
		else if (command.equals(POWER_INFORMATION_REQUEST))
			response = POWER_INFORMATION_RESPONSE;
		else if (command.equals(STICK_INITIALISE_REQUEST))
			response = STICK_INITIALISE_RESPONSE;
		return (response);
	}

	/**
	 * Send command to Stick, returning the payload if the response is valid (or
	 * null if not). This method is synchronised as it is called by independent
	 * threads, but each command/response pair must be handled separately.
	 * 
	 * @param command
	 *            module command (null to omit)
	 * @param address
	 *            module address (second partly only, null to omit)
	 * @param parameters
	 *            module parameters (null to omit)
	 * @return payload for valid response (null if invalid)
	 */
	private synchronized String sendCommand(String command, String address,
			String parameters)
	{
		String response = null; // initialise response
		String message = ""; // initialise message
		if (command != null) // command not null?
			message += command; // append it
		if (address != null) // address not null?
			message += CONTROLLER_ADDRESS + address; // append full address
		if (parameters != null) // parameters not null
			message += parameters; // append it
		logger.trace("sendCommand", "sending '" + formatBuffer(message) + "'");
		String crc = getCRCString(message); // get CRC for message
		String request = // put request together
		PROTOCOL_HEADER + message + crc + PROTOCOL_TRAILER;
		byte[] bytes = stringToBytes(request); // convert to hex byte string
		int retryCount = 0; // initialise retry count
		while (response == null)
		{ // no valid response yet?
			try
			{ // try to write request
				retryCount++; // increment retry count
				outputStream.write(bytes); // write request
				wait(RECEIVE_DELAY); // wait before trying read
				response = receiveData(command); // get response payload
				if (response == null)
				{ // invalid response?
					if (retryCount >= retryLimit)
					{ // at retry limit?
						logger.error("sendCommand",
								"retry limit reached with "
										+ commandName(command) + " for "
										+ address);
						break; // leave retry loop
					} else  // not at retry limit, debug?
						logger.trace("sendCommand", "retrying "
								+ commandName(command) + " for "
								+ address);
				}
			} catch (IOException exception)
			{ // write failed?
				logger.error("sendCommand", "could not send message - ", exception);
			}
		}
		return (response); // return response payload
	}

	/**
	 * Set up periodic polling for clock setting, energy consumption and power
	 * consumption.x
	 * 
	 * <ul>
	 * 
	 * <li>
	 * The Circle+ clock is set every 24 hours, starting immediately after this
	 * method is called.</li>
	 * 
	 * <li>
	 * Energy is measured at the given interval, starting at 1 minute past the
	 * first hour after this method is called. This allows for Circles to update
	 * their power logs on the hour.</li>
	 * 
	 * <li>
	 * Power is measured at the given interval in minutes, starting at this
	 * interval after this method is called.</li>
	 * 
	 * </ul>
	 */
	private void startPollingAllCircles()
	{
		pollingService = Executors.newSingleThreadScheduledExecutor();
		pollingService.scheduleWithFixedDelay(new Runnable(){

			@Override
			public void run()
			{
				if (controllerAddress != null)
				{
					requestSetting(controllerAddress, null);
				}
			}
	
		}, 0, 24 * 60 * energyInterval, TimeUnit.MINUTES);

		pollingService.scheduleWithFixedDelay(new Runnable(){

			@Override
			public void run()
			{
				for (Circle circle: circles)
				{
					requestEnergy(circle);
				}
			}
	
		}, 0, energyInterval, TimeUnit.MINUTES);
		
		pollingService.scheduleWithFixedDelay(new Runnable(){

			@Override
			public void run()
			{
				logger.trace("power requests being made!");

				for (Circle circle: circles)
				{
					int power = requestPower(circle);
					if (circle.recordNewPowerReading(power))
					{
						logger.info("power for: {} is: {}", circle.getAddress(), power);
						updateListenersStateChanged(circle);
					}
				}
			}
	
		}, 0, powerInterval, TimeUnit.SECONDS);
		
	}
	
	private void updateListenersStateChanged(Circle c)
	{
		this.setChanged();
		this.notifyObservers(c);
	}

	/**
	 * Using a thread to avoid bundle start delays, get calibration settings for
	 * each Circle and store them in global "calibrationMapping". Use the
	 * default calibration if it cannot be retrieved. Following calibration
	 * attempts, set up polling.
	 */
	private void calculateCalibration(final Circle circle)
	{
		Thread calibrationThread = new Thread()
		{
			public void run()
			{
				String circleAddress = circle.getAddress();
				logger.debug("Getting calibration for " + circle);
				Calibration calibration = requestCalibration(circleAddress);
				circle.setCalibration(calibration);
			}
		};
		calibrationThread.start();
	}

	/**
	 * Convert a string of text to a byte array.
	 * 
	 * @param text
	 *            character string
	 * @return byte array
	 */
	private byte[] stringToBytes(String text)
	{
		int count = text.length();
		byte[] bytes = new byte[count];
		for (int i = 0; i < count; i++)
		{
			char ch = text.charAt(i);
			byte value = (byte) (ch & 0XFF);
			bytes[i] = value;
		}
		return (bytes);
	}

	/**
	 * Wait for the given number of seconds.
	 * 
	 * @param wait
	 *            delay in seconds
	 */
	private void wait(int wait)
	{
		try
		{
			Thread.sleep(1000 * wait);
		} catch (InterruptedException exception)
		{
			// ignore exception
		}
	}
}
