package com.Android.CodeInTheAir.Device.Query;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.Android.CodeInTheAir.Global.AppContext;

public class System_User 
{
	public static void addUserPresentListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
	}
	
	public static void removeUserPresentListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
}
