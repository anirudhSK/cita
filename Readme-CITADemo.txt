CodeInTheAir - Phone Run time
-------------

Import in eclipse and run it in a phone. Do the following modifications

In file, com.Android.ServerClient::ServerClientSettings.java,
change CITAServer ipaddress to the address where CITAJSServer runs.

In file, com.Android.CodeInTheAir.ShellClient::ShellClientSettings.java,
change CITAServer ipaddress to the address where CITAServer runs.

In com.Android.CodeInTheAir.Global::AppContext.java,
change the DeviceName to a unique name for different devices.

CITAJSServer - Server runtime
-------------

Do the following modifications before running it,

In file, com.CITAJSServer.ShellClient::ShellClientSettings.java,
Change CITAServer ipaddress to the address where CITAServer runs.

CITAServer - Shell server
----------

No modification needed. Just run it.

CITA_Web - Web shell
--------

Do the following modifications,
In shell-request.py, change the ipaddress in the first line after import to the ipaddress where CITAServer runs.
In shell-response.py, change the ipaddress in the first line after import to the ipaddress where CITAServer runs.
Make sure python scripts are executable by doing all this stuff given below :

CONFIGURATION INSTRUCTIONS FOR WEB SCRIPTS  - FOR LINUX
-------------------------------------------------
0. Assuming you haven't done so, install LAMP for Linux. Ubuntu
instructions are here
:https://help.ubuntu.com/community/ApacheMySQLPHP
1. Use the version of cita-web that is attached here (Lenin sent this
to me a week back). It seems to work better with less strange errors
like "permission denied and file not found" compared to the version he
sent today. These are errors because CGI-Python-Apache configurations
aren't setup right.
2. Then the sequence of commands is given below :

 2001  unzip cita-web.zip
 2002  sudo cp -r ~/Desktop/cita-web /var/www/
 2003  cd /var/www
 2004  sudo chown anirudh cita-web  -R
 2005  sudo chmod +rwx cita-web -R  # to make the Python files executable
 2007  sudo vim /etc/apache2/sites-available/default

Now, once you open vim on that file, add the following :

      <Directory /var/www/cita-web>
       Options +ExecCGI
       AddHandler cgi-script .py
       Order allow,deny
       allow from all
       </Directory>

 2008  sudo /etc/init.d/apache2 restart

Make sure to restart it.

Now, go to shell.php. It should all hopefully work.

CONFIGURATION INSTRUCTIONS FOR WEB SCRIPTS  - FOR WINDOWS
-------------------------------------------------
0. Assuming you haven't done so, install Wamp Server for Windows. (Wamp Server: http://www.wampserver.com/en/)
The Wamp Server comes with Apache server (what CITA needs), PHP, and MySql.
1. Follow the instructions below to make sure the Aphache server executes Python CGI correctly.
 i. Put cita-web under /wamp/www
 ii. Open /wamp/bin/apache/Apache2.2.21/conf/httpd.conf
     and add the following,

      <Directory "c:/wamp/www/cita-web">
        Options ExecCGI
        AddHandler cgi-script .py
       	Order allow,deny
	       Allow from all
      </Directory>

 iii. Restart the Wamp server


Now, go to shell.php. It should all hopefully work.
------------------------------------------------------------------------

COMMON INSTRUCTIONS FOR SINGLE-PHONE and MULTI-PHONE SCRIPTS :
--------------------------------------------------------------------------
1. Start CITAServer first.
2. Then start CITAJSServer. You should get the following message in CITAServer
---
    New device connected
    Device name - server
---
3. Then start the CodeInTheAir app on each phone. You should again get the following message in CITAServer
---
    New device connected
    Device name - Hari
---
4. Run http://localhost/cita/shell.php. 
   
INSTRUCTIONS FOR SINGLE-PHONE SCRIPTS :
----------------------------------------------------------------------------
Top left text box should contain the name of the phone.
Test with the following commands

phone.vibrate();
sprint("Hello world"); // Hello world should be echoed in shell.php
phone.toast("Hello");  
a = 10 + 20;
sprint(a);
x = wifi.getStrongestAP();
sprint(x);

INSTRUCTIONS FOR MULTI-PHONE SCRIPTS :

1. The shell.php command seeds the right hand side text box with a randomly generated task ID, which is used for all commands being sent to the phone. The task ID is a process ID that is unique to a particular multi phone task.
2. So for a multi phone task to work, the task ID must be the same across all the phones and the server. Pick some task ID (or use one of the randomly chosen ones and paste it across all 3 shell.php windows ).

Here are the detailed instructions :

- Open three different shell.php windows

In the first window
- Change device name to - tiffany

In the second window
- Change device name to - server 
  (The name of server should be the same as the DeviceName in CITAJSServer: com.CITAJSServer.ShellClient::ShellClientSettings.java)
- Use the same task Id from the first window

In the third window
- Change device name to - anirudh
- Use the same task Id from first window


In the server window (second shell.php window)
- give some dummy command, say
sprint("Hello world");

This will initialize the task with the taskId on the server. Now it will help route commands between phones with the same task Id.


In the first window (phone tiffany)

function vibrate(e) { phone.vibrate(); }  // you have to add functions on one line for it to work. 

In the third window (phone anirudh)

addCallback("shake", "tiffany:vibrate");

Now, if you shake anirudh's phone, tiffany's phone must vibrate. 
