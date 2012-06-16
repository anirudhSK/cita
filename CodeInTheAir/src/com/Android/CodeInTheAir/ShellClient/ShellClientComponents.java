package com.Android.CodeInTheAir.ShellClient;

import android.os.Handler;

public class ShellClientComponents 
{
	public static ShellClientInterface shellClientInterface;
	public static ShellClientManager shellClientManager;
	
	static Handler sciHandler;
	
	public static void start()
	{
		shellClientManager = new ShellClientManager();
		shellClientInterface = new ShellClientInterface();	
		
		sciHandler = new Handler();	
		sciHandler.postDelayed(shellClientInterfaceTask, 0);		
	}

	static Runnable shellClientInterfaceTask = new Runnable()
	{
		public void run()
		{
			shellClientInterface.start();	
		}
	};
}
