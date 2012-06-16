import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ShellResponseInterface 
{		
	private ServerSocket serverSocket;
	private int port;
	
	public ShellResponseInterface(int port)
	{
		this.port = port;
	}
	
	public void start()
	{
		try
		{
			serverSocket = new ServerSocket(port);			
			while (true)
			{
				Socket socket = serverSocket.accept();			
				
				ShellResponseHandler handler = new ShellResponseHandler(socket);			
				handler.start();
			}
		}
		catch (IOException e)
		{
			System.out.println("Exception " + e.getMessage());
		}
	}
}
