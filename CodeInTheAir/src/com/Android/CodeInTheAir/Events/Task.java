package com.Android.CodeInTheAir.Events;


public class Task 
{
	String taskId;		
	TaskContext taskContext;
	
	public Task(String taskId)
	{
		this.taskId = taskId;		
		taskContext = new TaskContext(taskId);
	}
	
	public void start(String pkg)
	{
		taskContext.startLocalJSEngine(pkg);
	}
	
	public void stop()
	{
		
	}
	
	public TaskContext getTaskContext()
	{
		return taskContext;
	}
}
