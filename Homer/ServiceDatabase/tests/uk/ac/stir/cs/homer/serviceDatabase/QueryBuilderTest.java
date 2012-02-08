package uk.ac.stir.cs.homer.serviceDatabase;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.BuiltQuery;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryBuilder;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;

public class QueryBuilderTest {

	private QueryBuilder builder;
	
	@Before
	public void setUp() {
		builder = new QueryBuilder();
	}
	
	@Test
	public void getLocationsWithSpecificTV() {
		String mainTvId = "abcdef";
		QueryObject obj = new QueryObject().userDevice(mainTvId);
		
		BuiltQuery query = builder.locationQuery(obj);
		assertEquals("SELECT Location.* FROM Location, UserDevice WHERE UserDevice.location = Location.id AND UserDevice.id = ?",query.queryStr);
		assertEquals(1, query.args.size());
		assertEquals("abcdef", query.args.get(0));
	}
	
	@Test
	public void getLocationsWithTVs() {
		String tvTypeId = "asdgas";
		QueryObject obj = new QueryObject().userDeviceType(tvTypeId);
		BuiltQuery query = builder.locationQuery(obj);
		assertEquals("SELECT Location.* FROM Location, UserDevice, UserDeviceType WHERE UserDevice.location = Location.id AND UserDeviceType.id = UserDevice.type AND UserDeviceType.id = ?",query.queryStr);
		assertEquals(1, query.args.size());
		assertEquals("asdgas", query.args.get(0));
	}
	
	@Test
	public void getLocationsWithSystemDevice123() {
		String systemDevice = "123";
		QueryObject obj = new QueryObject().systemDevice(systemDevice);
		BuiltQuery query = builder.locationQuery(obj);
		assertEquals("SELECT Location.* FROM Location, UserDevice, SystemDevice WHERE UserDevice.location = Location.id AND SystemDevice.parent = UserDevice.id AND SystemDevice.id = ?",query.queryStr);
		assertEquals(1, query.args.size());
		assertEquals("123", query.args.get(0));
	}
	
	@Test
	public void getLocationsWithSystemDeviceTypeSonyTV() {
		String systemDeviceType = "SonyTV";
		QueryObject obj = new QueryObject().systemDeviceType(systemDeviceType);
		BuiltQuery query = builder.locationQuery(obj);
		assertEquals("SELECT Location.* FROM Location, UserDevice, SystemDevice, SystemDeviceType WHERE UserDevice.location = Location.id AND SystemDevice.parent = UserDevice.id AND SystemDeviceType.id = SystemDevice.type AND SystemDeviceType.id = ?",query.queryStr);
		assertEquals(1, query.args.size());
		assertEquals("SonyTV", query.args.get(0));
	}
	
	@Test
	public void getLocationsWithLocation() {
		String locationID = "Office";
		QueryObject obj = new QueryObject().location(locationID);
		BuiltQuery query = builder.locationQuery(obj);
		assertEquals("SELECT Location.* FROM Location WHERE Location.id = ?",query.queryStr);
		assertEquals(1, query.args.size());
		assertEquals("Office", query.args.get(0));
	}
	
	@Test
	public void getLocationsWithLocationContext() {
		String locationContextID = "Work";
		QueryObject obj = new QueryObject().locationContext(locationContextID);
		BuiltQuery query = builder.locationQuery(obj);
		assertEquals("SELECT Location.* FROM Location, LocationContext WHERE Location.context = LocationContext.id AND LocationContext.id = ?",query.queryStr);
		assertEquals(1, query.args.size());
		assertEquals("Work", query.args.get(0));
	}
	
	@Test
	public void getLocationsWithLocationContextAndUserDeviceType() {
		String locationContextID = "Work";
		String userDeviceType = "TV";
		QueryObject obj = new QueryObject().locationContext(locationContextID).userDeviceType(userDeviceType);
		BuiltQuery query = builder.locationQuery(obj);
		assertEquals("SELECT Location.* FROM Location, UserDevice, UserDeviceType, LocationContext WHERE UserDevice.location = Location.id AND UserDeviceType.id = UserDevice.type AND Location.context = LocationContext.id AND UserDeviceType.id = ? AND LocationContext.id = ?",query.queryStr);
		assertEquals(2, query.args.size());
		assertEquals("TV", query.args.get(0));
		assertEquals("Work", query.args.get(1));
	}
	
	@Test
	public void getLocationsWithActions() {
		QueryObject obj = new QueryObject().mustHaveActions(true);
		BuiltQuery query = builder.locationQuery(obj);
		assertEquals("SELECT Location.* FROM Location, UserDevice, SystemDevice, SystemDeviceType, Action WHERE UserDevice.location = Location.id AND SystemDevice.parent = UserDevice.id AND SystemDeviceType.id = SystemDevice.type AND Action.forType = SystemDeviceType.id AND Action.id IS NOT NULL",query.queryStr);
		assertEquals(0, query.args.size());
	}
	
