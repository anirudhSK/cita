package com.Android.CodeInTheAir.Types;

import org.json.JSONObject;

public class CompassInfo 
{
	public long time;
	public float x;
	public float y;
	public float z;
	
	public float xRaw;
	public float yRaw;
	public float zRaw;
	public int accuracy;
	
	public CompassInfo()
	{
		
	}
	
	public CompassInfo(long time, float x, float y, float z,
			float xRaw, float yRaw, float zRaw,
			int accuracy)
	{
		this.time = time;
		this.x = x;
		this.y = y;
		this.z = z;
		this.xRaw = xRaw;
		this.yRaw = yRaw;
		this.zRaw = zRaw;
		this.accuracy = accuracy;
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jCompass = null;
		try
		{
			jCompass = new JSONObject();
			jCompass.put("time", time);
			jCompass.put("x", x);
			jCompass.put("y", y);
			jCompass.put("z", z);
			jCompass.put("xRaw", xRaw);
			jCompass.put("yRaw", yRaw);
			jCompass.put("zRaw", zRaw);	
			jCompass.put("accuracy", accuracy);
		}
		catch (Exception e)
		{
			return null;
		}
		return jCompass;
	}
}
