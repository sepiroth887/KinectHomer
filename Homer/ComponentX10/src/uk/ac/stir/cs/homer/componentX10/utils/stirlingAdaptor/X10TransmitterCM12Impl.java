package uk.ac.stir.cs.homer.componentX10.utils.stirlingAdaptor;

import com.jpeterson.x10.Gateway;
import com.jpeterson.x10.Transmitter;
import com.jpeterson.x10.event.AddressEvent;
import com.jpeterson.x10.event.AddressListener;
import com.jpeterson.x10.event.FunctionEvent;
import com.jpeterson.x10.event.FunctionListener;
import com.jpeterson.x10.event.OnEvent;
import com.jpeterson.x10.event.X10Event;
import com.jpeterson.x10.module.CM11A;

import javax.comm.SerialPort;


/**
 * X10 transmitter implementation for CM12.
 *
 * @author	Michael E. Wilson and Kenneth J. Turner
 * @version	Revision 1.0: 07/05/03 (MEW)
 * <br/>	Revision 1.1: 14/07/03 (MEW)
 * <br/>	Revision 1.2: 13/05/09 (KJT)
 */

public class X10TransmitterCM12Impl implements
  AddressListener, FunctionListener, X10TransmitterService {

  /**
   * do the bits up here for setting up the adapter for the x10 controller
   */
  private CM11A transmitter;
  private final static String DEFAULT_TRANSMITTER =
    "com.jpeterson.x10.module.CM11A";
  private X10EventNotifier x10notifier;

  /**
   * CM12 Constructor
   *
   * @param ss             Parameter
   * @param ctxt           Parameter
   * @param notifier       Parameter
   */
  public X10TransmitterCM12Impl(SerialPort serialPort,
   X10EventNotifier notifier) {

    String transmitter = DEFAULT_TRANSMITTER;
    String port = serialPort.getName();

    openTransmitterGateway(port, transmitter, serialPort);

    x10notifier = notifier;
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#noDriverFound()
 */
  public void noDriverFound() {
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#openTransmitterGateway(java.lang.String, java.lang.String, javax.comm.SerialPort)
 */
  public void openTransmitterGateway(String port, String transmitterName,
   SerialPort s) {
	  System.out.println("opened transmitter gateway");
    try {
      transmitter = (CM11A) Class.forName(transmitterName).newInstance();
      transmitter.setPortName(port);
      transmitter.setSerialPort(s);
      transmitter.addAddressListener(this);
      transmitter.addFunctionListener(this);
      transmitter.allocate();
    }
    catch (Exception e) {
      System.out.println(e);
    }
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#closeTransmitterGateway()
 */
  public void closeTransmitterGateway() {
    if (transmitter instanceof Gateway) {
      Gateway gateway = (Gateway) transmitter;
      try {
	// removed by MEW... if in the bundle, doesn't close properly
	   gateway.waitGatewayState(Transmitter.QUEUE_EMPTY);
      }
      catch (Exception e) {}

      try {
    	  gateway.deallocate();
    	  System.out.println("closed transmitter gateway");
      }
      catch (Exception e) {}
     
      return;
    }
    System.out.println("failed to close transmitter gateway");
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#transmit(com.jpeterson.x10.event.X10Event[])
 */
  public void transmit(X10Event events[]) {
    // this for method sends each element of the array down the line
    // to the X10Transmitter
    for (int j = 0; j < events.length; j++)
      transmitter.transmit(events[j]);
    
    System.out.println("Event transmitted");
    
    //closeTransmitterGateway();
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#transmit(char, int)
 */
  public void transmit(char houseCode, int deviceNumber) {
    X10Event events[] = new X10Event[2];
    events[0] = new AddressEvent(this, houseCode, deviceNumber);
    events[1] = new OnEvent(this, houseCode);

    transmit(events);
  }

  /*
   More functionality needed here, see the X10 API under X10 events for what
   still needs to be done, e.g. all devices on/off, all devices in a particular
   room on/off.
  */

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#address(com.jpeterson.x10.event.AddressEvent)
 */

  public void address(AddressEvent e) {
    //System.out.println(e);
    eventReceived(null, e);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionAllUnitsOff(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionAllUnitsOff(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionAllLightsOn(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionAllLightsOn(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionOn(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionOn(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionOff(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionOff(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionDim(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionDim(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionBright(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionBright(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionAllLightsOff(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionAllLightsOff(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionHailRequest(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionHailRequest(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }


  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionHailAcknowledge(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionHailAcknowledge(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionPresetDim1(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionPresetDim1(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionPresetDim2(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionPresetDim2(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionStatusOn(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionStatusOn(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionStatusOff(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionStatusOff(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#functionStatusRequest(com.jpeterson.x10.event.FunctionEvent)
 */
  public void functionStatusRequest(FunctionEvent e) {
    //System.out.println(e);
    eventReceived(e, null);
  }

  /* (non-Javadoc)
 * @see uk.ac.stir.cs.x10.test#eventReceived(com.jpeterson.x10.event.FunctionEvent, com.jpeterson.x10.event.AddressEvent)
 */
  public void eventReceived(FunctionEvent functionEvent, AddressEvent addressEvent) {
    x10notifier.eventReceived(functionEvent, addressEvent);
  }

}

