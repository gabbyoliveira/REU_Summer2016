import socket
import os
import datetime
import json

# These have to be computed dynamically.
log_path = DYNAMIC_LOG_PATH
server_port = DYNAMIC_SERVER_PORT
try:
	client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	client_socket.connect(("localhost", server_port))
	trigger_data = json.dumps(TD)
	client_socket.send(trigger_data.encode())
except Exception as e:
	# Append the error to the client log file.
	log_file = open(log_path, "a+")
	log_file.write("{0}: {1}{2}".format(datetime.datetime.now(), e, os.linesep))
	log_file.close()
else:
	if client_socket is not None:
		client_socket.close()