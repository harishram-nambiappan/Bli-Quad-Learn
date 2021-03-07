from firebase import firebase
import socket
import os

firebase = firebase.FirebaseApplication('https://bli-quad-learn-default-rtdb.firebaseio.com')


sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(("192.168.43.100", 2600))

while True:
    sock.listen()
    conn, addr = sock.accept()
    #msg = conn.recv(1024).decode()
    msg_notes = conn.recv(1024).decode()
    print(msg_notes)
    conn.close()
    top_dir = os.listdir()
    flag = 0
    for i in range(len(top_dir)):
        if top_dir[i] == msg_notes.split("/")[0]:
            flag = 1
            note_list = len(os.listdir(msg_notes.split("/")[0]))
            note_file = open(msg_notes.split("/")[0]+"/"+str(note_list+1)
                              +".txt", "w")
            note_file.write(msg_notes.split("/")[1])
            note_file.close()
            break
    if flag == 0:
        os.mkdir(msg_notes.split("/")[0])
        note_file = open(msg_notes.split("/")[0]+"/1.txt", "w")
        note_file.write(msg_notes.split("/")[1])
        note_file.close()