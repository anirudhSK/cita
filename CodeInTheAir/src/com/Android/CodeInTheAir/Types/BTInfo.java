package com.Android.CodeInTheAir.Types;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BTInfo 
{
	public boolean enabled;
	public long time;
	public List<BTDeviceInfo> devices;
	public boolean isDiscovering;
	public String scan;
	
	public BTInfo()
	{
				
	}
	
	public BTInfo(boolean enabled, long time, List<BTDeviceInfo> devices, boolean isDiscovering, String scan)
	{
		this.enabled = enabled;
		this.time = time;
		this.devices = devices;
		this.isDiscovering = isDiscovering;
		this.scan = scan;
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jBTsInfo = null;
		try
		{
			jBTsInfo = new JSONObject();
			if (devices != null)
			{
				JSONArray jBTs = new JSONArray();
				for (int i = 0; i < devices.size(); i++)
				{
					JSONObject jBT = devices.get(i).encodeJSON();
					jBTs.put(jBT);
				}
				jBTsInfo.put("device", jBTs);
			}
			
			jBTsInfo.put("time", time);
			jBTsInfo.put("enabled", enabled);
			if (enabled)
			{
				jBTsInfo.put("discovering", isDiscovering);
			}
			jBTsInfo.put("scan", scan);
		}
		catch (Exception e)
		{
			return null;
		}
		return jBTsInfo;
	}
}
