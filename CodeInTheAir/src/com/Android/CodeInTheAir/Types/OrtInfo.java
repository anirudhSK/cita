package com.Android.CodeInTheAir.Types;

import org.json.JSONObject;

public class OrtInfo 
{
	public long time;
	
	public float x;
	public float y;
	public float z;
	
	public float xRaw;
	public float yRaw;
	public float zRaw;
	public int accuracy;
	
	public OrtInfo()
	{
		
	}
	
	public OrtInfo(long time, float x, float y, float z,
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
		JSONObject jOrt = null;
		try
		{
			jOrt = new JSONObject();
			jOrt.put("time", time);
			jOrt.put("x", x);
			jOrt.put("y", y);
			jOrt.put("z", z);
			jOrt.put("xRaw", xRaw);
			jOrt.put("yRaw", yRaw);
			jOrt.put("zRaw", zRaw);	
			jOrt.put("accuracy", accuracy);
		}
		catch (Exception e)
		{
			return null;
		}
		return jOrt;
	}
}
