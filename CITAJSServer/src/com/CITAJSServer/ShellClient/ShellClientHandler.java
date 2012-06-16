package com.CITAJSServer.ShellClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.CITAJSServer.Events.Log;
import com.CITAJSServer.Events.Task;
import com.CITAJSServer.Events.TaskManager;
import com.CITAJSServer.JSInterface.JS_Log_Interface;

public class ShellClientHandler 
{
	Task task;
	ShellJSLog shellLog;
	
	String taskId;
	boolean pageLoaded = false;	
	
	public ShellClientHandler(String taskId)
	{
		this.taskId = taskId;		
		
		task = TaskManager.createTask(taskId);
		shellLog = new ShellJSLog();
		task.getTaskContext().addJSLogInterface(shellLog);
		Log.v("shell", "loadingmain");
		task.start(getTaskPkg());
		
	}	
	
	public boolean execute(String callId, String command)
	{	
		Log.v("shell", "waiting");
		while (!pageLoaded)
		{
			try { Thread.sleep(100); } catch (Exception e) { };
		}		
		Log.v("shell", "waitingDone");
		
		command = command.replace("\"", "\\\\\\\"");		
		String evalCommand = "eval (\\\"" + command + "\\\")"; 		
		String commandBlock = "try { " + evalCommand + "} catch(err) { sprint(\\\"Exception : \\\" + err.message); }";
	
		String setCallIdCode = "setCallId(\\\"" + callId + "\\\");";
		commandBlock = "\"" + setCallIdCode + commandBlock + "\"";
		
		Log.v("strCommand", commandBlock);
		task.getTaskContext().callLocalJSFunc("main", "eval", commandBlock);
		return true;
	}
	
	
	private String getTaskPkg()
	{
		String shellCode = "";		
		try
		{			
			InputStream fileIS = this.getClass().getResourceAsStream("shell.html");
			BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
			String readString = new String();
			while((readString = buf.readLine())!= null)
			{
				shellCode += readString;
			}
			fileIS.close();
		}
		catch (Exception e)
		{
			Log.v("pkg exception", e.getMessage());
		}
		
		Log.v("shell", shellCode);
		
		JSONObject jMainObj = new JSONObject();
        try
        {
        	JSONObject jObj = new JSONObject();
	        jObj.put("file", "main");
	        jObj.put("code", shellCode);
	        
	        JSONArray jArr = new JSONArray();
	        jArr.put(jObj);
	        jMainObj.put("source", jArr);
	        jMainObj.put("mainFile", "main");	        	        
        }
        catch (Exception e)
        {
        	
        }        
        
        return jMainObj.toString();
	}
	
	public String getTaskId()
	{
		return task.getTaskContext().getTaskId();
	}
	
	class ShellJSLog implements JS_Log_Interface
	{		
		public void log(String tag, String value)
		{
			ShellClientComponents.shellClientInterface.sendResponse(taskId, tag, value);
		}
		
		public void log(String value)
		{
			
		}		
		
		public void log()
		{
			pageLoaded = true;
		}
	}
}
