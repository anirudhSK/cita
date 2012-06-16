package com.Android.CodeInTheAir.Callback;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.Android.CodeInTheAir.Callback.Callback_Constants.ResultCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_Accl;
import com.Android.CodeInTheAir.Types.AcclInfo;

public class Callback_Accl_Data extends Callback_Generic
{
	AcclListener acclListener;
	int period;
	
	public Callback_Accl_Data(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);
	}
	
	
	boolean setParam(String param, Object value)
	{
		try
		{
			if (param.equalsIgnoreCase("period"))
			{
				Log.v("CITA:accl", "setParam");
				period = (Integer)value;
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
		Log.v("CITA:accl", "start");
		acclListener = new AcclListener();
		Sensor_Accl.addListener(acclListener, period);
		super.start();
	}
	
	public void stop()
	{
		Sensor_Accl.removeListener(acclListener);
		super.stop();
	}
	
	private class AcclListener implements SensorEventListener 
    {
		int accuracy = 0;
		public void onSensorChanged(SensorEvent event)
        {   
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];
			float xRaw = x;
			float yRaw = y;
			float zRaw = z;
			
			AcclInfo acclInfo = new AcclInfo(System.currentTimeMillis(), x, y, z,
					xRaw, yRaw, zRaw, accuracy);
			
			Log.v("CITA:accl", "event");
			event(ResultCode.OK, "JSONObject", acclInfo.encodeJSON().toString());
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) 
        {
        	this.accuracy = accuracy;
        }
    }
}
