package uk.ac.stir.cs.homer.componentVisonicSensors.utils;


import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

public class EventObservers implements Observer
{
	private HashSet<Observer> observers = new HashSet< Observer>();
	
	public void addNewObserver( Observer o)
	{
		observers.add(o);
	}

	public void removeObserver(Observer observer) {
		observers.remove(observer);
		
	}
	
	@Override
	public void update(Observable o, Object arg)
	{
		for (Observer obs : observers)
		{
			obs.update(o, arg);
		}
	}
}
