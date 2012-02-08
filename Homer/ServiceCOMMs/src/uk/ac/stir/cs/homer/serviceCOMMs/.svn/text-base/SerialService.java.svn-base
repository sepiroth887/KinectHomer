package uk.ac.stir.cs.homer.serviceCOMMs;

import javax.comm.SerialPort;


/**
  Serial port service implementation.

  @author	Michael E. Wilson, Feng Wang, Kenneth J. Turner
  @version	Revision 1.0: 12/05/03 (MEW)
  <br/>		Revision 1.1: 01/06/08 (FW)
  <br/>		Revision 1.2: 06/06/09 (KJT)
*/

public class SerialService {

  private SerialPort port;
  private String portName;

  /**
    Constructor for the serial service implementation.

    @param port		serial port
    @param portName	port name
  */

  public SerialService(SerialPort port, String portName) {
    this.port = port;
    this.portName = portName;
  }

  /** Action on no driver found (no action). */

  public void noDriverFound() {
  }

  /**
    Gets the port attribute of the SerialServiceImpl object.

    @return		port
  */

  public SerialPort getPort() {
    return port;
  }

  /**
    Get port name.

    @return		port name
  */

  public String portName() {
    return portName;
  }

  /** Close port. */

  public void closePort() {
    if (port != null)
      port.close();
  }

}

