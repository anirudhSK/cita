package com.Android.CodeInTheAir.Callback;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_GSM;
import com.Android.CodeInTheAir.Types.CellInfo;
import com.Android.CodeInTheAir.Types.GSMInfo;

public class Callback_Sample_GSM extends Callback_Sample_Generic
{
	/* Possible parameters */
	boolean bConnectedSet;
	boolean bStrongestSet;
	boolean bAllSet;
	
	boolean bTopNSet;
	int topN;
	
	public Callback_Sample_GSM(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);		
		
		bConnectedSet = false;
		bStrongestSet = false;
		bAllSet = true;
		bTopNSet = false;
	}
		
	boolean setParam(String param, Object value)
	{
		Log.v("CITA:gsm.param", param);
		try
		{
			if (param.equalsIgnoreCase("filter"))
			{
				String filter = (String)value;
				if (filter.equalsIgnoreCase("connected"))
				{
					bConnectedSet = true;
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
			else if (param.equalsIgnoreCase("top"))
			{
				bTopNSet = true;
				topN = (Integer)value;
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
		super.onlyTimeSample();
		super.start();
	}	
	
	void stop(StopCode stopCode)
	{
		super.stop(stopCode);
	}	
	
	void sampleTask()
	{
		sample();
		action();
	}
	
	void sample()
	{
		currentSampleCount++;
		JSONObject jResult = null;
		long time = System.currentTimeMillis();
		
		if (Sensor_GSM.isEnabled())
		{
			List<CellInfo> arrCells = new ArrayList<CellInfo>();			
			
			if (bConnectedSet)
			{
				CellInfo cellInfo = Sensor_GSM.getConnectedCell();	
				arrCells.add(cellInfo);				
				
			}
			else if (bStrongestSet)
			{
				CellInfo cellInfo = Sensor_GSM.getStrongestCell();	
				arrCells.add(cellInfo);
			}
			else
			{
				if (bTopNSet)
				{
					arrCells = Sensor_GSM.getAroundCells(topN);
				}
				else
				{
					arrCells = Sensor_GSM.getAroundCells();
				}
			}
			GSMInfo gsmInfo = new GSMInfo(true, System.currentTimeMillis(), 
					arrCells);
			jResult = encodeJSON(time, currentSampleCount, gsmInfo);
		}
		else
		{
			GSMInfo gsmInfo = new GSMInfo(false, System.currentTimeMillis(), 
					null);
			jResult = encodeJSON(time, currentSampleCount, gsmInfo);
		}
		
		try
		{
			jResultArray.put(jResult);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private JSONObject encodeJSON(long time, int sample, GSMInfo gsmInfo)
	{
		JSONObject jResult = new JSONObject();
		
		try
		{
			jResult.put("gsm", gsmInfo.encodeJSON());				
			
			jResult.put("sampleTime", time);
			jResult.put("sampleCount", sample);
		}
		catch (Exception e)
		{
			
		}
		
		return jResult;
	}
}
