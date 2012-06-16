#!/usr/bin/python

import cgi
import socket
import time

addr = ('18.251.5.213', 8001)

form = cgi.FieldStorage()
session = form['session'].value
device = form['device'].value
task = form['task'].value
statement = '';
if form.has_key('statement'):
	statement = form['statement'].value

print "Content-type: text/html\n"
if (statement == ''): 
	sys.exit()

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(addr)
client_socket.send(device + '\n')
client_socket.send(task + '\n')
client_socket.send(session + '\n')
client_socket.send(statement + '\n')
data = client_socket.recv(4096);
client_socket.close()
print data
