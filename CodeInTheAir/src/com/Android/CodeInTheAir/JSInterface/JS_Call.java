package com.Android.CodeInTheAir.JSInterface;

import android.util.Log;

import com.Android.CodeInTheAir.Events.Constants;
import com.Android.CodeInTheAir.Events.Event_Call_NoParam;
import com.Android.CodeInTheAir.Events.Event_Call_Param;
import com.Android.CodeInTheAir.Events.TaskContext;

public class JS_Call extends JS_Generic 
{
	public JS_Call(String fileName, TaskContext taskContext)
	{
		super(fileName, taskContext);
	}
	
	public void call(String action)
	{
		Log.v("CITA:call.call", action);
		String source = Constants.LocalJS + ":" + fileName;	
		Event_Call_NoParam event = new Event_Call_NoParam(source, action);
		taskContext.postEvent(event);
	}
	
	public void call(String action, String params)
	{
		String source = Constants.LocalJS + ":" + fileName;	
		Event_Call_Param event = new Event_Call_Param(source, action, params);
		taskContext.postEvent(event);
	}
}
