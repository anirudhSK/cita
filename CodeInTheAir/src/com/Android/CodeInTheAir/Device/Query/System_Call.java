package com.Android.CodeInTheAir.Device.Query;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.Android.CodeInTheAir.Global.AppContext;

public class System_Call 
{
	private static TelephonyManager telephonyManager;
	
	public static void init()
	{
		telephonyManager = (TelephonyManager)AppContext.context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public static int getCallState()
	{
		return telephonyManager.getCallState();
	}
	
	public static void addCallStateListener(PhoneStateListener psListener)
	{
		telephonyManager.listen(psListener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	public static void removeCallStateListener(PhoneStateListener psListener)
	{
		telephonyManager.listen(psListener, PhoneStateListener.LISTEN_NONE);
	}
	
	public static void addPhoneStateListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));		
	}
	
	public static void removePhoneStateListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	public static void addOutgoingCallListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL));		
	}
	
	public static void removeOutgoingCallListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
}
