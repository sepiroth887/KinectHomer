package uk.ac.stir.cs.homer.componentX10.utils.stirlingAdaptor;


import com.jpeterson.x10.event.AddressEvent;
import com.jpeterson.x10.event.BrightEvent;
import com.jpeterson.x10.event.DimEvent;
import com.jpeterson.x10.event.OffEvent;
import com.jpeterson.x10.event.OnEvent;
import com.jpeterson.x10.event.X10Event;

import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  X10 action handler.

  @author	Kenneth J. Turner, Feng Wang
  @version	1.0:	04/06/08 (FW)
  <br/>		1.1:	29/05/09 (KJT)
  <br/>		1.2:	18/08/09 (KJT)
*/

public class KensX10Actions {

  /**
    Number of dim steps supported by an LM12 (20 for X10 Europe devices, 64
    reportedly for others)
  */
  private final static int dimSteps = 20;

  /** Current dim state for a given house address */
  private Hashtable<String,Integer> dimState = new Hashtable<String,Integer>();

  /** X10 transmitter service */
  private X10TransmitterService transmitterService;

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  /**
    Constructor for the X10Action object.

    @param context  bundle context
  */

  public KensX10Actions(X10TransmitterService transmitterService) {
    this.transmitterService = transmitterService;
  }

  /**
    Handle X10 action event.

    @param protocolCommand     protocol command (X10 command, e.g. "off")
    @param protocolAddress     protocol address (house/device code, e.g. "a1")
    @param protocolParameters  protocol parameters (unused)
  */

  public void handleEvent(String protocolCommand, String protocolAddress,
    String protocolParameters) {
    char houseCode = protocolAddress.toLowerCase().charAt(0);
    int deviceCode = -1;
    int dimPercent = -1;
    if (transmitterService != null) {
      try {
	deviceCode = Integer.parseInt(protocolAddress.substring(1));
	if ('a' <= houseCode && houseCode <= 'p' &&
	    1 <= deviceCode && deviceCode <= 16) {

	  // turn off
	  if (protocolCommand.equals("on")) {
	    if (!protocolParameters.equals(""))
	      logger.error("command '" + protocolCommand +
		"' may not have a parameter '" + protocolParameters + "'");
	    deviceOn(protocolAddress, houseCode, deviceCode);
	  }

	  // turn on
	  else if (protocolCommand.equals("off")) {
	    if (!protocolParameters.equals(""))
	    	logger.error("command '" + protocolCommand +
		"' may not have a parameter '" + protocolParameters + "'");
	    deviceOff(protocolAddress, houseCode, deviceCode);
	  }

	  // dim
	  else if (protocolCommand.equals("dim")) {
	    try {
	      dimPercent = Integer.parseInt(protocolParameters);
	      if (0 <= dimPercent && dimPercent <= 100)
		deviceDim(protocolAddress, houseCode, deviceCode, dimPercent);
	      else
	    	  logger.error("X10 dim parameter '" +
		  protocolParameters + "' must be in the range 0..100");
	    }
	    catch (NumberFormatException e) {
	    	logger.error("non-numeric dim amount '" +
		protocolParameters + "'");
	    }
	  }

	  // other command
	  else
		  logger.error("unrecognised X10 command '" +
	      protocolCommand + "'");
	}
	else
		logger.error("X10 house address '" + protocolAddress +
	    "' must be A..P 1..16");
      }
      catch (NumberFormatException e) {
    	  logger.error("non-numeric X10 device code in house address '" +
	  protocolAddress + "'");
      }
    }
    else
    	logger.error(
	"no X10 transmitter - restart X10Driver and PolicyAction");
  }

  /**
    Switch X10 device on. If the device has been dimmed, it must be turned off
    first or the "on" command will be ignored. After turning the device on, its
    dim level is set to -1 (i.e. unknown).

    @param protocolAddress	protocol address
    @param houseCode		house code
    @param deviceCode		device code
   */

  public void deviceOn(String protocolAddress, char houseCode, int deviceCode) {
	  logger.debug("Switching on " + houseCode + deviceCode);
    X10Event[] events = new X10Event[]{
      new AddressEvent(this, houseCode, deviceCode),
      new OnEvent(this, houseCode)
    };
    int dimLevel = getDim(protocolAddress);
    if (dimLevel != -1)
      deviceOff(protocolAddress, houseCode, deviceCode);
    setDim(protocolAddress, -1);
    transmitterService.transmit(events);
  }

  /**
    Switch X10 device off. After turning the device off, its dim level is set to
    -1 (i.e. unknown).

    @param protocolAddress	protocol address
    @param houseCode		house code
    @param deviceCode		device code
  */

  public void deviceOff(String protocolAddress,char houseCode, int deviceCode) {
    logger.debug("Switching off " + houseCode + deviceCode);
    X10Event[] events = new X10Event[]{
      new AddressEvent(this, houseCode, deviceCode),
      new OffEvent(this, houseCode)
    };
    setDim(protocolAddress, -1);
    transmitterService.transmit(events);
  }

  /**
    Dim an X10 device. This works only with LM12 lamp modules. The module
    remembers the current dim setting, so dim/bright events are relative to the
    last setting. If the dim state is unknown because this is the first command,
    the lamp must be turned off initially so that it is in a known dim state.
    When dimmed, the lamp will at first go to full power and will then dim down.

    @param protocolAddress	protocol address
    @param houseCode		house code
    @param deviceCode		device code
    @param dimPercent		dim percentage (0%-100%)
  */

  public void deviceDim(String protocolAddress,char houseCode, int deviceCode,
   int dimPercent) {
    logger.debug("Dimming " + houseCode + deviceCode + " level " +
      dimPercent + "%");
    // if dim level unknown, turn lamp on to get into known state
    int dimLevel = getDim(protocolAddress);
    if (dimLevel == -1) {
      deviceOff(protocolAddress, houseCode, deviceCode);
      dimLevel = dimSteps;
    }
    // get desired dim step from 0..dimSteps
    int dimStep = (int) ((dimPercent / 100.0) * dimSteps);
    // as a precaution, make sure dim step is in range
    if (dimStep < 0)
      dimStep = 0;
    else if (dimStep > dimSteps)
      dimStep = dimSteps;
    int dimDifference = dimStep - dimLevel;
    X10Event lampEvent = dimDifference < 0
      ? new DimEvent(this, houseCode, -dimDifference, dimSteps)
      : new BrightEvent(this, houseCode, dimDifference, dimSteps);
    X10Event[] events = new X10Event[] {
      new AddressEvent(this, houseCode, deviceCode),
      lampEvent
    };
    setDim(protocolAddress, dimStep);
    transmitterService.transmit(events);
  }

  /**
    Return dim level for device.

    @param protocolAddress	protocol address
    @return			current dim level 0..dimSteps (-1 if unknown)
  */

  public int getDim(String protocolAddress) {
    Object dimValue = dimState.get(protocolAddress);
    return(dimValue != null ? (Integer) dimValue : -1);
  }

  /**
    Set dim level for device.

    @param protocolAddress	protocol address
    @param dimValue		dim value
  */

  public void setDim(String protocolAddress, int dimValue) {
    dimState.put(protocolAddress, new Integer(dimValue));
  }

}

