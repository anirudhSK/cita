package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

import com.Android.CodeInTheAir.Callback.Callback_Constants.ResultCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_GSM;

public class Callback_GSM_RSSIChange extends Callback_Generic
{
	public Callback_GSM_RSSIChange(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);
	}
	
	public void start()
	{
		Sensor_GSM.addRSSIChangeListener(psListener);
		super.start();
	}
	
	public void stop()
	{
		Sensor_GSM.removeRSSIChangeListener(psListener);
		super.stop();
	}
	
	private PhoneStateListener psListener = new PhoneStateListener()
	{
		public void onSignalStrengthsChanged(SignalStrength ss)
		{
			JSONObject jResult = new JSONObject();
			if (ss.isGsm())
			{
				try
				{
					jResult.put("rssi", ss.getGsmSignalStrength());
					jResult.put("ber", ss.getGsmBitErrorRate());
				}
				catch (Exception e)
				{
					
				}
			}
			
			event(ResultCode.OK, "JSONObject", jResult.toString());
		}
		
		public void onCellLocationChanged(CellLocation location)
		{
			
		}
	};
}
