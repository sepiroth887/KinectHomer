/*
    Copyright (C) 1999  Jesse E. Peterson
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 */
package com.jpeterson.x10.module;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.TooManyListenersException;
import java.util.Vector;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import com.jpeterson.util.BinaryFormat;
import com.jpeterson.util.Condition;
import com.jpeterson.util.HexFormat;
import com.jpeterson.util.Unsigned;
import com.jpeterson.x10.ControlEvent;
import com.jpeterson.x10.Gateway;
import com.jpeterson.x10.GatewayException;
import com.jpeterson.x10.GatewayListener;
import com.jpeterson.x10.GatewayStateError;
import com.jpeterson.x10.InterruptedTransmissionException;
import com.jpeterson.x10.SerialGateway;
import com.jpeterson.x10.TooManyAttemptsException;
import com.jpeterson.x10.Transmitter;
import com.jpeterson.x10.TransmitterEvent;
import com.jpeterson.x10.TransmitterListener;
import com.jpeterson.x10.X10Util;
import com.jpeterson.x10.event.AddressEvent;
import com.jpeterson.x10.event.AddressListener;
import com.jpeterson.x10.event.AllLightsOffEvent;
import com.jpeterson.x10.event.AllLightsOnEvent;
import com.jpeterson.x10.event.AllUnitsOffEvent;
import com.jpeterson.x10.event.BrightEvent;
import com.jpeterson.x10.event.DimEvent;
import com.jpeterson.x10.event.ExtendedCodeEvent;
import com.jpeterson.x10.event.ExtendedDataTransferEvent;
import com.jpeterson.x10.event.FunctionEvent;
import com.jpeterson.x10.event.FunctionListener;
import com.jpeterson.x10.event.HailAcknowledgeEvent;
import com.jpeterson.x10.event.HailRequestEvent;
import com.jpeterson.x10.event.OffEvent;
import com.jpeterson.x10.event.OnEvent;
import com.jpeterson.x10.event.PresetDim1Event;
import com.jpeterson.x10.event.PresetDim2Event;
import com.jpeterson.x10.event.StatusOffEvent;
import com.jpeterson.x10.event.StatusOnEvent;
import com.jpeterson.x10.event.StatusRequestEvent;
import com.jpeterson.x10.event.X10Event;
import com.jpeterson.x10.module.event.CM11AEvent;
import com.jpeterson.x10.module.event.CM11AListener;
import com.jpeterson.x10.module.event.CM11AStatusEvent;
import com.jpeterson.x10.module.event.CM11AStatusListener;

/**
 * Gateway to X10 CM11A serial interface unit. The CM11A is both a producer and
 * consumer of X10Events. It receives X10Events from other software components
 * and transmits the event through the X10 protocol. It sends X10Events that it
 * receives on the power line.
 *
 * @author    Jesse Peterson <jesse@jpeterson.com>
 * @created   13 May 2009
 */
