package com.Android.CodeInTheAir.Types;

import org.json.JSONObject;

public class BatteryInfo 
{
	public boolean isPresent;
	public long time;	
	public int level;
	public int scale;
	public int temp;
	public int voltage;	
	public int current;
	public int charge;
	
	public String plugged;
	public String status;
	public String health;
	
	public BatteryInfo(boolean isPresent, long time, int level, int scale,
			int temp, int voltage, int current, int charge,
			String plugged, String status, String health)
	{
		this.isPresent = isPresent;
		this.time = time;
		this.level = level;
		this.scale = scale;
		this.temp = temp;
		this.voltage = voltage;
		this.current = current;
		this.charge = charge;
		this.plugged = plugged;
		this.status = status;
		this.health = health;
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jBattery = null;
		try
		{
			jBattery = new JSONObject();
			jBattery.put("present", isPresent);
			jBattery.put("time", time);
			if (isPresent)
			{
				jBattery.put("level", level);
				jBattery.put("scale", scale);
				jBattery.put("temp", temp);
				jBattery.put("voltage", voltage);
				jBattery.put("current", current);
				jBattery.put("charge", charge);
				jBattery.put("status", status);
				jBattery.put("health", health);
			}		
			
			
			jBattery.put("plugged", plugged);
		}	
		catch (Exception e)
		{
			return null;
		}
		return jBattery;
	}
}
