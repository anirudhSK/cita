package com.Android.CodeInTheAir.Types;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class GSMInfo 
{
	public boolean enabled;
	public long time;
	public List<CellInfo> cells;
	
	public GSMInfo()
	{
		cells = new ArrayList<CellInfo>();		
	}
	
	public GSMInfo(boolean enabled, long time, List<CellInfo> cells)
	{
		this.enabled = enabled;
		this.time = time;
		this.cells = cells;		
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jCellsInfo = null;
		try
		{
			jCellsInfo = new JSONObject();			
			jCellsInfo.put("cell", CellInfo.encodeJSON(cells));
			jCellsInfo.put("time", time);
			jCellsInfo.put("enabled", enabled);
		}
		catch (Exception e)
		{
			return null;
		}
		return jCellsInfo;
	}
}
