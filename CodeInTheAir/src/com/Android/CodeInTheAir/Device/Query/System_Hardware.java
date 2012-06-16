package com.Android.CodeInTheAir.Device.Query;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.Android.CodeInTheAir.Global.AppContext;

public class System_Hardware 
{
	public static void addHeadsetPlugListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
	}
	
	public static void removeHeadsetPlugListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
}
