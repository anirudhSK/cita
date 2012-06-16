package com.Android.CodeInTheAir.JSInterface;

import java.util.List;

import com.Android.CodeInTheAir.Device.Query.Sensor_Wifi;
import com.Android.CodeInTheAir.Events.TaskContext;
import com.Android.CodeInTheAir.Types.APInfo;
import com.Android.CodeInTheAir.Types.TypeUtils;

public class JS_Wifi extends JS_Generic 
{
	public JS_Wifi(String fileName, TaskContext taskContext)
	{
		super(fileName, taskContext);
	}
	
	/* 
	 * getAssociatedAP 
	 * */	
	public String getAssociatedAP()
	{
		APInfo apInfo = Sensor_Wifi.getAssociatedAP();
		return apInfo.encodeJSON().toString();
	}
	
	/* 
	 * getStrongestAP 
	 * */
	public String getStrongestAP()
	{
		APInfo apInfo = Sensor_Wifi.getAssociatedAP();
		return apInfo.encodeJSON().toString();
	}
	
	public String getStrongestAP(String filterSSIDArr)
	{
		String[] filterSSIDs = TypeUtils.JSONToStringArr(filterSSIDArr);
		APInfo apInfo = Sensor_Wifi.getStrongestAP(filterSSIDs);
		return apInfo.encodeJSON().toString();
	}
	
	
	/* 
	 * getSSIDs
	 * */
	public String getSSIDs()
	{
		String[] ssids = Sensor_Wifi.getSSIDs();
		return TypeUtils.encodeJSON(ssids).toString();
	}
		
	/*
	 * getAroundAPs
	 */
	
	public String getAroundAPs()
	{
		List<APInfo> aps = Sensor_Wifi.getAroundAPs();
		return APInfo.encodeJSON(aps).toString();
	}
	
	public String getAroundAPs(String filterSSIDArr)
	{
		String[] filterSSIDs = TypeUtils.JSONToStringArr(filterSSIDArr);
		List<APInfo> aps = Sensor_Wifi.getAroundAPs(filterSSIDs);
		return APInfo.encodeJSON(aps).toString();
	}
	
	public String getAroundAPs(int n)
	{		
		List<APInfo> aps = Sensor_Wifi.getAroundAPs(n);
		return APInfo.encodeJSON(aps).toString();
	}
	
	public String getAroundAPs(String filterSSIDArr, int n)
	{
		String[] filterSSIDs = TypeUtils.JSONToStringArr(filterSSIDArr);
		List<APInfo> aps = Sensor_Wifi.getAroundAPs(filterSSIDs, n);
		return APInfo.encodeJSON(aps).toString();
	}
	
	/*
	 * isAssociated
	 */	
	public boolean isAssociated()
	{
		return Sensor_Wifi.isAssociated();
	}
	
	/*
	 * isEnabled
	 */	
	public boolean isEnabled()
	{
		return Sensor_Wifi.isEnabled();
	}
	
	/*
	 * getState
	 */	
	public int getState()
	{
		return Sensor_Wifi.getState();
	}
	
	/*
	 * scan
	 */	
	public boolean scan()
	{
		return Sensor_Wifi.scan();
	}

	/*
	 * enable
	 */
	
	public boolean enable()
	{
		return Sensor_Wifi.enable();
	}
	
	/*
	 * disable
	 */	
	public boolean disable()
	{
		return Sensor_Wifi.disable();
	}
	
	/*
	 * disconnect
	 */	
	public boolean disconnect()
	{
		return Sensor_Wifi.disconnect();
	}
	
	/*
	 * reassociate
	 */	
	public boolean reassociate()
	{
		return Sensor_Wifi.reassociate();
	}
	
	/*
	 * reconnect
	 */	
	public boolean reconnect()
	{
		return Sensor_Wifi.reconnect();
	}
}
