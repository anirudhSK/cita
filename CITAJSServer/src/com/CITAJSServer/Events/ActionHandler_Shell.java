package com.CITAJSServer.Events;

import com.CITAJSServer.ShellClient.ShellClientComponents;


public class ActionHandler_Shell 
{
	TaskContext taskContext;
	
	public ActionHandler_Shell(TaskContext taskContext)
	{
		this.taskContext = taskContext;
	}
	
	public void handle(String taskId, String callId, String value)
	{		
		ShellClientComponents.shellClientInterface.sendResponse(taskId, callId, value);
	}
}
