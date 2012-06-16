package com.Android.CodeInTheAir.ShellClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import android.os.Looper;
import android.util.Log;

import com.Android.CodeInTheAir.Global.AppContext;

public class ShellClientInterface extends Thread
{
	Socket socket;
	BufferedReader serverReader;
	PrintWriter serverWriter;
	private final ReentrantLock lock = new ReentrantLock();
	
	public void run()
	{	
		Looper.prepare();
		boolean connected = false;
		while (true)
		{			
			try
			{
				socket = new Socket();				
				InetSocketAddress socketAddress = new InetSocketAddress(ShellClientSettings.CITAServer, ShellClientSettings.CITAPort);
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
			String strTask = serverReader.readLine();
			String strSession = serverReader.readLine();
			String strCommand = serverReader.readLine();
			
			ShellClientHandler handler = ShellClientComponents.shellClientManager.getHandler(strTask);
			handler.execute(strSession, strCommand);			
		}
	}


	public void sendResponse(String strTask, String strSession, String strResponse)
	{
		lock.lock();
		serverWriter.println(strTask);
		serverWriter.println(strSession);
		serverWriter.println(strResponse);
		lock.unlock();
	}
}
