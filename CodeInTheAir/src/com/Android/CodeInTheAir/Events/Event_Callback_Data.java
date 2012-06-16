package com.Android.CodeInTheAir.Events;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Android.CodeInTheAir.Callback.Callback_Constants.ResultCode;
import com.Android.CodeInTheAir.Events.Constants.EventType;


public class Event_Callback_Data extends Event_Generic
{	
	public String event;
	public String tag;
	
	public ResultCode resultCode;
	public String valueType;
	public String value;
	
	public long time;
	
	public Event_Callback_Data()
	{
		
	}
	
	public Event_Callback_Data(long time, String source, String event, String action, 
			String tag, ResultCode resultCode, String valueType, String value)
	{		
		this.time = time;		
		this.source = source;
		
		this.event = event;
		this.action = action;
		this.tag = tag;
		this.resultCode = resultCode;
		this.valueType = valueType;
		this.value = value;
	}
	
	public String encode()
	{
		JSONObject jParams = null;
		try
		{
			jParams = new JSONObject();
			if (event != null)
			{
				jParams.put("event", event);
			}
			if (tag != null)
			{
				jParams.put("tag", tag);
			}
			jParams.put("result", resultCode.toString());
			if (valueType.equalsIgnoreCase("JSONArray"))
			{
				jParams.put("value", new JSONArray(value));
			}
			else if (valueType.equalsIgnoreCase("JSONObject"))
			{
				jParams.put("value", new JSONObject(value));
			}
			else if (valueType.equalsIgnoreCase("String"))
			{
				jParams.put("value", value);
			}
			else if (valueType.equalsIgnoreCase("int"))
			{
				jParams.put("value", Integer.parseInt(value));
			}
			else if (valueType.equalsIgnoreCase("boolean"))
			{
				jParams.put("value", Boolean.parseBoolean(value));
			}
			else if (valueType.equalsIgnoreCase("double"))
			{
				jParams.put("value", Double.parseDouble(value));
			}
			else if (valueType.equalsIgnoreCase("long"))
			{
				jParams.put("value", Long.parseLong(value));
			}
			
			jParams.put("eventTime", time);			
		}
		catch (Exception e)
		{
			return null;
		}
		return jParams.toString();
	}
	
	public EventType getEventType()
	{
		return EventType.CALLBACK_DATA_EVENT;
	}
}