	@Test
	public void getLocationsWithActionsAndTriggers() {
		QueryObject obj = new QueryObject().mustHaveActions(true).mustHaveTriggers(true);
		BuiltQuery query = builder.locationQuery(obj);
		assertEquals("SELECT Location.* FROM Location, UserDevice, SystemDevice, SystemDeviceType, Trigger, Action WHERE UserDevice.location = Location.id AND SystemDevice.parent = UserDevice.id AND SystemDeviceType.id = SystemDevice.type AND Trigger.forType = SystemDeviceType.id AND Action.forType = SystemDeviceType.id AND Trigger.id IS NOT NULL AND Action.id IS NOT NULL",query.queryStr);
		assertEquals(0, query.args.size());
	}
	
	@Test
	public void getUserDeviceTypesInLocation() {
		QueryObject obj = new QueryObject().location("home");
		
		BuiltQuery query = builder.userDeviceTypeQuery(obj);
		assertEquals("SELECT UserDeviceType.* FROM Location, UserDevice, UserDeviceType WHERE UserDevice.location = Location.id AND UserDeviceType.id = UserDevice.type AND Location.id = ?",query.queryStr);
		assertEquals(1, query.args.size());
	}
	
	@Test
	public void getUserDeviceTypesWithActions() {
		QueryObject obj = new QueryObject().mustHaveActions(true);
		
		BuiltQuery query = builder.userDeviceTypeQuery(obj);
		assertEquals("SELECT UserDeviceType.* FROM UserDeviceType, UserDevice, SystemDevice, SystemDeviceType, Action WHERE UserDeviceType.id = UserDevice.type AND SystemDevice.parent = UserDevice.id AND SystemDeviceType.id = SystemDevice.type AND Action.forType = SystemDeviceType.id AND Action.id IS NOT NULL",query.queryStr);
		assertEquals(0, query.args.size());
	}
	
	@Test
	public void getUserDeviceTypesWithLocationContext() {
		QueryObject obj = new QueryObject().locationContext("locationContext");
		
		BuiltQuery query = builder.userDeviceTypeQuery(obj);
		assertEquals("SELECT UserDeviceType.* FROM LocationContext, Location, UserDevice, UserDeviceType WHERE Location.context = LocationContext.id AND UserDevice.location = Location.id AND UserDeviceType.id = UserDevice.type AND LocationContext.id = ?",query.queryStr);
		assertEquals(1, query.args.size());
	}
	
	@Test
	public void getUserDeviceTypesWithLocationContextAndAreTriggers() {
		QueryObject obj = new QueryObject().locationContext("locationContext").mustHaveTriggers(true);
		
		BuiltQuery query = builder.userDeviceTypeQuery(obj);
		assertEquals("SELECT UserDeviceType.* FROM LocationContext, Location, UserDevice, UserDeviceType, SystemDevice, SystemDeviceType, Trigger WHERE Location.context = LocationContext.id AND UserDevice.location = Location.id AND UserDeviceType.id = UserDevice.type AND SystemDevice.parent = UserDevice.id AND SystemDeviceType.id = SystemDevice.type AND Trigger.forType = SystemDeviceType.id AND LocationContext.id = ? AND Trigger.id IS NOT NULL",query.queryStr);
		assertEquals(1, query.args.size());
	}
	
	@Test
	public void getAllLocations() {
		QueryObject obj = new QueryObject();
		BuiltQuery query = builder.locationQuery(obj);
		assertEquals("SELECT Location.* FROM Location",query.queryStr);
		assertEquals(0, query.args.size());
	}
	@Test
	public void getAllLocationContexts() {
		QueryObject obj = new QueryObject();
		
		BuiltQuery query = builder.locationContextQuery(obj);
		assertEquals("SELECT LocationContext.* FROM LocationContext",query.queryStr);
		assertEquals(0, query.args.size());
	}
	@Test
	public void getAllUserDevices() {
		QueryObject obj = new QueryObject();
		
		BuiltQuery query = builder.userDeviceQuery(obj);
		assertEquals("SELECT UserDevice.* FROM UserDevice",query.queryStr);
		assertEquals(0, query.args.size());
	}
	@Test
	public void getAllUserDeviceTypes() {
		QueryObject obj = new QueryObject();
		
		BuiltQuery query = builder.userDeviceTypeQuery(obj);
		assertEquals("SELECT UserDeviceType.* FROM UserDeviceType",query.queryStr);
		assertEquals(0, query.args.size());
	}
}
