package com.Android.CodeInTheAir.Device.Query;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.Android.CodeInTheAir.Global.AppContext;
import com.Android.CodeInTheAir.Types.LocationInfo;

public class Sensor_Location 
{
	private static LocationManager locationManager;
    
    private static Location lastKnownLocation_GPS;
    private static Location lastKnownLocation_Network;
    private static Location lastKnownLocation_Best;    
  
    private static final int QUICK_PERIOD = 1000;
	private static final int SIGNIFICANT_TIME = 1000 * 60 * 2;
	private static final int SIGNIFICANT_ACCURACY = 200;
	
    
    public static void init()
    {
    	locationManager = (LocationManager)AppContext.context.getSystemService(Context.LOCATION_SERVICE);
    }
    
    
    public static boolean isEnabled()
    {
    	boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	boolean isNetworkGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    	return (isGPSEnabled || isNetworkGPSEnabled);
    }
    
    public static LocationInfo getLocation()
    {
		return LocationInfo.toLocationInfo(getLastKnownLocation());
    }
    
    public static LocationInfo getLocation(int significantTime, int significantAccuracy)
    {
		return LocationInfo.toLocationInfo(getLastKnownLocation(significantTime, significantAccuracy));
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
    
    public static String getProvider()
    {
    	Location location = getLastKnownLocation();
    	if (location != null)
    	{
    		return location.getProvider();
    	}
    	return null;
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
    
    
    /* Private methods */
    
    
    private static Location getLastKnownLocation()
    {
    	initBestLocation(SIGNIFICANT_TIME, SIGNIFICANT_ACCURACY);
		return lastKnownLocation_Best;
    }
    
    private static Location getLastKnownLocation(int significantTime, int significantAccuracy)
    {
    	if (significantTime == -1) significantTime = SIGNIFICANT_TIME;
    	if (significantAccuracy == -1) significantAccuracy = SIGNIFICANT_ACCURACY;
    	initBestLocation(significantTime, significantAccuracy);
		return lastKnownLocation_Best;
    }
    
    private static void initBestLocation(int significantTime, int significantAccuracy)
    {
    	lastKnownLocation_GPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		lastKnownLocation_Network = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (lastKnownLocation_GPS == null)
		{
			lastKnownLocation_Best = lastKnownLocation_Network;
			return;
		}
		if (lastKnownLocation_Network == null)
		{
			lastKnownLocation_Best = lastKnownLocation_GPS;
			
			return;
		}
		
		boolean isGPSBetter = isBetterLocation(lastKnownLocation_GPS, lastKnownLocation_Network, significantTime, significantAccuracy);
		if (isGPSBetter)
		{
			lastKnownLocation_Best = lastKnownLocation_GPS;
		}
		else
		{
			lastKnownLocation_Best = lastKnownLocation_Network;
		}
    }
    
    public static void addLocationListener(LocationListener locationListener)
    {
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, QUICK_PERIOD, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, QUICK_PERIOD, 0, locationListener);
    }
    
    public static void addLocationListener(LocationListener locationListener, int period, float distance)
    {
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, period, distance, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, period, distance, locationListener);
		
    }
    
    public static void removeLocationListener(LocationListener locationListener)
    {
    	locationManager.removeUpdates(locationListener);
    }
    


	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected static boolean isBetterLocation(Location location, Location currentBestLocation,
			int significantTime, int significantAccuracy) 
	{
	    if (currentBestLocation == null) 
	    {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > significantTime;
	    boolean isSignificantlyOlder = timeDelta < -significantTime;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) 
	    {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } 
	    else if (isSignificantlyOlder) 
	    {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > significantAccuracy;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) 
	    {
	        return true;
	    } 
	    else if (isNewer && !isLessAccurate) 
	    {
	        return true;
	    } 
	    else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) 
	    {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2) 
	{
	    if (provider1 == null) 
	    {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
}
