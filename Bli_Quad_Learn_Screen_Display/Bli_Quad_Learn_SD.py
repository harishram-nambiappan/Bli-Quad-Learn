from firebase import firebase
import cv2
import numpy as np
from google.cloud import storage
from ffpyplayer.player import MediaPlayer
import os


storage_client = storage.Client.from_service_account_json('Your_json_file')
buckets = list(storage_client.list_buckets())
st_buck = buckets[0]

firebase = firebase.FirebaseApplication('Firebase_Database_Reference', None)

while True:
    try:
        disp_comm = firebase.get("John/display", None)
        if disp_comm is not None:
            file = st_buck.blob(disp_comm)
            file.download_to_filename("temp.mp4")
            firebase.delete("John", "display")
            vid = cv2.VideoCapture("temp.mp4")
            audio = MediaPlayer("temp.mp4")
            while vid.isOpened():
                disp_comm = firebase.get("John/display", None)
                if disp_comm == "off":
                    firebase.delete("John", "display")
                    break
                else:
                    ret, frame = vid.read()
                    audiofr, val = audio.get_frame()
                    cv2.namedWindow("Bli-Quad-Learn Video Lectures")
                    cv2.imshow("Bli-Quad-Learn Video Lectures", frame)
                    if audiofr is not None and val != 'eof':
                        img, t = audiofr
                    key = cv2.waitKey(1)
                    if key == 27:
                        break
            vid.release()
            cv2.destroyAllWindows()
            audio.close_player()
            os.remove("temp.mp4")
    except Exception:
        pass
