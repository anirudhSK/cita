package com.Android.CodeInTheAir.Types;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SatellitesInfo 
{
	public long time;
	public List<SatelliteInfo> satellites;
	
	public SatellitesInfo()
	{
		satellites = new ArrayList<SatelliteInfo>();
	}
	
	public SatellitesInfo(long time, List<SatelliteInfo> satellites)
	{
		this.time = time;
		this.satellites = satellites; 
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jSatsInfo = null;
		try
		{
			jSatsInfo = new JSONObject();
			JSONArray jSts = new JSONArray();
			for (int i = 0; i < satellites.size(); i++)
			{
				JSONObject jSt = satellites.get(i).encodeJSON();
				jSts.put(jSt);
			}
			jSatsInfo.put("sat", jSts);
			jSatsInfo.put("time", time);
		}
		catch (Exception e)
		{
			return null;
		}
		return jSatsInfo;
	}
}
