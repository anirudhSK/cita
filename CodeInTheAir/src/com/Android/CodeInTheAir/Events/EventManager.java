package com.Android.CodeInTheAir.Events;

import com.Android.CodeInTheAir.Events.Constants.EventType;


public class EventManager 
{
	EventQueue eventQueue;
	EventHandler eventHandler;	
	
	ActionHandler_LocalJS actionHandler_LocalJS;
	ActionHandler_Shell actionHandler_Shell;
	ActionHandler_Server actionHandler_Server;
	
	TaskContext taskContext;
	
	public EventManager(TaskContext taskContext)
	{		
		eventQueue = new EventQueue();
		eventHandler = new EventHandler();
		actionHandler_LocalJS = new ActionHandler_LocalJS(taskContext);
		actionHandler_Shell = new ActionHandler_Shell(taskContext);
		actionHandler_Server = new ActionHandler_Server(taskContext);
		
		this.taskContext = taskContext;
		
	}	
	public void start()
	{
		eventHandler.start();
	}
	
	public void stop()
	{
		
	}
	
	public void addEvent(Event_Generic event)
	{
		asyncPostEvent(event);
	}
	
	public void asyncPostEvent(final Event_Generic event) 
    {
    	new Thread()
    	{
    		@Override 
    		public void run() 
    		{
    			eventQueue.add(event);
    		}
    	}.start();
    	
    }
	
	class EventHandler extends Thread
	{
		public void run()
		{
			while (true)
			{			
				Event_Generic event = eventQueue.dequeue();
				handle(event);				
			}
		}
		
		
		public void handle(Event_Generic event)
		{	
			String engine = null;
			String file = null;			
			String func = null;
			
			String[] actionEngineSplits = event.getAction().split(":");
			String[] sourceEngineSplits = event.getSource().split(":");
			
			// Get the engine
			if (actionEngineSplits.length == 2)
			{
				engine = actionEngineSplits[0];
			}
			else
			{
				engine = sourceEngineSplits[0];
			}
			
			// Get the file and func
			
			String actionSuffix = null;
			if (actionEngineSplits.length == 2)
			{
				actionSuffix = actionEngineSplits[1];
			}
			else
			{
				actionSuffix = actionEngineSplits[0];
			}
			
			String[] actionFileSplits = actionSuffix.split("\\.");			
			
			if (actionFileSplits.length == 2)
			{
				file = actionFileSplits[0];
				func = actionFileSplits[1];
			}
			else
			{
				file = sourceEngineSplits[1];
				func = actionFileSplits[0];
			}
			
			
			if (engine.equalsIgnoreCase(Constants.LocalJS))
			{
				if (event.getEventType() == EventType.CALLBACK_DATA_EVENT)
				{
					actionHandler_LocalJS.handle(file, func, event.encode());
				}
				else if (event.getEventType() == EventType.CALL_PARAM_EVENT)
				{
					actionHandler_LocalJS.handle(file, func, event.encode());
				}
				else if (event.getEventType() == EventType.CALL_NOPARAM_EVENT)
				{
					actionHandler_LocalJS.handle(file, func);
				}
			}
			else if (engine.equalsIgnoreCase(Constants.Shell))
			{
				if (event.getEventType() == EventType.CALLBACK_DATA_EVENT)
				{
					Event_Callback_Data dataEvent = (Event_Callback_Data)event;
					actionHandler_Shell.handle(taskContext.getTaskId(), dataEvent.tag, dataEvent.value);
				}
			}
			else if (engine.equalsIgnoreCase(Constants.Server))
			{
				if (event.getEventType() == EventType.CALLBACK_DATA_EVENT)
				{
					actionHandler_Server.handle(Constants.LocalJS, file, func, event.encode());
				}
				if (event.getEventType() == EventType.CALL_PARAM_EVENT)
				{
					 actionHandler_Server.handle(Constants.LocalJS, file, func, event.encode());
				}	
				else if (event.getEventType() == EventType.CALL_NOPARAM_EVENT)
				{
					actionHandler_Server.handle(Constants.LocalJS, file, func);
				}
			}
			else
			{
				if (event.getEventType() == EventType.CALLBACK_DATA_EVENT)
				{
					actionHandler_Server.handle(engine, file, func, event.encode());
				}
				if (event.getEventType() == EventType.CALL_PARAM_EVENT)
				{
					 actionHandler_Server.handle(engine, file, func, event.encode());
				}	
				else if (event.getEventType() == EventType.CALL_NOPARAM_EVENT)
				{
					actionHandler_Server.handle(engine, file, func);
				}
			}
		}
	}
}
