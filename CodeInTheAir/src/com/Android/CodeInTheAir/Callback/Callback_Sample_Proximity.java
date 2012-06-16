package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_Proximity;
import com.Android.CodeInTheAir.Types.ProximityInfo;

public class Callback_Sample_Proximity extends Callback_Sample_Generic
{
	Handler sampleHandler;
	ProximityListener listener;
	
	ProximityInfo lastProximityInfo;
	boolean proximityAvailable;
	
	/* Possible parameters */
	int period;

	public Callback_Sample_Proximity(Callback_Listener_Interface callbackListener)
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
		listener = new ProximityListener();
		lastProximityInfo = new ProximityInfo();
		
		if (Sensor_Proximity.isAvailable())
		{
			proximityAvailable = true;
		}
		else
		{
			proximityAvailable = false;
		}
		
		Sensor_Proximity.addListener(listener, period);
		
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		Sensor_Proximity.removeListener(listener);		
		super.stop(stopCode);
	}
	
	void sampleTask()
	{
		sample("time");
		action();
	}
	
	private class ProximityListener implements SensorEventListener 
    {
		int accuracy = 0;
		public void onSensorChanged(SensorEvent event)
        {   
			float value = event.values[SensorManager.DATA_X];
			ProximityInfo proximityInfo = new ProximityInfo(
					System.currentTimeMillis(), value, accuracy);
			lastProximityInfo = proximityInfo;
			
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
		
		jResult = encodeJSON(time, currentSampleCount, sampleType, lastProximityInfo, proximityAvailable);

		try
		{
			jResultArray.put(jResult);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private JSONObject encodeJSON(long time, int sample, String sampleType, ProximityInfo proximityInfo, boolean available)
	{
		JSONObject jResult = new JSONObject();
		
		try
		{
			if (available)
			{
				jResult.put("proximity", proximityInfo.encodeJSON());
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
