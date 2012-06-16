package com.Android.CodeInTheAir.Types;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class CellInfo 
{
	public int type;
	public int cid;
	public int lac;
	public int rssi;
	public int ber;
	public boolean connected;
	
	public CellInfo()
	{
		
	}
	
	public CellInfo(int type, int cid, int lac, int rssi, int ber, boolean connected)
	{
		this.type = type;
		this.cid = cid;
		this.lac = lac;
		this.rssi = rssi;
		this.ber = ber;
		this.connected = connected;
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jCell = null;
		try
		{
			jCell = new JSONObject();
			jCell.put("type", type);
			jCell.put("id", cid);
			jCell.put("lac", lac);
			jCell.put("rssi", rssi);
			jCell.put("connected", connected);
			if (connected)
			{
				jCell.put("ber", ber);
			}			
		}
		catch (Exception e)
		{
			return null;
		}
		return jCell;
	}
	
	public static JSONArray encodeJSON(List<CellInfo> cells)
	{
		JSONArray jCells = new JSONArray();
		try
		{			
			if  (cells != null)
			{				
				for (int i = 0; i < cells.size(); i++)
				{
					JSONObject jCell = cells.get(i).encodeJSON();
					jCells.put(jCell);
				}				
			}			
			
		}
		catch (Exception e)
		{
			return null;
		}
		return jCells;
	}
}