package uk.ac.stir.cs.homer.serviceCOMMs;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  Activator for CommAccess.

  <p>
  Modified by Feng Wang to solve the incompatibility between GPRS modem and X10
  controller. They both use the serial ports. Originally this program open all
  serial ports and make them as services This prevents the GPRS modem to open
  its port. After the modification, instead of opening all serial ports, this
  service only opens COM1 and COM2 if they both exist.

  <p>
  Generalised by Ken Turner to include ports defined by property
  <var>comm.ports</var> (e.g. "COM7,COM8,COM9").

  @author	Michael E. Wilson, Feng Wang, Kenneth J. Turner
  @version	Revision 1.0: 2003-05-12 (MEW)
  <br/>		Revision 1.1: 2008-06-01 (FW)
  <br/>		Revision 1.2: 2009-05-13 (KJT)
  <br/>		Attacked by cma 2009-06-09
 */

public class CommAccess  {

	private Vector<SerialPort> ports = new Vector<SerialPort>();
	private ArrayList<SerialService> serialServices = new ArrayList<SerialService>();;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
    Check if given name is in given list of names.

    @param name 	name to check
    @param names 	name list
    @return		true if name is in given list
	 */

	public boolean isIncluded(String name, String[] names) {
		boolean result = false;
		for (int i = 0; i < names.length; i++) {
			if (name.equalsIgnoreCase(names[i])) {
				result = true;
				break;
			}
		}
		return(result);
	}


	public SerialService openPort(String port)
	{
		openPorts(new String[] {port});
		return getSerialService(port);
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.stir.cs.device.serial.CommAccess#openPorts(java.lang.String[])
	 */

	public void openPorts(String[] includePorts) {
		// open serial ports
		String portList = "";

		for (Enumeration<?> enum0 = CommPortIdentifier.getPortIdentifiers();
		enum0.hasMoreElements(); ) {
			CommPortIdentifier portId = (CommPortIdentifier) enum0.nextElement();
			
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				String portName = portId.getName();
				if (!isIncluded(portName, includePorts)) {
					// logNote("ignoring port " + portName);
					continue;
				}

				try {
					SerialPort port = (SerialPort) portId.open("CommAccess", 2000);
					ports.add(port);
					portList += " " + portName;
					serialServices.add(new SerialService(port, port.getName()));
				}
				catch (PortInUseException e) {
					// the serial port is occupied, skip it
					logger.error("port " + portName + " in use");
				}
			}
		}
		if (portList.equals(""))
		{
			logger.error("no ports found");
		}
		else
			logger.debug("started ports" + portList);
	}


	public void closeAllPorts() throws Exception
	{
		String portList = "";
		for (int i = 0; i < ports.size(); i++) {
			SerialPort port = (SerialPort) (Object) ports.elementAt(i);
			portList += " " + port.getName();
			port.close();
		}
		logger.debug("stopped ports" + portList);
	}
	
	//struggling to get from a string to a port, so am going through the entire list of ports
	//already opened and closing any that are included in the includeports list.  messy.

	public void closePorts(String[] includePorts) throws Exception
	{
		String portList="";
		for (int i = 0; i < ports.size(); i++) {
			SerialPort port = (SerialPort)ports.elementAt(i);
			
			for (int j = 0; j < includePorts.length; j++) {
				if (port.getName().equalsIgnoreCase(includePorts[i]))
				{
					portList += " " + port.getName();
					ports.remove((Object)includePorts[i]);
					port.close();
					break;
				}
			}
		}
		if (!portList.equals(""))
		{
			logger.debug("stopped ports: " + portList);
		}
		else
			logger.debug("no ports were able to be stopped");
	}
	
	public SerialService getSerialService(String port)
	{
		for (SerialService ss: serialServices)
		{
			if (ss.getPort().getName().toString().equals(port))
				return ss;
		}
		return null;
	}
	

	public static void main (String[] args)
	{
		new CommAccess();
	}

	

	
}
