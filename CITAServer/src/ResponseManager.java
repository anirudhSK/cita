import java.util.HashMap;


public class ResponseManager 
{
	HashMap<String, ResponseBuffer> hResponseBuffer;
	
	public ResponseManager()
	{
		hResponseBuffer = new HashMap<String, ResponseBuffer>();
	}
	
	public void put(String session, CallResponse response)
	{
		if (!hResponseBuffer.containsKey(session))
		{
			hResponseBuffer.put(session, new ResponseBuffer());
		}
		hResponseBuffer.get(session).add(response);
	}
	
	public CallResponse get(String session)
	{
		if (!hResponseBuffer.containsKey(session))
		{
			hResponseBuffer.put(session, new ResponseBuffer());
		}
		CallResponse response = hResponseBuffer.get(session).dequeue();
		return response;		
	}
}
