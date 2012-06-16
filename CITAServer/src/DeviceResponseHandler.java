import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;


public class DeviceResponseHandler extends Thread
{
	Socket socket;	
	BufferedReader deviceReader;
	
	String device;
	
	DeviceResponseHandler(Socket socket, String device)
	{
		this.device = device;
		this.socket = socket;
	}
	
	public void run()
	{
		try {
			deviceReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
		} catch (Exception e) {
			System.out.println("Exception creating InputStream " + e.getMessage());
		}
		
		try {
			while(true)
			{
				String strTask = deviceReader.readLine();
				String strSession = deviceReader.readLine();
				String strResponse = deviceReader.readLine();
				
				System.out.println("Phone response: " + strResponse);
				
				if (strTask == null && strSession == null && strResponse == null)
				{					
					break;
				}
				
				CallResponse callResponse = new CallResponse(strResponse);				
				Components.ResponseManager.put(device + strTask + strSession, callResponse);
			}
		} catch (Exception e) {
			
		}
		
		try {
			
		}
		catch (Exception e) {
		}
	}
}
