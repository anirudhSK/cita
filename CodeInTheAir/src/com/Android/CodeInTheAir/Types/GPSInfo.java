package com.Android.CodeInTheAir.Types;

import org.json.JSONObject;

public class GPSInfo 
{
	public boolean enabled;
	public LocationInfo location;
	public SatellitesInfo satellites;
	public String status;
	public String event;
	
	
	public GPSInfo()
	{
		
	}
	
	public GPSInfo(boolean enabled, LocationInfo location, SatellitesInfo satellites,
			String status, String event)
	{
		this.enabled = enabled;		
		this.location = location;
		this.satellites = satellites;
		this.status = status;
		this.event = event;
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jGPSInfo = null;
		try
		{
			jGPSInfo = new JSONObject();
			jGPSInfo.put("enabled", enabled);
			if (location != null)
			{
				jGPSInfo.put("location", location.encodeJSON());
			}
			if (satellites != null)
			{
				jGPSInfo.put("satellites", satellites.encodeJSON());
			}
			if (status != null)
			{
				jGPSInfo.put("status", status);
			}
			if (event != null)
			{
				jGPSInfo.put("event", event);
			}			
		}
		catch (Exception e)
		{
			return null;
		}
		return jGPSInfo;
	}
}
