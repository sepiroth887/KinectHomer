CREATE TABLE IF NOT EXISTS UserTypeToSystemType (
	compositeType INT REFERENCES UserDeviceType(id) ON DELETE CASCADE,
	deviceType INT REFERENCES SystemDeviceType(id) ON DELETE CASCADE,
	PRIMARY KEY (compositeType, deviceType)
)