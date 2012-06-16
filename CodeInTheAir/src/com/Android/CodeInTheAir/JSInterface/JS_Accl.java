package com.Android.CodeInTheAir.JSInterface;

import com.Android.CodeInTheAir.Device.Query.Sensor_Accl;
import com.Android.CodeInTheAir.Events.TaskContext;

public class JS_Accl extends JS_Generic
{
	public JS_Accl(String fileName, TaskContext taskContext)
	{
		super(fileName, taskContext);
	}
	
	/*
	 * isAvailable
	 */
	
	public boolean isAvailable()
	{
		return Sensor_Accl.isAvailable();
	}
}
