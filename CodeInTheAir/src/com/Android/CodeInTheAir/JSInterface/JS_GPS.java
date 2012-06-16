package com.Android.CodeInTheAir.JSInterface;

import java.util.List;

import com.Android.CodeInTheAir.Device.Query.Sensor_GPS;
import com.Android.CodeInTheAir.Events.TaskContext;
import com.Android.CodeInTheAir.Types.LocationInfo;
import com.Android.CodeInTheAir.Types.SatelliteInfo;

public class JS_GPS extends JS_Generic 
{
	public JS_GPS(String fileName, TaskContext taskContext)
	{
		super(fileName, taskContext);
	}

	/* 
	 * getLocation 
	 * */	
	public String getLocation()
	{
		LocationInfo locationInfo = Sensor_GPS.getLocation();
		return locationInfo.encodeJSON().toString();
	}
	
	/*
	 *  getLat
	 */
	public double getLat()
	{
		return Sensor_GPS.getLat();
	}
	
	/*
	 *  getLng
	 */
	public double getLng()
	{
		return Sensor_GPS.getLng();
	}
	
	/*
	 *  getAlt
	 */
	public double getAlt()
	{
		return Sensor_GPS.getAlt();
	}
	
	/*
	 *  getAcc
	 */
	public double getAcc()
	{
		return Sensor_GPS.getAcc();
	}
	
	/*
	 *  getSpeed
	 */
	public double getSpeed()
	{
		return Sensor_GPS.getSpeed();
	}
	
	/*
	 *  getDir
	 */
	public double getDir()
	{
		return Sensor_GPS.getDir();
	}
	
	/*
	 *  getTime
	 */
	public long getTime()
	{
		return Sensor_GPS.getTime();
	}
	
	
	/* 
	 * isEnabled 
	 * */	
	public boolean isEnabled()
	{
		return Sensor_GPS.isEnabled();
	}
	
	/* 
	 * getSatellites 
	 * */	
	public String getSatellites()
	{
		List<SatelliteInfo> sats = Sensor_GPS.getSatellites();
		return SatelliteInfo.encodeJSON(sats).toString();
	}
	
	/* 
	 * getTimeToFix 
	 * */
	public int getTimeToFix()
	{
		return Sensor_GPS.getTimeToFix();
	}
}
