package com.CITAJSServer.Clients;
import java.io.PrintWriter;
import java.net.Socket;

import com.CITAJSServer.Events.Constants.EventType;


public class DeviceSendHandler
{
	Socket socket;
	public DeviceSendHandler(Socket socket)
	{
		this.socket = socket;
	}
	
	public String sendCommand(String taskId, EventType type, String file, String func, String params)
	{		
		PrintWriter deviceWriter;	
		System.out.printf("In sendCOmmand \n");

		if (socket == null)
		{
			return "Phone not connected yet";		
		}
		
		try
		{
			deviceWriter = new PrintWriter(socket.getOutputStream(), true);			
		}
		catch (Exception e)
		{
			return "Error creating stream";			
		}		
		try
		{
			deviceWriter.println(taskId);
			deviceWriter.println(type.toString());
			deviceWriter.println(file);
			deviceWriter.println(func);
			
			if (type == EventType.CALL_PARAM_EVENT)
			{
				System.out.printf("Printing out parameters %s \n", params);
				deviceWriter.println(params);
			}
			
			return "Sent";
		}
		catch (Exception e)
		{
			return "Error sending command to phone";
		}

	}
}
