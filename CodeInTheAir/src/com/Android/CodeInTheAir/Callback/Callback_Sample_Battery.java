package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.System_Battery;
import com.Android.CodeInTheAir.Types.BatteryInfo;

public class Callback_Sample_Battery extends Callback_Sample_Generic 
{	
	BatteryReceiver batteryReceiver;
	
	
	public Callback_Sample_Battery(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);				
	}
		
	boolean setParam(String param, Object value)
	{
		if (super.setParam(param, value))
		{
			return true;
		}
		return false;
	}
	
	
	public void start()
	{
		batteryReceiver = new BatteryReceiver();		
		System_Battery.addBatteryChangeListener(batteryReceiver);		
		super.start();
	}
	
	void stop(StopCode stopCode)
	{
		System_Battery.removeBatteryChangeListener(batteryReceiver);		
		super.stop(stopCode);
	}
	
	void sampleTask()
	{
		BatteryInfo batteryInfo = System_Battery.getBatteryInfo();
		sample("time", batteryInfo);
		action();
	}
	
	private class BatteryReceiver extends BroadcastReceiver 
    {		
      	public void onReceive(Context c, Intent intent) 
	    {
      		boolean isPresent = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
    		int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            
            int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            int current = System_Battery.getCurrent();
            int charge = System_Battery.getChargeCounter();
            
            
            int iStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);            
            String status = "Unknown";
            switch (iStatus)
            {
            case BatteryManager.BATTERY_STATUS_CHARGING:
            	status = "Charging";
            	break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
            	status = "Discharging";
            	break;
            case BatteryManager.BATTERY_STATUS_FULL:
            	status = "Full";
            	break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
            	status = "NotCharging";
            	break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
            	status = "Unknown";
            	break;
            }
            
            int iPlugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            String plugged = "none";
            switch (iPlugged)
            {
            case BatteryManager.BATTERY_PLUGGED_AC:
            	plugged = "ac";
            	break;
            case BatteryManager.BATTERY_PLUGGED_USB:
            	plugged = "usb";
            	break;            
            }
            
            int iHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            String health = "Good";
            switch (iHealth)
            {
            case BatteryManager.BATTERY_HEALTH_GOOD:
            	health = "Good";
            	break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
            	health = "Dead";
            	break;            
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
            	health = "OverVoltage";
            	break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
            	health = "Overheat";
            	break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
            	health = "unknown";
            	break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
            	health = "failure";
            	break;
            }
            
            BatteryInfo batteryInfo = new BatteryInfo(isPresent,
            		System.currentTimeMillis(), level, scale,
            		temp, voltage, current, charge, plugged, status, health);
 
          	if (bEventSampleSet)
        	{
        		sample("event", batteryInfo);
        		action();
        	}
	    }
    }
    
	
	
	private void sample(String sampleType, BatteryInfo batteryInfo)
	{
		currentSampleCount++;
		JSONObject jResult = null;
		long time = System.currentTimeMillis();	

		jResult = encodeJSON(time, currentSampleCount, sampleType, batteryInfo);

		
		try
		{
			jResultArray.put(jResult);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private JSONObject encodeJSON(long time, int sample, String sampleType, BatteryInfo batteryInfo)
	{
		JSONObject jResult = new JSONObject();
		
		try
		{
			jResult.put("battery", batteryInfo.encodeJSON());
			
			jResult.put("sampleTime", time);
			jResult.put("sampleCount", sample);
			jResult.put("sampleType", sampleType);
		}
		catch (Exception e)
		{
			
		}
		
		return jResult;
	}
}
