package com.CITAJSServer.Events;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.CITAJSServer.JSInterface.JS_Context;
import com.CITAJSServer.JSInterface.JS_Log;
import com.CITAJSServer.JSInterface.JS_Log_Interface;

public class WebViewWrapper 
{
	ScriptEngineManager factory;
	ScriptEngine engine;
	String fileName;
	TaskContext taskContext;
	
	public WebViewWrapper(String fileName, TaskContext taskContext)
	{
		factory = new ScriptEngineManager();
		engine = factory.getEngineByName("JavaScript");
		this.fileName = fileName;
		this.taskContext = taskContext;
		
		initWebView();
	}
	
	public void load(String code)
	{
		try
		{
			engine.eval(code);
		}
		catch (Exception e)
		{
			System.out.println("JS Load Exception: " + e.getMessage());
		}
	}
	
	public void callFuncJSON(String func, String params)
	{
		String callString =  func + "(JSON.parse('";
		callString = callString + params;	
		callString = callString + "'))";
		try
		{
			engine.eval(callString);
		}
		catch (Exception e)
		{
			
		}
	}
	
	public void callFunc(String func, String params)
	{
		String callString = func + "(";
		callString = callString + params;	
		callString = callString + ")";		
		try
		{
			engine.eval(callString);
		}
		catch (Exception e)
		{
			System.out.println("JS callFunc Exception: " + e.getMessage());
		}
	}
	
	public void callFunc(String func)
	{
		String callString = func + "(";
		callString = callString + ")";
		
		
		try
		{
			engine.eval(callString);
		}
		catch (Exception e)
		{
			System.out.println("JS CallFunc Exception: " + e.getMessage());
		}  
	}
	
	private void initWebView()
	{
		String id = TaskUtils.getUniqueId();
		JS_Context.addContext(id, fileName, taskContext);
		try
		{
			engine.eval("importPackage(com.CITAJSServer.JSInterface)");
			engine.eval("callback = new JS_Callback(\"" + id + "\")");
			engine.eval("call = new JS_Call(\"" + id + "\")");
		}
		catch (Exception e)
		{
			System.out.println("JS init Exception: " + e.getMessage());
		}
	}
	
	public void addLogInterface(JS_Log_Interface js_log)
	{
		JS_Log.logInterface = js_log;
		try
		{
			engine.eval("log = new JS_Log();");
		}
		catch (Exception e)
		{
			System.out.println("JS log Exception: " + e.getMessage());
		}
	}
}
