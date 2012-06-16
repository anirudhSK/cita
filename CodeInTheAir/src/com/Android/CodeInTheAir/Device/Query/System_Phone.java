package com.Android.CodeInTheAir.Device.Query;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;

import com.Android.CodeInTheAir.Global.AppContext;

public class System_Phone 
{
	private static TelephonyManager telephonyManager;
	
	public static void init()
	{
		telephonyManager = (TelephonyManager)AppContext.context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public static String getDeviceId()
	{
		return telephonyManager.getDeviceId();
	}
	
	public static String getDeviceSoftwareVersion()
	{
		return telephonyManager.getDeviceSoftwareVersion();
	}
	
	public static String getPhoneNumber()
	{
		return telephonyManager.getLine1Number();
	}
	
	public static int getPhoneType()
	{
		return telephonyManager.getPhoneType();
	}
	
	public static String getSimSerialNumber()
	{
		return telephonyManager.getSimSerialNumber();
	}
	
	public static String getSubscriberId()
	{
		return telephonyManager.getSubscriberId();
	}
	
	public static String getNetworkOperatorName()
	{
		return telephonyManager.getNetworkOperatorName();
	}
	
	public static String getNetworkOperator()
	{
		return telephonyManager.getNetworkOperator();
	}
	
	public static String getNetworkOperatorIso()
	{
		return telephonyManager.getNetworkCountryIso();
	}
	
	public static String getSimOperatorName()
	{
		return telephonyManager.getSimOperatorName();
	}
	
	public static String getSimOperator()
	{
		return telephonyManager.getSimOperator();
	}
	
	public static String getSimOperatorIso()
	{
		return telephonyManager.getSimCountryIso();
	}
	
	public static void addAirplaneModeListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
	}
	
	public static void removeAirplaneModeListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
}
