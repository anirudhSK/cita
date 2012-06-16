package com.Android.CodeInTheAir.JSInterface;

import android.util.Log;

import com.Android.CodeInTheAir.Events.Constants;
import com.Android.CodeInTheAir.Events.TaskContext;

public class JS_Callback extends JS_Generic
{
	public JS_Callback(String fileName, TaskContext taskContext)
	{
		super(fileName, taskContext);
	}
	
	public void add(String event, String action, String param, String tag)
	{
		Log.v("CITA:callback", event + " " + param + " " + action + " " + tag);
		event = event.toLowerCase();		
		String source = Constants.LocalJS + ":" + fileName;		
		taskContext.addCallback(event, param, action, tag, source);	
	}
	
	public void remove(String event, String action)
	{
		taskContext.removeCallback(event, action);
	}
	
	public void remove(String event)
	{
		taskContext.removeCallback(event);
	}

}
