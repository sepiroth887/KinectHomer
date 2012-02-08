SELECT SystemDeviceType.id, SystemDevice.id, UserDeviceType.id, Location.id, LocationContext.id 
FROM SystemDevice, SystemDeviceType, UserDevice, UserDeviceType, Location, LocationContext 
WHERE ((UserDevice.id = ? ) 
AND (SystemDevice.parent = UserDevice.id) 
AND (SystemDevice.type = SystemDeviceType.id) 
AND (UserDevice.type = UserDeviceType.id) 
AND (UserDevice.location = Location.id) 
AND (Location.context = LocationContext.id));