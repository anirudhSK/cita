#!/usr/bin/python

import cgi
import socket
import time

addr = ('128.30.76.165', 8001)

form = cgi.FieldStorage()
session = form['session'].value
device = form['device'].value


print "Content-type: text/html\n"

statement = '';
if form.has_key('statement'):
	statement = form['statement'].value
        statement=5

if (statement == ''): 
	sys.exit()
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(addr)
client_socket.send(device + '\n')
client_socket.send(session + '\n')
client_socket.send(statement + '\n')
data = client_socket.recv(4096);
client_socket.close()

print data