public class CM11A extends SerialGateway implements Runnable,
SerialPortEventListener, Serializable, Transmitter {
	private transient Vector<CM11AListener> eventListeners = null;
	private transient Vector<CM11AStatusListener> statusListeners = null;
	private transient Vector<EventListener> addressListeners = null;
	private transient Vector<EventListener> functionListeners = null;

	private transient SerialPort serialPort;
	private transient OutputStream outputStream;
	private transient InputStream inputStream;

	private transient Thread processThread;
	private transient Vector<CM11ATransmissionEvent> transmissionQueue;
	private transient Condition shouldProcess;
	private char monitoredHouseCode;
	private BitSet onOffStatus;
	private BitSet dimStatus;
	private BitSet lastAddressedDevice;
	private int currentDay;
	private int julianDay;
	private int hours, minutes, seconds;
	private int batteryUsage;
	private boolean powerFailureAutoRecover;

	private Hashtable<Macro, Integer> macroOffsets;
	private Hashtable<Integer, Object> offset2macro;
	private Vector<TimerInitiator> timerInitiators;
	private Vector<MacroInitiator> macroInitiators;

	private byte[] eeprom;
	private final static int EEPROM_SIZE = 1024;                   // 1k, 1024 bytes max
	private final static int PAGE = 16;
	private final static int SIZEOF_INITIAL_OFFSET = 2;
	private final static int SIZEOF_TIMER_TERMINATOR = 1;
	private final static byte TIMER_TERMINATOR = (byte) 0xff;

	/** Description of the Field */
	public final static byte CM11_RECEIVE_EVENT = (byte) 0x5a;
	/** Description of the Field */
	public final static byte CM11_POWER_FAILURE = (byte) 0xa5;
	/** Description of the Field */
	public final static byte CM11_MACRO_INITIATED = (byte) 0x5b;
	private final static byte CM11_RECEIVE_EVENT_RSP = (byte) 0xc3;
	private final static byte CM11_RING_ENABLE = (byte) 0xeb;
	private final static byte CM11_CLOCK_DOWNLOAD = (byte) 0x9b;
	private final static byte CM11_MACRO_DOWNLOAD_INITIATOR = (byte) 0xfb;

	private final static int[] value2deviceCode = {
		13,				// position 0
		5,				// position 1
		3,				// position 2
		11,				// position 3
		15,				// position 4
		7,				// position 5
		1,				// position 6
		9,				// position 7
		14,				// position 8
		6,				// position 9
		4,				// position 10
		12,				// position 11
		16,				// position 12
		8,				// position 13
		2,				// position 14
		10};				// position 15

	private final static char[] value2houseCode = {
		'M',			// value 0
		'E',			// value 1
		'C',			// value 2
		'K',			// value 3
		'O',			// value 4
		'G',			// value 5
		'A',			// value 6
		'I',			// value 7
		'N',			// value 8
		'F',			// value 9
		'D',			// value 10
		'L',			// value 11
		'P',			// value 12
		'H',			// value 13
		'B',			// value 14
	'J'};			// value 15

	private static Hashtable<Integer, String> value2day;

	static {
		value2day = new Hashtable<Integer, String>();
		value2day.put(new Integer(1), "Sunday");
		value2day.put(new Integer(2), "Monday");
		value2day.put(new Integer(4), "Tuesday");
		value2day.put(new Integer(8), "Wednesday");
		value2day.put(new Integer(16), "Thursday");
		value2day.put(new Integer(32), "Friday");
		value2day.put(new Integer(64), "Saturday");
	}

	// maximum number of days in a month. The index into the array is the
	// month, zero based. e.g., 0 for January, 1 for February, 11 for
	// December.
	private final static int[] daysInMonth = {31, 29, 31, 30, 31, 30, 31, 31,
		30, 31, 30, 31};

	/**
	 * Construct a new CM11A object.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public CM11A() {
		super();
		powerFailureAutoRecover = true;	// true by default
		setSerialPort(null);
		setPortName("COM2");
		setBaudRate(4800);
		setDataBits(SerialPort.DATABITS_8);
		setStopBits(SerialPort.STOPBITS_1);
		setParity(SerialPort.PARITY_NONE);
		shouldProcess = new Condition(false);
		transmissionQueue = new Vector<CM11ATransmissionEvent>();
		macroOffsets = new Hashtable<Macro, Integer>();
		offset2macro = new Hashtable<Integer, Object>();
		timerInitiators = new Vector<TimerInitiator>();
		macroInitiators = new Vector<MacroInitiator>();
		eeprom = new byte[EEPROM_SIZE];
		monitoredHouseCode = 'A';
		onOffStatus = new BitSet(16);
		dimStatus = new BitSet(16);
		lastAddressedDevice = new BitSet(16);
		setGatewayState(Transmitter.QUEUE_EMPTY);
	}

	/**
	 * Indicate if you want the CM11A object to autorecover upon detecting a power
	 * failure. If set to true, when the CM11A object detects a power failure
	 * signal from the CM11A device, the command to set the clock will be sent. If
	 * set to false, it is up to another device to set the clock via a call to
	 * <code>setClock</code> before the CM11A can be used. <p>
	 *
	 * By default, auto recover is turned on.
	 *
	 * @param autoRecover  True if the object should automatically recover upon
	 *      detecting a power failure of the CM11A device.
	 * @see                getPowerFailureAutoRecover
	 * @see                setClock
	 * @author             <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public void setPowerFailureAutoRecover(boolean autoRecover) {
		powerFailureAutoRecover = autoRecover;
	}

	/**
	 * Determine if this CM11A object has been configured to auto recover upon
	 * sensing a power failure at the CM11A device. <p>
	 *
	 * By default, auto recover is turned on.
	 *
	 * @return   True if auto recover is turned on, false otherwise.
	 * @see      setPowerFailureAutoRecover
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public boolean getPowerFailureAutoRecover() {
		return (powerFailureAutoRecover);
	}

	/**
	 * Encapsulates state transition rules. Only implements Transmitter specific
	 * states. Lets parent's stateTransition handle the generic gateway states.
	 *
	 * @param state  Parameter
	 * @author       Jesse Peterson <6/29/99>
	 */
	public void stateTransition(long state) {
		int id = 0;
		long oldGatewayState;
		long newGatewayState;
		boolean topOfQueueChanged = false;
		oldGatewayState = getGatewayState();
		newGatewayState = oldGatewayState;

		if (Transmitter.QUEUE_EMPTY == state) {
			id = TransmitterEvent.QUEUE_EMPTIED;
			newGatewayState &= ~Transmitter.QUEUE_NOT_EMPTY;
			newGatewayState |= state;
			topOfQueueChanged = false;
		}
		else if (Transmitter.QUEUE_NOT_EMPTY == state) {
			id = TransmitterEvent.QUEUE_UPDATED;
			newGatewayState &= ~Transmitter.QUEUE_EMPTY;
			newGatewayState |= state;
			topOfQueueChanged = true;
		}
		else {
			super.stateTransition(state);
			return;
		}

		if (newGatewayState != oldGatewayState) {
			setGatewayState(newGatewayState);
			fireControlEvent(new TransmitterEvent(this, id, topOfQueueChanged,
					oldGatewayState,
					newGatewayState));
		}
	}

	/**
	 * Implementation of Transmitter. Other software components can send X10
	 * events to the CM11A to have sent through the X10 protocol to X10 devices on
	 * the power line network.
	 *
	 * @param evt	X10 event to transmit
	 * @exception GatewayStateError	if called for a transmitter in the
	 *					DEALLOCATED or DEALLOCATING_RESOURCES
	 *					states
	 */
	public void transmit(X10Event evt)
	throws GatewayStateError {
		// If deallocating resouces, throw an error
		if (testGatewayState(Gateway.DEALLOCATING_RESOURCES |
				Gateway.DEALLOCATED))
			throw new GatewayStateError("Can not transmit.  Transmitter is currently in the DEALLOCATING_RESOURCES or DEALLOCATED state.");

		try {
			waitGatewayState(Gateway.ALLOCATED);
		}
		catch (InterruptedException e) {
			throw new GatewayStateError("Caught InterruptedException while waiting for the gateway to enter an ALLOCATED state:  " + e.getMessage());
		}
		catch (IllegalArgumentException e) {
			throw new GatewayStateError("Caught IllegalArgumentException while waiting for the gateway to enter an ALLOCATED state:  " + e.getMessage());
		}

		CM11AX10EventTransmission event = new CM11AX10EventTransmission(evt);
		transmissionQueue.addElement(event);
		stateTransition(Transmitter.QUEUE_NOT_EMPTY);
		shouldProcess.setTrue();
	}

	/**
	 * Request the status from the CM11A interface.
	 *
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public void updateStatus() {
		updateStatus(null);
	}

	/**
	 * Request the status from the CM11A interface.
	 *
	 * @param statusListener  Notification is sent to the listener
	 * @author                <a href="mailto:jesse@jpeterson.com">Jesse Peterson
	 *      </a>
	 */
	public void updateStatus(CM11AStatusListener statusListener) {
		// If deallocating resouces, throw an error
		if (testGatewayState(Gateway.DEALLOCATING_RESOURCES |
				Gateway.DEALLOCATED))
			throw new GatewayStateError("Can not transmit.  Transmitter is currently in the DEALLOCATING_RESOURCES or DEALLOCATED state.");

		try {
			waitGatewayState(Gateway.ALLOCATED);
		}
		catch (InterruptedException e) {
			throw new GatewayStateError("Caught InterruptedException while waiting for the gateway to enter an ALLOCATED state:  " + e.getMessage());
		}
		catch (IllegalArgumentException e) {
			throw new GatewayStateError("Caught IllegalArgumentException while waiting for the gateway to enter an ALLOCATED state:  " + e.getMessage());
		}

		transmissionQueue.addElement(new CM11AStatusTransmission(this, statusListener));
		stateTransition(Transmitter.QUEUE_NOT_EMPTY);
		shouldProcess.setTrue();
	}

	/**
	 * Get the house code that the CM11A is configured to monitor. The CM11A device
	 * will record changes to devices on the monitored house code. It record the
	 * on/off status and if a device is dimmed or not. The data can be retrieved
	 * from the methods getOnOffStatus, getDimStatus, get last addressed device.
	 * Use <code>setClock</code> to change the monitored house code.
	 *
	 * @return   The character from 'A' through 'P' that indicates the house code
	 *      that is being monitored.
	 * @see      setClock
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public char getMonitoredHouseCode() {
		return (monitoredHouseCode);
	}

	/**
	 * Indication of the On/Off status as the CM11A sees it for the monitored house
	 * code. The value is set after a call to <code>updateStatus</code>.
	 *
	 * @return   The bit is set (true) if the device is on, not set (false) if the
	 *      device is off. The bit index indicates the device: bit index 0 = device
	 *      1, bit index 1 = device 2, ..., bit index 15 = device 16.
	 * @see      updateStatus
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public BitSet getOnOffStatus() {
		return (onOffStatus);
	}

	/**
	 * Indication of the dim status as the CM11A sees it for the monitored house
	 * code. The value is set after a call to <code>updateStatus</code>.
	 *
	 * @return   The bit is set (true) if the device is dimmed, not set (false) if
	 *      the device is not dimmed. The bit index indicates the device: bit index
	 *      0 = device 1, bit index 1 = device 2, ..., bit index 15 = device 16.
	 * @see      updateStatus
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public BitSet getDimStatus() {
		return (dimStatus);
	}

	/**
	 * Indication of the last addressed device as the CM11A sees it for the
	 * monitored house code. The value is set after a call to <code>updateStatus</code>
	 * .
	 *
	 * @return   The bit is set (true) if the device was addressed, not set (false)
	 *      if the device was not addressed. The bit index indicates the device:
	 *      bit index 0 = device 1, bit index 1 = device 2, ..., bit index 15 =
	 *      device 16.
	 * @see      updateStatus
	 * @see      setClock
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public BitSet getLastAddressedDevice() {
		return (lastAddressedDevice);
	}

	/**
	 * Retrieve the CM11A's idea of what the current day is. The value is
	 * initialized after a call to <code>updateStatus()</code>.
	 *
	 * @return   The CM11A's idea of what the current day is. Will be one of
	 *      Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY,
	 *      Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY.
	 * @see      updateStatus
	 * @see      setClock
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public int getCurrentDay() {
		return (currentDay);
	}

	/**
	 * Retrieve the CM11A's idea of the current day of the year. The value is zero
	 * based; 0 for January 1, 31 for February 1, ... The value is initialized
	 * after a call to <code>updateStatus()</code>. <p>
	 *
	 * The utility methods <code>CM11A.extractMonth()</code> and <code>CM11A.extractDay()</code>
	 * have been provided to convert this value into month and day representations.
	 * <p>
	 *
	 * As far as I can tell, the CM11A has no way of determining leap years. It
	 * therefore always uses 366 days in a year. The caller is responsible for
	 * determining if the current day has been corrected for a non-leap year.
	 *
	 * @return   Day of year.
	 * @see      extractMonth
	 * @see      extractDay
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public int getJulianDay() {
		return (julianDay);
	}

	/**
	 * Retrieve the CM11A's idea of what the current hour is. The value is
	 * initialized after a call to <code>updateStatus()</code>.
	 *
	 * @return   The CM11A's idea of what the current hour is. Expressed as 24 hour
	 *      value.
	 * @see      updateStatus
	 * @see      setClock
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public int getHours() {
		return (hours);
	}

	/**
	 * Retrieve the CM11A's idea of what the current minute is. The value is
	 * initialized after a call to <code>updateStatus()</code>.
	 *
	 * @return   The CM11A's idea of what the current minute is.
	 * @see      updateStatus
	 * @see      setClock
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public int getMinutes() {
		return (minutes);
	}

	/**
	 * Retrieve the CM11A's idea of what the current second is. The value is
	 * initialized after a call to <code>updateStatus()</code>.
	 *
	 * @return   The CM11A's idea of what the current second is.
	 * @see      updateStatus
	 * @see      setClock
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public int getSeconds() {
		return (seconds);
	}

	/**
	 * Retrieve the CM11A's idea of what the current battery usage is. The value is
	 * initialized after a call to <code>updateStatus()</code>.
	 *
	 * @return   The CM11A's idea of what the current battery usage is. Expressed
	 *      in minutes.
	 * @see      updateStatus
	 * @see      setClock
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public int getBatteryUsage() {
		return (batteryUsage);
	}

	/**
	 * Update the CM11A interface's internal clock, set the monitored house code,
	 * reset battery timer, clear monitored statuses, and purge timer.
	 *
	 * @param hour                  Current time, hours. 0 - 23.
	 * @param minute                Current time, minutes. 0 - 59.
	 * @param second                Current time, seconds. 0 - 59.
	 * @param month                 Current month, zero based. e.g., 0 for January
	 *      (Calendar.JANUARY), 1 for February (Calendar.FEBRUARY).
	 * @param day                   The new clock value
	 * @param dayOfWeek             A day of the week constant from the class
	 *      <CODE>java.util.Calendar</CODE>. Should be one of the following: <CODE>Calendar.SUNDAY</CODE>
	 *      , <CODE>Calendar.MONDAY</CODE>, <CODE>Calendar.TUESDAY</CODE>, <CODE>Calendar.WEDNESDAY</CODE>
	 *      , <CODE>Calendar.THURSDAY</CODE>, <CODE>Calendar.FRIDAY</CODE>, or
	 *      <CODE>Calendar.SATURDAY</CODE>.
	 * @param houseCode             the house code of the event. Valid codes are
	 *      'A' through 'P', uppercase.
	 * @param clearBatteryTimer     If true, the interface's battery timer will be
	 *      cleared.
	 * @param clearMonitoredStatus  If true, the interface's monitored statuses
	 *      will be cleared.
	 * @param purgeTimer            If true, this will purge the interfaces timer,
	 *      but I don't know what that means???
	 * @parma                       day Current day. 1 - 31. (Maximum day depends
	 *      on month)
	 * @author                      Jesse Peterson <jesse@jpeterson.com>
	 */
	public void setClock(int hour, int minute, int second, int month, int day,
			int dayOfWeek, char houseCode, boolean clearBatteryTimer,
			boolean clearMonitoredStatus, boolean purgeTimer) {
		byte[] packet = new byte[7];
		int minutes;
		int dayOfYear;

		if ((hour < 0) || (hour > 23))
			throw new IllegalArgumentException("Hour must be between 0 and 23, inclusive.");

		if ((minute < 0) || (minute > 59))
			throw new IllegalArgumentException("Minute must be between 0 and 59, inclusive.");

		if ((second < 0) || (second > 59))
			throw new IllegalArgumentException("Second must be between 0 and 59, inclusive.");

		packet[0] = CM11_CLOCK_DOWNLOAD;

		packet[1] = (byte) second;

		minutes = (hour * 60) + minute;
		packet[2] = (byte) (minutes % 120);
		packet[3] = (byte) ((minutes - packet[2]) / 120);

		dayOfYear = CM11A.dayOfYear(month, day);
		packet[4] = (byte) (dayOfYear & 0xff);

		switch (dayOfWeek) {
		default:
		case Calendar.SUNDAY:
			packet[5] = (byte) 0x01;
			break;
		case Calendar.MONDAY:
			packet[5] = (byte) 0x02;
			break;
		case Calendar.TUESDAY:
			packet[5] = (byte) 0x04;
			break;
		case Calendar.WEDNESDAY:
			packet[5] = (byte) 0x08;
			break;
		case Calendar.THURSDAY:
			packet[5] = (byte) 0x10;
			break;
		case Calendar.FRIDAY:
			packet[5] = (byte) 0x20;
			break;
		case Calendar.SATURDAY:
			packet[5] = (byte) 0x40;
			break;
		}

		if (dayOfYear > 0xff)
			// set high bit
			packet[5] |= 0x80;

		packet[6] = (byte) ((X10Util.houseCode2byte(houseCode)) << 4);

		if (clearBatteryTimer)
			packet[6] |= (byte) 0x04;

		if (clearMonitoredStatus)
			packet[6] |= (byte) 0x01;

		if (purgeTimer)
			packet[6] |= (byte) 0x02;

		//transmissionQueue.addElement(new CM11AStandardTransmission(packet));
		// add element to head of transmission queue
		transmissionQueue.insertElementAt(new CM11AMacroDownloadTransmission(packet), 0);
		stateTransition(Transmitter.QUEUE_NOT_EMPTY);
		shouldProcess.setTrue();

		// store values
		this.monitoredHouseCode = houseCode;
		this.currentDay = dayOfWeek;
		this.julianDay = dayOfYear;
		this.hours = hour;
		this.minutes = minute;
		this.seconds = second;
		if (clearBatteryTimer)
			batteryUsage = 0;

	}

	/**
	 * Enable the serial Ring Indicator signal.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public void ringEnable() {
		byte[] packet = new byte[1];

		// If deallocating resouces, throw an error
		if (testGatewayState(Gateway.DEALLOCATING_RESOURCES |
				Gateway.DEALLOCATED))
			throw new GatewayStateError("Can not transmit.  Transmitter is currently in the DEALLOCATING_RESOURCES or DEALLOCATED state.");

		try {
			waitGatewayState(Gateway.ALLOCATED);
		}
		catch (InterruptedException e) {
			throw new GatewayStateError("Caught InterruptedException while waiting for the gateway to enter an ALLOCATED state:  " + e.getMessage());
		}
		catch (IllegalArgumentException e) {
			throw new GatewayStateError("Caught IllegalArgumentException while waiting for the gateway to enter an ALLOCATED state:  " + e.getMessage());
		}

		packet[0] = CM11_RING_ENABLE;
		transmissionQueue.addElement(new CM11AStandardTransmission(packet));
		stateTransition(Transmitter.QUEUE_NOT_EMPTY);
		shouldProcess.setTrue();
	}

	/**
	 * Disable the serial Ring Indicator signal.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public void ringDisable() {
		byte[] packet = new byte[1];

		// If deallocating resouces, throw an error
		if (testGatewayState(Gateway.DEALLOCATING_RESOURCES |
				Gateway.DEALLOCATED))
			throw new GatewayStateError("Can not transmit.  Transmitter is currently in the DEALLOCATING_RESOURCES or DEALLOCATED state.");

		try {
			waitGatewayState(Gateway.ALLOCATED);
		}
		catch (InterruptedException e) {
			throw new GatewayStateError("Caught InterruptedException while waiting for the gateway to enter an ALLOCATED state:  " + e.getMessage());
		}
		catch (IllegalArgumentException e) {
			throw new GatewayStateError("Caught IllegalArgumentException while waiting for the gateway to enter an ALLOCATED state:  " + e.getMessage());
		}

		packet[0] = CM11_RING_ENABLE;
		transmissionQueue.addElement(new CM11AStandardTransmission(packet));
		stateTransition(Transmitter.QUEUE_NOT_EMPTY);
		shouldProcess.setTrue();
	}

	/**
	 * Add a CM11A event listener.
	 *
	 * @param l  CM11AListener to add.
	 * @see      removeCM11AListener
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public synchronized void addCM11AListener(CM11AListener l) {
		if (eventListeners == null)
			eventListeners = new Vector<CM11AListener>();

		// add a listener if it is not already registered
		if (!eventListeners.contains(l))
			eventListeners.addElement(l);

	}

	/**
	 * Remove a CM11A event listener.
	 *
	 * @param l  CM11AListener to remove.
	 * @see      addCM11AListener
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public synchronized void removeCM11AListener(CM11AListener l) {
		// remove it if it is registered
		if (eventListeners != null)
			eventListeners.removeElement(l);

	}

	/**
	 * Fire a CM11A event.
	 *
	 * @param evt  The event to send to listeners.
	 * @author     <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	@SuppressWarnings("unchecked")
	protected void fireCM11AEvent(CM11AEvent evt) {
		Vector<CM11AListener> v = null;

		// make a copy of the listener object vector so that it cannot
		// be changed while we are firing events
		synchronized (this) {
			if (eventListeners != null)
				v = (Vector<CM11AListener>) eventListeners.clone();

		}

		if (v != null) {
			int eventId = evt.getId();

			// fire the event to all listeners
			int cnt = v.size();
			for (int i = 0; i < cnt; i++) {
				CM11AListener client = (CM11AListener) v.elementAt(i);
				switch (eventId) {
				case CM11AEvent.POWER_FAILURE:
					client.powerFailure(evt);
					break;
				case CM11AEvent.MACRO_INITIATED:
					client.macroInitiated(evt);
					break;
				}
			}
		}
	}

	/**
	 * Add a CM11A status listener.
	 *
	 * @param l  CM11AStatusListener to add.
	 * @see      removeCM11AStatusListener
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public synchronized void addCM11AStatusListener(CM11AStatusListener l) {
		if (statusListeners == null)
			statusListeners = new Vector<CM11AStatusListener>();

		// add a listener if it is not already registered
		if (!statusListeners.contains(l))
			statusListeners.addElement(l);

	}

	/**
	 * Remove a CM11A status listener.
	 *
	 * @param l  CM11AStatusListener to remove.
	 * @see      addCM11AStatusListener
	 * @author   <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public synchronized void removeCM11AStatusListener(CM11AStatusListener l) {
		// remove it if it is registered
		if (statusListeners != null)
			statusListeners.removeElement(l);

	}

	/**
	 * Fire a CM11A status event.
	 *
	 * @param evt  The event to send to listeners.
	 * @author     <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	@SuppressWarnings("unchecked")
	protected void fireCM11AStatusEvent(CM11AStatusEvent evt) {
		Vector<CM11AStatusListener> v = null;

		// make a copy of the listener object vector so that it cannot
		// be changed while we are firing events
		synchronized (this) {
			if (statusListeners != null)
				v = (Vector<CM11AStatusListener>) statusListeners.clone();

		}

		if (v != null) {
			// fire the event to all listeners
			int cnt = v.size();
			for (int i = 0; i < cnt; i++) {
				CM11AStatusListener client = (CM11AStatusListener) v.elementAt(i);
				client.status(evt);
			}
		}
	}

	/**
	 * Add an X10 Address listener. The CM11A will send any X10 address events that
	 * it intercepts on the power line to all registered address listeners.
	 *
	 * @param l  X10 AddressListener to add.
	 * @see      removeAddressListener
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized void addAddressListener(AddressListener l) {
		if (addressListeners == null)
			addressListeners = new Vector<EventListener>();

		// add a listener if it is not already registered
		if (!addressListeners.contains(l))
			addressListeners.addElement(l);

	}

	/**
	 * Remove an Address listener.
	 *
	 * @param l  AddressListener to remove.
	 * @see      addAddressListener
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized void removeAddressListener(AddressListener l) {
		// remove it if it is registered
		if (addressListeners != null)
			addressListeners.removeElement(l);

	}

	/**
	 * Add an X10 Function listener. The CM11A will send any X10 function events
	 * that it intercepts on the power line to all registered function listeners.
	 *
	 * @param l  X10 FunctionListener to add.
	 * @see      removeFunctionListener
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized void addFunctionListener(FunctionListener l) {
		if (functionListeners == null)
			functionListeners = new Vector<EventListener>();

		// add a listener if it is not already registered
		if (!functionListeners.contains(l))
			functionListeners.addElement(l);

	}

	/**
	 * Remove a Function listener.
	 *
	 * @param l  FunctionListener to remove.
	 * @see      addFunctionListener
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized void removeFunctionListener(FunctionListener l) {
		// remove it if it is registered
		if (functionListeners != null)
			functionListeners.removeElement(l);

	}

	/**
	 * Fire an X10 Transmission. When an X10 event is received from the power line,
	 * this method is called with the X10 event to send to all registered X10
	 * transmission listeners.
	 *
	 * @param evt  The event to send to X10Transmission listeners.
	 * @author     Jesse Peterson <jesse@jpeterson.com>
	 */
	@SuppressWarnings("unchecked")
	protected void fireX10Event(X10Event evt) {
		Vector<EventListener> v = null;

		if (evt instanceof AddressEvent) {
			// make a copy of the listener object vector so that it cannot
			// be changed while we are firing events
			synchronized (this) {
				if (addressListeners != null)
					v = (Vector<EventListener>) addressListeners.clone();

			}

			if (v != null) {
				// fire the event to all listeners
				int cnt = v.size();
				for (int i = 0; i < cnt; i++) {
					AddressListener client = (AddressListener) v.elementAt(i);
					client.address((AddressEvent) evt);
				}
			}
		}
		else if (evt instanceof FunctionEvent) {
			// make a copy of the listener object vector so that it cannot
			// be changed while we are firing events
			synchronized (this) {
				if (functionListeners != null)
					v = (Vector<EventListener>) functionListeners.clone();

			}

			if (v != null) {
				FunctionEvent functionEvent = (FunctionEvent) evt;
				byte functionCode = functionEvent.getFunction();

				// fire the event to all listeners
				int cnt = v.size();

				for (int i = 0; i < cnt; i++) {
					FunctionListener client = (FunctionListener) v.elementAt(i);
					switch (functionCode) {
					case X10Util.X10_FUNCTION_ALL_UNITS_OFF:
						client.functionAllUnitsOff(functionEvent);
						break;
					case X10Util.X10_FUNCTION_ALL_LIGHTS_ON:
						client.functionAllLightsOn(functionEvent);
						break;
					case X10Util.X10_FUNCTION_ON:
						client.functionOn(functionEvent);
						break;
					case X10Util.X10_FUNCTION_OFF:
						client.functionOff(functionEvent);
						break;
					case X10Util.X10_FUNCTION_DIM:
						client.functionDim(functionEvent);
						break;
					case X10Util.X10_FUNCTION_BRIGHT:
						client.functionBright(functionEvent);
						break;
					case X10Util.X10_FUNCTION_ALL_LIGHTS_OFF:
						client.functionAllLightsOff(functionEvent);
						break;
					case X10Util.X10_FUNCTION_HAIL_REQUEST:
						client.functionHailRequest(functionEvent);
						break;
					case X10Util.X10_FUNCTION_HAIL_ACKNOWLEDGE:
						client.functionHailAcknowledge(functionEvent);
						break;
					case X10Util.X10_FUNCTION_PRESET_DIM_1:
						client.functionPresetDim1(functionEvent);
						break;
					case X10Util.X10_FUNCTION_PRESET_DIM_2:
						client.functionPresetDim2(functionEvent);
						break;
					case X10Util.X10_FUNCTION_STATUS_ON:
						client.functionStatusOn(functionEvent);
						break;
					case X10Util.X10_FUNCTION_STATUS_OFF:
						client.functionStatusOff(functionEvent);
						break;
					case X10Util.X10_FUNCTION_STATUS_REQUEST:
						client.functionStatusRequest(functionEvent);
						break;
					}
				}
			}
		}
	}

	/**
	 * Indicates if the the interface to the CM11A is up.
	 *
	 * @return   Returns true if the interface is up, false if the interface
	 *      connection is down.
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public boolean isRunning() {
		return (testGatewayState(Gateway.ALLOCATED));
	}

	/**
	 * Start the serial link between the computer and the actual CM11A module.
	 *
	 * @exception GatewayException   Exception
	 * @exception GatewayStateError  Exception
	 * @author                       Jesse Peterson <jesse@jpeterson.com>
	 */
	public void allocate() throws GatewayException, GatewayStateError {
		// If deallocating resouces, throw an error
		if (testGatewayState(Gateway.DEALLOCATING_RESOURCES))
			throw new GatewayStateError("CM11A can not be allocated.  It is currently in the DEALLOCATING_RESOURCES state.");

		// If already being allocated or allocated, just return
		if (testGatewayState(Gateway.ALLOCATED |
				Gateway.ALLOCATING_RESOURCES))
			// already allocating
			return;

		// set the new state
		stateTransition(Gateway.ALLOCATING_RESOURCES);

		if (System.getProperty("DEBUG") != null)
			System.out.println("DEBUG: Starting interface");

		try {
			CommPortIdentifier.getPortIdentifier(portName);
			// serialPort = (SerialPort) portId.open("CM11A", 2000);
			serialPort = sp;
			serialPort.setSerialPortParams(baudRate,
					dataBits,
					stopBits,
					parity);
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
			serialPort.removeEventListener();    //added by cma to as i was getting the too many listeners exception when i only wanted one listener anyway
			serialPort.addEventListener(this);
			serialPort.notifyOnRingIndicator(true);
			serialPort.notifyOnDataAvailable(true);
		}
		catch (NoSuchPortException e) {
			setGatewayState(Gateway.DEALLOCATED);
			throw new GatewayException("Requested com port " + portName +
			" does not exist.");
		}
		/*
	catch (PortInUseException e)
	{
	setGatewayState(Gateway.DEALLOCATED);
	throw new GatewayException("Request com port " + portName +
	" in use.");
	}
		 */
		catch (TooManyListenersException e) {
			setGatewayState(Gateway.DEALLOCATED);
			throw new GatewayException("Too many listeners on port " +
					portName + ".");
		}
		catch (UnsupportedCommOperationException e) {
			setGatewayState(Gateway.DEALLOCATED);
			throw new GatewayException("Unnsupported comm operation");
		}
		catch (IOException e) {
			setGatewayState(Gateway.DEALLOCATED);
			throw new GatewayException("Unable to get input/output stream");
		}

		processThread = new Thread(this);
		processThread.setDaemon(true);
		processThread.start();

		setGatewayState(Gateway.RESUMED);
		stateTransition(Gateway.ALLOCATED);

		// prime the link between the computer and the CM11A with
		// a status request.
		updateStatus();

		if (System.getProperty("DEBUG") != null)
			System.out.println("DEBUG: Interface started");

	}

	/**
	 * Stop the serial link between the computer and the actual CM11A module.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized void deallocate() {
		// If allocating resouces, throw an error
		if (testGatewayState(Gateway.ALLOCATING_RESOURCES))
			throw new GatewayStateError("CM11A can not be deallocated.  It is currently in the ALLOCATING_RESOURCES state.");
		// If already being deallocated or deallocated, just return
		if (testGatewayState(Gateway.DEALLOCATED |
				Gateway.DEALLOCATING_RESOURCES))
			// already deallocating
			return;
		stateTransition(Gateway.DEALLOCATING_RESOURCES);

		// stop thread
		shouldProcess.setTrue();

		if (processThread != null) {
			try {
				// wait for the read thread to stop
				processThread.join(5000);
			}
			catch (InterruptedException exception) {
				System.err.println("CM11A.deallocate: interrupted exception " +
						exception);
				// e.printStackTrace();
			}
			if (processThread.isAlive())
				// kill it
				processThread = null;
			// was: processThread.stop ();
			// (Thread.stop() has been deprecated)

			processThread = null;
		}

		if (inputStream != null) {
			try {
				byte[] discard = new byte[1];
				while (inputStream.available() > 0)
					inputStream.read(discard);
				inputStream.close();
			}
			catch (IOException exception) {
				System.err.println("CM11A.deallocate: I/O exception " + exception);
			}
		}

		if (outputStream != null) {
			try {
				outputStream.close();
			}
			catch (IOException exception) {
				System.err.println("CM11A.deallocate: I/O exception " + exception);
			}
		}

		try {
			serialPort.removeEventListener();
		}
		catch (IllegalStateException exception) {
			System.err.println("CM11A.deallocate: port already closed");
		}

		serialPort = null;
		inputStream = null;
		outputStream = null;

		stateTransition(Gateway.DEALLOCATED);
		if (System.getProperty("DEBUG") != null)
			System.out.println("DEBUG: Interface stopped");
	}

	/**
	 * Calls TransmitterListener methods. Lets parent's dispatchControlEvent call
	 * the GatewayListener methods.
	 *
	 * @param event  Parameter
	 * @author       Jesse Peterson <6/29/99>
	 */
	public void dispatchControlEvent(ControlEvent event) {
		if (event instanceof TransmitterEvent) {
			GatewayListener listener;

			// make a copy of the listener object vector so that it cannot
			// be changed while we are firing event
			Vector<?> v;
			synchronized (this) {
				v = (Vector<?>) gatewayListeners.clone();
			}

			// fire the event to all listeners
			int cnt = v.size();
			for (int i = 0; i < cnt; i++)
				switch (event.getId()) {
				case TransmitterEvent.QUEUE_EMPTIED:
					listener = (GatewayListener) v.elementAt(i);
					if (listener instanceof TransmitterListener)
						((TransmitterListener) listener).queueEmptied((TransmitterEvent) event);

					break;
				case TransmitterEvent.QUEUE_UPDATED:
					listener = (GatewayListener) v.elementAt(i);
					if (listener instanceof TransmitterListener)
						((TransmitterListener) listener).queueUpdated((TransmitterEvent) event);

					break;
				}

		}
		else
			super.dispatchControlEvent(event);

	}

	/**
	 * Process CM11A events.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 *cma got rid of the wait as there were a ton of deamon threads being left unfinshed at runtime
	 */
	public void run() {
		while (true)
			try {
				shouldProcess.waitForTrue();
				shouldProcess.setFalse();
				if (System.getProperty("DEBUG") != null)
					System.out.println("DEBUG: process thread awakened");

				if (testGatewayState(Gateway.DEALLOCATING_RESOURCES |
						Gateway.DEALLOCATED))
					return;

				// make sure there are no pending requests in the input stream
				processPendingRequests();

				processTransmissionQueue();

				// make sure there are no pending requests in the input stream
				processPendingRequests();      
			}
		catch (InterruptedException e) {
		}

	}

	/**
	 * Process the queue of events to be processed by the CM11A.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public void processTransmissionQueue() {
		Object obj;
		CM11ATransmissionEvent transmissionEvent;

		while (!transmissionQueue.isEmpty())
			try {
				obj = transmissionQueue.elementAt(0);

				if (obj instanceof CM11ATransmissionEvent) {
					transmissionEvent = (CM11ATransmissionEvent) obj;
					try {
						if (System.getProperty("DEBUG") != null)
							System.out.println("DEBUG: Transmitting " + transmissionEvent);

						transmissionEvent.transmit(inputStream, outputStream);

						// transmit complete
						if (System.getProperty("DEBUG") != null)
							System.out.println("DEBUG: Transmission of " + transmissionEvent + " complete.");

						// Fire X10 event upon successful transmission of
						// X10 event. This allows proper propagation of events
						// to listeners that may take action upon certain events.
						if (obj instanceof CM11AX10EventTransmission)
							fireX10Event(((CM11AX10EventTransmission) obj).getEvent());

						// remove from queue
						transmissionQueue.removeElementAt(0);
						long state = getGatewayState();
						fireControlEvent(new TransmitterEvent(this,
								TransmitterEvent.QUEUE_UPDATED,
								true, state, state));
					}
					catch (EOFException e) {
						System.err.println("CM11A.processTransmissionQueue: " +
								"Unexpected EOF exception while transmitting event " + transmissionEvent);
						// e.printStackTrace();

						// unable to transmit this transmission event
						transmissionQueue.removeElementAt(0);
						long state = getGatewayState();
						fireControlEvent(new TransmitterEvent(this,
								TransmitterEvent.QUEUE_UPDATED,
								true, state, state));
					}
					catch (IOException e) {
						System.err.println("CM11A.processTransmissionQueue: " +
								"I/O exception while transmitting event " + transmissionEvent);
						// e.printStackTrace();

						// unable to transmit this transmission event
						transmissionQueue.removeElementAt(0);
						long state = getGatewayState();
						fireControlEvent(new TransmitterEvent(this,
								TransmitterEvent.QUEUE_UPDATED,
								true, state, state));
					}
					catch (TooManyAttemptsException e) {
						System.err.println("CM11A.processTransmissionQueue: " +
								"unable to send " + transmissionEvent + " due too many retries");
						// e.printStackTrace();

						// unable to transmit this transmission event
						transmissionQueue.removeElementAt(0);
						long state = getGatewayState();
						fireControlEvent(new TransmitterEvent(this,
								TransmitterEvent.QUEUE_UPDATED,
								true, state, state));
					}
					catch (InterruptedTransmissionException e) {
						// handle interruption
						switch (e.getEventCode()) {
						case CM11_RECEIVE_EVENT:
							receiveEvent();
							break;
						case CM11_POWER_FAILURE:
							powerFailure();
							break;
						case CM11_MACRO_INITIATED:
							macroInitiated();
							break;
						}

						// handle any other requests
						processPendingRequests();

						// let processing fall through on successful completion of
						// interrupted transmission exception handling so that
						// this transmission is retried.
					}
				}
				else {
					// queue element is not a CM11ATransmissionEvent
					transmissionQueue.removeElementAt(0);
					long state = getGatewayState();
					fireControlEvent(new TransmitterEvent(this,
							TransmitterEvent.QUEUE_UPDATED,
							true, state, state));
				}
			}
		catch (ArrayIndexOutOfBoundsException exception) {
			// should not happen
			System.err.println("CM11A.processTransmissionQueue: exception " +
					exception);
			exception.printStackTrace();
		}

		stateTransition(Transmitter.QUEUE_EMPTY);
	}

	/**
	 * Process the input stream, handling unsolicited events. This method will
	 * drain the input stream of all available bytes.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public void processPendingRequests() {
		byte byteValue;
		int result;
		byte[] buffer = new byte[20];

		if (inputStream == null)
			// input stream has already been closed
			return;

		try {
			while (inputStream.available() > 0) {
				result = inputStream.read();

				if (result == -1)
					System.err.println("Received end of stream indicator while trying to process pending requests.");

				byteValue = (byte) result;

				if (byteValue == CM11_RECEIVE_EVENT)
					receiveEvent();

				else if (byteValue == CM11_POWER_FAILURE)
					powerFailure();

				else if (byteValue == CM11_MACRO_INITIATED)
					macroInitiated();

				else {
					// consume all bytes in input stream
					// at this point protocol is broke.  By consuming
					// all bytes, we can hopefully get back on track
					System.err.println("CM11A.processPendingRequests: " +
							"breakdown in protocol, consuming all bytes - is this port " +
					"connected to an X10 module?");
					while (inputStream.available() > 0)
						inputStream.read(buffer);
				}
			}
		}
		catch (IOException e) {
			System.err.println("CM11A.processPendingRequests: I/O exception " +
			"while trying to process pending requests");
			// e.printStackTrace();
			return;
		}
	}

	/**
	 * Process a request by the interface to download data. The request is
	 * identified by a byte value of 0x5a.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	private void receiveEvent() {
		int numBytes;
		int numBytesRead;
		byte[] buffer;
		if (System.getProperty("DEBUG") != null)
			System.out.println("DEBUG: Receive event from interface Poll Signal");

		try {
			outputStream.write(CM11_RECEIVE_EVENT_RSP);
		}
		catch (IOException e) {
			System.err.println("CM11A.receiveEvent: " +
			"I/O exception reponding to interface poll signal");
			// e.printStackTrace();
			return;
		}

		try {
			numBytes = inputStream.read();
		}
		catch (IOException e) {
			System.err.println("CM11A.receiveEvent: " +
					"I/O exception reading the number of bytes in interface poll signal " +
			"buffer");
			// e.printStackTrace();
			return;
		}
		if (System.getProperty("DEBUG") != null)
			System.out.println("DEBUG: Number of bytes to read " + numBytes);

		if (numBytes == -1) {
			// error
			System.err.println("CM11A.receiveEvent: " +
			"error reading the number of bytes in interface poll signal buffer");
			return;
		}

		buffer = new byte[numBytes];

		try {
			numBytesRead = inputStream.read(buffer);
		}
		catch (IOException e) {
			System.err.println("CM11A.receiveEvent: " +
			"I/O exception reading the interface poll signal buffer");
			// e.printStackTrace();
			return;
		}
		if (System.getProperty("DEBUG") != null)
			System.out.println("DEBUG: buffer " + buffer);

		if (numBytes != numBytesRead) {
			System.err.println("CM11A.receiveEvent: " +
					"incorrect number of bytes in the interface poll signal buffer - " +
					"expected " + numBytes + ", but received " + numBytesRead + " bytes");
			return;
		}

		decodeX10Events(buffer);
	}

	/**
	 * Process notification of a power failure.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	private void powerFailure() {
		if (System.getProperty("DEBUG") != null)
			System.out.println("A power failure has occurred. Power is now restored.");

		fireCM11AEvent(new CM11AEvent(this, CM11AEvent.POWER_FAILURE));

		if (powerFailureAutoRecover) {
			if (System.getProperty("DEBUG") != null)
				System.out.println("Power Failure Auto Recorvering...");

			// set clock
			Calendar now = Calendar.getInstance();
			setClock(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),
					now.get(Calendar.SECOND), now.get(Calendar.MONTH),
					now.get(Calendar.DAY_OF_MONTH),
					now.get(Calendar.DAY_OF_WEEK), monitoredHouseCode, false,
					false, false);
		}
	}

	/**
	 * Process notification of a macro being initiated.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	private void macroInitiated() {
		int numBytesRead;
		short index;
		byte[] buffer = new byte[2];
		Macro macro;
		MacroElement element;
		int[] devices;
		FunctionEvent function;
		char houseCode;
		HexFormat hex = new HexFormat();

		if (System.getProperty("DEBUG") != null)
			System.out.println("DEBUG: macro initiated EEPROM address message");

		fireCM11AEvent(new CM11AEvent(this, CM11AEvent.MACRO_INITIATED));

		try {
			numBytesRead = inputStream.read(buffer);
		}
		catch (IOException e) {
			System.err.println("CM11A.macroInitiated: " +
			"exception reading the macro EEPROM address");
			// e.printStackTrace();
			return;
		}

		if (numBytesRead != 2) {
			System.err.println("Incorrect number of bytes in the macro initiated address buffer.  Expected 2 bytes, but received " + numBytesRead + " bytes.");
			return;
		}

		// print out address
		if (System.getProperty("DEBUG") != null)
			System.out.println("macro initiated: address 0x" + hex.format(buffer[0]) + hex.format(buffer[1]));

		// send resultant events to listeners
		// mask high bit, the device always sets it.
		index = (short) (((buffer[0] & (byte) 0x7F) << 8) | buffer[1]);
		macro = (Macro) offset2macro.get(new Integer(index));
		if (macro != null)
			for (Enumeration<?> e = macro.elements(); e.hasMoreElements(); ) {
				element = (MacroElement) e.nextElement();

				function = element.getFunction();
				houseCode = function.getHouseCode();
				devices = element.getDevices();

				if (((function instanceof DimEvent) ||
						(function instanceof BrightEvent)) &&
						element.getBrightenFirst()) {
					if (devices.length > 0) {
						// send addresses first
						for (int i = 0; i < devices.length; i++) {
							if (System.getProperty("DEBUG") != null)
								System.out.println("Address device " + devices[i]);     // DEBUG

							fireX10Event(new AddressEvent(this, houseCode, devices[i]));
						}

						if (System.getProperty("DEBUG") != null)
							System.out.println("Brighten First");                    // DEBUG

						fireX10Event(new BrightEvent(this, houseCode, 22, 22));
					}
				}

				if (devices.length > 0)
					// send addresses first
					for (int i = 0; i < devices.length; i++) {
						if (System.getProperty("DEBUG") != null)
							System.out.println("Address device " + devices[i]);      // DEBUG

						fireX10Event(new AddressEvent(this, houseCode, devices[i]));
					}

				// then function
				if (System.getProperty("DEBUG") != null)
					System.out.println("Firing " + element.getFunction());     // DEBUG

				fireX10Event(createEvent(element.getFunction(), this));
			}

	}

	/**
	 * Create a copy of a FunctionEvent but with the specified source.
	 *
	 * @param function                      Parameter
	 * @param newSource                     The new source to use in the copied
	 *      FunctionEvent.
	 * @return                              A new FunctionEvent subclass based on
	 *      the function in the provided function event. The event source will be
	 *      set to the provided source.
	 * @author                              Jesse Peterson <jesse@jpeterson.com>
	 */
	protected FunctionEvent createEvent(FunctionEvent function,
			Object newSource) {
		switch (function.getFunction()) {
		case X10Util.X10_FUNCTION_ALL_UNITS_OFF:
			return (new AllUnitsOffEvent(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_ALL_LIGHTS_ON:
			return (new AllLightsOnEvent(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_ON:
			return (new OnEvent(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_OFF:
			return (new OffEvent(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_DIM:
			return (new DimEvent(newSource, function.getHouseCode(),
					function.getDims(), function.getDimMax()));
		case X10Util.X10_FUNCTION_BRIGHT:
			return (new BrightEvent(newSource, function.getHouseCode(),
					function.getDims(), function.getDimMax()));
		case X10Util.X10_FUNCTION_ALL_LIGHTS_OFF:
			return (new AllLightsOffEvent(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_EXTENDED_CODE:
			ExtendedCodeEvent evt = (ExtendedCodeEvent) function;
			return (new ExtendedCodeEvent(newSource, evt.getHouseCode(),
					evt.getData(), evt.getCommand()));
		case X10Util.X10_FUNCTION_HAIL_REQUEST:
			return (new HailRequestEvent(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_HAIL_ACKNOWLEDGE:
			return (new HailAcknowledgeEvent(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_PRESET_DIM_1:
			return (new PresetDim1Event(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_PRESET_DIM_2:
			return (new PresetDim2Event(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_EXTENDED_DATA_TRANSFER:
			return (new ExtendedDataTransferEvent(newSource,
					function.getHouseCode()));
		case X10Util.X10_FUNCTION_STATUS_ON:
			return (new StatusOnEvent(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_STATUS_OFF:
			return (new StatusOffEvent(newSource, function.getHouseCode()));
		case X10Util.X10_FUNCTION_STATUS_REQUEST:
			return (new StatusRequestEvent(newSource, function.getHouseCode()));
		default:
			throw new IllegalArgumentException("Unkown function: " +
					function.getFunction());
		}
	}

	/**
	 * Receive serial events from the CM11A module.
	 *
	 * @param event  Parameter
	 * @author       Jesse Peterson <jesse@jpeterson.com>
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
			if (System.getProperty("DEBUG") != null)
				System.out.println("Serial event BI(" + event + ")");

			break;
		case SerialPortEvent.OE:
			if (System.getProperty("DEBUG") != null)
				System.out.println("Serial event OE(" + event + ")");

			break;
		case SerialPortEvent.FE:
			if (System.getProperty("DEBUG") != null)
				System.out.println("Serial event FE(" + event + ")");

			break;
		case SerialPortEvent.PE:
			if (System.getProperty("DEBUG") != null)
				System.out.println("Serial event PE(" + event + ")");

			break;
		case SerialPortEvent.CD:
			if (System.getProperty("DEBUG") != null)
				System.out.println("Serial event CD(" + event + ")");

			break;
		case SerialPortEvent.CTS:
			if (System.getProperty("DEBUG") != null)
				System.out.println("Serial event CTS(" + event + ")");

			break;
		case SerialPortEvent.DSR:
			if (System.getProperty("DEBUG") != null)
				System.out.println("Serial event DSR(" + event + ")");

			break;
		case SerialPortEvent.RI:
			if (System.getProperty("DEBUG") != null)
				System.out.println("Serial event RI(" + event + ")");

			break;
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			if (System.getProperty("DEBUG") != null)
				System.out.println("Serial event: OUTPUT_BUFFER_EMPTY" + event + ")");

			break;
		case SerialPortEvent.DATA_AVAILABLE:
			if (System.getProperty("DEBUG") != null)
				System.out.println("DEBUG: Received data");

			// set condition variable to true so that the process
			// thread will awaken.
			shouldProcess.setTrue();
			break;
		}
	}

	/**
	 * Decode the event buffer uploaded from the CM11A module to the computer. The
	 * event buffer contains X10 events that the CM11A module has detected on the
	 * power line. This bean will fire events to all registered X10 Transmission
	 * Listeners for each event in the event buffer.
	 *
	 * @param buffer  the event buffer uploaded from the CM11A. The buffer is 2 to
	 *      9 bytes in size. With respect to the Interface communication protocol,
	 *      the 'Upload Buffer Size' is not part of the buffer.
	 * @author        Jesse Peterson <jesse@jpeterson.com>
	 */
	private void decodeX10Events(byte[] buffer) {
		byte hiNibble;
		byte loNibble;

		if (System.getProperty("DEBUG") != null) {
			System.out.println("DEBUG: decoding X10 events");
			BinaryFormat binaryFormat = new BinaryFormat();
			System.out.println("DEBUG: function/address byte - " + binaryFormat.format(buffer[0]));
		}

		if (buffer.length < 2) {
			System.err.println("The buffer is too short");
			return;
		}
		if (buffer.length > 9) {
			System.err.println("The buffer is too long");
			return;
		}

		X10Event evt;
		for (int i = 1; i < buffer.length; i++) {
			hiNibble = (byte) ((buffer[i] >> 4) & 0x0f);
			loNibble = (byte) (buffer[i] & 0x0f);

			if ((buffer[0] & (1 << (i - 1))) != 0)
				// function
				switch (loNibble) {
				case X10Util.X10_FUNCTION_ALL_UNITS_OFF:
					evt = new AllUnitsOffEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_ALL_LIGHTS_ON:
					evt = new AllLightsOnEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_ON:
					evt = new OnEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_OFF:
					evt = new OffEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_DIM:
					evt = new DimEvent(this,
							X10Util.byte2houseCode(hiNibble),
							(int) Unsigned.unsigned(buffer[++i]),
							210);
					break;
				case X10Util.X10_FUNCTION_BRIGHT:
					evt = new BrightEvent(this,
							X10Util.byte2houseCode(hiNibble),
							(int) Unsigned.unsigned(buffer[++i]),
							210);
					break;
				case X10Util.X10_FUNCTION_ALL_LIGHTS_OFF:
					evt = new AllLightsOffEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_EXTENDED_CODE:
					evt = new ExtendedCodeEvent(this,
							X10Util.byte2houseCode(hiNibble),
							buffer[++i],                                            // data
							buffer[++i]);                                           // command
					break;
				case X10Util.X10_FUNCTION_HAIL_REQUEST:
					evt = new HailRequestEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_HAIL_ACKNOWLEDGE:
					evt = new HailAcknowledgeEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_PRESET_DIM_1:
					evt = new PresetDim1Event(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_PRESET_DIM_2:
					evt = new PresetDim2Event(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_EXTENDED_DATA_TRANSFER:
					evt = new ExtendedDataTransferEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_STATUS_ON:
					evt = new StatusOnEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_STATUS_OFF:
					evt = new StatusOffEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				case X10Util.X10_FUNCTION_STATUS_REQUEST:
					evt = new StatusRequestEvent(this,
							X10Util.byte2houseCode(hiNibble));
					break;
				default:
					evt = null;
				break;
				}

			else
				// address
				evt = new AddressEvent(this,
						X10Util.byte2houseCode(hiNibble),
						X10Util.byte2deviceCode(loNibble));

			if (evt != null) {
				fireX10Event(evt);
				if (System.getProperty("DEBUG") != null)
					System.out.println("DEBUG: event " + evt);

			}
		}
	}

	/**
	 * Decode a status buffer. Also notifies CM11AStatusListeners.
	 *
	 * @param bytes  Parameter
	 * @param off    Parameter
	 * @param len    Parameter
	 * @return       Returns the CM11AStatusEvent decoded from the bytes.
	 * @author       Jesse Peterson <jesse@jpeterson.com>
	 */
	public CM11AStatusEvent decodeStatus(byte[] bytes, int off, int len) {
		BinaryFormat binaryFormat = new BinaryFormat();
		String binary;
		int binaryLength;
		int dayOfWeek;
		CM11AStatusEvent event;

		if (len > bytes.length)
			len = bytes.length;

		if (len != 14)
			return (null);

		// battery usage, in minutes
		//System.out.println("Battery Usage: " + (byte)bytes[1] + (byte)bytes[0]);
		batteryUsage = (bytes[1] << 8) | bytes[0];
		if (batteryUsage == 0xffff)
			batteryUsage = 0;

		// time
		seconds = bytes[2];
		minutes = bytes[3] % 60;
		hours = (bytes[4] * 2) + ((bytes[3] / 60));

		// julian day
		julianDay = bytes[5] + (((bytes[6] & 0x80) >>> 7) * 256);

		// current day of week
		dayOfWeek = bytes[6] & 0x7f;

		switch (dayOfWeek) {
		default:
		case 1:
			currentDay = Calendar.SUNDAY;
			break;
		case 2:
			currentDay = Calendar.MONDAY;
			break;
		case 4:
			currentDay = Calendar.TUESDAY;
			break;
		case 8:
			currentDay = Calendar.WEDNESDAY;
			break;
		case 16:
			currentDay = Calendar.THURSDAY;
			break;
		case 32:
			currentDay = Calendar.FRIDAY;
			break;
		case 64:
			currentDay = Calendar.SATURDAY;
			break;
		}

		// monitored house code
		// byte 7, hi nibble
		monitoredHouseCode = value2houseCode[(bytes[7] >>> 4) & 0xf];

		// last addressed device
		// swap bytes 8 and 9
		binary = binaryFormat.format((short) ((bytes[9] << 8) | bytes[8]));
		binaryLength = binary.length();

		// clear bit set
		for (int i = 0, size = lastAddressedDevice.size(); i < size; i++)
			lastAddressedDevice.clear(i);

		for (int i = 0; i < binaryLength; i++)
			if (binary.charAt((binaryLength - 1) - i) == '1')
				lastAddressedDevice.set(value2deviceCode[i]);


		// on/off status (bits 31-16)
		// swap bytes 10 and 11
		binary = binaryFormat.format((short) ((bytes[11] << 8) | bytes[10]));
		binaryLength = binary.length();

		// clear bit set
		for (int i = 0, size = onOffStatus.size(); i < size; i++)
			onOffStatus.clear(i);

		for (int i = 0; i < binaryLength; i++)
			if (binary.charAt((binaryLength - 1) - i) == '1')
				onOffStatus.set(value2deviceCode[i]);


		// dim status (bits 15-0)
		// swap bytes 12 and 13
		binary = binaryFormat.format((short) ((bytes[13] << 8) | bytes[12]));
		binaryLength = binary.length();

		// clear bit set
		for (int i = 0, size = dimStatus.size(); i < size; i++)
			dimStatus.clear(i);

		for (int i = 0; i < binaryLength; i++)
			if (binary.charAt((binaryLength - 1) - i) == '1')
				dimStatus.set(value2deviceCode[i]);


		event = new CM11AStatusEvent(this, batteryUsage, seconds, minutes,
				hours, julianDay, dayOfWeek,
				monitoredHouseCode, lastAddressedDevice,
				onOffStatus, dimStatus);

		fireCM11AStatusEvent(event);

		// print status
		if (System.getProperty("DEBUG") != null) {
			System.out.println("Battery usage: " + batteryUsage + " minutes");
			System.out.println("Interface Time: " + hours + ":" + minutes + ":" + seconds);
			System.out.println("Julian Day: " + julianDay);
			System.out.println("Current Day of Week: " + currentDay);
			System.out.println("Monitored House Code: " + monitoredHouseCode);
			System.out.println("Last Addressed Device: " + lastAddressedDevice);
			System.out.println("On/Off Status: " + onOffStatus);
			System.out.println("Dim Status: " + dimStatus);
		}

		return (event);
	}

	/**
	 * Serialize the object.
	 *
	 * @param stream           Parameter
	 * @exception IOException  Exception
	 * @author                 Jesse Peterson <jesse@jpeterson.com>
	 */
	@SuppressWarnings("unchecked")
	private void writeObject(ObjectOutputStream stream)
	throws IOException {
		Vector<EventListener> v;

		// perform default writing first
		stream.defaultWriteObject();

		// write address listeners
		// clone the vector in case one is added or removed
		v = null;
		synchronized (this) {
			if (addressListeners != null)
				v = (Vector<EventListener>) addressListeners.clone();

		}

		// if we have a collection
		if (v != null) {
			int cnt = v.size();
			for (int i = 0; i < cnt; i++) {
				// get the listener element from the collection
				AddressListener l = (AddressListener) v.elementAt(i);

				// if the listener is serializable, write it to the stream
				if (l instanceof Serializable)
					stream.writeObject(l);

			}
		}

		// a null object marks the end of the address listeners
		stream.writeObject(null);

		// write function listeners
		// clone the vector in case one is added or removed
		v = null;
		synchronized (this) {
			if (functionListeners != null)
				v = (Vector<EventListener>) functionListeners.clone();

		}

		// if we have a collection
		if (v != null) {
			int cnt = v.size();
			for (int i = 0; i < cnt; i++) {
				// get the listener element from the collection
				FunctionListener l = (FunctionListener) v.elementAt(i);

				// if the listener is serializable, write it to the stream
				if (l instanceof Serializable)
					stream.writeObject(l);

			}
		}

		// a null object marks the end of the function listeners
		stream.writeObject(null);
	}

	/**
	 * Deserialize the object.
	 *
	 * @param stream                      Parameter
	 * @exception IOException             Exception
	 * @exception ClassNotFoundException  Exception
	 * @author                            Jesse Peterson <jesse@jpeterson.com>
	 */
	private void readObject(ObjectInputStream stream)
	throws ClassNotFoundException, IOException {
		stream.defaultReadObject();

		// transient state variables
		shouldProcess = new Condition(false);
		transmissionQueue = new Vector<CM11ATransmissionEvent>();

		Object l;
		// deserialize AddressListeners
		while (null != (l = stream.readObject()))
			addAddressListener((AddressListener) l);

		// deserialize FunctionListeners
		while (null != (l = stream.readObject()))
			addFunctionListener((FunctionListener) l);

	}

	/**
	 * Transmit the CM11A macro initiators to the X10 CM11A device. This method
	 * stores the macro in memory on the CM11A device. The macros can be executed
	 * from the device with the controlling computer turned off or event
	 * disconnected.
	 *
	 * @exception OutOfMacroMemoryException  There are too many timer timer
	 *      initiators, macro initiators, and their associated macros to be
	 *      downloaded. Some of the initiators and/or macros must be removed in
	 *      order to successfully download to the CM11A device.
	 * @author                               Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized void downloadInitiators()
	throws OutOfMacroMemoryException {
		Object key;
		int numBytes;
		int remainder;
		byte[] packet;
		byte[] component;

		// initialize hashtable that will store offset based on macro.
		macroOffsets.clear();

		// encode EEPROM array
		for (int i = 0; i < eeprom.length; i++)
			eeprom[i] = 0;

		numBytes = encodeEEPROM();
		//System.out.println(HexDump.dump(eeprom));  // DEBUG
		// numBytes needs to be multiple of 16
		remainder = numBytes % PAGE;
		if (remainder > 0)
			numBytes += (PAGE - remainder);

		// send EEPROM array to device
		for (int i = 0, max = numBytes / PAGE; i < max; i++) {
			packet = new byte[19];
			packet[0] = CM11_MACRO_DOWNLOAD_INITIATOR;
			component = toBytes(i * PAGE, 2, true);
			System.arraycopy(component, 0, packet, 1, 2);

			System.arraycopy(eeprom, i * PAGE, packet, 3, PAGE);
			//System.out.println(HexDump.dump(packet));  // DEBUG
			transmissionQueue.addElement(new CM11AMacroDownloadTransmission(packet));
			stateTransition(Transmitter.QUEUE_NOT_EMPTY);
			shouldProcess.setTrue();
		}
		//System.exit(100);  // DEBUG

		// create hashtable to allow lookup of macro based on offset.
		offset2macro.clear();
		for (Enumeration<Macro> e = macroOffsets.keys(); e.hasMoreElements(); ) {
			key = e.nextElement();
			offset2macro.put(macroOffsets.get(key), key);
		}
	}

	/**
	 * Convert an integer value to a byte array.
	 *
	 * @param value      Integer value to convert.
	 * @param len        Number of bytes to use.
	 * @param bigEndian  If true, most significant byte is byte 0. If false, least
	 *      significant byte is byte 0.
	 * @return           Byte array set to integer value.
	 * @author           Jesse Peterson <jesse@jpeterson.com>
	 */
	protected static byte[] toBytes(int value, int len, boolean bigEndian) {
		byte[] result = new byte[len];

		for (int i = 0; i < len; i++)
			if (bigEndian)
				result[result.length - i - 1] = (byte) (value >>> (8 * i));

			else
				result[i] = (byte) (value >>> (8 * i));

		return (result);
	}

	/**
	 * Encode the EEPROM array.
	 *
	 * @return                               Total number of bytes to be
	 *      transmitted to the device.
	 * @exception OutOfMacroMemoryException  Throw if there are too many macros to
	 *      fit into the devices EEPROM memory.
	 * @author                               Jesse Peterson <jesse@jpeterson.com>
	 */
	protected int encodeEEPROM()
	throws OutOfMacroMemoryException {
		int macroOffset;
		int macroInitiatorOffset;
		int offset;
		int totalSize;
		Macro macro;
		TimerInitiator timerInitiator;
		MacroInitiator macroInitiator;
		byte[] component;
		Integer integer;
		int startMacroOffset;
		int stopMacroOffset;

		macroOffset = 0;
		macroInitiatorOffset = 0;
		offset = 0;

		// calculate macro offset
		macroOffset += SIZEOF_INITIAL_OFFSET;
		macroOffset += timerInitiators.size() *
		TimerInitiator.SIZEOF_TIMER_INITIATOR;
		macroOffset += SIZEOF_TIMER_TERMINATOR;
		macroInitiatorOffset = macroOffset;
		macroOffset += macroInitiators.size() *
		MacroInitiator.SIZEOF_MACRO_INITIATOR;
		macroOffset += 2;                                             // MACRO_INITIATOR_TERMINATOR

		try {
			// encode the macros
			for (Enumeration<TimerInitiator> e = timerInitiators.elements(); e.hasMoreElements(); ) {
				timerInitiator = (TimerInitiator) e.nextElement();

				macro = timerInitiator.getStartMacro();
				if (macro != null)
					macroOffset += encodeMacro(macro, macroOffset);

				macro = timerInitiator.getStopMacro();
				if (macro != null)
					macroOffset += encodeMacro(macro, macroOffset);

			}
			for (Enumeration<MacroInitiator> e = macroInitiators.elements(); e.hasMoreElements(); ) {
				macroInitiator = (MacroInitiator) e.nextElement();

				macro = macroInitiator.getMacro();
				if (macro != null)
					macroOffset += encodeMacro(macro, macroOffset);

			}

			totalSize = macroOffset;

			// encode the macro initiator table offset
			component = toBytes(macroInitiatorOffset, SIZEOF_INITIAL_OFFSET, true);
			System.arraycopy(component, 0, eeprom, offset, SIZEOF_INITIAL_OFFSET);
			offset += SIZEOF_INITIAL_OFFSET;

			// encode the timer initiators
			for (Enumeration<TimerInitiator> e = timerInitiators.elements(); e.hasMoreElements(); ) {
				timerInitiator = (TimerInitiator) e.nextElement();

				macro = timerInitiator.getStartMacro();
				if (macro != null) {
					integer = (Integer) macroOffsets.get(macro);
					if (integer != null)
						startMacroOffset = integer.intValue();

					else
						startMacroOffset = -1;

				}
				else
					startMacroOffset = -1;

				macro = timerInitiator.getStopMacro();
				if (macro != null) {
					integer = (Integer) macroOffsets.get(macro);
					if (integer != null)
						stopMacroOffset = integer.intValue();

					else
						stopMacroOffset = -1;

				}
				else
					stopMacroOffset = -1;

				component = timerInitiator.getBytes(startMacroOffset,
						stopMacroOffset);
				System.arraycopy(component, 0, eeprom, offset,
						component.length);
				offset += component.length;
			}

			// encode the timer terminator
			eeprom[offset] = TIMER_TERMINATOR;
			offset += SIZEOF_TIMER_TERMINATOR;

			// encode the macro initiators
			for (Enumeration<MacroInitiator> e = macroInitiators.elements(); e.hasMoreElements(); ) {
				macroInitiator = (MacroInitiator) e.nextElement();

				macro = macroInitiator.getMacro();
				if (macro != null) {
					integer = (Integer) macroOffsets.get(macro);
					if (integer != null)
						startMacroOffset = integer.intValue();

					else
						startMacroOffset = -1;

				}
				else
					startMacroOffset = -1;

				component = macroInitiator.getBytes(startMacroOffset);
				System.arraycopy(component, 0, eeprom, offset,
						component.length);
				offset += component.length;
			}

			// encode the macro initiator terminator
			eeprom[offset] = TIMER_TERMINATOR;
			offset += SIZEOF_TIMER_TERMINATOR;
			eeprom[offset] = TIMER_TERMINATOR;
			offset += SIZEOF_TIMER_TERMINATOR;

			return (totalSize);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			throw new OutOfMacroMemoryException("Unable to download macros device. Too many macros.");
		}
		catch (ArrayStoreException e) {
			throw new OutOfMacroMemoryException("Unable to download macros device. Too many macros.");
		}
	}

	/**
	 * Encode a macro into the EEPROM array.
	 *
	 * @param macro                               Macro to encode.
	 * @param macroOffset                         Index in EEPROM to copy macro to.
	 * @return                                    Return Value
	 * @author                                    Jesse Peterson
	 *      <jesse@jpeterson.com>
	 */
	protected int encodeMacro(Macro macro, int macroOffset) {
		byte[] component;

		if (macroOffsets.containsKey(macro))
			// don't encode the macro again
			return (0);

		// copy the macro to the eeprom array.
		component = macro.getBytes();
		System.arraycopy(component, 0, eeprom, macroOffset, component.length);

		// store the macro offset
		macroOffsets.put(macro, new Integer(macroOffset));

		// return the number of bytes use to store the macro
		return (component.length);
	}

	/**
	 * Add a timer initiator. The initiator will not be triggered until it is
	 * downloaded to the CM11A device with a call to <CODE>downloadInitiators()</CODE>
	 *
	 * @param timerInitiator  A timer initiator to add to the CM11A.
	 * @author                Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized void addTimerInitiator(TimerInitiator timerInitiator) {
		timerInitiators.addElement(timerInitiator);
	}

	/**
	 * Remove a timer initiator. The initiator will still be active until a call to
	 * <CODE>downloadInitiators()</CODE>
	 *
	 * @param timerInitiator  The timer initiator to remove.
	 * @return                <CODE>true</CODE> if the timer initiator was found,
	 *      <CODE>false</CODE> otherwise.
	 * @author                Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized boolean removeTimerInitiator(TimerInitiator timerInitiator) {
		return (timerInitiators.removeElement(timerInitiator));
	}

	/**
	 * Clear all timer initiators.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized void clearTimerInitiators() {
		timerInitiators.removeAllElements();
	}

	/**
	 * Retrieve an <CODE>Enumeration</CODE> of all timer initiators.
	 *
	 * @return   Return Value
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized Enumeration<TimerInitiator> timerInitiators() {
		return (timerInitiators.elements());
	}

	/**
	 * Add a macro initiator. The initiator will not be triggered until it is
	 * downloaded to the CM11A device with a call to <CODE>downloadInitiators()</CODE>
	 *
	 * @param macroInitiator  A macro initiator to add to the CM11A.
	 * @author                Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized void addMacroInitiator(MacroInitiator macroInitiator) {
		macroInitiators.addElement(macroInitiator);
	}

	/**
	 * Remove a macro initiator. The initiator will still be active until a call to
	 * <CODE>downloadInitiators()</CODE>
	 *
	 * @param macroInitiator  The macro initiator to remove.
	 * @return                <CODE>true</CODE> if the macro initiator was found,
	 *      <CODE>false</CODE> otherwise.
	 * @author                Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized boolean removeMacroInitiator(MacroInitiator macroInitiator) {
		return (macroInitiators.removeElement(macroInitiator));
	}

	/**
	 * Clear all macro initiators.
	 *
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized void clearMacroInitiators() {
		macroInitiators.removeAllElements();
	}

	/**
	 * Retrieve an <CODE>Enumeration</CODE> of all timer initiators.
	 *
	 * @return   Return Value
	 * @author   Jesse Peterson <jesse@jpeterson.com>
	 */
	public synchronized Enumeration<MacroInitiator> macroInitiators() {
		return (macroInitiators.elements());
	}

	/**
	 * Make sure that the month is valid. If it is greater than December(11), set
	 * the month to December. If the month is less than January(0), set the month
	 * to January.
	 *
	 * @param month  Month, zero based. e.g., 0 for January, 1 for February.
	 * @return       Normalized month.
	 * @author       Jesse Peterson <jesse@jpeterson.com>
	 */
	protected static int normalizeMonth(int month) {
		if (month > 11)
			return (11);
		if (month < 0)
			return (0);
		return (month);
	}

	/**
	 * Make sure that the day is valid. If it greater than the maximum day for the
	 * specified month, set the day to the maximum day. If it is less than the
	 * minimum day for the specified month, set the day to the minimum day.
	 *
	 * @param month  Month, zero based. e.g., 0 for January, 1 for February.
	 * @param day    Day to normalize.
	 * @return       Return Value
	 * @reutrn       Normalized day.
	 * @author       Jesse Peterson <jesse@jpeterson.com>
	 */
	protected static int normalizeDay(int month, int day) {
		if (day < 1)
			return (1);
		month = normalizeMonth(month);
		if ((month == 0) || (month == 2) || (month == 4) || (month == 6) ||
				(month == 7) || (month == 9) || (month == 11)) {
			// max of 31 days
			if (day > 31)
				return (31);
			return (day);
		}
		else if ((month == 3) || (month == 5) || (month == 8) || (month == 10)) {
			// max of 30 days
			if (day > 30)
				return (30);
			return (day);
		}
		else {
			// max of 29 days
			if (day > 29)
				return (29);
			return (day);
		}
	}

	/**
	 * Convert a date in the format of month and day to day of year format. The
	 * algorythm uses 366 days in a year, where 0 is January 1 and 365 is December
	 * 31.
	 *
	 * @param month  Month, zero based. e.g., 0 for January, 1 for February.
	 * @param day    Day of month
	 * @return       Day of year.
	 * @author       Jesse Peterson <jesse@jpeterson.com>
	 */
	protected static int dayOfYear(int month, int day) {
		int dayOfYear = 0;

		month = normalizeMonth(month);
		day = normalizeDay(month, day);

		for (int i = 0; i < month; i++)
			dayOfYear += daysInMonth[i];

		dayOfYear += (day - 1);

		return (dayOfYear);
	}

	/**
	 * Utility method to convert the Julian day returned in <code>getJulianDay</code>
	 * to the month the day represents. The month is zero based, i.e. 0 for
	 * January, 1 for February, ..., 11 for December. <P>
	 *
	 * As far as I can tell, the CM11A has no way of determining leap years. It
	 * therefore always uses 366 days in a year. The caller is responsible for
	 * determininging if the current day has been corrected for a non-leap year.
	 *
	 * @param julianDay  Parameter
	 * @return           Month represented by the julian day value. The month is
	 *      zero based, i.e. 0 for January, 1 for February, ..., 11 for December.
	 * @author           <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public static int extractMonth(int julianDay) {
		int i;
		int accumulator;

		accumulator = 0;

		for (i = 0;
		(i < daysInMonth.length) &&
		((accumulator + daysInMonth[i]) <= julianDay);
		i++)
			accumulator += daysInMonth[i];

		return (i);
	}

	/**
	 * Utility method to convert the Julian day returned in <code>getJulianDay</code>
	 * to a the day of the month represented. <P>
	 *
	 * As far as I can tell, the CM11A has no way of determining leap years. It
	 * therefore always uses 366 days in a year. The caller is responsible for
	 * determininging if the current day has been corrected for a non-leap year.
	 *
	 * @param julianDay  Parameter
	 * @return           Day of the month represented by the Julian day
	 * @see              extractMonth
	 * @see              getJulianDay
	 * @author           <a href="mailto:jesse@jpeterson.com">Jesse Peterson</a>
	 */
	public static int extractDay(int julianDay) {
		int month;
		int monthDays = 0;

		month = extractMonth(julianDay);

		for (int i = 0; i < month; i++)
			monthDays += daysInMonth[i];

		return (julianDay - monthDays + 1);
	}
}

