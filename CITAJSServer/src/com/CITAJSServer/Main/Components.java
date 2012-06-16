package com.CITAJSServer.Main;

import com.CITAJSServer.Clients.DeviceInterface;
import com.CITAJSServer.Clients.Settings;
import com.CITAJSServer.Events.TaskManager;
import com.CITAJSServer.ShellClient.ShellClientComponents;

public class Components 
{
	public static DeviceInterface DeviceInterface;

	public static void start()
	{
		DeviceInterface = new DeviceInterface(Settings.DevicePort);
		TaskManager.init();
		
		new Thread()
		{
			public void run()
			{
				DeviceInterface.start();
			}
		}.start();
		
		
		ShellClientComponents.start();
	}
}
