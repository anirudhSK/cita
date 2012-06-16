package com.Android.CodeInTheAir.Device.Query;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.Android.CodeInTheAir.Global.AppContext;

public class Sensor_Ort 
{
	private static SensorManager sensorManager;
	private static Sensor ort;
	
	private static boolean isAvailable;
	
	public static void init()
    {
		sensorManager = (SensorManager)AppContext.context.getSystemService(Context.SENSOR_SERVICE);
		
		if (sensorManager.getSensorList(Sensor.TYPE_ORIENTATION).size() > 0)
		{
			ort = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);
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
		sensorManager.registerListener(listener, ort, period); 
	}
	
	public static void removeListener(SensorEventListener listener)
	{
		sensorManager.unregisterListener(listener);
	}
}
