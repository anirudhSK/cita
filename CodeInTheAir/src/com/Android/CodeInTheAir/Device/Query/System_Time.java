package com.Android.CodeInTheAir.Device.Query;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.Android.CodeInTheAir.Global.AppContext;

public class System_Time 
{
	public static long getTime()
	{
		return System.currentTimeMillis();		
	}
	
	public static void addTimeChangeListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_CHANGED));
	}
	
	public static void removeTimeChangeListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	
	public static void addTimeZoneChangeListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED));
	}
	
	public static void removeTimeZoneChangeListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	public static void addTimeTickListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
	}
	
	public static void removeTimeTickListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	public static void addDateChangeListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_DATE_CHANGED));
	}
	
	public static void removeDateChangeListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
}
