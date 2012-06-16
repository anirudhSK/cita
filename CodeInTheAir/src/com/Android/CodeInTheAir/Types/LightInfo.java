package com.Android.CodeInTheAir.Types;

import org.json.JSONObject;

public class LightInfo 
{
	public long time;
	public float value;
	public int accuracy;
	
	public LightInfo()
	{
		
	}
	
	public LightInfo(long time, float value, int accuracy)
	{
		this.time = time;
		this.value = value;
		this.accuracy = accuracy;
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jLight = null;
		try
		{
			jLight = new JSONObject();
			jLight.put("time", time);
			jLight.put("value", value);	
			jLight.put("accuracy", accuracy);
		}
		catch (Exception e)
		{
			return null;
		}
		return jLight;
	}
}
