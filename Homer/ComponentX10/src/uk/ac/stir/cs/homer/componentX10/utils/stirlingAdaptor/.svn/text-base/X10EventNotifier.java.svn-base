package uk.ac.stir.cs.homer.componentX10.utils.stirlingAdaptor;

import com.jpeterson.x10.event.AddressEvent;
import com.jpeterson.x10.event.FunctionEvent;

/**
 * X10 event notifier.
 * 
 * @author Michael E. Wilson and Kenneth J. Turner
 * @version Revision 1.0: 14/07/03 (MEW) <br/> Revision 1.2: 13/05/09 (KJT)
 */

public class X10EventNotifier
{
	private X10Listener x10listener;
	
	public X10EventNotifier(X10Listener  x10listener)
	{
		this.x10listener = x10listener;
	}

	/**
	 * Description of the Method
	 * 
	 * @param functionEvent
	 *            Parameter
	 * @param addressEvent
	 *            Parameter
	 */
	public void eventReceived(FunctionEvent functionEvent,
			AddressEvent addressEvent)
	{
		if (functionEvent != null)
			x10listener.notifyX10FunctionEvent(functionEvent);
		if (addressEvent != null)
			x10listener.notifyX10AddressEvent(addressEvent);
		
	}
}
