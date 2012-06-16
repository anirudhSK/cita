package com.CITAJSServer.Events;

import java.util.HashMap;

public class TaskManager 
{
	static HashMap<String, Task> hTaskIdTask;
	
	public static void init()
	{
		hTaskIdTask = new HashMap<String, Task>();
		CallbackManager.init();
	}
	
	public static Task createTask()
	{
		String taskId = TaskUtils.getUniqueId();
		Task task = new Task(taskId);		
		hTaskIdTask.put(taskId, task);		
		return task;
	}
	
	public static Task createTask(String taskId)
	{
		Task task = new Task(taskId);		
		hTaskIdTask.put(taskId, task);		
		return task;
	}
	
	public static Task getTask(String taskId)
	{
		if (hTaskIdTask.containsKey(taskId))
		{
			return hTaskIdTask.get(taskId);
		}
		return null;		
	}
	
	
}
