package com.Android.ServerClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import android.util.Log;

import com.Android.CodeInTheAir.Events.Constants;
import com.Android.CodeInTheAir.Events.Constants.EventType;
import com.Android.CodeInTheAir.Events.Event_Call_NoParam;
import com.Android.CodeInTheAir.Events.Event_Call_Param;
import com.Android.CodeInTheAir.Events.Task;
import com.Android.CodeInTheAir.Events.TaskManager;
import com.Android.CodeInTheAir.Global.AppContext;

public class ServerClientInterface extends Thread
{
	Socket socket;
	BufferedReader serverReader;
	PrintWriter serverWriter;
	private final ReentrantLock lock = new ReentrantLock();
	
	public void run()
	{	
		boolean connected = false;
		while (true)
		{			
			try
			{
				socket = new Socket();				
				InetSocketAddress socketAddress = new InetSocketAddress(ServerClientSettings.CITAServer, ServerClientSettings.CITAPort);
				socket.connect(socketAddress, 500);		
				connected = true;
			}
			catch (Exception e)
			{
				connected = false;
			}
			
			if (connected)
			{
				try
				{
					serverReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
					serverWriter = new PrintWriter(socket.getOutputStream(), true);
				}
				catch (Exception e)
				{
					connected = false;
				}
			}
			
			if (connected)
			{
				try
				{
					serverWriter.println(AppContext.DeviceName);
					handleCommands();
				}
				catch (Exception e)
				{
					Log.v("CITA:CITA", "message : " + e.getMessage());
					connected = false;
				}
			}
			
			try
			{
				socket.close();
			}
			catch (Exception e)
			{
				
			}
			
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception e)
			{
				
			}
		}
	}
	
	
	private void handleCommands() throws Exception
	{				
		while (true)
		{				
			String taskId = serverReader.readLine();
			String type = serverReader.readLine();
			String file = serverReader.readLine();
			String func = serverReader.readLine();
			String param = null;
			if (type.equals(EventType.CALL_PARAM_EVENT.toString()))
			{
				param = serverReader.readLine();
			}
			
			Task task = TaskManager.getTask(taskId);
			if (task == null)
			{
				continue;
			}
			
			String action = Constants.LocalJS + ":" + file + "." + func;
			if (type.equals(EventType.CALL_PARAM_EVENT.toString()))
			{
				Event_Call_Param event = new Event_Call_Param("server", action, param);
				task.getTaskContext().postEvent(event);
			}
			else
			{
				Event_Call_NoParam event = new Event_Call_NoParam("server", action);
				task.getTaskContext().postEvent(event);
			}
		}
	}


	public void sendCommand(String engine, String taskId, EventType type, String file, String func, String params)
	{
		lock.lock();
		serverWriter.println(taskId);
		serverWriter.println(type.toString());
		serverWriter.println(engine);
		serverWriter.println(file);
		serverWriter.println(func);
		
		if (type == EventType.CALL_PARAM_EVENT)
		{
			serverWriter.println(params);
		}
		
		lock.unlock();
	}
}
