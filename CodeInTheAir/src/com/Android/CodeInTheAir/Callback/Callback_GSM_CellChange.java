package com.Android.CodeInTheAir.Callback;

import org.json.JSONObject;

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.gsm.GsmCellLocation;

import com.Android.CodeInTheAir.Callback.Callback_Constants.ResultCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_GSM;

public class Callback_GSM_CellChange extends Callback_Generic
{
	public Callback_GSM_CellChange(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);
	}	
	
	public void start()
	{
		Sensor_GSM.addCellChangeListener(psListener);
		super.start();
	}
	
	public void stop()
	{
		Sensor_GSM.removeCellChangeListener(psListener);
		super.stop();
	}
	
	private PhoneStateListener psListener = new PhoneStateListener()
	{
		public void onSignalStrengthsChanged(SignalStrength ss)
		{
			
		}
		
		public void onCellLocationChanged(CellLocation location)
		{
			JSONObject jResult = new JSONObject();			
			// only works on GSM phones			
			GsmCellLocation loc = (GsmCellLocation)location;			
			try
			{
				jResult.put("cid", loc.getCid());
				jResult.put("lac", loc.getLac());
			}
			catch (Exception e)
			{
					
			}

			event(ResultCode.OK, "JSONObject", jResult.toString());
		}
	};
}
