package uk.ac.stir.cs.homer.homerFrameworkAPI.homerObjects;

import uk.ac.stir.cs.homer.homerFrameworkAPI.configData.ConfigData;

public class SystemDeviceType {

	private String id;
	private String name;
	private String jsonConfigData;
	private DeviceTypeResource[] resources;
	
	public SystemDeviceType(String id, String name, ConfigData configData)
	{
		this.id = id;
		this.name = name;
		this.jsonConfigData = (configData!=null)? configData.convertToJSONString() : null;
	}
	public SystemDeviceType(String id, String name, ConfigData configData, DeviceTypeResource... resources)
	{
		this.id = id;
		this.name = name;
		this.jsonConfigData = (configData!=null)? configData.convertToJSONString() : null;
		this.resources = resources;
	}
	
	public String getId() {
		return id;
	}
	
	public String getJsonConfigData() {
		return jsonConfigData;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String newName) {
		this.name = newName;
	}
	public DeviceTypeResource[] getResources()
	{
		return resources == null? new DeviceTypeResource[]{} : resources;
	}
}
