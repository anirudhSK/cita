package com.CITAJSServer.JSInterface;

import com.CITAJSServer.Events.Constants;
import com.CITAJSServer.Events.Event_Call_NoParam;
import com.CITAJSServer.Events.Event_Call_Param;
import com.CITAJSServer.Events.Log;
import com.CITAJSServer.Events.TaskContext;

public class JS_Call
{
	String fileName;
	TaskContext taskContext;
	public JS_Call(String id)
	{
		fileName = JS_Context.getFileName(id);
		taskContext = JS_Context.getTaskContext(id);
	}
	
	public void call(String action)
	{
		Log.v("call.call", action);
		String source = Constants.LocalJS + ":" + fileName;	
		Event_Call_NoParam event = new Event_Call_NoParam(source, action);
		taskContext.postEvent(event);
	}
	
	public void call(String action, String params)
	{
		String source = Constants.LocalJS + ":" + fileName;	
		Event_Call_Param event = new Event_Call_Param(source, action, params);
		taskContext.postEvent(event);
	}
}
