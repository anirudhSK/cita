package com.Android.CodeInTheAir.Types;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/* WiFi AP Info */
public class APInfo 
{
	public String bssid;
	public String ssid;
	public int rssi;
	public int channel;
	public String capabilities;
	public boolean associated;
	
	public APInfo()
	{
		
	}
	
	public APInfo(String bssid, String ssid, int rssi, int channel, String capabilities, boolean associated)
	{
		this.bssid = bssid;
		this.ssid = ssid;
		this.rssi = rssi;
		this.channel = channel;
		this.capabilities = capabilities;
		this.associated = associated;
	}
	
	
	public JSONObject encodeJSON()
	{
		JSONObject jAP = null;
		try
		{
			jAP = new JSONObject();
			if (ssid != null)
			{
				jAP.put("ssid", ssid);
			}
			if (bssid != null)
			{
				jAP.put("bssid", bssid);
			}
			if (rssi != 0)
			{
				jAP.put("rssi", rssi);
			}
			if (channel != 0)
			{
				jAP.put("channel", channel);
			}
			if (capabilities != null)
			{
				jAP.put("capabilities", capabilities);
			}
			jAP.put("associated", associated);			
		}
		catch (Exception e)
		{
			return null;
		}
		return jAP;
	}

	
	public static JSONArray encodeJSON(List<APInfo> aps)
	{
		JSONArray jAPs = new JSONArray();
		try
		{			
			if  (jAPs != null)
			{				
				for (int i = 0; i < aps.size(); i++)
				{
					JSONObject jCell = aps.get(i).encodeJSON();
					jAPs.put(jCell);
				}				
			}			
		}
		catch (Exception e)
		{
			return null;
		}
		return jAPs;
	}
}