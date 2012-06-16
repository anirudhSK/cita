package com.CITAJSServer.JSInterface;

import java.util.HashMap;

import com.CITAJSServer.Events.TaskContext;

public class JS_Context 
{
	public static HashMap<String, TaskContext> hTaskContext = new HashMap<String, TaskContext>();
	public static HashMap<String, String> hFileName = new HashMap<String, String>();
	
	public static void addContext(String id, String fileName, TaskContext taskContext)
	{
		hFileName.put(id, fileName);
		hTaskContext.put(id, taskContext);
	}
	
	public static String getFileName(String id)
	{
		return hFileName.get(id);
	}
	
	public static TaskContext getTaskContext(String id)
	{
		return hTaskContext.get(id);
	}
}
