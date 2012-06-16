package com.Android.CodeInTheAir.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.Android.CodeInTheAir.JSInterface.JS_Log_Interface;

public class LocalJSEngine
{
	HashMap<String, WebViewWrapper> hWebkitClient;
	TaskContext taskContext;
	
	HashMap<String, String> hCode;
	List<String> orderedFiles;
	
	int currentJSToLoad;
	
	String mainFile;
	String mainFunc;
	JS_Log_Interface js_log;
	
	public LocalJSEngine(TaskContext taskContext)
	{		
		this.taskContext = taskContext;
		hWebkitClient = new HashMap<String, WebViewWrapper>();
		hCode = new HashMap<String, String>();
		orderedFiles = new ArrayList<String>();	
		currentJSToLoad = 0;
	}
	
	public void init(String pkg)
	{
		try
		{
			JSONObject jPkg = new JSONObject(pkg);
			mainFile = jPkg.getString("mainFile");
			if (!jPkg.isNull("mainFunc"))
			{
				mainFunc = jPkg.getString("mainFunc");
			}
			JSONArray jSource = (JSONArray)jPkg.get("source");
			for (int i = 0; i < jSource.length(); i++)
			{
				JSONObject jCode = (JSONObject)jSource.get(i);
				String jsName = jCode.getString("file");
				String code = jCode.getString("code");
				hCode.put(jsName, code);
				if (!jsName.equals(mainFile))
				{
					orderedFiles.add(jsName);
				}
			}
			orderedFiles.add(mainFile);
		}
		catch (Exception e)
		{
			Log.v("CITA:Parsing package exception", e.getMessage());
		}
	}
	
	public void load()
	{		
		Log.v("CITA:shell", "loading");
		String file = orderedFiles.get(currentJSToLoad);
		String code = hCode.get(file);
		WebViewWrapper webView = new WebViewWrapper(file, taskContext);
		webView.addLogInterface(js_log);		
		webView.load(code);
		hWebkitClient.put(file, webView);
	}
	
	
	public void callFunc(String file, String func)
	{
		WebViewWrapper webView = hWebkitClient.get(file);
		webView.callFunc(func);
	}
	
	public void callFunc(String file, String func, String params)
	{
		Log.v("CITA:=====", "file: " + file);
		WebViewWrapper webView = hWebkitClient.get(file);
		webView.callFunc(func, params);
	}
	
	public void callFuncJSON(String file, String func, String params)
	{
		WebViewWrapper webView = hWebkitClient.get(file);
		webView.callFuncJSON(func, params);
	}
	
	public void addJSLogInterface(JS_Log_Interface js_log)
	{
		this.js_log = js_log;
	}
}
