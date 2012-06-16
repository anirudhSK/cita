package com.Android.CodeInTheAir.Events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.util.Log;

import com.Android.CodeInTheAir.Callback.Callback_Accl_Data;
import com.Android.CodeInTheAir.Callback.Callback_Accl_Shake;
import com.Android.CodeInTheAir.Callback.Callback_Accl_Walking;
import com.Android.CodeInTheAir.Callback.Callback_Constants.ResultCode;
import com.Android.CodeInTheAir.Callback.Callback_Constants.StopCode;
import com.Android.CodeInTheAir.Callback.Callback_GSM_CellChange;
import com.Android.CodeInTheAir.Callback.Callback_GSM_RSSIChange;
import com.Android.CodeInTheAir.Callback.Callback_Generic;
import com.Android.CodeInTheAir.Callback.Callback_Listener_Interface;
import com.Android.CodeInTheAir.Callback.Callback_Sample_Accl;
import com.Android.CodeInTheAir.Callback.Callback_Sample_GSM;

public class CallbackManager 
{	
	static ConcurrentHashMap<String, String> hEventStrCbId;
	static ConcurrentHashMap<String, String> hCbIdEventStr;	
	static ConcurrentHashMap<String, Callback_Generic> hCbIdCallbackObj;
	static ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>>> hTaskEventActionTagSubId;
	static ConcurrentHashMap<String, ConcurrentHashMap<String, CallbackInfo>> hCbIdSubIdCallbackInfo;	
	
	static ConcurrentHashMap<String, String> hSubIdCbId;
	
    private static ReentrantReadWriteLock  lock;
    
	public static void init()
	{
		hEventStrCbId = new ConcurrentHashMap<String, String>();
		hCbIdEventStr = new ConcurrentHashMap<String, String>();
		hCbIdCallbackObj = new ConcurrentHashMap<String, Callback_Generic>();
		hCbIdSubIdCallbackInfo = new ConcurrentHashMap<String, ConcurrentHashMap<String, CallbackInfo>>();		
		hTaskEventActionTagSubId = new ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>>>();		
		hSubIdCbId = new ConcurrentHashMap<String, String>();
		
		lock = new ReentrantReadWriteLock();
	}
	
