import java.io.PrintWriter;
import java.net.Socket;


public class DeviceRequestHandler
{
	Socket socket;
	public DeviceRequestHandler(Socket socket)
	{
		this.socket = socket;
	}
	
	public String sendCommand(String task, String session, String command)
	{		
		PrintWriter deviceWriter;	
		

		if (socket == null)
		{
			return "Phone not connected yet";		
		}
		
		try
		{
			deviceWriter = new PrintWriter(socket.getOutputStream(), true);			
		}
		catch (Exception e)
		{
			return "Error creating stream";			
		}		
		try
		{
			deviceWriter.println(task);
			deviceWriter.println(session);
			deviceWriter.println(command);
			
			return "Sent";
		}
		catch (Exception e)
		{
			return "Error sending command to phone";
		}

	}
}
