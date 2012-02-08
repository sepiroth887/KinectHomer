package org.anddev.android.weatherforecast.weather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uk.ac.stir.cs.homer.serviceWeather.WeatherConditions;

/**
 * SAXHandler capable of extracting information out of the xml-data returned by
 * the Google Weather API.
 */
public class GoogleWeatherHandler extends DefaultHandler {

	// ===========================================================
	// Fields
	// ===========================================================

	private WeatherSet myWeatherSet = null;
	private boolean in_current_conditions = false;
	private boolean in_forecast_conditions = false;

	private boolean usingSITemperature = false; // false means Fahrenheit

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public WeatherSet getWeatherSet() {
		return this.myWeatherSet;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void startDocument() throws SAXException {
		this.myWeatherSet = new WeatherSet();
	}

	
	public void endDocument() throws SAXException {
	}

	
	public void startElement(String namespaceURI, String alwaysEmptyString,
			String localName, Attributes atts) throws SAXException {
		// 'Outer' Tags
		if (localName.equals("forecast_information")) {
			this.in_current_conditions = false;
			this.in_forecast_conditions = false;
		} else if (localName.equals("current_conditions")) {
			this.myWeatherSet
					.setWeatherCurrentCondition(new WeatherConditions());
			this.in_current_conditions = true;
			this.in_forecast_conditions = false;
		} else if (localName.equals("forecast_conditions")) {
			this.myWeatherSet.getWeatherForecastConditions().add(
					new WeatherForecastCondition());
			this.in_current_conditions = false;
			this.in_forecast_conditions = true;
		} else {
			String dataAttribute = atts.getValue("data");
			// 'Inner' Tags of "<forecast_information>"
			if (localName.equals("city")) {
			} else if (localName.equals("postal_code")) {
			} else if (localName.equals("latitude_e6")) {
				/* One could use this to convert city-name to Lat/Long. */
			} else if (localName.equals("longitude_e6")) {
				/* One could use this to convert city-name to Lat/Long. */
			} else if (localName.equals("forecast_date")) {
			} else if (localName.equals("current_date_time")) {
			} else if (localName.equals("unit_system")) {
				if (dataAttribute.equals("SI"))
					this.usingSITemperature = true;
			}
			// SHARED(!) 'Inner' Tags within "<current_conditions>" AND
			// "<forecast_conditions>"
			else if (localName.equals("day_of_week")) {
				if (this.in_current_conditions) {
					this.myWeatherSet.getWeatherCurrentCondition()
							.setDayofWeek(dataAttribute);
				} else if (this.in_forecast_conditions) {
					this.myWeatherSet.getLastWeatherForecastCondition()
							.setDayofWeek(dataAttribute);
				}
			} else if (localName.equals("icon")) {
				if (this.in_current_conditions) {
					this.myWeatherSet.getWeatherCurrentCondition().setIconURL(
							"http://www.google.com" + dataAttribute);
				} else if (this.in_forecast_conditions) {
					this.myWeatherSet.getLastWeatherForecastCondition()
							.setIconURL("http://www.google.com" +dataAttribute);
				}
			} else if (localName.equals("condition")) {
				if (this.in_current_conditions) {
					this.myWeatherSet.getWeatherCurrentCondition()
							.setCondition(dataAttribute);
				} else if (this.in_forecast_conditions) {
					this.myWeatherSet.getLastWeatherForecastCondition()
							.setCondition(dataAttribute);
				}
			}
			// 'Inner' Tags within "<current_conditions>"
			else if (localName.equals("temp_f")) {
				this.myWeatherSet.getWeatherCurrentCondition()
						.setTempFahrenheit(Integer.parseInt(dataAttribute));
			} else if (localName.equals("temp_c")) {
				this.myWeatherSet.getWeatherCurrentCondition().setTempCelcius(
						Integer.parseInt(dataAttribute));
			} else if (localName.equals("humidity")) {
				this.myWeatherSet.getWeatherCurrentCondition().setHumidity(
						dataAttribute);
			} else if (localName.equals("wind_condition")) {
				this.myWeatherSet.getWeatherCurrentCondition()
						.setWindSummary(dataAttribute);
			}
			// 'Inner' Tags within "<forecast_conditions>"
			else if (localName.equals("low")) {
				int temp = Integer.parseInt(dataAttribute);
				if (this.usingSITemperature) {
					this.myWeatherSet.getLastWeatherForecastCondition()
							.setTempMinCelsius(temp);
				} else {
					this.myWeatherSet.getLastWeatherForecastCondition()
							.setTempMinCelsius(
									WeatherUtils.fahrenheitToCelsius(temp));
				}
			} else if (localName.equals("high")) {
				int temp = Integer.parseInt(dataAttribute);
				if (this.usingSITemperature) {
					this.myWeatherSet.getLastWeatherForecastCondition()
							.setTempMaxCelsius(temp);
				} else {
					this.myWeatherSet.getLastWeatherForecastCondition()
							.setTempMaxCelsius(
									WeatherUtils.fahrenheitToCelsius(temp));
				}
			}
		}
	}

	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("forecast_information")) {
		} else if (localName.equals("current_conditions")) {
			this.in_current_conditions = false;
		} else if (localName.equals("forecast_conditions")) {
			this.in_forecast_conditions = false;
		}
	}

	
	public void characters(char ch[], int start, int length) {
		/*
		 * Would be called on the following structure: <element>characters</element>
		 */
	}
}
