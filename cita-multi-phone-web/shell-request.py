#!/usr/bin/python

import cgi
import socket
import time
import partitioning 


addr = ('18.251.5.213', 8001)

form = cgi.FieldStorage()
session = form['session'].value
# device = form['device'].value # There is no device anymore, since this is a multi device program. 
task = form['task'].value
statement = '';
if form.has_key('statement'):
	statement = form['statement'].value

print "Content-type: text/html\n"
if (statement == ''): 
	sys.exit()

#print statement
# partition the code here ;
# strip off any "/n" in the code
srcCode=statement.replace("\n"," ");
partitionedCode=partitioning.partitionCode(srcCode)
for device in partitionedCode :
 client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
 client_socket.connect(addr)                                       
 print "Device is ",device 
 client_socket.send(device + '\n')
 print "task is ",task
 client_socket.send(task + '\n')
 client_socket.send(session + '\n')
 client_socket.send(partitionedCode[device] + '\n')
 data = client_socket.recv(4096);
 client_socket.close()
 print data

# send data to server
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(addr)                                       
client_socket.send("server" + '\n')
client_socket.send(task + '\n')
client_socket.send(session + '\n')
client_socket.send("a=10;" + '\n')
data = client_socket.recv(4096);
client_socket.close()
print data
