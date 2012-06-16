import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ShellResponseHandler extends Thread
{
	Socket socket;
	PrintWriter shellWriter;
	BufferedReader shellReader;
	
	ShellResponseHandler(Socket socket)
	{
		this.socket = socket;
	}
	
	public void run()
	{
		try {
			 shellWriter = new PrintWriter(socket.getOutputStream(), true);
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
			String strTask = shellReader.readLine();
			String strSession = shellReader.readLine();
			
			System.out.println("Response Query: " + strSession);			
			
			CallResponse callResponse = Components.ResponseManager.get(strDevice + strTask + strSession);
			
			System.out.println("Response Query: " + callResponse.Response);
			shellWriter.println(callResponse.Response);
			
		} catch (Exception e) {
			
		}		
		try {
			socket.close();
		}
		catch (Exception e) {
		}
	}		
}
