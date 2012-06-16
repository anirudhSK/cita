package com.Android.CodeInTheAir.Device.Query;

public class Sensors 
{
	public static void init()
	{
		Sensor_Wifi.init();
		Sensor_GSM.init();
		Sensor_GPS.init();
		Sensor_NetworkGPS.init();
		Sensor_PassiveGPS.init();
		Sensor_Location.init();
		Sensor_Accl.init();
		Sensor_Compass.init();
		Sensor_Ort.init();
		Sensor_Light.init();
		Sensor_Proximity.init();
		Sensor_Bluetooth.init();
	}
}
