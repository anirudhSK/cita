package com.Android.CodeInTheAir.Events;

import com.Android.CodeInTheAir.ShellClient.ShellClientComponents;


public class ActionHandler_Shell 
{
	TaskContext taskContext;
	
	public ActionHandler_Shell(TaskContext taskContext)
	{
		this.taskContext = taskContext;
	}
	
	public void handle(String taskId, String sessionId, String value)
	{		
		ShellClientComponents.shellClientInterface.sendResponse(taskId, sessionId, value);
	}
}
