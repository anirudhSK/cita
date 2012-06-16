import java.util.LinkedList;

public class ResponseBuffer 
{
	LinkedList<CallResponse> queue;	
	
	public ResponseBuffer()
	{
		queue = new LinkedList<CallResponse>();	
	}

	public void add(CallResponse response)
	{
		synchronized(queue) 
		{
			queue.addFirst(response);
			queue.notifyAll();
		}
	}
	
	public CallResponse dequeue()
	{
		CallResponse response = null;
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
			response = queue.removeLast();			
		}
		return response;				
	}
}
