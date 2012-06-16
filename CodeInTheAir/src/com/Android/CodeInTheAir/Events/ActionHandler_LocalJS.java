package com.Android.CodeInTheAir.Events;

import android.util.Log;

public class ActionHandler_LocalJS 
{
	TaskContext taskContext;
	
	public ActionHandler_LocalJS(TaskContext taskContext)
	{
		this.taskContext = taskContext;
	}
	
	public void handle(String file, String func, String params)
	{		
		Log.v("CITA:callFunc", file + " " + func);
		taskContext.callLocalJSFunc(file, func, params);
	}
	
	public void handle(String file, String func)
	{
		taskContext.callLocalJSFunc(file, func);
	}
}
