package com.Android.CodeInTheAir.Types;

import org.json.JSONObject;

import android.location.Location;

public class LocationInfo 
{
	public long fixTime;
	public double lat;
	public double lng;
	public String provider;
	
	public boolean hasAlt;
	public double alt;
	
	public boolean hasAccuracy;
	public double accuracy;
	
	public boolean hasSpeed;
	public double speed;
	
	public boolean hasBearing;
	public double bearing;
	
	public boolean hasSatellites;
	public int satellites;
	
	public LocationInfo()
	{
		
	}
	
	public LocationInfo(long fixTime, double lat, double lng, String provider,
			 boolean hasAccuracy, double accuracy,
			boolean hasAlt, double alt, boolean hasSpeed, double speed,
			boolean hasBearing, double bearing,
			boolean hasSatellites, int satellites)
	{
		this.fixTime = fixTime;
		this.lat = lat;
		this.lng = lng;
		this.provider = provider;
		this.hasAlt = hasAlt;
		this.alt = alt;
		this.hasAccuracy = hasAccuracy;
		this.accuracy = accuracy;
		this.hasSpeed = hasSpeed;
		this.speed = speed;
		this.hasBearing = hasBearing;
		this.bearing = bearing;
		this.hasSatellites = hasSatellites;
		this.satellites = satellites;	
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jLoc = null;
		try
		{
			jLoc = new JSONObject();
			jLoc.put("time", fixTime);
			jLoc.put("lat", lat);
			jLoc.put("lng", lng);
			jLoc.put("provider", provider);
			if (hasAlt)
			{
				jLoc.put("alt", alt);
			}
			if (hasAccuracy)
			{
				jLoc.put("accuracy", accuracy);
			}
			if (hasSpeed)
			{
				jLoc.put("speed", speed);
			}
			if (hasBearing)
			{
				jLoc.put("bearing", bearing);
			}
			if (hasSatellites)
			{
				jLoc.put("satellites", satellites);
			}
		}
		catch (Exception e)
		{
			return null;
		}
		return jLoc;
	}
	
	public static LocationInfo toLocationInfo(Location l)
    {
    	LocationInfo locationInfo = new LocationInfo();    	
    	
    	if (l != null)
    	{
    		boolean hasSt = false;
    		int st = 0;
    		if (l.getExtras().size() != 0)
    		{
    			hasSt = true;
    			st = l.getExtras().getInt("satellites");
    		}
    		
    		locationInfo = new LocationInfo(l.getTime(), l.getLatitude(), 
    				l.getLongitude(), l.getProvider(),
    				l.hasAccuracy(), l.getAccuracy(),
    				l.hasAltitude(), l.getAltitude(),
    				l.hasSpeed(), l.getSpeed(),     				    				
    				l.hasBearing(), l.getBearing(), 
    				hasSt, st);
    	}
    	
    	return locationInfo;
    }
}
