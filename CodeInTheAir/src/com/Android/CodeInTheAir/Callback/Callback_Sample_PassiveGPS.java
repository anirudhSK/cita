package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_PassiveGPS;
import com.Android.CodeInTheAir.Types.GPSInfo;
import com.Android.CodeInTheAir.Types.LocationInfo;

public class Callback_Sample_PassiveGPS extends Callback_Sample_Generic 
{
	PassiveLocationListener locationListener;	
	
	/* Possible parameters */
	float minDistance;
	int minTime;
	
	public Callback_Sample_PassiveGPS(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);
		
		minDistance = 0;
		minTime = 0;
	}
		
	boolean setParam(String param, Object value)
	{
		try
		{
			if (param.equalsIgnoreCase("minTime"))
			{
				minTime = (Integer)value;
			}
			else if (param.equalsIgnoreCase("minDistance"))
			{
				minDistance = (Float)value;
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
		locationListener = new PassiveLocationListener();		
		
		Sensor_PassiveGPS.addLocationListener(locationListener, minTime, minDistance);
		
		super.onlyEventSample();
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		Sensor_PassiveGPS.removeLocationListener(locationListener);
		super.stop(stopCode);
	}
	
	
	private class PassiveLocationListener implements LocationListener 
    {
        public void onLocationChanged(Location loc) 
        {
        	sample("event");
        	action();
        }
           
        public void onProviderDisabled(String provider) 
        {
        		
        }

        public void onProviderEnabled(String provider) 
        {
        	
        }

        public void onStatusChanged(String provider, int status, Bundle extras) 
        {
        	
        }
    }
    
	
	
	private void sample(String sampleType)
	{
		currentSampleCount++;
		JSONObject jResult = null;
		long time = System.currentTimeMillis();
		
		LocationInfo locationInfo = Sensor_PassiveGPS.getLocation();
		GPSInfo gpsInfo = new GPSInfo(true, locationInfo, null, null, null);
		jResult = encodeJSON(time, currentSampleCount, sampleType, gpsInfo);

		try
		{
			jResultArray.put(jResult);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private JSONObject encodeJSON(long time, int sample, String sampleType, GPSInfo gpsInfo)
	{
		JSONObject jResult = new JSONObject();
		
		try
		{
			jResult.put("gps", gpsInfo.encodeJSON());
			
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
