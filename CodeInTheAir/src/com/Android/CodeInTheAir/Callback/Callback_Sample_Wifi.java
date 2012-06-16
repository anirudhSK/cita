package com.Android.CodeInTheAir.Callback;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_Wifi;
import com.Android.CodeInTheAir.Types.APInfo;
import com.Android.CodeInTheAir.Types.WifiInfo;

public class Callback_Sample_Wifi extends Callback_Sample_Generic 
{
	WifiReceiver wifiReceiver;
	
	/* Possible parameters */
	boolean bAssociatedSet;
	boolean bStrongestSet;
	boolean bAllSet;
	
	boolean bFilterSSIDsSet;
	String[] filterSSIDs;
	
	boolean bTopNSet;
	int topN;
	
	boolean bScanNSet;
	int scanN;
	
	public Callback_Sample_Wifi(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);		
		
		bAssociatedSet = false;
		bStrongestSet = false;
		bAllSet = true;
		bFilterSSIDsSet = false;
		bTopNSet = false;
		bScanNSet = false;
		
	}
	
	boolean setParam(String param, Object value)
	{		
		try
		{
			if (param.equalsIgnoreCase("filter"))
			{
				String filter = (String)value;
				if (filter.equalsIgnoreCase("associated"))
				{
					bAssociatedSet = true;
					bAllSet = false;
				}
				else if (filter.equalsIgnoreCase("strongest"))
				{
					bStrongestSet = true;
					bAllSet = false;
				}
				else if (filter.equalsIgnoreCase("all"))
					
				{
					bAllSet = true;
				}
				else
				{
					return false;
				}
			}
			else if (param.equalsIgnoreCase("ssid"))
			{								
				JSONArray ssidArray = (JSONArray)value;
				if (ssidArray.length() > 0)
				{
					bFilterSSIDsSet = true;
					filterSSIDs = new String[ssidArray.length()];
					for (int i = 0; i < ssidArray.length(); i++)
					{
						filterSSIDs[i] = ssidArray.getString(i);
					}
				}
			}
			else if (param.equalsIgnoreCase("top"))
			{
				bTopNSet = true;
				topN = (Integer)value;
			}			
			else if (param.equalsIgnoreCase("scan"))
			{
				scanN = (Integer)value;
				if (scanN != 0)
				{
					bScanNSet = true;
				}
			}
			else
			{
				if (super.setParam(param, value))
				{
					return true;
				}
				return false;
			}
		}
		catch (Exception e)
		{
			
		}
		
		return true;
	}
	
	public void start()
	{		
		wifiReceiver = new WifiReceiver();
		
		super.onlyTimeSample();
		super.start();
	}	
	
	void stop(StopCode stopCode)
	{
		super.stop(stopCode);
	}	

	
	void sampleTask()
	{
		if (bScanNSet)
		{
			if (currentSampleCount % scanN == 0)
			{
				Sensor_Wifi.addScanListener(wifiReceiver);
				boolean scanResult = Sensor_Wifi.scan();
				if (!scanResult)
				{
					Sensor_Wifi.removeScanListener(wifiReceiver);
					sample("failedScan");
					action();
				}
			}
		}
		sample("noScan");
		action();
	}
	
	
	class WifiReceiver extends BroadcastReceiver 
	{
	    public void onReceive(Context c, Intent intent) 
	    {
	    	Sensor_Wifi.removeScanListener(wifiReceiver);
	    	sample("scan");
	    	action();
	    }
	}
	
	void sample(String scan)
	{
		currentSampleCount++;
		JSONObject jResult = null;
		long time = System.currentTimeMillis();
		
		if (Sensor_Wifi.isEnabled())
		{
			List<APInfo> lstAPs = new ArrayList<APInfo>();
			if (bAssociatedSet)
			{
				APInfo apInfo = Sensor_Wifi.getAssociatedAP();	
				lstAPs.add(apInfo);
			}
			else if (bStrongestSet)
			{
				APInfo apInfo;
				if (bFilterSSIDsSet)
				{
					apInfo = Sensor_Wifi.getStrongestAP(filterSSIDs);				
				}
				else
				{
					apInfo = Sensor_Wifi.getStrongestAP();
				}
				lstAPs.add(apInfo);
			}
			else
			{
				if (bFilterSSIDsSet)
				{
					if (bTopNSet)
					{
						lstAPs = Sensor_Wifi.getAroundAPs(filterSSIDs, topN);
					}
					else
					{
						lstAPs = Sensor_Wifi.getAroundAPs(filterSSIDs);
					}
				}
				else
				{
					if (bTopNSet)
					{
						lstAPs = Sensor_Wifi.getAroundAPs(topN);
					}
					else
					{
						lstAPs = Sensor_Wifi.getAroundAPs();
					}
				}
				
			}
			WifiInfo wifiInfo = new WifiInfo(true, System.currentTimeMillis(), lstAPs, scan);
			jResult = encodeJSON(time, currentSampleCount, wifiInfo);
		}
		else
		{
			WifiInfo wifiInfo = new WifiInfo(false, System.currentTimeMillis(), null, null);
			jResult = encodeJSON(time, currentSampleCount, wifiInfo);
		}
		
		try
		{
			jResultArray.put(jResult);
		}
		catch (Exception e)
		{
			
		}
		
	}	
	
	private JSONObject encodeJSON(long time, int sample, WifiInfo wifiInfo)
	{
		JSONObject jResult = new JSONObject();
		
		try
		{
			jResult.put("wifi", wifiInfo.encodeJSON());
			
			jResult.put("sampleTime", time);
			jResult.put("sampleCount", sample);
		}
		catch (Exception e)
		{
			
		}
		
		return jResult;
	}
}
