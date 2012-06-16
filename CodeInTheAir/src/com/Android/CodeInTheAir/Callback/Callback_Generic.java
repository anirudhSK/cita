package com.Android.CodeInTheAir.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.Android.CodeInTheAir.Callback.Callback_Constants.ResultCode;
import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;

public class Callback_Generic 
{
	public String cbId;
	public boolean isRunning;
	Callback_Listener_Interface callbackListener;
	
	
	public Callback_Generic(Callback_Listener_Interface callbackListener)
	{
		this.callbackListener = callbackListener;
	}
	
	public void setCallbackId(String cbId)
	{
		this.cbId = cbId;
	}
	
		
	public boolean initParams(String params)
	{			
		if (params == null || params.equals(""))
		{
			return true;
		}
		
		try
		{
			JSONObject jParams = new JSONObject(params);
			JSONArray names = jParams.names();
			
			if (names != null)
			{
				for (int i = 0; i < names.length(); i++)
				{
					String p = names.getString(i);
					Object v = jParams.get(p);
					if (!setParam(p, v))
					{
						return false;					
					}
				}
			}			
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
	boolean setParam(String param, Object value)
	{
		return true;
	}
	
	public boolean updateParams(String param)
	{
		return initParams(param);
	}
	
	public void start()
	{
		isRunning = true;
	}
	
	public void stop()
	{
		Log.v("CITA:stop","stop call");
		stop(StopCode.CALL);
	}	
	
	void stop(StopCode stopCode)
	{
		isRunning = false;
		callbackListener.stop(cbId, stopCode);
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
	
	void event(ResultCode resultCode, String valueType, String value)
	{
		callbackListener.event(cbId, ResultCode.OK, valueType, value);
	}
}
