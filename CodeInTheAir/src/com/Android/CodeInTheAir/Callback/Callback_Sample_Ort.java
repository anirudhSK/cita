package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_Ort;
import com.Android.CodeInTheAir.Types.OrtInfo;

public class Callback_Sample_Ort extends Callback_Sample_Generic 
{
	OrtListener listener;
	
	OrtInfo lastOrtInfo;
	boolean ortAvailable;
	
	/* Possible parameters */
	int period;
	
	public Callback_Sample_Ort(Callback_Listener_Interface callbackListener)
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
		listener = new OrtListener();
		lastOrtInfo = new OrtInfo();
		
		
		if (Sensor_Ort.isAvailable())
		{
			ortAvailable = true;
		}
		else
		{
			ortAvailable = false;
		}
		
		Sensor_Ort.addListener(listener, period);
		
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		Sensor_Ort.removeListener(listener);
		
		super.stop(stopCode);
	}
	
	void sampleTask()
	{
		sample("time");
		action();
	}	
	
	private class OrtListener implements SensorEventListener 
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
			
			OrtInfo ortInfo = new OrtInfo(System.currentTimeMillis(), 
					x, y, z,
					xRaw, yRaw, zRaw, accuracy);
			lastOrtInfo = ortInfo;
			
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
		
		jResult = encodeJSON(time, currentSampleCount, sampleType, lastOrtInfo, ortAvailable);

		try
		{
			jResultArray.put(jResult);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private JSONObject encodeJSON(long time, int sample, String sampleType, OrtInfo ortInfo, boolean available)
	{
		JSONObject jResult = new JSONObject();
		
		try
		{
			if (available)
			{
				jResult.put("ort", ortInfo.encodeJSON());
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
