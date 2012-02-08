package uk.ac.stir.cs.homer.serviceWeather;

/**
 * Holds the information between the <current_conditions>-tag of what the Google
 * Weather API returned.
 */
public class WeatherConditions {

	// ===========================================================
	// Fields
	// ===========================================================

	private String dayofWeek = null;
	private Integer tempCelcius = null;
	private Integer tempFahrenheit = null;
	private String iconURL = null;
	private String condition = null;
	private String windSummary = null;
	private String windDirection = null;
	private int windSpeed = 0;
	private String humidity = null;

	// ===========================================================
	// Constructors
	// ===========================================================

	public WeatherConditions() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getDayofWeek() {
		return this.dayofWeek;
	}

	public void setDayofWeek(String dayofWeek) {
		this.dayofWeek = dayofWeek;
	}

	public Integer getTempCelcius() {
		return this.tempCelcius;
	}

	public void setTempCelcius(Integer temp) {
		this.tempCelcius = temp;
	}

	public Integer getTempFahrenheit() {
		return this.tempFahrenheit;
	}

	public void setTempFahrenheit(Integer temp) {
		this.tempFahrenheit = temp;
	}

	public String getIconURL() {
		return this.iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	public String getCondition() {
		return this.condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getWindSummary() {
		return this.windSummary;
	}

	public void setWindSummary(String windSummary) {
		this.windSummary = windSummary;
		
		//in format: W at 11 mpg
		String[] eachPart = windSummary.split(" ");
		this.setWindDirection(eachPart[0]);
		this.setWindSpeed(Integer.parseInt(eachPart[3]));
	}

	public String getHumidity() {
		return this.humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public void setWindDirection(String windDirection)
	{
		this.windDirection = windDirection;
	}

	public String getWindDirection()
	{
		return windDirection;
	}

	public void setWindSpeed(int windSpeed)
	{
		this.windSpeed = windSpeed;
	}

	public int getWindSpeed()
	{
		return windSpeed;
	}
}
