package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_Light;
import com.Android.CodeInTheAir.Types.LightInfo;

public class Callback_Sample_Light extends Callback_Sample_Generic 
{
	LightListener listener;
	
	LightInfo lastLightInfo;
	boolean lightAvailable;
	
	/* Possible parameters */
	int period;
	
	public Callback_Sample_Light(Callback_Listener_Interface callbackListener)
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
		listener = new LightListener();
		lastLightInfo = new LightInfo();
		
		
		if (Sensor_Light.isAvailable())
		{
			lightAvailable = true;
		}
		else
		{
			lightAvailable = false;
		}
		
		Sensor_Light.addListener(listener, period);
		
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		Sensor_Light.removeListener(listener);		
		super.stop(stopCode);
	}
	
	
	void sampleTask()
	{
		sample("time");
		action();
	}
	
	private class LightListener implements SensorEventListener 
    {
		int accuracy = 0;
		public void onSensorChanged(SensorEvent event)
        {   
			float value = event.values[SensorManager.DATA_X];
			LightInfo lightInfo = new LightInfo(
					System.currentTimeMillis(), value, accuracy);
			lastLightInfo = lightInfo;
			
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
		
		jResult = encodeJSON(time, currentSampleCount, sampleType, lastLightInfo, lightAvailable);

		try
		{
			jResultArray.put(jResult);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private JSONObject encodeJSON(long time, int sample, String sampleType, LightInfo lightInfo, boolean available)
	{
		JSONObject jResult = new JSONObject();
		
		try
		{
			if (available)
			{
				jResult.put("light", lightInfo.encodeJSON());
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
