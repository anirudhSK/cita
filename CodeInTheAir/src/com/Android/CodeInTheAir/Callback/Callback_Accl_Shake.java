package com.Android.CodeInTheAir.Callback;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.Android.CodeInTheAir.Callback.Callback_Constants.ResultCode;
import com.Android.CodeInTheAir.Device.Query.Sensor_Accl;

public class Callback_Accl_Shake extends Callback_Generic
{
	
	AcclListener listener;
	
	public Callback_Accl_Shake(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);
	}	
	
	
	public void start()
	{
		listener = new AcclListener();
		Sensor_Accl.addListener(listener, SensorManager.SENSOR_DELAY_NORMAL);
		super.start();
	}
	
	public void stop()
	{
		Sensor_Accl.removeListener(listener);
		super.stop();
	}
	
	 private class AcclListener implements SensorEventListener 
	 {
	   	int window = 5;
    	int boolWindow = 10;
    	float[] mag = new float[window];
    	boolean[] arrMoving = new boolean[boolWindow];
	    	
    	boolean isFilled = false;
	    	
    	boolean prevMoving = false;

    	int currentPointer = 0;
    	int currentBoolPointer = 0;
	    	
    	int currentSample = 0;
	    	
    	public float stdDev()
    	{
    		float ave = 0;
    		for (int i = 0; i < window; i++)
    		{
    			ave += mag[i];
	    	}
    		ave /= window;
	    		
    		float var = 0;
    		for (int i = 0; i < window; i++)
    		{
    			var += ((mag[i] - ave) * (mag[i] - ave)); 
	    	}
    		var /= window;
	    		
	    	return (float)Math.sqrt(var);
	   	}
	    	
    	public void onSensorChanged(SensorEvent event)
    	{    		    
    		currentSample++;
    			
    		float m = (event.values[0] * event.values[0]) 
    			+ (event.values[1] * event.values[1])
    			+ (event.values[2] * event.values[2]);
	    		
	    		
	    	m = (float)Math.sqrt(m);
	    		
	    	mag[currentPointer] = m;
	    	currentPointer++;
	    	if (currentPointer < window)
	    	{
	    		if (!isFilled)
	    		{
	    			return;
	    		}	
	    	}
	    	else
	    	{
	    		isFilled = true;
	    	}
	    	currentPointer %= window;
	    	
	    	float var = stdDev();
	    		
	    	
	    	if (var < 1)
	    	{
	    		arrMoving[currentBoolPointer] = false;   			
	    	}
	    	else
	    	{
	    		arrMoving[currentBoolPointer] = true;
	    	}
	    		
	    	currentBoolPointer++;
	    	currentBoolPointer %= boolWindow;
	    		
	    	boolean moving = false;
	    	for (int i=0;i<boolWindow;i++)
	    	{
	    		if (arrMoving[i])
	    		{
	    			moving = true;
	    		}
	    	}
	    		
	    	if (prevMoving != moving && moving == true)
	    	{
	    		event(ResultCode.OK, "double", var+"");
	    	}
	    		    		
	    	prevMoving = moving;    		
  		
	    }
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	}
}
