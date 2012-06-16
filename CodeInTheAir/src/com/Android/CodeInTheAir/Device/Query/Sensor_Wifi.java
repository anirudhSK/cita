package com.Android.CodeInTheAir.Device.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.Android.CodeInTheAir.Global.AppContext;
import com.Android.CodeInTheAir.Types.APInfo;

public class Sensor_Wifi 
{
	private static WifiManager wifiManager;
	
	public static void init()
	{
		wifiManager = (WifiManager)AppContext.context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public static APInfo getAssociatedAP()
	{
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		APInfo apInfo = new APInfo();
		
		if (wifiManager.isWifiEnabled())
		{
			apInfo = new APInfo(wifiInfo.getBSSID(), wifiInfo.getSSID(), wifiInfo.getRssi(), 0, null, true);			
		}
		
		return apInfo;
	}
	
	public static APInfo getStrongestAP()
	{
		List<APInfo> arrAPInfo = getAroundAPs();
		if (arrAPInfo.size() > 0)
		{
			return arrAPInfo.get(0);
		}
		else
		{
			return new APInfo();
		}
	}

	
	public static APInfo getStrongestAP(String[] filterSSIDs)
	{
		List<APInfo> arrAPInfo = getAroundAPs(filterSSIDs);
		if (arrAPInfo.size() > 0)
		{
			return arrAPInfo.get(0);
		}
		else
		{
			return new APInfo();
		}
	}
	
	public static String[] getSSIDs()
	{
		List<APInfo> arrAPInfo = getAroundAPs();
		HashSet<String> hSSIDs = new HashSet<String>();
		
		for (int i = 0; i < arrAPInfo.size(); i++)
		{
			if (!hSSIDs.contains(arrAPInfo.get(i).ssid))
			{
				hSSIDs.add(arrAPInfo.get(i).ssid);
			}
		}
		
		String[] strSSIDs = hSSIDs.toArray(new String[hSSIDs.size()]);
		return strSSIDs;
	}

	
	public static List<APInfo> getAroundAPs()
	{
		List<APInfo> lstAPInfo = new ArrayList<APInfo>();
		List<ScanResult> aps = wifiManager.getScanResults();
		
		if (aps == null)
		{
			return lstAPInfo;
		}
		
		
		for (int i = 0; i < aps.size(); i++)
		{
			APInfo apInfo = new APInfo(aps.get(i).BSSID, aps.get(i).SSID, aps.get(i).level, aps.get(i).frequency, aps.get(i).capabilities, false);			
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			if (wifiInfo != null)
			{
				if (apInfo.bssid.equals(wifiInfo.getBSSID()))
				{
					apInfo.associated = true;
				}
			}
			
			lstAPInfo.add(apInfo);
		}
		
		APInfo[] arrAPInfo = (APInfo[])lstAPInfo.toArray(new APInfo[lstAPInfo.size()]);
		arrAPInfo = getSortedAPs(arrAPInfo);
		
		lstAPInfo = new ArrayList<APInfo>();
		for (int i = 0; i < arrAPInfo.length; i++)
		{
			lstAPInfo.add(arrAPInfo[i]);
		}
		
		return lstAPInfo;
	}
	
	public static List<APInfo> getAroundAPs(String[] filterSSIDs)
	{
		List<APInfo> lstAPInfo = getAroundAPs();
		List<APInfo> truncLstAPInfo = new ArrayList<APInfo>();
		for (int i = 0; i < lstAPInfo.size(); i++)
		{
			for (int j = 0; j < filterSSIDs.length; j++)
			{
				if (lstAPInfo.get(i).ssid.equals(filterSSIDs[j]))
				{
					truncLstAPInfo.add(lstAPInfo.get(i));
				}
			}
		}
		
		
		return truncLstAPInfo;
	}
	
	
	public static List<APInfo> getAroundAPs(int topN)
	{
		List<APInfo> lstAPInfo = getAroundAPs();
		List<APInfo> truncLstAPInfo = new ArrayList<APInfo>();
		for (int i = 0; i < lstAPInfo.size(); i++)
		{
			if (topN > 0)
			{
				if (i < topN)
				{
					truncLstAPInfo.add(lstAPInfo.get(i));
				}
			}
			else
			{
				if (lstAPInfo.size() - i <= -topN)
				{
					truncLstAPInfo.add(lstAPInfo.get(i));
				}
			}
		}
		
		
		return truncLstAPInfo;
	}

	
	public static List<APInfo> getAroundAPs(String[] filterSSIDs, int topN)
	{
		List<APInfo> lstAPInfo = getAroundAPs(filterSSIDs);
		List<APInfo> truncLstAPInfo = new ArrayList<APInfo>();
		for (int i = 0; i < lstAPInfo.size(); i++)
		{
			if (topN > 0)
			{
				if (i < topN)
				{
					truncLstAPInfo.add(lstAPInfo.get(i));
				}
			}
			else
			{
				if (lstAPInfo.size() - i <= -topN)
				{
					truncLstAPInfo.add(lstAPInfo.get(i));
				}
			}
		}
		
		
		return truncLstAPInfo;
	}
	
	public static boolean isAssociated()
	{
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		
		if (wifiInfo == null)
		{
			return false;
		}
		
		return (wifiInfo.getBSSID() == null);
	}
	
	public static boolean isEnabled()
	{
		return wifiManager.isWifiEnabled();		
	}
	
	
	public static int getState()
	{
		return wifiManager.getWifiState();
	}
	
	public static boolean scan()
	{
		return wifiManager.startScan();
	}
	
	public static boolean enable()
	{
		return wifiManager.setWifiEnabled(true);
	}

	public static boolean disable()
	{
		return wifiManager.setWifiEnabled(false);
	}
	
	public static boolean disconnect()
	{
		return wifiManager.disconnect();
	}
	
	public static boolean reconnect()
	{
		return wifiManager.reconnect();
	}
	
	public static boolean reassociate()
	{
		return wifiManager.reassociate();		
	}

	 
	/*
	 * private methods
	 */
	
	private static APInfo[] getSortedAPs(APInfo[] apInfo)
	{
		for (int i = 0; i < apInfo.length - 1; i++)
		{
			for (int j = i+1; j < apInfo.length; j++)
			{
				if (apInfo[i].rssi < apInfo[j].rssi)
				{
					APInfo tAPInfo = apInfo[i];
					apInfo[i] = apInfo[j];
					apInfo[j] = tAPInfo;
				}
			}
		}
		return apInfo;
	}
	

	/* Listeners and Events */
	/* State Listeners */
	public static void addStateListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
	}
	
	public static void removeStateListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	/* Scan Listeners */
	public static void addScanListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}
	
	public static void removeScanListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
		
	/* RSSI Changed Listeners */
	public static void addRSSIChangeListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
	}
	
	public static void removeRSSIChangeListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
}
