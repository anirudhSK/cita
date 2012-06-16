package com.Android.CodeInTheAir.Events;

import com.Android.CodeInTheAir.Events.Constants.EventType;


public class Event_Call_Param extends Event_Generic
{	
	String value;
	public Event_Call_Param()
	{
		
	}
	
	public Event_Call_Param(String source, String action, String value)
	{		
		this.source = source;
		this.action = action;
		this.value = value;
	}
	
	public String encode()
	{
		return value;
	}
	
	public EventType getEventType()
	{
		return EventType.CALL_PARAM_EVENT;
	}
}