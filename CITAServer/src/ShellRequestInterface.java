import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ShellRequestInterface 
{		
	private ServerSocket serverSocket;
	private int port;
	
	public ShellRequestInterface(int port)
	{
		this.port = port;
	}
	
	public void start()
	{
		try
		{
			serverSocket = new ServerSocket(port);
			System.out.println("Server waiting for shell");
			
			while (true)
			{
				Socket socket = serverSocket.accept();
				System.out.println("New shell connected");				
				
				ShellRequestHandler handler = new ShellRequestHandler(socket);
				System.out.println("Starting shell thread");
				handler.start();
			}
		}
		catch (IOException e)
		{
			System.out.println("Exception " + e.getMessage());
		}
	}
}
