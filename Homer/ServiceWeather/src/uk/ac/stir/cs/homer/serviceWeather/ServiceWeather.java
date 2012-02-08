package uk.ac.stir.cs.homer.serviceWeather;


public class ServiceWeather 
{
	public static WeatherConditions getWeather(String location)
	{
		GoogleWeatherForecaster googleWeatherForecaster = new GoogleWeatherForecaster(location);
		return googleWeatherForecaster.getTodaysWeather();
	}
}
