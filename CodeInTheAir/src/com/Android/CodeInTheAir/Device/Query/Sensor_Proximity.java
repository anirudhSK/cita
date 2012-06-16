package com.Android.CodeInTheAir.Device.Query;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.Android.CodeInTheAir.Global.AppContext;

public class Sensor_Proximity 
{
	private static SensorManager sensorManager;
	private static Sensor proximity;
	
	private static boolean isAvailable;
	
	public static void init()
    {
		sensorManager = (SensorManager)AppContext.context.getSystemService(Context.SENSOR_SERVICE);
		
		if (sensorManager.getSensorList(Sensor.TYPE_PROXIMITY).size() > 0)
		{
			proximity = sensorManager.getSensorList(Sensor.TYPE_PROXIMITY).get(0);
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
		sensorManager.registerListener(listener, proximity, period); 
	}
	
	public static void removeListener(SensorEventListener listener)
	{
		sensorManager.unregisterListener(listener);
	}
}
