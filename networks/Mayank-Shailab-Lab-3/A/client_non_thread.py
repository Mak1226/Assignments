import struct
import random
import os
import sys
import signal
import aioconsole
import asyncudp
import asyncio

magic = 0xC461
ver = 1
hello = 0
data = 1
alive = 2
goodbye = 3
timeout = 15

time_dict = {}
seq_dict = {}

def make_header(cmd,s_id,seq):
    return struct.pack('!HBBII',magic,ver,cmd,seq,s_id)

async def send_data(client,session_id,server):

    while True:
        try:
            msg = await aioconsole.ainput("")
        except EOFError:
            msg = "eof"
        seq_dict[session_id] += 1

        if msg == 'q' or msg == 'eof':
            header = make_header(3, session_id, seq_dict[session_id])
            client.sendto(header,server)
            print(f'msg sent {seq_dict[session_id]}')
            time_dict[session_id] = asyncio.get_event_loop().time()

            await asyncio.sleep(0.001)
            return

        header = make_header(1, session_id, seq_dict[session_id])
        header_msg = header + msg.encode()
        client.sendto(header_msg,server)
        time_dict[session_id] = asyncio.get_event_loop().time()
        
 

async def receive_data(client):
    while True:
        data, _ = await client.recvfrom()

        MAGIC, VER, CMD, _, _ = struct.unpack('!HBBII', data[:12])

        if MAGIC != magic or VER != ver:
            print("Invalid Session")
            continue

        if CMD==0:
            print("HELLO from server")

        elif CMD==2:
            print("ALIVE")

        elif CMD==3:
            print("GOODBYE from server!")
            mypid = os.getpid()
            os.kill(mypid, signal.SIGTERM)
            sys.exit()

async def timer_check(session_id,client,server):
    while True:
        await asyncio.sleep(timeout)
        current_time = asyncio.get_event_loop().time()
        if session_id in time_dict and current_time - time_dict[session_id] >= timeout:
            header = make_header(3, session_id, seq_dict[session_id]+1)
            client.sendto(header,server)
            print("Session Timed Out")
            pid = os.getpid()
            os.kill(pid,signal.SIGTERM)
            sys.exit()


async def main():
    session_id = random.randint(0, 0xFFFFFFFF)
    port = int(sys.argv[2])
    host = sys.argv[1]

    if host == 'localhost':
        host = '127.0.0.1'
    
    server = (host,port)
    client = await asyncudp.create_socket(remote_addr=server)

    seq_dict[session_id] = 0
    header = make_header(0,session_id,seq_dict[session_id])
    time_dict[session_id] = asyncio.get_event_loop().time()
    client.sendto(header,server)

    timer = asyncio.create_task(timer_check(session_id,client,server))
    receiver = asyncio.create_task(receive_data(client))
    sender = asyncio.create_task(send_data(client,session_id,server))

    await asyncio.gather(sender,receiver,timer)

if __name__ == "__main__":
    asyncio.run(main())
