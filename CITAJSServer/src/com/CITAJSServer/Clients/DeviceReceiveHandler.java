package com.CITAJSServer.Clients;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import com.CITAJSServer.Events.Constants.EventType;
import com.CITAJSServer.Events.Event_Call_NoParam;
import com.CITAJSServer.Events.Event_Call_Param;
import com.CITAJSServer.Events.Task;
import com.CITAJSServer.Events.TaskManager;


public class DeviceReceiveHandler extends Thread
{
	Socket socket;	
	BufferedReader deviceReader;
	
	String device;
	
	DeviceReceiveHandler(Socket socket, String device)
	{
		this.device = device;
		this.socket = socket;
	}
	
	public void run()
	{
		try {
			deviceReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
		} catch (Exception e) {
			System.out.println("Exception creating InputStream " + e.getMessage());
		}
		
		try {
			while(true)
			{
				String taskId = deviceReader.readLine();
				String type = deviceReader.readLine();
				String engine = deviceReader.readLine();
				String file = deviceReader.readLine();
				String func = deviceReader.readLine();
				String param = null;
				
				if (type.equals(EventType.CALL_PARAM_EVENT.toString()))
				{
					param = deviceReader.readLine();
				}
				
				System.out.println("Func: " + taskId + " " + type + " " + engine + " " + file + " " + func + " " + param);
				
				if (taskId == null || file == null || func == null)
				{					
					break;
				}
				
				Task task = TaskManager.getTask(taskId);
				if (task == null)
				{   
					System.out.println("Attention : Task ID is null, so ditch sending this out. \n");
					continue;
				}
				
				String action = engine + ":" + file + "." + func;
				if (type.equals(EventType.CALL_PARAM_EVENT.toString()))
				{   System.out.println("Event call without parameters");
					Event_Call_Param event = new Event_Call_Param(device, action, param);
					task.getTaskContext().postEvent(event);
				}
				else
				{
					Event_Call_NoParam event = new Event_Call_NoParam(device, action);
					task.getTaskContext().postEvent(event);
				}
			}
		} catch (Exception e) {
			
		}
		
		try {
			
		}
		catch (Exception e) {
		}
	}
}
