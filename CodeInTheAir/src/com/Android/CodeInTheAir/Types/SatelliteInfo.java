package com.Android.CodeInTheAir.Types;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SatelliteInfo 
{
	public double azimuth;
	public double elevation;
	public int prn;
	public double snr;
	public boolean hasAlmanac;
	public boolean hasEphemeris;
	public boolean usedInFix;
	
	public SatelliteInfo()
	{
		
	}
	
	public SatelliteInfo(double azimuth, double elevation, 
			int prn, double snr, boolean hasAlmanac,  boolean hasEphemeris, boolean usedInFix)
	{
		this.azimuth = azimuth;
		this.elevation = elevation;
		this.prn = prn;
		this.snr = snr;
		this.hasAlmanac = hasAlmanac;
		this.hasEphemeris = hasEphemeris;
		this.usedInFix = usedInFix;
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jSat = null;
		try
		{
			jSat = new JSONObject();
			jSat.put("azimuth", azimuth);
			jSat.put("elevation", elevation);
			jSat.put("prn", prn);
			jSat.put("snr", snr);
			jSat.put("hasAlmanac", hasAlmanac);
			jSat.put("hasEphemeris", hasEphemeris);
			jSat.put("usedInFix", usedInFix);
		}
		catch (Exception e)
		{
			return null;
		}
		return jSat;
	}
	
	public static JSONArray encodeJSON(List<SatelliteInfo> sats)
	{
		JSONArray jSats = new JSONArray();
		try
		{			
			if  (jSats != null)
			{				
				for (int i = 0; i < sats.size(); i++)
				{
					JSONObject jCell = sats.get(i).encodeJSON();
					jSats.put(jCell);
				}				
			}			
		}
		catch (Exception e)
		{
			return null;
		}
		return jSats;
	}
}
