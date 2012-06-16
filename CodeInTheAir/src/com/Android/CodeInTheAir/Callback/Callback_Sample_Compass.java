package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_Compass;
import com.Android.CodeInTheAir.Types.CompassInfo;

public class Callback_Sample_Compass extends Callback_Sample_Generic 
{
	CompassListener listener;
	
	CompassInfo lastCompassInfo;
	boolean compassAvailable;
	
	/* Possible parameters */
	int period;	
	
	public Callback_Sample_Compass(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);
		
		period = SensorManager.SENSOR_DELAY_NORMAL;
	}	
	
	boolean setParam(String param, Object value)
	{
		try
		{
			if (param.equalsIgnoreCase("period"))
			{
				period = (Integer)value;
			}
			else
			{
				if (super.setParam(param, value))
				{
					return true;
				}
				return false;
			}
		}
		catch (Exception e)
		{
			
		}
		
		return true;
	}
	
	public void start()
	{
		listener = new CompassListener();
		lastCompassInfo = new CompassInfo();
		
				if (Sensor_Compass.isAvailable())
		{
			compassAvailable = true;
		}
		else
		{
			compassAvailable = false;
		}
		
		Sensor_Compass.addListener(listener, period);
		
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		Sensor_Compass.removeListener(listener);
		
		super.stop(stopCode);
	}
	
	
	void sampleTask()
	{
		sample("time");
		action();
	}
	
	
	private class CompassListener implements SensorEventListener 
    {
		int accuracy = 0;
		public void onSensorChanged(SensorEvent event)
        {   
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];
			float xRaw = event.values[SensorManager.RAW_DATA_X];
			float yRaw = event.values[SensorManager.RAW_DATA_Y];
			float zRaw = event.values[SensorManager.RAW_DATA_Z];
			
			CompassInfo compassInfo = new CompassInfo(System.currentTimeMillis(), 
					x, y, z,
					xRaw, yRaw, zRaw, accuracy);
			lastCompassInfo = compassInfo;
			
			if (bEventSampleSet)
			{
				sample("event");
				action();
			}		
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) 
        {
        	this.accuracy = accuracy;
        }
    }
    
	
	
	private void sample(String sampleType)
	{
		currentSampleCount++;
		JSONObject jResult = null;
		long time = System.currentTimeMillis();
		
		jResult = encodeJSON(time, currentSampleCount, sampleType, lastCompassInfo, compassAvailable);

		try
		{
			jResultArray.put(jResult);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private JSONObject encodeJSON(long time, int sample, String sampleType, CompassInfo compassInfo, boolean available)
	{
		JSONObject jResult = new JSONObject();
		
		try
		{
			if (available)
			{
				jResult.put("compass", compassInfo.encodeJSON());
			}			
			jResult.put("sampleTime", time);
			jResult.put("sampleCount", sample);
			jResult.put("sampleType", sampleType);
		}
		catch (Exception e)
		{
			
		}
		
		return jResult;
	}
}
