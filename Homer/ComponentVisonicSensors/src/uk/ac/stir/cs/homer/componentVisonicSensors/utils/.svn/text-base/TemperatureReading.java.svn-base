package uk.ac.stir.cs.homer.componentVisonicSensors.utils;

public class TemperatureReading
{
	private String sensorID;
	private double temperature, prevtemperature;
	private int humidity, prevhumidity;
	
	public TemperatureReading(String sensorID, double prevTemperature, double newTemp, int prevHumidity, int newHumidty)
	{
		this.sensorID = sensorID;
		this.prevtemperature = prevTemperature;
		this.temperature = newTemp;
		this.prevhumidity = prevHumidity;
		this.humidity = newHumidty;
	}
	
	public String getSensorID()
	{
		return sensorID;
	}
	public void setSensorID(String sensorID)
	{
		this.sensorID = sensorID;
	}
	public double getTemperature()
	{
		return temperature;
	}
	public double getPreviousTemperature()
	{
		return prevtemperature;
	}
	public void setTemperature(double temperature)
	{
		prevtemperature = this.temperature;
		this.temperature = temperature;
	}
	public int getHumidity()
	{
		return humidity;
	}
	public int getPreviousHumidity()
	{
		return prevhumidity;
	}
	public void setHumidity(int humidity)
	{
		prevhumidity = this.humidity;
		this.humidity = humidity;
	}
	public boolean tempHasRisen()
	{
		return (prevtemperature < temperature);
	}
	public boolean humHasRisen()
	{
		return (prevhumidity < humidity);
	}
}
