import socket
import threading
import struct
import time
import os
import signal
import sys
from queue import Queue

magic = 0xC461
ver = 1
hello = 0
data = 1
alive = 2
goodbye = 3
timeout = 15
buffer = 1024

server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
port = int(sys.argv[1])
server.bind(('localhost',port))
print(f"Listening on port {port}...")

clients = {}
client_address = {}
last_msg = {}
q = {}

def make_header(seq,s_id,cmd):
    return struct.pack('!HBBII',magic,ver,cmd,seq,s_id)

def server_response(SID):
    while True:
        CMD, SEQ, _, MSG = q[SID].get()

        if clients[SID] < SEQ:
            for seq in range(clients[SID],SEQ):
                print(f"{hex(SID)} [{seq}] Lost Packet")
            clients[SID] = SEQ + 1
        
        elif clients[SID] - 1 == SEQ:
            print(f"{hex(SID)} [{SEQ}] Duplicate Packet")
            continue
        
        elif clients[SID] - 1 > SEQ:
            header = make_header(clients[SID] + 1, SID, 3)
            server.sendto(header,client_address[SID])
            print(f"{hex(SID)} [{SEQ}] Session Closed due to lost packet")
            clients.pop(SID)
            client_address.pop(SID)
            last_msg.pop(SID)

        else:
            clients[SID] += 1

        if CMD==0:
            header = make_header(clients[SID]-1,SID,0)
            server.sendto(header, client_address[SID])
            print(f"{hex(SID)} [{clients[SID]-1}] Session created")

        elif CMD==1:
            print(f"{hex(SID)} [{clients[SID]-1}] {MSG}")
            header = make_header(clients[SID]-1,SID,2)
            server.sendto(header,client_address[SID])

        elif CMD==3:
            header = make_header(clients[SID],SID,3)
            server.sendto(header, client_address[SID])
            SEQ = clients[SID]
            clients.pop(SID)
            client_address.pop(SID)
            last_msg.pop(SID)
            if SID not in clients and SID not in client_address:
                print(f"{hex(SID)} [{SEQ-1}] GOODBYE from client")
                print(f"{hex(SID)} session closed")


def commands():
    while True:
        cmd = input("")
        if cmd == 'q':
            for c in clients:
                header = make_header(clients[c], c, 3)
                server.sendto(header,client_address[c])
            pid = os.getpid()
            os.kill(pid,signal.SIGTERM)
            sys.exit()
    
def check_timer():
    while True:
        time.sleep(1)
        temp_msg = last_msg.copy()
        for SID in temp_msg:
            if time.time() - last_msg[SID] > timeout:
                header = make_header(clients[SID],SID,3)
                server.sendto(header,client_address[SID])
                print(f"{hex(SID)} [{clients[SID]}] Session Timeout")
                clients.pop(SID)
                client_address.pop(SID)
                last_msg.pop(SID)

if __name__ == "__main__":

    command_thread = threading.Thread(target=commands, daemon=True)
    timer_thread = threading.Thread(target=check_timer, daemon=True)
    command_thread.start()
    timer_thread.start()
    while True:
        data, address = server.recvfrom(buffer)
        MAGIC, VER, CMD, SEQ, SID = struct.unpack('!HBBII', data[:12])

        if MAGIC != magic or VER != ver:
            print("Invalid Session")
            continue

        last_msg[SID] = time.time()
        client_address[SID] = address
        
        if SID not in clients:
            clients[SID] = 0
            q[SID] = Queue()
            client_thread = threading.Thread(target=server_response,args=(SID,))
            client_thread.start()
                    
        q[SID].put((CMD, SEQ, SID, data[12:].decode()))
