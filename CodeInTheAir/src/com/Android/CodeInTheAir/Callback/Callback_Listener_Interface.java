package com.Android.CodeInTheAir.Callback;

import com.Android.CodeInTheAir.Callback.Callback_Constants.ResultCode;
import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;


public interface Callback_Listener_Interface
{
	public void event(String cbId, ResultCode resultCode, String valueType, String value);
	public void stop(String cbId, StopCode stopCode);
}
