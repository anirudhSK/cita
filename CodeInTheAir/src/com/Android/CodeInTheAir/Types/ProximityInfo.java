package com.Android.CodeInTheAir.Types;

import org.json.JSONObject;

public class ProximityInfo 
{
	public long time;
	public float value;
	public int accuracy;
	
	public ProximityInfo()
	{
		
	}
	
	public ProximityInfo(long time, float value, int accuracy)
	{
		this.time = time;
		this.value = value;
		this.accuracy = accuracy;
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jProximity = null;
		try
		{
			jProximity = new JSONObject();
			jProximity.put("time", time);
			jProximity.put("value", value);	
			jProximity.put("accuracy", accuracy);
		}
		catch (Exception e)
		{
			return null;
		}
		return jProximity;
	}
}
