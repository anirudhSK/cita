package com.Android.CodeInTheAir.Device.Actuate;

import android.content.Context;
import android.os.Vibrator;

import com.Android.CodeInTheAir.Global.AppContext;

public class System_Notify 
{
	public static void vibrate()
	{
		Vibrator v = (Vibrator) AppContext.context.getSystemService(Context.VIBRATOR_SERVICE);		
		long milliseconds = 1000;
		v.vibrate(milliseconds);		
	}	
	
	public static void vibrate(long ms)
	{
		Vibrator v = (Vibrator) AppContext.context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(ms);		
	}
}
