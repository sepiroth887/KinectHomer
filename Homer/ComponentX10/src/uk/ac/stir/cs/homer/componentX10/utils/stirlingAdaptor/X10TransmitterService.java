package uk.ac.stir.cs.homer.componentX10.utils.stirlingAdaptor;

import com.jpeterson.x10.event.X10Event;

import javax.comm.SerialPort;


/**
 * X10 motion sensor service.
 *
 * @author	Michael E. Wilson and Kenneth J. Turner
 * @version	Revision 1.0: 14/07/03 (MEW)
 * <br/>	Revision 1.2: 13/05/09 (KJT)
 */

public interface X10TransmitterService  {

  /**
   * Set up transmitter
   *
   * @param port             Parameter
   * @param transmitterName  Parameter
   * @param serialPort       Parameter
   */
  public void openTransmitterGateway(String port, String transmitterName, SerialPort serialPort);

  /** Clears up the adapter and closes the gateway  */
  public void closeTransmitterGateway();


  /**
   * Sends the event array to the X10Transmitter
   *
   * @param events    Parameter
   */
  public void transmit(X10Event events[]);


 /**
   * Description of the Method
   *
   * @param houseCode     Parameter
   * @param deviceNumber  Parameter
   */
  public void transmit(char houseCode, int deviceNumber);

}

