package com.Android.CodeInTheAir.Callback;

import android.util.Log;

import com.Android.CodeInTheAir.Callback.Callback_Constants.CallbackSampleLifeTime;

public class Callback_Utils 
{
	public static boolean shouldStopSampling(CallbackSampleLifeTime lifeTime, long runtime, long startTime)
	{		
		Log.v("CITA:shouldstopsampling", lifeTime.toString() + " " + runtime + " " + startTime);		
		if (lifeTime == CallbackSampleLifeTime.ONCE)
		{
			return true;
		}
		else if (lifeTime == CallbackSampleLifeTime.TIMED)
		{
			long currentTime = System.currentTimeMillis();
			long totalRunTime = currentTime - startTime;
			if (totalRunTime > runtime)
			{
				Log.v("CITA:shouldstopsampling", "stopping");		
				return true;
			}
		}
		
		return false;
	}
}
