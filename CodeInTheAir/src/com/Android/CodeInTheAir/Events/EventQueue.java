package com.Android.CodeInTheAir.Events;

import java.util.LinkedList;

public class EventQueue 
{
	LinkedList<Event_Generic> queue;	
	
	public EventQueue()
	{
		queue = new LinkedList<Event_Generic>();	
	}

	public void add(Event_Generic event)
	{
		synchronized(queue) 
		{
			queue.addFirst(event);
			queue.notifyAll();
		}
	}
	
	public Event_Generic dequeue()
	{
		Event_Generic event = null;
		synchronized(queue) 
		{
			while (queue.size() == 0) 
			{
				try 
				{
					queue.wait();
			    } 
				catch (InterruptedException ex) 
				{
					return null;
				}
			}
			event = queue.removeLast();			
		}
		return event;				
	}
}
