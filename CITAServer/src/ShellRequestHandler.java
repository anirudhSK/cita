import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ShellRequestHandler extends Thread
{
	Socket socket;
	PrintWriter shellWriter;
	BufferedReader shellReader;
	
	ShellRequestHandler(Socket socket)
	{
		this.socket = socket;
	}
	
	public void run()
	{
		try {
			 shellWriter = new PrintWriter(socket.getOutputStream(),true);
		} catch (Exception e) {
			System.out.println("Exception creating OutputStream " + e.getMessage());
		}
		
		try {
			shellReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
		} catch (Exception e) {
			System.out.println("Exception creating InputStream " + e.getMessage());
		}
		
		try {
			String strDevice = shellReader.readLine();	
			System.out.println("Device: " + strDevice);
			String strTask = shellReader.readLine();
			String strSession = shellReader.readLine();
			String strCommand = shellReader.readLine();
			System.out.println("Command: " + strCommand);
			
			handleCommand(strDevice, strTask, strSession, strCommand);			
						
		} catch (Exception e) {
			shellWriter.println("Some exception");
		}
		
		try {
			socket.close();
		}
		catch (Exception e) {
		}
	}
	
	private void handleCommand(String device, String task, String session, String command)
	{	
		DeviceRequestHandler requestHandler = Components.DeviceInterface.getRequestHandler(device);	
		String response = requestHandler.sendCommand(task, session, command);
		shellWriter.println(response);
	}		
}
