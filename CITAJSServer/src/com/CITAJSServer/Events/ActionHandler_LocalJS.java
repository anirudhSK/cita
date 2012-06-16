package com.CITAJSServer.Events;


public class ActionHandler_LocalJS 
{
	TaskContext taskContext;
	
	public ActionHandler_LocalJS(TaskContext taskContext)
	{
		this.taskContext = taskContext;
	}
	
	public void handle(String file, String func, String params)
	{		
		taskContext.callLocalJSFunc(file, func, params);
	}
	
	public void handle(String file, String func)
	{
		taskContext.callLocalJSFunc(file, func);
	}
}
