package com.Android.CodeInTheAir.Device.Query;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.Android.CodeInTheAir.Global.AppContext;
import com.Android.CodeInTheAir.Types.LocationInfo;

public class Sensor_PassiveGPS 
{
	private static LocationManager locationManager;
	
	private static final int QUICK_PERIOD = 1000;
	
	public static void init()
    {
    	locationManager = (LocationManager)AppContext.context.getSystemService(Context.LOCATION_SERVICE);
    }
	
    public static boolean isEnabled()
    {
    	return locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
    }
    
    public static LocationInfo getLocation()
    {
    	Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
    	return LocationInfo.toLocationInfo(location);
    }
    
    public static double getLat()
    {
    	Location location = getLastKnownLocation();
    	if (location != null)
    	{
    		return location.getLatitude();
    	}
    	return 0;
    }
    
    public static double getLng()
    {
    	Location location = getLastKnownLocation();
    	if (location != null)
    	{
    		return location.getLongitude();
    	}
    	return 0;
    }
    
    public static double getAcc()
    {
    	Location location = getLastKnownLocation();
    	if (location != null)
    	{
    		return location.getAccuracy();
    	}
    	return 0;
    }
    
    
    public static long getTime()
    {
    	Location location = getLastKnownLocation();
    	if (location != null)
    	{
    		return location.getTime();
    	}
    	return 0;
    }
    
    public static String getProvider()
    {
    	Location location = getLastKnownLocation();
    	if (location != null)
    	{
    		return location.getProvider();
    	}
    	return null;
    }
    
    /* Private methods */
    
    private static Location getLastKnownLocation()
    {
    	Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
    	return location;
    }
    
    /* Listener and Events */
    /* Location listener */
    public static void addLocationListener(LocationListener locationListener)
    {
		locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, QUICK_PERIOD, 0, locationListener);
    }
    
    public static void addLocationListener(LocationListener locationListener, int period, float distance)
    {
		locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, period, distance, locationListener);
    }
    
    public static void removeLocationListener(LocationListener locationListener)
    {
    	locationManager.removeUpdates(locationListener);
    }
}
