package com.Android.CodeInTheAir.Device.Query;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.Android.CodeInTheAir.Global.AppContext;

public class Sensor_Light 
{
	private static SensorManager sensorManager;
	private static Sensor light;
	
	private static boolean isAvailable;
	
	public static void init()
    {
		sensorManager = (SensorManager)AppContext.context.getSystemService(Context.SENSOR_SERVICE);
		
		if (sensorManager.getSensorList(Sensor.TYPE_LIGHT).size() > 0)
		{
			light = sensorManager.getSensorList(Sensor.TYPE_LIGHT).get(0);
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
		sensorManager.registerListener(listener, light, period); 
	}
	
	public static void removeListener(SensorEventListener listener)
	{
		sensorManager.unregisterListener(listener);
	}
}
