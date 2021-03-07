import os
import socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(("192.168.43.100",2800))

while True:
    sock.listen()
    conn, addr = sock.accept()
    path = conn.recv(1024).decode()
    pfile = open(path, "r")
    pcont = pfile.readlines()
    content = pcont[0]
    for i in range(1,len(pcont)):
        content = content + pcont[i]
    conn.send(bytes(content,'utf-8'))
    conn.close()
    