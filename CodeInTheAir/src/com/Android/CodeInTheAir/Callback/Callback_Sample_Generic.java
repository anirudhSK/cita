package com.Android.CodeInTheAir.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.Android.CodeInTheAir.Callback.Callback_Constants.CallbackSampleLifeTime;
import com.Android.CodeInTheAir.Callback.Callback_Constants.ResultCode;
import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;

public class Callback_Sample_Generic extends Callback_Generic 
{
	Handler sampleHandler;
	
	public CallbackSampleLifeTime lifeTime;	
	public long runTime;
	public int sampleTime;	
	public int chunk;
	
	long startTime;
	JSONArray jResultArray;
	
	int currentChunkSize;
	int currentSampleCount;
	
	boolean bEventSampleSet;
	boolean bTimeSampleSet;
	
	public Callback_Sample_Generic(Callback_Listener_Interface callbackListener)
	{		
		super(callbackListener);
		lifeTime = CallbackSampleLifeTime.LIFE;
		runTime = 10000;
		sampleTime = 1000;
		chunk = 1;		
		
		currentChunkSize = 0;
		currentSampleCount = 0;
		
		bEventSampleSet = false;
		bTimeSampleSet = true;
		
		sampleHandler = new Handler();
	}

	
	boolean setParam(String param, Object value)
	{
		try
		{
			if (param.equalsIgnoreCase("lifeTime"))
			{
				String strLifeTime = (String)value;
				if (strLifeTime.equalsIgnoreCase("once"))
				{
					lifeTime = CallbackSampleLifeTime.ONCE;
				}
				else if (strLifeTime.equalsIgnoreCase("timed"))
				{
					lifeTime = CallbackSampleLifeTime.TIMED;
				}
				else if (strLifeTime.equalsIgnoreCase("life"))
				{
					lifeTime = CallbackSampleLifeTime.LIFE;
				}
				else
				{
					return false;
				}
			}
			else if (param.equalsIgnoreCase("runTime"))
			{								
				runTime = (Integer)value;
			}
			else if (param.equalsIgnoreCase("sampleTime"))
			{								
				sampleTime = (Integer)value;
			}
			else if (param.equalsIgnoreCase("chunk"))
			{								
				chunk = (Integer)value;
			}
			else if (param.equalsIgnoreCase("sampleType"))
			{
				String filter = (String)value;
				if (filter.equalsIgnoreCase("event"))
				{
					bEventSampleSet = true;
					bTimeSampleSet = false;
				}
				else if (filter.equalsIgnoreCase("time"))
					
				{
					bEventSampleSet = false;
					bTimeSampleSet = true;
				}
				else if (filter.equalsIgnoreCase("both"))
				{
					bEventSampleSet = true;
					bTimeSampleSet = true;
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
			Log.v("CITA:exception", e.getMessage());
			return false;
		}	
		
		return true;
	}
	
	public void start()
	{		
		jResultArray = new JSONArray();
		startTime = System.currentTimeMillis();
		
		if (bTimeSampleSet)
		{
			schedule(0);
		}
		super.start();
	}

	void stop(StopCode stopCode)
	{
		if (bTimeSampleSet)
		{
			sampleHandler.removeCallbacks(sampleTask);
		}
		super.stop(stopCode);
	}
	
	
	Runnable sampleTask = new Runnable()
	{
		public void run() 
		{
			sampleTask();
		}
	};
	
	void sampleTask()
	{
		
	}
	
	void onlyTimeSample()
	{
		bTimeSampleSet = true;
		bEventSampleSet = false;
	}
	
	void onlyEventSample()
	{
		bTimeSampleSet = false;
		bEventSampleSet = true;
	}
	
	void action()
	{
		currentChunkSize++;
		if (currentChunkSize == chunk)
		{
			if (chunk == 1)
			{
				JSONObject jObject = new JSONObject();
				try
				{
					jObject = (JSONObject)jResultArray.get(0);
				}
				catch (Exception e)
				{
					
				}
				event(ResultCode.OK, "JSONObject", jObject.toString());
			}
			else
			{
				event(ResultCode.OK, "JSONArray", jResultArray.toString());
			}
			jResultArray = new JSONArray();
			currentChunkSize = 0;
		}
		
		if (Callback_Utils.shouldStopSampling(lifeTime, runTime, startTime))
		{
			if (currentChunkSize != 0)
			{
				if (chunk == 1)
				{
					JSONObject jObject = new JSONObject();
					try
					{
						jObject = (JSONObject)jResultArray.get(0);
					}
					catch (Exception e)
					{
						
					}
					event(ResultCode.OK, "JSONObject", jObject.toString());
				}
				else
				{
					event(ResultCode.OK, "JSONArray", jResultArray.toString());
				}
			}
			if (lifeTime == CallbackSampleLifeTime.ONCE)
			{
				stop(StopCode.ONCE);
			}
			else
			{
				Log.v("CITA:stop", "calling stop");
				stop(StopCode.TIMEOUT);
			}			
		}
		else
		{
			if (bTimeSampleSet)
	    	{
				schedule(sampleTime);
	    	}
		}
	}
	
	void schedule(int delay)
	{
		sampleHandler.postDelayed(sampleTask, delay);
	}
}
