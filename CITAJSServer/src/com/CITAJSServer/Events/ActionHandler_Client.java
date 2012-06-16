package com.CITAJSServer.Events;

import com.CITAJSServer.Events.Constants.EventType;
import com.CITAJSServer.Main.Components;

public class ActionHandler_Client 
{
	TaskContext taskContext;
	
	public ActionHandler_Client(TaskContext taskContext)
	{
		this.taskContext = taskContext;
	}
	
	public void handle(String client, String file, String func, String params)
	{		
		Components.DeviceInterface.sendCommand(client, taskContext.getTaskId(), EventType.CALL_PARAM_EVENT, file, func, params);
	}
	
	public void handle(String client, String file, String func)
	{
		Components.DeviceInterface.sendCommand(client, taskContext.getTaskId(), EventType.CALL_NOPARAM_EVENT, file, func, null);
	}
}
