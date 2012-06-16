package com.Android.CodeInTheAir.Types;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class WifiInfo 
{
	public long time;
	public boolean enabled;	
	public List<APInfo> aps;
	public String scan;
	
	public WifiInfo()
	{
		aps = new ArrayList<APInfo>();		
	}
	
	public WifiInfo(boolean enabled, long time, List<APInfo> aps, String scan)
	{
		this.enabled = enabled;		
		this.time = time;
		this.aps = aps;		
		this.scan = scan;
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jWifiInfo = null;
		try
		{
			jWifiInfo = new JSONObject();
			if (aps != null)
			{
				JSONArray jAPs = new JSONArray();
				for (int i = 0; i < aps.size(); i++)
				{
					JSONObject jAP = aps.get(i).encodeJSON();
					jAPs.put(jAP);
				}
				jWifiInfo.put("ap", jAPs);
			}			
			
			jWifiInfo.put("time", time);
			jWifiInfo.put("enabled", enabled);
			jWifiInfo.put("scan", scan);
		}
		catch (Exception e)
		{
			return null;
		}
		return jWifiInfo;
	}
}
