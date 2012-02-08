package uk.ac.stir.cs.homer.serviceWeather;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.anddev.android.weatherforecast.weather.GoogleWeatherHandler;
import org.anddev.android.weatherforecast.weather.WeatherForecastCondition;
import org.anddev.android.weatherforecast.weather.WeatherSet;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Code adapted form 'plusminus's code which was originally written
 * as a google android application.
 * 
 * @author 'plusminus' 	v1.0
 * @author cma			v2.0
 *
 */
public class GoogleWeatherForecaster
{
	private GoogleWeatherHandler gwh;

	
	/**
	 * create a new google weather forecaster for specific location
	 * 
	 * @param location can be either postcode or [city, country] format
	 */
	public GoogleWeatherForecaster(String location)
	{
		gwh = getGoogleWeatherHandler(location);
	}
	
	private GoogleWeatherHandler getGoogleWeatherHandler(String location) {
		GoogleWeatherHandler gwh = null;
		try
		{
			String queryString = "http://www.google.com/ig/api?weather="
				+ location;
			
			/* Replace blanks with HTML-Equivalent. */
			URL url = new URL(queryString.replace(" ", "%20"));

			
			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();

			gwh = new GoogleWeatherHandler();
			xr.setContentHandler(gwh);

			/* Parse the xml-data our URL-call returned. */
			xr.parse(new InputSource(url.openStream()));

		}
		catch (Exception e)
		{
			System.out.println("soemthing went wrong");
			e.printStackTrace();
		}
		return gwh;
	}

	public void updateLocation(String location) {
		gwh = getGoogleWeatherHandler(location);
	}
	public WeatherConditions getTodaysWeather()
	{
		return gwh.getWeatherSet().getWeatherCurrentCondition();
	}
	
	public WeatherForecastCondition getTomorrowsWeather()
	{
		return getDaysWeather(1);
	}
	
	public WeatherForecastCondition getTwoDaysAwayWeather()
	{
		return getDaysWeather(2);
	}
	
	public WeatherForecastCondition getThreeDaysAwayWeather()
	{
		return getDaysWeather(3);
	}
	
	public WeatherForecastCondition getDaysWeather(int day)
	{
		/* Our Handler now provides the parsed weather-data to us. */
		WeatherSet ws = gwh.getWeatherSet();
		return ws.getWeatherForecastConditions().get(day);
	}

	
}
