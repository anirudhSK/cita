package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_GPS;
import com.Android.CodeInTheAir.Types.GPSInfo;
import com.Android.CodeInTheAir.Types.LocationInfo;

public class Callback_Sample_GPS extends Callback_Sample_Generic 
{
	GPSLocationListener locationListener;
	GPSStatusListener statusListener;
	
	boolean gpsEnabled;
	String gpsStatus;
	String gpsEvent;
	
	/* Possible parameters */
	float minDistance;	
	int minTime;
	
	public Callback_Sample_GPS(Callback_Listener_Interface callbackListener)
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
		locationListener = new GPSLocationListener();
		statusListener = new GPSStatusListener();
		
		Sensor_GPS.addLocationListener(locationListener, minTime, minDistance);
		Sensor_GPS.addGpsStatusListener(statusListener);
		
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		Sensor_GPS.removeGpsStatusListener(statusListener);
		Sensor_GPS.removeLocationListener(locationListener);
		
		super.stop(stopCode);
	}
	
	void sampleTask()
	{
		sample("time");
		action();
	}
	
	private class GPSLocationListener implements LocationListener 
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
        	gpsEnabled = false;
        }

        public void onProviderEnabled(String provider) 
        {
        	gpsEnabled = true;
        }

        public void onStatusChanged(String provider, int status, Bundle extras) 
        {
        	switch (status)
        	{
	        	case LocationProvider.AVAILABLE:
	        		gpsStatus = "available";
	        		break;
	        	case LocationProvider.TEMPORARILY_UNAVAILABLE:
	        		gpsStatus = "unavailable";
	        		break;
	        	case LocationProvider.OUT_OF_SERVICE:
	        		gpsStatus = "outOfService";
	        		break;
        	}
        }
    }
    
	
	private class GPSStatusListener implements GpsStatus.Listener 
	{
	    public void onGpsStatusChanged(int event) 
	    {
	        switch (event) 
	        {
	            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
	            	gpsEvent = "gpsevent";
	                break;
	            case GpsStatus.GPS_EVENT_FIRST_FIX:
	            	gpsEvent = "firstFix";
	                break;
	            case GpsStatus.GPS_EVENT_STARTED:
	            	gpsEvent = "started";
	            	break;
	            case GpsStatus.GPS_EVENT_STOPPED:
	            	gpsEvent = "stopped";
	            	break;
	        }
	    }
	}
	
	
	private void sample(String sampleType)
	{
		currentSampleCount++;
		JSONObject jResult = null;
		long time = System.currentTimeMillis();
		
		if (Sensor_GPS.isEnabled())
		{
			gpsEnabled = true;
		}
		else
		{
			gpsEnabled = false;
		}
		
		LocationInfo locationInfo = Sensor_GPS.getLocation();
		
		GPSInfo gpsInfo = new GPSInfo(gpsEnabled, locationInfo, null, gpsStatus, gpsEvent);		
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
