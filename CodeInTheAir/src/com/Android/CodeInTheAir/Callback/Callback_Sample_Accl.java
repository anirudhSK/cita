package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_Accl;
import com.Android.CodeInTheAir.Types.AcclInfo;

public class Callback_Sample_Accl extends Callback_Sample_Generic
{
	AcclListener listener;
	
	AcclInfo lastAcclInfo;
	boolean acclAvailable;
	
	/* Possible parameters */
	int period;
	
	public Callback_Sample_Accl(Callback_Listener_Interface callbackListener)
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
		listener = new AcclListener();
		lastAcclInfo = new AcclInfo();		
		
		if (Sensor_Accl.isAvailable())
		{
			acclAvailable = true;
		}
		else
		{
			acclAvailable = false;
		}
		
		Sensor_Accl.addListener(listener, period);		
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		Sensor_Accl.removeListener(listener);		
		super.stop(stopCode);
	}
	
	void sampleTask()
	{
		sample("time");
		action();
	}
	
	private class AcclListener implements SensorEventListener 
    {
		int accuracy = 0;
		public void onSensorChanged(SensorEvent event)
        {   
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];
			float xRaw = x;
			float yRaw = y;
			float zRaw = z;
			
			AcclInfo acclInfo = new AcclInfo(System.currentTimeMillis(), x, y, z,
					xRaw, yRaw, zRaw, accuracy);
			lastAcclInfo = acclInfo;
			
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
		
		jResult = encodeJSON(time, currentSampleCount, sampleType, lastAcclInfo, acclAvailable);

		try
		{
			jResultArray.put(jResult);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private JSONObject encodeJSON(long time, int sample, String sampleType, AcclInfo acclInfo, boolean available)
	{
		JSONObject jResult = new JSONObject();
		
		try
		{
			if (available)
			{
				jResult.put("accl", acclInfo.encodeJSON());
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
