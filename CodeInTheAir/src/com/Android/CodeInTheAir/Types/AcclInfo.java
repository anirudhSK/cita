package com.Android.CodeInTheAir.Types;

import org.json.JSONObject;

public class AcclInfo 
{
	public long time;
	public float x;
	public float y;
	public float z;
	
	public float xRaw;
	public float yRaw;
	public float zRaw;
	public int accuracy;
	
	public AcclInfo()
	{
		
	}
	
	public AcclInfo(long time, float x, float y, float z,
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
		JSONObject jAccl = null;
		try
		{
			jAccl = new JSONObject();
			jAccl.put("time", time);
			jAccl.put("x", x);
			jAccl.put("y", y);
			jAccl.put("z", z);
			jAccl.put("xRaw", xRaw);
			jAccl.put("yRaw", yRaw);
			jAccl.put("zRaw", zRaw);	
			jAccl.put("accuracy", accuracy);
		}
		catch (Exception e)
		{
			return null;
		}
		return jAccl;
	}
}
