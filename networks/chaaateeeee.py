import socket
import threading
import struct
import time
import random
import os
import sys
import signal

magic = 0xC461
ver = 1
hello = 0
data = 1
alive = 2
goodbye = 3
buffer = 1024
timeout = 1500
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

time_dict = {}

def make_header(cmd,s_id,seq):
    return struct.pack('!HBBII',magic,ver,cmd,seq,s_id)

def send_data():
    global running
    session_id = random.randint(0, 0xFFFFFFFF)
    sequence_no = 0

    header = make_header(0, session_id, sequence_no)
    client.sendto(header,(('10.42.0.1',50000)))
    time_dict[session_id] = time.time()
    timing = threading.Thread(target=timer_check,args=(session_id,sequence_no),daemon=True)
    timing.start()

    while True:
        msg = sys.stdin.readline().rstrip()
        sequence_no += 1

        if msg == 'q' or msg == 'eof':
            header = make_header(3, session_id, sequence_no)
            client.sendto(header,(('10.42.0.1',50000)))
            break

        else :
            header = make_header(1, session_id, sequence_no)
            header_msg = header + msg.encode()
            client.sendto(header_msg,(('10.42.0.1',50000)))
            time_dict[session_id] = time.time()
        
 

def receive_data():
    while True:
        data, _ = client.recvfrom(buffer)

        MAGIC, VER, CMD, SEQ, SID = struct.unpack('!HBBII', data[:12])

        if MAGIC != magic or VER != ver:
            print("Invalid Session")
            continue

        if CMD==0:
            print("HELLO from server")

        elif CMD==2:
            print("ALIVE")
            pass

        elif CMD==3:
            print("GOODBYE from server!")
            break

def timer_check(session_id,sequence_no):
    while True:
        time.sleep(timeout)
        current_time = time.time()
        if session_id in time_dict and current_time - time_dict[session_id] >= timeout:
            header = make_header(3, session_id, sequence_no)
            client.sendto(header,(('10.42.0.1',50000)))
            print("Session Timed Out")
            pid = os.getpid()
            os.kill(pid,signal.SIGTERM)
            sys.exit()

sender_thread = threading.Thread(target=send_data,daemon=True)
receiver_thread = threading.Thread(target=receive_data)

sender_thread.start()
receiver_thread.start()