package com.CITAJSServer.JSInterface;

import com.CITAJSServer.Events.Constants;
import com.CITAJSServer.Events.TaskContext;

public class JS_Callback
{
	String fileName;
	TaskContext taskContext;
	public JS_Callback(String id)
	{
		fileName = JS_Context.getFileName(id);
		taskContext = JS_Context.getTaskContext(id);
	}
	
	
	public void add(String event, String param, String action, String tag)
	{
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
