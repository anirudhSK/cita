package com.CITAJSServer.Callback;

public class Callback_Constants 
{
	public enum CallbackSampleLifeTime
	{
		ONCE, TIMED, LIFE
	}
	
	public enum ResultCode
	{
		OK, ERROR, BAD_PARAM
	}
	
	public enum StopCode
	{
		TIMEOUT, ONCE, CALL, EXCEPTION
	}
}
