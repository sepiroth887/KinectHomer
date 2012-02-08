package uk.ac.stir.cs.homer.homerFrameworkAPI.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.HomerComponent;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasConditions;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.encoding.IDUtil;
import uk.ac.stir.cs.homer.homerFrameworkAPI.homerObjects.SystemDeviceType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.Comparitor;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.HomerNumber;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.Parameter;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.ParameterData;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.Unit;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Condition;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.core.IntVar;

public class TimeAndDateComponent implements HomerComponent, WhichHasConditions
{
	public static final String SYSTEM_DEVICE_TIME = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "TIME");
	public static final String SYSTEM_DEVICE_DATE = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "DATE");
	
	public static final String CONDITION_TIME_IS = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "TIME IS");
	public static final String CONDITION_TIME_IS_BEFORE = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "TIME IS BEFORE");
	public static final String CONDITION_TIME_IS_AFTER = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "TIME IS AFTER");
	public static final String CONDITION_TIME_IS_BETWEEN = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "TIME IS BETWEEN");
	
	public static final String CONDITION_DATE_IS_WEEKDAY = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "DATE IS WEEKDAY");
	public static final String CONDITION_DATE_IS_WEEKEND = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "DATE IS WEEKEND");
	public static final String CONDITION_DATE_IS_MONDAY = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "DATE IS MONDAY");
	public static final String CONDITION_DATE_IS_TUESDAY = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "DATE IS TUESDAY");
	public static final String CONDITION_DATE_IS_WEDNESDAY = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "DATE IS WEDNESDAY");
	public static final String CONDITION_DATE_IS_THURSDAY = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "DATE IS THURSDAY");
	public static final String CONDITION_DATE_IS_FRIDAY = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "DATE IS FRIDAY");
	public static final String CONDITION_DATE_IS_SATURDAY = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "DATE IS SATURDAY");
	public static final String CONDITION_DATE_IS_SUNDAY = IDUtil.getUniqueIdentifier(TimeAndDateComponent.class, "DATE IS SUNDAY");
	
	@Override
	public SystemDeviceType[] getSystemDeviceTypeData()
	{
		return new SystemDeviceType[] { new SystemDeviceType(SYSTEM_DEVICE_TIME, "time", null), new SystemDeviceType(SYSTEM_DEVICE_DATE, "day", null) };
	}

	@Override
	public void registerComponentInstance(String systemDeviceTypeID,
			String sysDeviceID, String[] parameters)
	{}

	@Override
	public void editComponentInstance(String systemDeviceTypeID,
			String sysDeviceID, String[] newParameters, String[] oldParameters)
	{}

	@Override
	public void deleteComponentInstance(String systemDeviceTypeID,
			String sysDeviceID, String[] parameters)
	{}

	@Override
	public List<Condition> getConditions(String systemDeviceTypeID)
	{
		List<Condition> conditions = new ArrayList<Condition>();
		if (systemDeviceTypeID.equals(SYSTEM_DEVICE_DATE))
		{
			conditions.add(new Condition(CONDITION_DATE_IS_MONDAY, "is a Monday", Condition.DEFAULT_DATE_IMAGE, null, new String[]{}));
			conditions.add(new Condition(CONDITION_DATE_IS_TUESDAY, "is a Tuesday", Condition.DEFAULT_DATE_IMAGE, null, new String[]{}));
			conditions.add(new Condition(CONDITION_DATE_IS_WEDNESDAY, "is a Wednesday", Condition.DEFAULT_DATE_IMAGE, null, new String[]{}));
			conditions.add(new Condition(CONDITION_DATE_IS_THURSDAY, "is a Thurday", Condition.DEFAULT_DATE_IMAGE, null, new String[]{}));
			conditions.add(new Condition(CONDITION_DATE_IS_FRIDAY, "is a Friday", Condition.DEFAULT_DATE_IMAGE, null, new String[]{}));
			conditions.add(new Condition(CONDITION_DATE_IS_SATURDAY, "is a Saturday", Condition.DEFAULT_DATE_IMAGE, null, new String[]{}));
			conditions.add(new Condition(CONDITION_DATE_IS_SUNDAY, "is a Sunday", Condition.DEFAULT_DATE_IMAGE, null, new String[]{}));
			conditions.add(new Condition(CONDITION_DATE_IS_WEEKDAY, "is a weekday", Condition.DEFAULT_DATE_IMAGE, null, new String[]{}));
			conditions.add(new Condition(CONDITION_DATE_IS_WEEKEND, "is a weekend ", Condition.DEFAULT_DATE_IMAGE, null, new String[]{}));
		}
		else if (systemDeviceTypeID.equals(SYSTEM_DEVICE_TIME))
		{
			Parameter hourIs = new Parameter("", new HomerNumber(0, 23, 1, 0, 17, Unit.HOURS), ":", Comparitor.EQUAL);
			Parameter minuteIs = new Parameter(new HomerNumber(0, 59, 1, 0, 0, Unit.MINUTES), Comparitor.EQUAL);
			ParameterData timeIs = new ParameterData(hourIs, minuteIs);

			Parameter hourIsLT = new Parameter("", new HomerNumber(0, 23, 1, 0, 17, Unit.HOURS), ":", Comparitor.LESS_THAN);
			Parameter minuteIsLT = new Parameter(new HomerNumber(0, 59, 1, 0, 0, Unit.MINUTES), Comparitor.LESS_THAN);
			ParameterData timeIsLT = new ParameterData(hourIsLT, minuteIsLT);
			
			Parameter hourIsGT = new Parameter("", new HomerNumber(0, 23, 1, 0, 17, Unit.HOURS), ":", Comparitor.GREATER_THAN);
			Parameter minuteIsGT = new Parameter(new HomerNumber(0, 59, 1, 0, 0, Unit.MINUTES), Comparitor.GREATER_THAN);
			ParameterData timeIsGT = new ParameterData(hourIsGT, minuteIsGT);
			
			ParameterData timeIsBetween = new ParameterData(hourIsGT, minuteIsGT, hourIsLT, minuteIsLT);
			
			conditions.add(new Condition(CONDITION_TIME_IS, "is", Condition.DEFAULT_TIME_IMAGE, timeIs, new String[]{}));
			conditions.add(new Condition(CONDITION_TIME_IS_AFTER, "is later than", Condition.DEFAULT_TIME_IMAGE, timeIsGT, new String[]{}));
			conditions.add(new Condition(CONDITION_TIME_IS_BEFORE, "is earlier than", Condition.DEFAULT_TIME_IMAGE, timeIsLT, new String[]{}));
			conditions.add(new Condition(CONDITION_TIME_IS_BETWEEN, "is between", Condition.DEFAULT_TIME_IMAGE, timeIsBetween, new String[]{}));
		}
		return conditions;
	}

	@Override
	public boolean checkCondition(String sysDeviceTypeID, String sysDeviceID,
			String conditionID, String[] parameters)
	{
		Calendar calendar = GregorianCalendar.getInstance();
		
		if (sysDeviceTypeID.equals(SYSTEM_DEVICE_TIME))
		{
			Calendar timeEntered = Calendar.getInstance();
			timeEntered.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parameters[0]));
			timeEntered.set(Calendar.MINUTE, Integer.parseInt(parameters[1]));
			
			if (conditionID.equals(CONDITION_TIME_IS))
			{
				return (calendar.get(Calendar.HOUR_OF_DAY) == timeEntered.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.MINUTE) == timeEntered.get(Calendar.MINUTE));
			}
			if (conditionID.equals(CONDITION_TIME_IS_AFTER))
			{
				return calendar.before(timeEntered);
			}
			if (conditionID.equals(CONDITION_TIME_IS_BEFORE))
			{
				return calendar.after(timeEntered);
			}
			if (conditionID.equals(CONDITION_TIME_IS_BETWEEN))
			{
				Calendar lastTimeEntered = Calendar.getInstance();
				lastTimeEntered.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parameters[2]));
				lastTimeEntered.set(Calendar.MINUTE, Integer.parseInt(parameters[3]));
				return calendar.after(timeEntered) && calendar.before(lastTimeEntered);
			}
		}
		else if (sysDeviceTypeID.equals(SYSTEM_DEVICE_DATE))
		{
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			
			if (conditionID.equals(CONDITION_DATE_IS_WEEKDAY))
			{
				return (conditionID.equals(CONDITION_DATE_IS_WEEKDAY) && dayOfWeek >=0 && conditionID.equals(CONDITION_DATE_IS_WEEKDAY) && dayOfWeek <=4);
			}
			if (conditionID.equals(CONDITION_DATE_IS_WEEKEND))
			{
				return (conditionID.equals(CONDITION_DATE_IS_WEEKEND) && dayOfWeek >=5 && conditionID.equals(CONDITION_DATE_IS_WEEKEND) && dayOfWeek <=6);
			}		
			return translateDay(conditionID) == dayOfWeek;
		}
		return false;
	}

	public static int translateDay(String timeConditionID)
	{
		if (timeConditionID.equals(CONDITION_DATE_IS_MONDAY)) return 0;
		if (timeConditionID.equals(CONDITION_DATE_IS_TUESDAY)) return 1;
		if (timeConditionID.equals(CONDITION_DATE_IS_WEDNESDAY)) return 2;
		if (timeConditionID.equals(CONDITION_DATE_IS_THURSDAY)) return 3;
		if (timeConditionID.equals(CONDITION_DATE_IS_FRIDAY)) return 4;
		if (timeConditionID.equals(CONDITION_DATE_IS_SATURDAY)) return 5;
		if (timeConditionID.equals(CONDITION_DATE_IS_SUNDAY)) return 6;
		return -1;
	}

	public static String describeDay(int day)
	{
		switch (day)
		{
		case 0: return "Monday";
		case 1: return "Tuesday";
		case 2: return "Wednesday";
		case 3: return "Thursday";
		case 4: return "Friday";
		case 5: return "Saturday";
		case 6: return "Sunday";
		default: return "Can't translate day as it must be within range 0 - 6.";
		}
	}
	
	/**
	 * 
	 * @param The number of minutes since the start of the day.
	 * @return
	 */
	public static String describeTime(int time)
	{
		int hours = time / 60;
		int minutes = time % 60;
		
		return pad(hours) + ":" + pad(minutes);
	}

	private static String pad(int number)
	{
		String num = Integer.toString(number);
		return num.length() == 1? "0"+num : num;
	}
	
	/**
	 * Translate time into number of minutes since the start of the day
	 * @param hours (0-23)
	 * @param minutes (0-59)
	 * @return
	 */
	public static int translateTime(int hours, int minutes)
	{
		return (hours*60)+minutes;
	}

}
