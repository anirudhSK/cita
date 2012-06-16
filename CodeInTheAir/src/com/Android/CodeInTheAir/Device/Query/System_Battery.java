package com.Android.CodeInTheAir.Device.Query;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.Android.CodeInTheAir.Global.AppContext;
import com.Android.CodeInTheAir.Types.BatteryInfo;

public class System_Battery 
{
	
	public static BatteryInfo getBatteryInfo()
	{
		BatteryInfo batteryInfo = new BatteryInfo(
				isPresent(),
				System.currentTimeMillis(), 
				getLevel(), 
				-1,
				getTemp(),
				getVoltage(),
				getCurrent(),
				getChargeCounter(),
				getPlugged(),
				getStatus(),
				getHealth());
		return batteryInfo;
	}
	
	public static int getChargeCounter()
	{
		String value = readLine("/sys/class/power_supply/battery/charge_counter");
		if (value == null)
		{
			return -1;
		}
	
        return Integer.parseInt(value);
	}
	
	public static int getLevel()
	{
		String value = readLine("/sys/class/power_supply/battery/capacity");
		if (value == null)
		{
			return -1;
		}
	
        return Integer.parseInt(value);
	}
	
	public static int getVoltage()
	{
		String value = readLine("/sys/class/power_supply/battery/voltage_now");
		if (value == null)
		{
			return -1;
		}
	
        return Integer.parseInt(value);
	}
	
	public static int getCurrent()
	{
		String value = readLine("/sys/class/power_supply/battery/current_now");
		if (value == null)
		{
			return -1;
		}
	
        return Integer.parseInt(value);
	}
	
	public static int getTemp()
	{
		String value = readLine("/sys/class/power_supply/battery/temp");
		if (value == null)
		{
			return -1;
		}
	
        return Integer.parseInt(value);
	}
	
	public static boolean isPresent()
	{
		String value = readLine("/sys/class/power_supply/battery/present");
		if (value == null)
		{
			return false;
		}
	
        int present = Integer.parseInt(value);
        if (present == 1)
        {
        	return true;
        }
        else
        {
        	return false;
        }
	}
	
	public static String getStatus()
	{
		String value = readLine("/sys/class/power_supply/battery/status");
		return value;
	}
	
	public static String getHealth()
	{
		String value = readLine("/sys/class/power_supply/battery/health");
		return value;
	}
	
	public static String getPlugged()
	{
		String value = readLine("/sys/class/power_supply/ac/online");
		if (value != null)
		{
			if (value.equals("1"))
			{
				return "ac";
			}
		}		
		
		value = readLine("/sys/class/power_supply/usb/online");
		if (value != null)
		{
			if (value.equals("1"))
			{
				return "usb";
			}
		}
		return "none";
	}
	
	private static String readLine(String file)
	{
		String value = null;
		try
		{
			File f = new File(file);
			if (!f.exists())
			{
				return null;
			}
			FileInputStream fs = new FileInputStream(f);            
	        DataInputStream ds = new DataInputStream(fs);
	
	        value = ds.readLine();
	        
	        ds.close();             
	        fs.close();
		}
		catch (Exception e)
		{
			
		}
        return value;		
	}
	
	
	/* Listeners and Events */
	public static void addBatteryChangeListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
	
	public static void removeBatteryChangeListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	public static void addBatteryLowListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
	}
	
	public static void removeBatteryLowListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	public static void addBatteryOkayListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_OKAY));
	}
	
	public static void removeBatteryOkayListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	public static void addPowerConnectedListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
	}
	
	public static void removePowerConnectedListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	public static void addPowerDisconnectedListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
	}
	
	public static void removePowerDisconnectedListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
}
