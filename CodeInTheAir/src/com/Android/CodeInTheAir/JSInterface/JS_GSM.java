package com.Android.CodeInTheAir.JSInterface;

import java.util.List;

import com.Android.CodeInTheAir.Device.Query.Sensor_GSM;
import com.Android.CodeInTheAir.Events.TaskContext;
import com.Android.CodeInTheAir.Types.CellInfo;

public class JS_GSM extends JS_Generic
{
	public JS_GSM(String fileName, TaskContext taskContext)
	{
		super(fileName, taskContext);
	}
	
	/* 
	 * getConnectedCell 
	 * */	
	public String getConnectedCell()
	{
		CellInfo cellInfo = Sensor_GSM.getConnectedCell();
		return cellInfo.encodeJSON().toString();
	}
	
	/* 
	 * getStrongestCell 
	 * */
	public String getStrongestCell()
	{
		CellInfo cellInfo = Sensor_GSM.getStrongestCell();
		return cellInfo.encodeJSON().toString();
	}
	
	/* 
	 * getAroundCells
	 * */
	public String getAroundCells()
	{
		List<CellInfo> cells = Sensor_GSM.getAroundCells();
		return CellInfo.encodeJSON(cells).toString();
	}
	
	public String getAroundCells(int n)
	{
		List<CellInfo> cells = Sensor_GSM.getAroundCells(n);
		return CellInfo.encodeJSON(cells).toString();
	}
	
	/* 
	 * isGSMPhone() 
	 * */
	public boolean isGSMPhone()
	{
		return Sensor_GSM.isGSMPhone();
	}
	
	/* 
	 * isEnabled() 
	 * */
	public boolean isEnabled()
	{
		return Sensor_GSM.isEnabled();
	}

}
