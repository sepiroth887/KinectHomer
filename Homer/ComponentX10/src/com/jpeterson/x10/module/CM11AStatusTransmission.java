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
import java.io.OutputStream;

import com.jpeterson.util.HexFormat;
import com.jpeterson.x10.InterruptedTransmissionException;
import com.jpeterson.x10.TooManyAttemptsException;
import com.jpeterson.x10.module.event.CM11AStatusEvent;
import com.jpeterson.x10.module.event.CM11AStatusListener;

/**
 * Create a status request. The CM11A performs monitoring on a certain house
 * code. The status request downloads the status of the monitored house code.
 *
 * @author    Jesse Peterson <jesse@jpeterson.com>
 * @created   15 May 2009
 */
public class CM11AStatusTransmission extends Object
   implements CM11ATransmissionEvent {
  private int attempts;
  private int maxAttempts;
  private CM11A cm11a;
  private CM11AStatusListener listener;

  private final static byte STATUS_REQ = (byte) 0x8b;

  private final static int STATUS_SIZE = 14;

  /**
   * Create a standard CM11 transmission event to request the status of the
   * monitoring performed by the CM11 interface
   *
   * @param parent    The CM11A device to retrieve the status of.
   * @param listener  CM11AStatusListener to notify when status retrieved. May be
   *      null.
   * @author          Jesse Peterson <jesse@jpeterson.com>
   */
  public CM11AStatusTransmission(CM11A parent, CM11AStatusListener listener) {
    attempts = 0;
    setMaxAttempts(3);
    cm11a = parent;
    this.listener = listener;
  }

  /**
   * Transmit a CM11 status command.
   *
   * @param in                                    Input stream to read from
   * @param out                                   Output stream to write to
   * @exception TooManyAttemptsException          Too many retries have occurred
   * @exception InterruptedTransmissionException  An unsolicited interrupt has
   *      been received during the transmission.
   * @exception IOException                       Some sort of I/O or I/O
   *      protocol error has occurred
   * @exception EOFException                      Exception
   * @author                                      Jesse Peterson
   *      <jesse@jpeterson.com>
   */
  public void transmit(InputStream in, OutputStream out)
    throws
    EOFException, IOException, InterruptedTransmissionException, TooManyAttemptsException {
    byte[] buffer = new byte[STATUS_SIZE];
    int numBytesRead = 0;
    CM11AStatusEvent event;
    HexFormat hex = new HexFormat();

    // mark off an attempt
    ++attempts;

    if (attempts > maxAttempts)
      throw new TooManyAttemptsException();

    if (System.getProperty("DEBUG") != null) {
      System.out.println("Sending CM11AStatusTransmission");
      System.out.println("PC->CM11A: 0x" + hex.format(STATUS_REQ));
    }

    // send status request
    out.write(STATUS_REQ);

    // wait a second
    try {
      Thread.sleep(1000);
    }
    catch (InterruptedException e) {
      // do nothing
    }
    if (System.getProperty("DEBUG") != null)
      System.out.println("Bytes available: " + in.available());

    // expect STATUS_SIZE bytes to be returned
    if (in.available() < STATUS_SIZE) {
      if (System.getProperty("DEBUG") != null)
	System.out.println("Not enough bytes for a status message yet");

      // if we got here, we don't have the correct number of bytes
      // available, maybe we have an unsolicited event.
      int result;
      byte byteValue;

      result = in.read();

      if (result == -1)
	throw new EOFException("Unexpected end of stream indicator received while retrieving status.");
      byteValue = (byte) result;

      if (System.getProperty("DEBUG") != null) {
	System.out.println("byte read: 0x" + hex.format(byteValue));
	System.out.println("Second chance bytes available: " + in.available());
      }

      if ((byteValue == CM11A.CM11_RECEIVE_EVENT) ||
	(byteValue == CM11A.CM11_POWER_FAILURE) ||
	(byteValue == CM11A.CM11_MACRO_INITIATED))
	throw new InterruptedTransmissionException(byteValue);
      else if (in.available() >= (STATUS_SIZE - 1)) {
	if (System.getProperty("DEBUG") != null)
	  System.out.println("Now, enough bytes are available.");

	// may be slow to get the bytes uploaded, give it another
	// chance.
	buffer[0] = byteValue;
	numBytesRead = in.read(buffer, 1, buffer.length - 1) + 1;
	// received the status buffer, continue on
      }
      else {
	System.err.println("CM11AStatusTransmission.transmit: " +
	  "breakdown in protocol, consuming all bytes - is this port " +
	  "connected to an X10 module?");

	// consume all bytes in input stream
	while (in.available() > 0)
	  in.read(buffer);

	// retransmit
	transmit(in, out);
	return;
      }
    }
    else {
      if (System.getProperty("DEBUG") != null)
	System.out.println("Reading...");

      numBytesRead = in.read(buffer);
    }

    if (System.getProperty("DEBUG") != null)
      System.out.println("Checking number of bytes read...");

    if (numBytesRead != STATUS_SIZE) {
      System.err.println("Invalid status buffer size.  Received " + numBytesRead + " bytes out of " + STATUS_SIZE + " bytes.");

      // consume all bytes in input stream
      while (in.available() > 0)
	in.read(buffer);

      // retransmit
      transmit(in, out);
      return;
    }

    if (System.getProperty("DEBUG") != null)
      System.out.println("Calling method to decode status...");

    event = cm11a.decodeStatus(buffer, 0, buffer.length);

    if ((listener != null) && (event != null))
      // notify listener
      listener.status(event);

    // transmission complete
  }

  /**
   * Retrieve the number of transmission attempts.
   *
   * @return   the number of transmission attempts
   * @author   Jesse Peterson <jesse@jpeterson.com>
   */
  public int getNumAttempts() {
    return (attempts);
  }

  /**
   * Set the number of transmission attempts
   *
   * @param maxAttempts  the maximum number of transmission attempts
   * @author             Jesse Peterson <jesse@jpeterson.com>
   */
  public void setMaxAttempts(int maxAttempts) {
    this.maxAttempts = maxAttempts;
  }

  /**
   * Create a string representation of the transmission.
   *
   * @return   String representation of the transmission.
   * @author   Jesse Peterson <jesse@jpeterson.com>
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    // HexFormat hexFormat = new HexFormat();
    // String prefix = "";

    buffer.append("CM11AStatusTransmission");
    return (buffer.toString());
  }
}

