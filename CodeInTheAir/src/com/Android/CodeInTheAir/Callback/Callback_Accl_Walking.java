package com.Android.CodeInTheAir.Callback;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.Android.CodeInTheAir.Callback.Callback_Constants.ResultCode;
import com.Android.CodeInTheAir.Global.AppContext;

public class Callback_Accl_Walking extends Callback_Generic
{
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private SensorEventListener acclSensorListener;

	public boolean isWalking = false;
	public boolean isMoving = false;
	
	private long previousMillis = 0;
	private long sumGap = 0;
	private int countGap = 0;
	
	// Some constants for walking detector.
	public final int DFT_WINDOW = 64;
	public final int DFT_WINDOW_SLIDE = 16;
	public final double WALK_DFT_PEAK_POWER_CUTOFF = 7;
  
	// Some constants for movement detector.
	public final int STDDEV_WINDOW = 16;
	public final double STDDEV_CUTOFF = 0.1;
	
	private double fftInBuffer[];
	private double fftOutBuffer[];

	double stateScore[];

	private int samplesSeen = 0;

	String AcclString = "";
	
	public Callback_Accl_Walking(Callback_Listener_Interface callbackListener)
	{
		super(callbackListener);
	}	
	
	
	public void start()
	{
	    sensorManager = (SensorManager)AppContext.context.getSystemService(Context.SENSOR_SERVICE);
        
        fftInBuffer = new double[DFT_WINDOW];
        fftOutBuffer = new double[(DFT_WINDOW)/2];
        
        acclSensorListener = new AcclListener();
        accelerometer = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        sensorManager.registerListener(acclSensorListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        stateScore = new double[3]; // 0 - stopped, 1 - moving, 2 - walking
        stateScore[0] = 0; stateScore[1] = 0; stateScore[2] = 0;
		
		super.start();
	}
	
	public void stop()
	{
		sensorManager.unregisterListener(acclSensorListener);
		super.stop();
	}
	
	private void computeDFT(double X[], double Y[]) {
	      int N = X.length;

	      for (int i = 1; i < N/2; ++i) {
	        double realPart = 0;
	        double imgPart = 0;
	        for (int j = 0; j < N; ++j) {
	          realPart += (X[j] * Math.cos(-(2.0 * Math.PI * i * j)/N));
	          imgPart += (X[j] * Math.sin(-(2.0 * Math.PI * i * j)/N));
	        }

	        Y[i] = Math.sqrt(realPart * realPart + imgPart * imgPart);
	      }
	    }
	
	private class AcclListener implements SensorEventListener
    {
      public void onSensorChanged(SensorEvent event)
      {
        try
        {
          long ms = System.currentTimeMillis();
          

          if (previousMillis > 0) {
        	  sumGap += (ms - previousMillis);
        	  countGap++;
          }
          
          previousMillis = ms;
          
          float accX = event.values[SensorManager.DATA_X];
          float accY = event.values[SensorManager.DATA_Y];
          float accZ = event.values[SensorManager.DATA_Z];

          double accMag = Math.sqrt(accX * accX + accY * accY + accZ * accZ);

          fftInBuffer[samplesSeen++] = accMag;
         
          if (samplesSeen == DFT_WINDOW) {
            // Execute DFT on buffer.
            // TODO do we need separate thread or task to do this.
            // TODO is this fast enough or do we need NDK or fixed point.
            computeDFT(fftInBuffer, fftOutBuffer);

            // Find peak power in fftOut.
            // If above threshold, walking otherwise not walking.
            double peakPower = -Double.MAX_VALUE;
            int peakDFTBin = -1;
            
            double sumPower = 0;
            int countPower = 0;
            for (int i = 1; i < (DFT_WINDOW/2); ++i) {
              if (fftOutBuffer[i] > peakPower) {
                peakPower = fftOutBuffer[i];
                peakDFTBin = i;
              }
              
              sumPower += fftOutBuffer[i];
              countPower++;
            }
            
            double averageGap = (sumGap * 1.0)/countGap;
            
            double peakPowerRatio = (peakPower * countPower)/sumPower;
            //tv.setText("" + peakPowerRatio + " Gap " + averageGap);

            // Compute stddev of last STDDEV_WINDOW samples.
            double sigmaX = 0, sigmaXsq = 0;
            for (int i = DFT_WINDOW - STDDEV_WINDOW; i < DFT_WINDOW; ++i) {
            	sigmaXsq += (fftInBuffer[i] * fftInBuffer[i]);
            	sigmaX += fftInBuffer[i];
            }
            
            int N = STDDEV_WINDOW;
            double stddev = Math.sqrt((sigmaXsq - ((sigmaX * sigmaX)/N))/N);
            
            boolean currentMoving = false;
            boolean currentWalking = false;
            
            if (stddev >= STDDEV_CUTOFF) {
            	currentMoving = true;
            } else {
            	currentMoving = false;
            }
            
            if (peakPowerRatio >= WALK_DFT_PEAK_POWER_CUTOFF) {
              currentWalking = true;
            } else {
              currentWalking = false;
            }

            int currentState = -1;
            if (currentMoving == false) {
            	currentState = 0;
            } else if (currentMoving == true && currentWalking == false) {
            	currentState = 1;
            } else if (currentWalking == true) {
            	currentState = 2;
            }

            double newStateScore[] = new double[3];
            
            int bestHMMState = -1;
            double bestHMMScore = -Double.MAX_VALUE;
            
            for (int i = 0; i < 3; ++i) {
                double maxScore = -Double.MAX_VALUE;
                double emissionScore = 0;
                if (currentState == 0 && i > 0) {
                	emissionScore = Math.log(0.001);
                } else if (currentState == 1 && i == 0) {
                	emissionScore = Math.log(0.001);
                } else if (currentState == 1 && i == 2) {
                	emissionScore = Math.log(0.5);
                } else if (currentState == 2 && i != 2) {
                	emissionScore = Math.log(0.001);
                }
                
            	for (int j = 0; j < 3; ++j) {
            		double transitionScore = (j == i) ? 0 : Math.log(0.05);
            		double candScore = stateScore[j] + emissionScore + transitionScore;
            		
            		if (candScore > maxScore) {
            			maxScore = candScore;
            		}
            	}
            	
            	newStateScore[i] = maxScore;
            	if (maxScore > bestHMMScore) {
            		bestHMMScore = maxScore;
            		bestHMMState = i;
            	}
            }
            
            for (int i = 0; i < 3; ++i) {
            	stateScore[i] = newStateScore[i];
            }
            
            if (bestHMMState == 0) {
            	//tv.setText("Stopped");
            } else if (bestHMMState == 1) {
            	//tv.setText("Moving, but not walking");
            } else {
            	event(ResultCode.OK, "double", "0");
            }
            
            // Shift DFT_WINDOW_SLIDE samples to the left.
            for (int i = DFT_WINDOW_SLIDE; i < DFT_WINDOW; ++i) {
              fftInBuffer[i-DFT_WINDOW_SLIDE] = fftInBuffer[i];
            }

            samplesSeen = DFT_WINDOW - DFT_WINDOW_SLIDE;
            sumGap = 0;
            countGap = 0;
          }
        }

        catch(Exception e)
        {
        }
      }

      public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    }
}