	public static String addCallback(
			String taskId, String source,
			String event, String param, String action, String tag
			)
	{
		String eventStr = event + param;		
		String subId = TaskUtils.getUniqueId();
		CallbackInfo callbackInfo = new CallbackInfo(taskId, source, event, action, tag);
		String cbId = null;
		Callback_Generic callbackObj = null;
		
		lock.writeLock().lock();		
	
		try
		{
			if (!hEventStrCbId.containsKey(eventStr))
			{
				cbId = TaskUtils.getUniqueId();
				callbackObj = getCallbackObj(event);
				if (callbackObj == null)
				{
					return null;
				}
				Log.v("CITA:Param-----------", param);
				boolean initResult = callbackObj.initParams(param);
				if (!initResult)
				{						
					return null;
				}
				callbackObj.setCallbackId(cbId);
			}
			else
			{
				cbId = hEventStrCbId.get(eventStr);
			}
			
			
			if (!hTaskEventActionTagSubId.containsKey(taskId))
			{
				hTaskEventActionTagSubId.put(taskId, new ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>>());
			}		
			ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> hEventActionTagSubId = hTaskEventActionTagSubId.get(taskId);		
			if (!hEventActionTagSubId.containsKey(event))
			{
				hEventActionTagSubId.put(event, new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>());
			}
			ConcurrentHashMap<String, ConcurrentHashMap<String, String>> hActionTagSubId = hEventActionTagSubId.get(event);
			if (!hActionTagSubId.containsKey(action))
			{
				hActionTagSubId.put(action, new ConcurrentHashMap<String, String>());
			}
			ConcurrentHashMap<String, String> hTagSubId = hActionTagSubId.get(action);
			if (!hTagSubId.containsKey(tag))
			{
				hTagSubId.put(tag, subId);
			}
			else
			{
				subId = hTagSubId.get(tag);
				return subId;
			}
			
			hSubIdCbId.put(subId, cbId);
			
			if (!hEventStrCbId.containsKey(eventStr))
			{	
				callbackObj.start();
				hEventStrCbId.put(eventStr, cbId);		
				hCbIdEventStr.put(cbId, eventStr);
				hCbIdCallbackObj.put(cbId, callbackObj);		
				hCbIdSubIdCallbackInfo.put(cbId, new ConcurrentHashMap<String, CallbackInfo>());
			}	
			
			ConcurrentHashMap<String, CallbackInfo> hSubIdCallbackInfo = hCbIdSubIdCallbackInfo.get(cbId);
			hSubIdCallbackInfo.put(subId, callbackInfo);
		}
		catch (Exception e)
		{
			
		}
		finally
		{
			lock.writeLock().unlock();	
		}		
				
		return subId;
	}
	
	
	public static void removeCallback(
			String taskId,
			String event, String action, String tag,
			boolean isLocked
			)
	{		
		
		if (!isLocked)
		{
			lock.writeLock().lock();
		}
		
		if (hTaskEventActionTagSubId.containsKey(taskId))
		{
			ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> hEventActionTagSubId = hTaskEventActionTagSubId.get(taskId);
			if (hEventActionTagSubId.containsKey(event))
			{
				ConcurrentHashMap<String, ConcurrentHashMap<String, String>> hActionTagSubId = hEventActionTagSubId.get(event);
				if (hActionTagSubId.containsKey(action))
				{
					ConcurrentHashMap<String, String> hTagSubId = hActionTagSubId.get(action);
					if (hTagSubId.containsKey(tag))
					{
						String subId = hTagSubId.get(tag);
						String cbId = hSubIdCbId.get(subId);
						
						hCbIdSubIdCallbackInfo.get(cbId).remove(subId);
						if (hCbIdSubIdCallbackInfo.get(cbId).keySet().size() == 0)
						{
							Callback_Generic callbackObj = hCbIdCallbackObj.get(cbId);
							callbackObj.stop();
							
							String eventStr = hCbIdEventStr.get(cbId);
							
							hEventStrCbId.remove(eventStr);
							hCbIdEventStr.remove(cbId);
							hCbIdCallbackObj.remove(cbId);
							
							hCbIdSubIdCallbackInfo.remove(cbId);							
						}						
						hTagSubId.remove(tag);						
						hSubIdCbId.remove(subId);					
					}					
				}
				if (hActionTagSubId.get(action).keySet().size() == 0)
				{
					hActionTagSubId.remove(action);
				}
			}
			if (hEventActionTagSubId.get(event).keySet().size() == 0)
			{
				hEventActionTagSubId.remove(event);
			}
		}
		if (hTaskEventActionTagSubId.get(taskId).keySet().size() == 0)
		{
			hTaskEventActionTagSubId.remove(taskId);
		}		
		
		if (!isLocked)
		{
			lock.writeLock().unlock();
		}
	}
	
	public static void removeCallback(
			String taskId,
			String event, String action,
			boolean isLocked
			)
	{
		if (!isLocked)
		{
			lock.writeLock().lock();
		}
		
		if (hTaskEventActionTagSubId.containsKey(taskId))
		{
			ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> hEventActionTagSubId = hTaskEventActionTagSubId.get(taskId);
			if (hEventActionTagSubId.containsKey(event))
			{
				ConcurrentHashMap<String, ConcurrentHashMap<String, String>> hActionTagSubId = hEventActionTagSubId.get(event);
				if (hActionTagSubId.containsKey(action))
				{
					ConcurrentHashMap<String, String> hTagSubId = hActionTagSubId.get(action);
					Iterator<String> i = hTagSubId.keySet().iterator();
					while (i.hasNext())
					{
						String tag = i.next();
						removeCallback(taskId, event, action, tag, true);
					}
				}
			}
		}
		
		if (!isLocked)
		{
			lock.writeLock().unlock();
		}
	}
	
	
	public static void removeCallback(
			String taskId,
			String event,
			boolean isLocked
			)
	{
		if (!isLocked)
		{
			lock.writeLock().lock();
		}
		if (hTaskEventActionTagSubId.containsKey(taskId))
		{
			ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> hEventActionTagSubId = hTaskEventActionTagSubId.get(taskId);
			if (hEventActionTagSubId.containsKey(event))
			{
				ConcurrentHashMap<String, ConcurrentHashMap<String, String>> hActionTagSubId = hEventActionTagSubId.get(event);
				Iterator<String> i = hActionTagSubId.keySet().iterator();
				while (i.hasNext())
				{
					String action = i.next();
					Log.v("CITA:ConcurrentHashMap", taskId + " " + event + action);
					removeCallback(taskId, event, action, true);					
				}
			}
		}
		if (!isLocked)
		{
			lock.writeLock().unlock();
		}
	}
	
	
	public static void removeCallback(String taskId)
	{
		lock.writeLock().lock();
		
		if (hTaskEventActionTagSubId.containsKey(taskId))
		{
			ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> hEventActionTagSubId = hTaskEventActionTagSubId.get(taskId);
			Iterator<String> i = hEventActionTagSubId.keySet().iterator();
			while (i.hasNext())
			{
				String event = i.next();
				removeCallback(taskId, event, true);				
			}			
		}
		
		lock.writeLock().unlock();		
	}
	
	
	public static void removeCallbackId(String subId)
	{	
		lock.writeLock().lock();
		
		if (hSubIdCbId.containsKey(subId))
		{
			String cbId = hSubIdCbId.get(subId);
			CallbackInfo callbackInfo = hCbIdSubIdCallbackInfo.get(cbId).get(subId);
			removeCallback(callbackInfo.taskId, callbackInfo.event, callbackInfo.action, callbackInfo.tag, true);			
		}
		
		lock.writeLock().unlock();		
	}
	
