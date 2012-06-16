package com.Android.CodeInTheAir.Callback;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_Bluetooth;
import com.Android.CodeInTheAir.Types.BTDeviceInfo;
import com.Android.CodeInTheAir.Types.BTInfo;

public class Callback_Sample_Bluetooth extends Callback_Sample_Generic 
{
	BTReceiver btReceiver;
	
	boolean btEnabled;
	boolean isDiscovering;
	
	/* Possible parameters */
	boolean bBondedSet;	
	boolean bAllSet;
	
	List<BTDeviceInfo> lstBT;
	BTInfo currentBTInfo;
	
	public Callback_Sample_Bluetooth(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);				
		
		bBondedSet = false;
		bAllSet = true;
	}
		
	public boolean setParam(String param, Object value)
	{
		try
		{
			if (param.equalsIgnoreCase("filter"))
			{
				String filter = (String)value;
				if (filter.equalsIgnoreCase("bonded"))
				{
					bBondedSet = true;
					bAllSet = false;
				}
				else if (filter.equalsIgnoreCase("all"))					
				{
					bAllSet = true;
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
			else
			{
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
		btReceiver = new BTReceiver();		
		currentBTInfo = new BTInfo();
		
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		super.stop(stopCode);
	}
	
	void sampleTask()
	{
		if (!bBondedSet)
		{
			if (Sensor_Bluetooth.isDiscovering())
			{
				isDiscovering = true;
				sample("noScan");
				action();
			}
			else
			{
				isDiscovering = false;
				boolean result = Sensor_Bluetooth.startDiscovery();
				if (!result)
				{
					sample("failedScan");
					action();
				}
			}
		}
		else
		{
			sample("noScan");						
		}
	}
	
	class BTReceiver extends BroadcastReceiver 
	{
	    public void onReceive(Context c, Intent intent) 
	    {
	    	String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) 
            {                
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTDeviceInfo bluetoothInfo = new BTDeviceInfo(device.getName(), device.getAddress(), device.getBondState());
                
                if (lstBT == null)
                {
                	lstBT = new ArrayList<BTDeviceInfo>();                	
                }
                lstBT.add(bluetoothInfo);
            } 
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) 
            {            	
            	Sensor_Bluetooth.cancelDiscovery();
                sample("scan");
                action();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) 
            {
                lstBT = new ArrayList<BTDeviceInfo>();
            }
	    }
	}
	
	private void sample(String scan)
	{
		currentSampleCount++;
		JSONObject jResult = null;
		long time = System.currentTimeMillis();
		
		if (Sensor_Bluetooth.isEnabled())
		{	
			if (bBondedSet)
			{
				List<BTDeviceInfo> btBonded = Sensor_Bluetooth.getBondedDevices();
				BTInfo btInfo = new BTInfo(true, System.currentTimeMillis(), btBonded, isDiscovering, scan);
				jResult = encodeJSON(time, currentSampleCount, btInfo);
			}
			else
			{
				currentBTInfo = new BTInfo(true, System.currentTimeMillis(), lstBT, false, scan);
				jResult = encodeJSON(time, currentSampleCount, currentBTInfo);
			}
		}
		else
		{
			BTInfo btInfo = new BTInfo(false, System.currentTimeMillis(), null, isDiscovering, null);
			jResult = encodeJSON(time, currentSampleCount, btInfo);
		}
		
		try
		{
			jResultArray.put(jResult);
		}
		catch (Exception e)
		{
			
		}
		
	}	
	
	private JSONObject encodeJSON(long time, int sample, BTInfo btInfo)
	{
		JSONObject jResult = new JSONObject();
		
		try
		{
			jResult.put("bt", btInfo.encodeJSON());
			
			jResult.put("sampleTime", time);			
			jResult.put("sampleCount", sample);
		}
		catch (Exception e)
		{
			
		}
		
		return jResult;
	}	
}
