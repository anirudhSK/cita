
public class Components 
{
	public static DeviceInterface DeviceInterface;
	public static ShellRequestInterface ShellRequestInterface;
	public static ShellResponseInterface ShellResponseInterface;
	public static ResponseManager ResponseManager;
	
	public static void start()
	{
		ShellRequestInterface = new ShellRequestInterface(Settings.ShellRequestPort);
		ShellResponseInterface = new ShellResponseInterface(Settings.ShellResponsePort);		
		DeviceInterface = new DeviceInterface(Settings.DevicePort);
		ResponseManager = new ResponseManager();
		
		new Thread()
		{
			public void run()
			{
				ShellRequestInterface.start();
			}		
		}.start();	
		
		new Thread()
		{
			public void run()
			{
				ShellResponseInterface.start();
			}		
		}.start();
				
		DeviceInterface.start();
	}
}
