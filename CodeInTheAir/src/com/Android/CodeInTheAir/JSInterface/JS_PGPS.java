package com.Android.CodeInTheAir.JSInterface;

import com.Android.CodeInTheAir.Device.Query.Sensor_PassiveGPS;
import com.Android.CodeInTheAir.Events.TaskContext;
import com.Android.CodeInTheAir.Types.LocationInfo;

public class JS_PGPS extends JS_Generic 
{
	public JS_PGPS(String fileName, TaskContext taskContext)
	{
		super(fileName, taskContext);
	}

	/* 
	 * getLocation 
	 * */	
	public String getLocation()
	{
		LocationInfo locationInfo = Sensor_PassiveGPS.getLocation();
		return locationInfo.encodeJSON().toString();
	}
	
	/*
	 *  getLat
	 */
	public double getLat()
	{
		return Sensor_PassiveGPS.getLat();
	}
	
	/*
	 *  getLng
	 */
	public double getLng()
	{
		return Sensor_PassiveGPS.getLng();
	}
	
	
	/*
	 *  getAcc
	 */
	public double getAcc()
	{
		return Sensor_PassiveGPS.getAcc();
	}

	/*
	 *  getTime
	 */
	public long getTime()
	{
		return Sensor_PassiveGPS.getTime();
	}
	
	/*
	 *  getProvider
	 */
	public String getProvider()
	{
		return Sensor_PassiveGPS.getProvider();
	}
	
	
	/* 
	 * isEnabled 
	 * */	
	public boolean isEnabled()
	{
		return Sensor_PassiveGPS.isEnabled();
	}
}
