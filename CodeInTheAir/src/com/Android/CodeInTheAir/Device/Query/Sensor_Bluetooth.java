package com.Android.CodeInTheAir.Device.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import com.Android.CodeInTheAir.Global.AppContext;
import com.Android.CodeInTheAir.Types.BTDeviceInfo;

public class Sensor_Bluetooth 
{
	private static BluetoothAdapter bluetoothAdapter;
	
	public static void init()
	{
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	public static boolean startDiscovery()
	{
		return bluetoothAdapter.startDiscovery();
	}
	
	public static List<BTDeviceInfo> getBondedDevices()
	{
		List<BTDeviceInfo> lstBTInfo = new ArrayList<BTDeviceInfo>();
		
		Iterator<BluetoothDevice> i = bluetoothAdapter.getBondedDevices().iterator();
		while (i.hasNext())
		{
			BluetoothDevice bd = i.next();
			BTDeviceInfo btInfo = new BTDeviceInfo(bd.getName(), bd.getAddress(), bd.getBondState());
			lstBTInfo.add(btInfo);
		}
		return lstBTInfo;
	}
	
	public static boolean cancelDiscovery()
	{
		return bluetoothAdapter.cancelDiscovery();
	}
	
	public static boolean isDiscovering()
	{
		return bluetoothAdapter.isDiscovering();
	}
	
	public static boolean isEnabled()
	{
		return bluetoothAdapter.isEnabled();
	}
	
	public static boolean enable()
	{
		return bluetoothAdapter.enable();
	}
	
	public static boolean disable()
	{
		return bluetoothAdapter.disable();		
	}
	
	/* Listeners and Events */
	
	public static void addStateListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
	}
	
	public static void removeStateListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	public static void addDiscoveryListener(BroadcastReceiver receiver)
	{
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        AppContext.context.registerReceiver(receiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        AppContext.context.registerReceiver(receiver, filter);
        
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        AppContext.context.registerReceiver(receiver, filter);
	}
	
	public static void removeDiscoveryListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
}
