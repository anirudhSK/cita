package com.CITAJSServer.Clients;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.CITAJSServer.Events.Constants.EventType;


public class DeviceInterface 
{
	private HashMap<String, DeviceSendHandler> hDeviceSendHandler;	
	private ServerSocket serverSocket;
	private int port;
	
	public DeviceInterface(int port)
	{
		this.port = port;		
		hDeviceSendHandler = new HashMap<String, DeviceSendHandler>();
	}
	
	public void start()
	{
		try
		{
			serverSocket = new ServerSocket(port);
			System.out.println("Server waiting for device");
			
			while (true)
			{
				Socket socket = serverSocket.accept();
				System.out.println("New device connected");			
				
				BufferedReader deviceReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
				
				String strDevice = deviceReader.readLine();				
				System.out.println("Device name - " + strDevice);				
				
				DeviceSendHandler sendHandler = new DeviceSendHandler(socket);				
				hDeviceSendHandler.put(strDevice, sendHandler);
				
				DeviceReceiveHandler receiveHandler = new DeviceReceiveHandler(socket, strDevice);
				receiveHandler.start();
			}
		}
		catch (IOException e)
		{
			System.out.println("Exception " + e.getMessage());
		}
	}
	
	public DeviceSendHandler getSendHandler(String device)
	{
		if (hDeviceSendHandler.containsKey(device))
		{
			return hDeviceSendHandler.get(device);
		}
		return null;
	}
	
	public void sendCommand(String client, String taskId, EventType type, String file, String func, String params)
	{
		DeviceSendHandler sendHandler = getSendHandler(client);
		sendHandler.sendCommand(taskId, type, file, func, params);
	}
}
