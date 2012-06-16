package com.Android.CodeInTheAir.UI;

import com.Android.CodeInTheAir.Device.Query.Sensors;
import com.Android.CodeInTheAir.Events.TaskManager;
import com.Android.CodeInTheAir.ShellClient.ShellClientMain;
import com.Android.ServerClient.ServerClientInterface;

public class Components 
{
	public static ServerClientInterface ServerClientInterface;
	public static void start()
	{
        Sensors.init();
        TaskManager.init();
        
        ShellClientMain.run();
        
		ServerClientInterface = new ServerClientInterface();
		ServerClientInterface.start();
	}
}
