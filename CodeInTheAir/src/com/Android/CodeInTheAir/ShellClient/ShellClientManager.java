package com.Android.CodeInTheAir.ShellClient;

import java.util.HashMap;

public class ShellClientManager 
{
	HashMap<String, ShellClientHandler> hSessionHandler;
	
	public ShellClientManager()
	{
		hSessionHandler = new HashMap<String, ShellClientHandler>();
	}
	
	public ShellClientHandler getHandler(String session)
	{
		if (!hSessionHandler.containsKey(session))
		{
			ShellClientHandler handler = new ShellClientHandler(session);
			hSessionHandler.put(session, handler);
		}
		
		return hSessionHandler.get(session);
	}
}
