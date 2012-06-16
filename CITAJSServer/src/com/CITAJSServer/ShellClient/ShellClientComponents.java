package com.CITAJSServer.ShellClient;


public class ShellClientComponents 
{
	public static ShellClientInterface shellClientInterface;
	public static ShellClientManager shellClientManager;

	public static void start()
	{
		shellClientManager = new ShellClientManager();
		shellClientInterface = new ShellClientInterface();	
		shellClientInterface.start();
	}

}
