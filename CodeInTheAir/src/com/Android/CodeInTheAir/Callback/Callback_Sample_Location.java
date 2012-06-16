package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_Location;
import com.Android.CodeInTheAir.Types.GPSInfo;
import com.Android.CodeInTheAir.Types.LocationInfo;

public class Callback_Sample_Location extends Callback_Sample_Generic
{
	BestLocationListener locationListener;
	
	boolean locationEnabled;
	
	/* Possible parameters */
	float minDistance;
	int minTime;
	int sigTime;
	int sigAccuracy;
	
	public Callback_Sample_Location(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);		
		minDistance = 0;
		minTime = 0;
		sigTime = -1;
		sigAccuracy = -1;
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
			else if (param.equalsIgnoreCase("sigTime"))
			{
				sigTime = (Integer)value;
			}
			else if (param.equalsIgnoreCase("sigAccuracy"))
			{
				sigAccuracy = (Integer)value;
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
		locationListener = new BestLocationListener();		
		
		Sensor_Location.addLocationListener(locationListener, minTime, minDistance);
		
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		Sensor_Location.removeLocationListener(locationListener);
		
		super.stop(stopCode);
	}
	
	void sampleTask()
	{
		sample("time");
		action();
	}
	
	private class BestLocationListener implements LocationListener 
    {
        public void onLocationChanged(Location loc) 
        {
        	if (bEventSampleSet)
        	{
        		sample("event");
        		action();
        	}
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
		
		if (Sensor_Location.isEnabled())
		{
			locationEnabled = true;
		}
		else
		{
			locationEnabled = false;
		}
		
		LocationInfo locationInfo = Sensor_Location.getLocation(sigTime, sigAccuracy);		
		GPSInfo gpsInfo = new GPSInfo(locationEnabled, locationInfo, null, null, null);
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
