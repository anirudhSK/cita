package com.Android.CodeInTheAir.Device.Query;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.Android.CodeInTheAir.Global.AppContext;

public class Sensor_Accl 
{
	private static SensorManager sensorManager;
	private static Sensor accelerometer;
	
	private static boolean isAvailable;
	
	public static void init()
    {
		sensorManager = (SensorManager)AppContext.context.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0)
		{
			accelerometer = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
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
		sensorManager.registerListener(listener, accelerometer, period); 
	}
	
	public static void removeListener(SensorEventListener listener)
	{
		sensorManager.unregisterListener(listener);
	}
}
