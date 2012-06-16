package com.CITAJSServer.JSInterface;


public class JS_Log
{
	public static JS_Log_Interface logInterface;
	
	public void log(String tag, String value)
	{
		System.out.println("log: " + tag + " " + value);
		logInterface.log(tag, value);
	}
	
	public void log(String value)
	{
		logInterface.log(value);
	}		
	
	public void log()
	{
		System.out.println("log: pageLoaded");
		logInterface.log();
	}
}
