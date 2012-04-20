INSERT INTO SystemDeviceType (id, name) VALUES ('A59AB0C5B3CCFDD6716C94DD810450BFABF6FAF3', 'Simulated Boiler');
INSERT INTO SystemDeviceType (id, name) VALUES ('109A82B49033637C11890612C63611F8E1A7FFD9', 'Email');
INSERT INTO SystemDeviceType (id, name) VALUES ('B21DAC8319C9D02AB7E6A6EDA4FF2D02A1B1F520', 'SMS');
INSERT INTO SystemDeviceType (id, name, jsonconfigdata) VALUES ('AD62F2B8FD15214930310B23A80BBFAC77E573B2', 'Google Weather', '{"parameter1":{"dataType":{"min":2,"max":50,"defaultValue":"Stirling","type":2},"preText":"Location (city, town or postcode):"}}');
INSERT INTO SystemDeviceType (id, name, jsonconfigdata) VALUES ('3F7F4FE2FEBF6EBD3FBF2838890504D17D5CDB6F', 'X10 Lamp Module', '{"parameter1":{"dataType":{"min":2,"unit":"NONE","max":3,"defaultValue":"A1","type":2},"preText":"X10 Code:"}}');
INSERT INTO SystemDeviceType (id, name, jsonconfigdata) VALUES ('F7F0E891C43B36F12ADC140B407A6116EA5FDB85', 'X10 Appliance Module', '{"parameter1":{"dataType":{"min":2,"unit":"NONE","max":3,"defaultValue":"A1","type":2},"preText":"X10 Code:"}}');
INSERT INTO SystemDeviceType (id, name, jsonconfigdata) VALUES ('6DCBBA45A062442DC0C006C36A5C95EA5E27DB8F', 'Visonic Door Sensor', '{"parameter2":{"dataType":{"min":2,"unit":"NONE","max":2,"defaultValue":"04","type":2},"preText":"Opening Code:"},"parameter1":{"dataType":{"min":6,"unit":"NONE","max":6,"defaultValue":"","type":2},"preText":"Sensor Code:"},"parameter3":{"dataType":{"min":2,"unit":"NONE","max":2,"defaultValue":"84","type":2},"preText":"Closing Code:"}}');
INSERT INTO SystemDeviceType (id, name, jsonconfigdata) VALUES ('A591B8E851DD6270C54EC5544F30CBE72E7D6963', 'Visonic Window Sensor', '{"parameter2":{"dataType":{"min":2,"unit":"NONE","max":2,"defaultValue":"04","type":2},"preText":"Opening Code:"},"parameter1":{"dataType":{"min":6,"unit":"NONE","max":6,"defaultValue":"","type":2},"preText":"Sensor Code:"},"parameter3":{"dataType":{"min":2,"unit":"NONE","max":2,"defaultValue":"84","type":2},"preText":"Closing Code:"}}');
INSERT INTO SystemDeviceType (id, name, jsonconfigdata) VALUES ('1F76555FD88BE0F28A458C7B19418B9393AEC227', 'Oregon Temperature and Humidity Sensor', '{"parameter1":{"dataType":{"min":6,"unit":"NONE","max":6,"defaultValue":"","type":2},"preText":"Sensor Code:"}}');
INSERT INTO SystemDeviceType (id, name, jsonconfigdata) VALUES ('1B6D604CFEC8DF655860321319EC5F2B82A57FC8', 'Visonic Pendant Alarm', '{"parameter2":{"dataType":{"min":2,"unit":"NONE","max":2,"defaultValue":"0C","type":2},"preText":"Activated Code:"},"parameter1":{"dataType":{"min":6,"unit":"NONE","max":6,"defaultValue":"","type":2},"preText":"Sensor Code:"}}');
INSERT INTO SystemDeviceType (id, name, jsonconfigdata) VALUES ('33629B2A802282DFFE995A760522EC5B5C6E6A3B', 'Visonic PIR', '{"parameter2":{"dataType":{"min":2,"unit":"NONE","max":2,"defaultValue":"0C","type":2},"preText":"Activated Code:"},"parameter1":{"dataType":{"min":6,"unit":"NONE","max":6,"defaultValue":"","type":2},"preText":"Sensor Code:"}}');

INSERT INTO Condition (id, description, forType, image, actionIDresultingInThisState) VALUES ('EBC1F37A7AD84B92B08DCFD80FAE7E8F42D4DECD', 'is off', 'A59AB0C5B3CCFDD6716C94DD810450BFABF6FAF3', '82', null);  //boiler is off
INSERT INTO Condition (id, description, forType, image, actionIDresultingInThisState) VALUES ('B668C00D803FA6164F7110A503428B478E416F58', 'is off', '3F7F4FE2FEBF6EBD3FBF2838890504D17D5CDB6F', '82', null);  //lamp is off
INSERT INTO Condition (id, description, forType, image, actionIDresultingInThisState) VALUES ('7C3454A35C95E28BA79A811859BADA0966706DB2', 'is off', 'F7F0E891C43B36F12ADC140B407A6116EA5FDB85', '82', null);  //x10 is off
INSERT INTO Condition (id, description, forType, image, actionIDresultingInThisState) VALUES ('93783A47BB37001D11AAF82075A78D3654AFD114', 'is closed', 'A591B8E851DD6270C54EC5544F30CBE72E7D6963', '70', null);  //window is closed
INSERT INTO Condition (id, description, forType, image, actionIDresultingInThisState) VALUES ('D09902C4E5A5AF2FF989914248C8A5395EA4227A', 'is closed', '6DCBBA45A062442DC0C006C36A5C95EA5E27DB8F', '43', null);  //door is closed

INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Boiler',				3 ,12 ,61, 		'EBC1F37A7AD84B92B08DCFD80FAE7E8F42D4DECD');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Front Door',			6 ,7 ,45,		'D09902C4E5A5AF2FF989914248C8A5395EA4227A');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Back Door',				3 ,7 ,44, 		'D09902C4E5A5AF2FF989914248C8A5395EA4227A');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Temp and Humidity',		2 ,9, 166, 		null);
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Stirling Weather',		5 ,4 ,168, 		null);
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Bedside Lamp',			2 ,1 ,9, 		'B668C00D803FA6164F7110A503428B478E416F58');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Hall Light',			6 ,1 ,8, 		'B668C00D803FA6164F7110A503428B478E416F58');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Fan',					4 ,5 ,15, 		'7C3454A35C95E28BA79A811859BADA0966706DB2');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Window',				2 ,8 ,71, 		'93783A47BB37001D11AAF82075A78D3654AFD114');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Motion Sensor',			6 ,11 ,91, 		null);
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Claires Email',			7 ,13,81, 		null);
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Claires Mobile',		7 ,14 ,16, 		null);
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('House Mobile',			7 ,14 ,18, 		null);
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('TV',					1 ,2 ,29, 		'7C3454A35C95E28BA79A811859BADA0966706DB2');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Media PC',				1 ,6 ,13, 		'7C3454A35C95E28BA79A811859BADA0966706DB2');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Grans Pendant Alarm',	1 ,10 ,21,		null);
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('House Email',			7 ,13 ,59, 		null);
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Motion Sensor',			4 ,11 ,91, 		null);
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Temp and Humidity',		1 ,9 ,166, 		null);
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Window',				4 ,8 ,71, 		'93783A47BB37001D11AAF82075A78D3654AFD114');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Coffee Machine',		8 ,3 ,2, 		'7C3454A35C95E28BA79A811859BADA0966706DB2');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Left Window',		    4 ,8 ,73, 		'93783A47BB37001D11AAF82075A78D3654AFD114');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Right Window',		 	4 ,8 ,73, 		'93783A47BB37001D11AAF82075A78D3654AFD114');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Door',		 			4 ,7 ,44, 		'D09902C4E5A5AF2FF989914248C8A5395EA4227A');
INSERT INTO UserDevice (name, location, type, image, defaultstate) VALUES ('Lamp',		 			4 ,1 ,9, 		'B668C00D803FA6164F7110A503428B478E416F58');

INSERT INTO SystemDevice (type, parent) VALUES ('A59AB0C5B3CCFDD6716C94DD810450BFABF6FAF3', 1);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["A1"]', '3F7F4FE2FEBF6EBD3FBF2838890504D17D5CDB6F', 6);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["A2"]', '3F7F4FE2FEBF6EBD3FBF2838890504D17D5CDB6F', 7);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["A4"]', '3F7F4FE2FEBF6EBD3FBF2838890504D17D5CDB6F', 25);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["B2"]', 'F7F0E891C43B36F12ADC140B407A6116EA5FDB85', 14);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["B3"]', 'F7F0E891C43B36F12ADC140B407A6116EA5FDB85', 21);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["Stirling"]', 'AD62F2B8FD15214930310B23A80BBFAC77E573B2', 5);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["B1"]', 'F7F0E891C43B36F12ADC140B407A6116EA5FDB85', 8);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["B4"]', 'F7F0E891C43B36F12ADC140B407A6116EA5FDB85', 15);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["","04","84"]', '6DCBBA45A062442DC0C006C36A5C95EA5E27DB8F', 2);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["","04","84"]', '6DCBBA45A062442DC0C006C36A5C95EA5E27DB8F', 3);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["","04","84"]', 'A591B8E851DD6270C54EC5544F30CBE72E7D6963', 9);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["","04","84"]', 'A591B8E851DD6270C54EC5544F30CBE72E7D6963', 20);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('[""]', '1F76555FD88BE0F28A458C7B19418B9393AEC227', 4);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('[""]', '1F76555FD88BE0F28A458C7B19418B9393AEC227', 19);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["C928DE","0C"]', '1B6D604CFEC8DF655860321319EC5F2B82A57FC8', 16);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["","0C"]', '33629B2A802282DFFE995A760522EC5B5C6E6A3B', 10);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["82DDCB","0C"]', '33629B2A802282DFFE995A760522EC5B5C6E6A3B', 18);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["0891AE","04","84"]', 'A591B8E851DD6270C54EC5544F30CBE72E7D6963', 22);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["5D53AE","04","84"]', 'A591B8E851DD6270C54EC5544F30CBE72E7D6963', 23);
INSERT INTO SystemDevice (jsonconfig, type, parent) VALUES ('["5B11AE","04","84"]', '6DCBBA45A062442DC0C006C36A5C95EA5E27DB8F', 24);

INSERT INTO SystemDevice (type, parent) VALUES ('109A82B49033637C11890612C63611F8E1A7FFD9', 11);
INSERT INTO SystemDevice (type, parent) VALUES ('109A82B49033637C11890612C63611F8E1A7FFD9', 17);
INSERT INTO SystemDevice (type, parent) VALUES ('B21DAC8319C9D02AB7E6A6EDA4FF2D02A1B1F520', 12);
INSERT INTO SystemDevice (type, parent) VALUES ('B21DAC8319C9D02AB7E6A6EDA4FF2D02A1B1F520', 13);


