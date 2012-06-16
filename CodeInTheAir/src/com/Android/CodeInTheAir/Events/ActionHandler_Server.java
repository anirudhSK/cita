package com.Android.CodeInTheAir.Events;

import com.Android.CodeInTheAir.Events.Constants.EventType;
import com.Android.CodeInTheAir.UI.Components;

public class ActionHandler_Server 
{
	TaskContext taskContext;
	
	public ActionHandler_Server(TaskContext taskContext)
	{
		this.taskContext = taskContext;
	}
	
	public void handle(String engine, String file, String func, String params)
	{		
		Components.ServerClientInterface.sendCommand(engine, taskContext.getTaskId(), EventType.CALL_PARAM_EVENT, file, func, params);
	}
	
	public void handle(String engine, String file, String func)
	{
		Components.ServerClientInterface.sendCommand(engine, taskContext.getTaskId(), EventType.CALL_NOPARAM_EVENT, file, func, null);
	}
}
