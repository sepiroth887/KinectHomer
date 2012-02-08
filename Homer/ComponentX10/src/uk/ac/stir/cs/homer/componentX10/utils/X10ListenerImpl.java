package uk.ac.stir.cs.homer.componentX10.utils;

import java.util.Observable;

import uk.ac.stir.cs.homer.componentX10.utils.stirlingAdaptor.X10Listener;

import com.jpeterson.x10.event.AddressEvent;
import com.jpeterson.x10.event.FunctionEvent;

public class X10ListenerImpl extends Observable implements X10Listener
{

	
	@Override
	public void notifyX10AddressEvent(AddressEvent addressEvent)
	{

	}

	@Override
	public void notifyX10FunctionEvent(FunctionEvent functionEvent)
	{
		this.setChanged();
		this.notifyObservers(functionEvent);
	}

	

}
