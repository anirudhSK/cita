#!/usr/bin/python

import cgi
import socket
import time

addr = ('18.251.5.213', 8002)

form = cgi.FieldStorage()
session = form['session'].value
#device = form['device'].value # There is no device anymore. It's a multi phone program 
task = form['task'].value

print "Content-type: text/html\n"

##client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
##client_socket.connect(addr)
##
##client_socket.send(device + '\n')
##client_socket.send(task + '\n')
##client_socket.send(session + '\n')
##
###command = client_socket.recv(4096);
##response = client_socket.recv(4096);
##client_socket.close()
##print response
