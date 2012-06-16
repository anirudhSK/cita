package com.Android.CodeInTheAir.Events;

public class CallbackInfo 
{
	public String taskId;	
	public String source;
	
	public String event;
	public String action;
	public String tag;
	
	public CallbackInfo(String taskId, String source, 
			String event, String action, String tag)
	{
		this.taskId = taskId;
		this.source = source;
		
		this.event = event;
		this.action = action;
		this.tag = tag;
	}
}
