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
timeout = 20
buffer = 1024
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
time_dict = {}
seq_dict = {}

host = sys.argv[1]
port = int(sys.argv[2])
server = (host,port)


def make_header(cmd,s_id,seq):
    return struct.pack('!HBBII',magic,ver,cmd,seq,s_id)

def send_data():
    session_id = random.randint(0, 0xFFFFFFFF)
    seq_dict[session_id] = 0
    header = make_header(0, session_id, seq_dict[session_id])
    client.sendto(header,(server))
    time_dict[session_id] = time.time()
    timing = threading.Thread(target=timer_check,args=(session_id,),daemon=True)
    timing.start()

    while True:
        msg = sys.stdin.readline().rstrip()
        seq_dict[session_id] += 1

        if msg == 'q' or msg == 'eof':
            header = make_header(3, session_id, seq_dict[session_id])
            client.sendto(header,(server))
            break

        else :
            header = make_header(1, session_id, seq_dict[session_id])
            header_msg = header + msg.encode()
            client.sendto(header_msg,(server))
            time_dict[session_id] = time.time()
        

def receive_data():
    while True:
        data, _ = client.recvfrom(buffer)

        MAGIC, VER, CMD, _, _ = struct.unpack('!HBBII', data[:12])

        if MAGIC != magic or VER != ver:
            print("Invalid Session")
            continue

        if CMD==0:
            print("HELLO from server")

        elif CMD==2:
            print(f"ALIVE")

        elif CMD==3:
            print("GOODBYE from server!")
            break

def timer_check(session_id):
    while True:
        time.sleep(timeout)
        current_time = time.time()
        if session_id in time_dict and current_time - time_dict[session_id] >= timeout:
            header = make_header(3, session_id, seq_dict[session_id]+1)
            client.sendto(header,(server))
            print("Session Timed Out")
            time.sleep(0.01)
            pid = os.getpid()
            os.kill(pid,signal.SIGTERM)
            sys.exit()

sender_thread = threading.Thread(target=send_data,daemon=True)
receiver_thread = threading.Thread(target=receive_data)

sender_thread.start()
receiver_thread.start()