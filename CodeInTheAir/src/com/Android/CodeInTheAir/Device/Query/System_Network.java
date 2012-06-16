package com.Android.CodeInTheAir.Device.Query;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.Android.CodeInTheAir.Global.AppContext;

public class System_Network 
{
	private static TelephonyManager telephonyManager;
	private static ConnectivityManager connectivityManager;
	private static WifiManager wifiManager;
	
	public static void init()
	{
		telephonyManager = (TelephonyManager)AppContext.context.getSystemService(Context.TELEPHONY_SERVICE);
		connectivityManager = (ConnectivityManager)AppContext.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiManager = (WifiManager)AppContext.context.getSystemService(Context.WIFI_SERVICE);
	}
	
	
	public static int getCellNetworkType()
	{
		return telephonyManager.getNetworkType();
	}
	
	public static int getCellDataActivity()
	{
		return telephonyManager.getDataActivity();
	}
	
	public static int getCellDataState()
	{
		return telephonyManager.getDataState();
	}
	
	public static int getSimtate()
	{
		return telephonyManager.getSimState();
	}
	
	public static boolean isCellNetworkRoaming()
	{
		return telephonyManager.isNetworkRoaming();
	}
	
	public static NetworkInfo getCellNetworkInfo()
	{
		return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	}
	
	public static String getCellIPAddress()
	{
		try 
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) 
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) 
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) 
					{
						if (!inetAddress.getHostAddress().toString().equals(getWifiIPAddress()))
						{
							return inetAddress.getHostAddress().toString();
		                }
					}
		        }
			}
		} catch (SocketException ex) 
		{
			return null;
		}
		return null;
	}
	
	public static NetworkInfo getWifiNetworkInfo()
	{
		return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	}
	
	public static NetworkInfo[] getAllNetworkInfo()
	{
		return connectivityManager.getAllNetworkInfo();
	}
	
	public static int getWifiNetworkId()
	{
		if (wifiManager.isWifiEnabled() == false ||
				wifiManager.getConnectionInfo() == null || 
				wifiManager.getConnectionInfo().getBSSID() == null)
		{
			return -1;
		}
		return wifiManager.getConnectionInfo().getNetworkId();
	}
	
	public static int getWifiLinkSpeed()
	{
		if (wifiManager.isWifiEnabled() == false ||
				wifiManager.getConnectionInfo() == null || 
				wifiManager.getConnectionInfo().getBSSID() == null)
		{
			return -1;
		}
		return wifiManager.getConnectionInfo().getLinkSpeed();
	}
	
	public static String getWifiIPAddress()
	{
		if (wifiManager.isWifiEnabled() == false ||
				wifiManager.getConnectionInfo() == null || 
				wifiManager.getConnectionInfo().getBSSID() == null)
		{
			return null;
		}
		
		DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
		if (dhcpInfo == null)
		{
			return null;
		}
		
		int ipAddress = dhcpInfo.ipAddress;
		return convertIPAddress(ipAddress);
	}
	
	public static DhcpInfo getWifiDhcpInfo()
	{
		return wifiManager.getDhcpInfo();
	}
	
	public static int getWifiState()
	{
		return wifiManager.getWifiState();
	}
	
	public static String getWifiIPPrefix()
	{
		if (wifiManager.isWifiEnabled() == false ||
				wifiManager.getConnectionInfo() == null || 
				wifiManager.getConnectionInfo().getBSSID() == null)
		{
			return null;
		}
		
		DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
		if (dhcpInfo == null)
		{
			return null;
		}
		
		int ipPrefix = dhcpInfo.ipAddress & dhcpInfo.netmask;
		return convertIPAddress(ipPrefix);
	}
	
	private static String convertIPAddress(int ipAddress)
	{
		byte[] quads = new byte[4];
		
	    for (int k = 0; k < 4; k++)
	    {
	    	quads[k] = (byte) ((ipAddress >> k * 8) & 0xFF);
	    }
	    String strIPPrefix = null;
	    try
	    {
	    	strIPPrefix = InetAddress.getByAddress(quads).getHostAddress();
	    }
	    catch (Exception e)
	    {
	    	
	    }
	    return strIPPrefix;
	}
	

	
	
	/* Listeners and Events */
	public static void addConnectivityListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}
	
	public static void removeConnectivityListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	public static void addWifiNetworkStateListener(BroadcastReceiver receiver)
	{
		AppContext.context.registerReceiver(receiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
	}
	
	public static void removeWifiNetworkStateListener(BroadcastReceiver receiver)
	{
		AppContext.context.unregisterReceiver(receiver);
	}
	
	
	public static void addCellDataStateListener(PhoneStateListener psListener)
	{
		telephonyManager.listen(psListener, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
	}
	
	public static void removeCellDataStateListener(PhoneStateListener psListener)
	{
		telephonyManager.listen(psListener, PhoneStateListener.LISTEN_NONE);
	}
	
	public static void addCellDataActivityListener(PhoneStateListener psListener)
	{
		telephonyManager.listen(psListener, PhoneStateListener.LISTEN_DATA_ACTIVITY);
	}
	
	public static void removeCellDataActivityListener(PhoneStateListener psListener)
	{
		telephonyManager.listen(psListener, PhoneStateListener.LISTEN_NONE);
	}
}