	private static void removeCallbackCbId(String cbId)
	{
		lock.writeLock().lock();
		
		if (hCbIdSubIdCallbackInfo.containsKey(cbId))
		{
			Iterator<String> i = hCbIdSubIdCallbackInfo.get(cbId).keySet().iterator();
			
			while (i.hasNext())
			{
				removeCallbackId(i.next());
			}
		}
		
		lock.writeLock().unlock();
	}
	
	
	public static Callback_Generic getCallbackObj(String event)
	{
		if (event.equalsIgnoreCase("accl.data"))
		{
			return new Callback_Accl_Data(new CallbackListener());
		}
		
		if (event.equalsIgnoreCase("accl.sample"))
		{
			return new Callback_Sample_Accl(new CallbackListener());
		}
		
		if (event.equalsIgnoreCase("gsm.cellChange"))
		{
			return new Callback_GSM_CellChange(new CallbackListener());
		}
		
		if (event.equalsIgnoreCase("gsm.rssiChange"))
		{
			return new Callback_GSM_RSSIChange(new CallbackListener());
		}
		
		if (event.equalsIgnoreCase("gsm.sample"))
		{
			return new Callback_Sample_GSM(new CallbackListener());
		}
		
		if (event.equalsIgnoreCase("shake"))
		{
			return new Callback_Accl_Shake(new CallbackListener());
		}
		
		if (event.equalsIgnoreCase("walking"))
		{
			return new Callback_Accl_Walking(new CallbackListener());
		}
		
		return null;
	}
	
	
	static class CallbackListener implements Callback_Listener_Interface
	{
		public void event(String cbId, ResultCode resultCode, String valueType, String value)
		{
			Log.v("CITA:event", "event");
			List<CallbackInfo> lstCallbackInfo = new ArrayList<CallbackInfo>();
			lock.readLock().lock();			
			if (hCbIdSubIdCallbackInfo.containsKey(cbId))
			{
				Iterator<String> i = hCbIdSubIdCallbackInfo.get(cbId).keySet().iterator();
				while (i.hasNext())
				{
					String subId = i.next();
					CallbackInfo callbackInfo = hCbIdSubIdCallbackInfo.get(cbId).get(subId);
					lstCallbackInfo.add(callbackInfo);
				}
			}			
			lock.readLock().unlock();					
			
			Log.v("CITA:event", lstCallbackInfo.size() + "");
			for (int i = 0; i < lstCallbackInfo.size(); i++)
			{
				CallbackInfo callbackInfo = lstCallbackInfo.get(i);				
				
				long time = System.currentTimeMillis();
				Event_Callback_Data event = new Event_Callback_Data(time, callbackInfo.source, 
						callbackInfo.event, callbackInfo.action, callbackInfo.tag,
						resultCode, valueType, value);
				
				Task task = TaskManager.getTask(callbackInfo.taskId);
				if (task != null)
				{
					task.getTaskContext().postEvent(event);
				}
			}
			
		}
		
		public void stop(String cbId, StopCode stopCode)
		{
			if (stopCode != StopCode.CALL)
			{
				removeCallbackCbId(cbId);
			}
		}
	}
}
