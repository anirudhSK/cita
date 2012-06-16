package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_NetworkGPS;
import com.Android.CodeInTheAir.Types.GPSInfo;
import com.Android.CodeInTheAir.Types.LocationInfo;

public class Callback_Sample_NetworkGPS extends Callback_Sample_Generic 
{
	NetworkLocationListener locationListener;
	
	boolean networkLocationEnabled;
	
	/* Possible parameters */
	float minDistance;
	int minTime;
	
	public Callback_Sample_NetworkGPS(Callback_Listener_Interface callbackListener)
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
		locationListener = new NetworkLocationListener();
		
		Sensor_NetworkGPS.addLocationListener(locationListener, minTime, minDistance);
		
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		Sensor_NetworkGPS.removeLocationListener(locationListener);
		
		super.stop(stopCode);
	}
	
	void sampleTask()
	{
		sample("time");
		action();
	}
	
	private class NetworkLocationListener implements LocationListener 
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
        	networkLocationEnabled = false;
        	
        }

        public void onProviderEnabled(String provider) 
        {
        	networkLocationEnabled = true;
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
		
		if (Sensor_NetworkGPS.isEnabled())
		{
			networkLocationEnabled = true;
		}
		else
		{
			networkLocationEnabled = false;
		}
		
		LocationInfo locationInfo = Sensor_NetworkGPS.getLocation();
		GPSInfo gpsInfo = new GPSInfo(networkLocationEnabled, locationInfo, null, null, null);
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
