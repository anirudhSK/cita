package com.Android.CodeInTheAir.Events;

import com.Android.CodeInTheAir.JSInterface.JS_Log_Interface;

public class TaskContext 
{
	String taskId;
	EventManager eventManager;	
	LocalJSEngine localJSEngine;
	
	public TaskContext(String taskId)
	{
		this.taskId = taskId;
		eventManager = new EventManager(this);		
		localJSEngine = new LocalJSEngine(this);		
	}
	
	public String addCallback(String event, String param, String action, String tag, String source)
	{		
		return CallbackManager.addCallback(taskId, source, event, param, action, tag);
	}
	
	public void removeCallback(String event, String action, String tag)
	{
		CallbackManager.removeCallback(taskId, event, action, tag, false);
	}
	
	public void removeCallback(String event, String action)
	{
		CallbackManager.removeCallback(taskId, event, action, false);
	}
	
	public void removeCallback(String event)
	{
		CallbackManager.removeCallback(taskId, event, false);
	}
	
	public void removeCallback()
	{
		CallbackManager.removeCallback(taskId);
	}
	
	public void removeCallbackId(String subId)
	{
		CallbackManager.removeCallbackId(subId);
	}
	
	public void postEvent(Event_Generic event)
	{
		eventManager.addEvent(event);
	}
	
	
	public void startLocalJSEngine(String pkg)
	{
		eventManager.start();
		localJSEngine.init(pkg);
		localJSEngine.load();
	}
	
	public void callLocalJSFunc(String codeId, String func)
	{
		localJSEngine.callFunc(codeId, func);
	}
	
	public void callLocalJSFunc(String codeId, String func, String params)
	{
		localJSEngine.callFunc(codeId, func, params);
	}
	
	public void callLocalJSFuncJSON(String codeId, String func, String params)
	{
		localJSEngine.callFuncJSON(codeId, func, params);
	}
	
	public void addJSLogInterface(JS_Log_Interface js_log)
	{
		localJSEngine.addJSLogInterface(js_log);
	}	
	
	public String getTaskId()
	{
		return taskId;
	}
}
