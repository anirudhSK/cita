package com.Android.CodeInTheAir.Device.Query;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.Android.CodeInTheAir.Global.AppContext;

public class System_Identity 
{
	private static TelephonyManager telephonyManager;
	private static WifiManager wifiManager;
	
	public static void init()
	{
		wifiManager = (WifiManager)AppContext.context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public static String getWifiMacAddress()
	{
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo == null)
		{
			return null;
		}
		return wifiInfo.getMacAddress();
	}
	
	public static String getBluetoothAddress()
	{
		return BluetoothAdapter.getDefaultAdapter().getAddress();
	}
	
	public static String getBluetoothName()
	{
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null)
		{
			return null;
		}
		return btAdapter.getName();
	}

	public static String getDeviceId()
	{
		return telephonyManager.getDeviceId();
	}
}
