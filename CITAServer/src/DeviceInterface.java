import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class DeviceInterface 
{
	private HashMap<String, DeviceRequestHandler> hDeviceRequestHandler;	
	private ServerSocket serverSocket;
	private int port;
	
	public DeviceInterface(int port)
	{
		this.port = port;		
		hDeviceRequestHandler = new HashMap<String, DeviceRequestHandler>();
	}
	
	public void start()
	{
		try
		{
			serverSocket = new ServerSocket(port);
			System.out.println("Server waiting for device");
			
			while (true)
			{
				Socket socket = serverSocket.accept();
				System.out.println("New device connected");			
				
				BufferedReader deviceReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
				
				String strDevice = deviceReader.readLine();				
				System.out.println("Device name - " + strDevice);				
				
				DeviceRequestHandler requestHandler = new DeviceRequestHandler(socket);				
				hDeviceRequestHandler.put(strDevice, requestHandler);
				
				DeviceResponseHandler responseHandler = new DeviceResponseHandler(socket, strDevice);
				responseHandler.start();
			}
		}
		catch (IOException e)
		{
			System.out.println("Exception " + e.getMessage());
		}
	}
	
	public DeviceRequestHandler getRequestHandler(String device)
	{
		if (hDeviceRequestHandler.containsKey(device))
		{
			return hDeviceRequestHandler.get(device);
		}
		return null;
	}	
}
