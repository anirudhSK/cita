package com.Android.CodeInTheAir.JSInterface;

import com.Android.CodeInTheAir.Device.Query.Sensor_Location;
import com.Android.CodeInTheAir.Events.TaskContext;
import com.Android.CodeInTheAir.Types.LocationInfo;

public class JS_Loc extends JS_Generic 
{
	public JS_Loc(String fileName, TaskContext taskContext)
	{
		super(fileName, taskContext);
	}

	/* 
	 * getLocation 
	 * */	
	public String getLocation()
	{
		LocationInfo locationInfo = Sensor_Location.getLocation();
		return locationInfo.encodeJSON().toString();
	}
	
	public String getLocation(int sigTime, int sigAcc)
	{
		LocationInfo locationInfo = Sensor_Location.getLocation(sigTime, sigAcc);
		return locationInfo.encodeJSON().toString();
	}
	
	/*
	 *  getLat
	 */
	public double getLat()
	{
		return Sensor_Location.getLat();
	}
	
	/*
	 *  getLng
	 */
	public double getLng()
	{
		return Sensor_Location.getLng();
	}
	
	
	/*
	 *  getAcc
	 */
	public double getAcc()
	{
		return Sensor_Location.getAcc();
	}

	/*
	 *  getTime
	 */
	public long getTime()
	{
		return Sensor_Location.getTime();
	}
	
	/*
	 *  getProvider
	 */
	public String getProvider()
	{
		return Sensor_Location.getProvider();
	}
	
	
	/* 
	 * isEnabled 
	 * */	
	public boolean isEnabled()
	{
		return Sensor_Location.isEnabled();
	}
}
