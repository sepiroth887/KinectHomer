package uk.ac.stir.cs.homer.homerFrameworkAPI.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class ServiceUtils
{

	public static <T> T newService(BundleContext context, T serviceInstance)
	{
		context.registerService(serviceInstance.getClass().getName(), serviceInstance, null);
		return serviceInstance;	 	
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] getServices(BundleContext context, Class<T> c)
	{
		ServiceTracker tracker = new ServiceTracker(context, c.getName(), null);
		tracker.open();

		T[] bundles = null;
		try
		{
			bundles = (T[]) tracker.getServices();
		} catch (NullPointerException e)
		{
			System.out.println("Tracker = null");
		}
		return bundles;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getService(BundleContext context, Class<T> c)
	{
		ServiceTracker tracker = new ServiceTracker(context, c.getName(), null);
		tracker.open();

		T o = null;
		try
		{
			o = (T) tracker.getService();
		} catch (NullPointerException e)
		{
			System.out.println("Tracker = null");
		}
		return o;
	}
	
}
