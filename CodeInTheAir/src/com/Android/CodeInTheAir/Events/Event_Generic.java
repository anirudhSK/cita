package com.Android.CodeInTheAir.Events;

import com.Android.CodeInTheAir.Events.Constants.EventType;


public abstract class Event_Generic 
{
	String source;
	String action;
	
	public abstract String encode();
	public abstract EventType getEventType();
	
	public String getSource()
	{
		return source;
	}
	public String getAction()
	{
		return action;
	}
}
