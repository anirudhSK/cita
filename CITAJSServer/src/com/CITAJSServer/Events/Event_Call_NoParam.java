package com.CITAJSServer.Events;
import com.CITAJSServer.Events.Constants.EventType;


public class Event_Call_NoParam extends Event_Generic
{	
	public Event_Call_NoParam()
	{
		
	}
	
	public Event_Call_NoParam(String source, String action)
	{		
		this.source = source;
		this.action = action;
	}
	
	public String encode()
	{
		return null;
	}
	
	public EventType getEventType()
	{
		return EventType.CALL_NOPARAM_EVENT;
	}
}
