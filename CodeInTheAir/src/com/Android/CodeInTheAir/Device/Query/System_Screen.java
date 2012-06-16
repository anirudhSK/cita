package com.Android.CodeInTheAir.Device.Query;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.Android.CodeInTheAir.Global.AppContext;

public class System_Screen 
{
	private static  Display display;
	private static PowerManager powerManager;
	
	public static void init()
	{
		display = ((WindowManager) AppContext.context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();  
		powerManager = ((PowerManager) AppContext.context.getSystemService(Context.POWER_SERVICE));
	}
	
	public static int getOrientation()
	{
		return display.getOrientation();
	}
	
	public static int getRotation()
	{
		return display.getRotation();
	}
	
	public static int getWidth()
	{
		return display.getWidth();
	}
	
	public static int getHeight()
	{
		return display.getHeight();
	}
	
	public static int getPixelFormat()
	{
		return display.getPixelFormat();
	}
	
	public static float getRefreshRate()
	{
		return display.getRefreshRate();
	}
	
	public static DisplayMetrics getDisplayMetrics()
	{
		DisplayMetrics metrics = new DisplayMetrics(); 
		display.getMetrics(metrics);
		return metrics;
	}
	
	public static boolean isScreenOn()
	{
		return powerManager.isScreenOn();
	}
	
	public static void addScreenOffListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}
	
	public static void removeScreenOffListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	public static void addScreenOnListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
	}
	
	public static void removeScreenOnListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
}
