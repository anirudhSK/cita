package com.Android.CodeInTheAir.Device.Query;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.Android.CodeInTheAir.Global.AppContext;

public class Sensor_Compass 
{
	private static SensorManager sensorManager;
	private static Sensor compass;
	
	private static boolean isAvailable;
	
	public static void init()
    {
		sensorManager = (SensorManager)AppContext.context.getSystemService(Context.SENSOR_SERVICE);
		
		if (sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).size() > 0)
		{
			compass = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
			isAvailable = true;
		}
		else
		{
			isAvailable = false;
		}
    }
	
	public static boolean isAvailable()
	{
		return isAvailable;
	}
	
	
	/* Listeners and Events */
	public static void addListener(SensorEventListener listener, int period)
	{
		sensorManager.registerListener(listener, compass, period); 
	}
	
	public static void removeListener(SensorEventListener listener)
	{
		sensorManager.unregisterListener(listener);
	}
}
