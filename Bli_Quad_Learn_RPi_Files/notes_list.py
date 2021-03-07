import os
import socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(("192.168.43.100", 2700))

while True:
    sock.listen()
    conn, addr = sock.accept()
    course = conn.recv(1024).decode()
    print(course)
    nlist = os.listdir(course)
    nstring = nlist[0]
    for i in range(1, len(nlist)):
        nstring = nstring + "/" + nlist[i]
    conn.send(bytes(nstring, 'utf-8'))
    conn.close()