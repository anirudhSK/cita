package com.Android.CodeInTheAir.JSInterface;

import com.Android.CodeInTheAir.Device.Query.Sensor_NetworkGPS;
import com.Android.CodeInTheAir.Events.TaskContext;
import com.Android.CodeInTheAir.Types.LocationInfo;

public class JS_NGPS extends JS_Generic 
{
	public JS_NGPS(String fileName, TaskContext taskContext)
	{
		super(fileName, taskContext);
	}

	/* 
	 * getLocation 
	 * */	
	public String getLocation()
	{
		LocationInfo locationInfo = Sensor_NetworkGPS.getLocation();
		return locationInfo.encodeJSON().toString();
	}
	
	/*
	 *  getLat
	 */
	public double getLat()
	{
		return Sensor_NetworkGPS.getLat();
	}
	
	/*
	 *  getLng
	 */
	public double getLng()
	{
		return Sensor_NetworkGPS.getLng();
	}
	
	
	/*
	 *  getAcc
	 */
	public double getAcc()
	{
		return Sensor_NetworkGPS.getAcc();
	}

	/*
	 *  getTime
	 */
	public long getTime()
	{
		return Sensor_NetworkGPS.getTime();
	}
	
	
	/* 
	 * isEnabled 
	 * */	
	public boolean isEnabled()
	{
		return Sensor_NetworkGPS.isEnabled();
	}
}
