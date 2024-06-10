import struct
import os
import signal
import sys
import asyncudp
import aioconsole
import asyncio

magic = 0xC461
ver = 1
hello = 0
data = 1
alive = 2
goodbye = 3
timeout = 25

clients = {}
client_address = {}
last_msg = {}
q = {}

def session_removal(sid):
    clients.pop(sid)
    client_address.pop(sid)
    last_msg.pop(sid)

def make_header(seq,s_id,cmd):
    return struct.pack('!HBBII',magic,ver,cmd,seq,s_id)

async def server_response(SID, address, server):
    while True:
        try:
            CMD, SEQ, _, MSG = await q[SID].get()
        except asyncio.CancelledError:
            await session_removal(SID)
            return 
        except Exception as e:
            print(f'Error in client {e}')
            await session_removal(SID)
            return

        last_msg[SID] = asyncio.get_event_loop().time()

        if clients[SID] < SEQ:
            for seq in range(clients[SID],SEQ):
                print(f"{hex(SID)} [{seq}] Lost Packet")
            clients[SID] = SEQ + 1
        
        elif clients[SID] - 1 == SEQ:
            print(f"{hex(SID)} [{SEQ}] Duplicate Packet")
            return
        
        elif clients[SID] - 1 > SEQ:
            header = make_header(clients[SID] + 1, SID, 3)
            server.sendto(header,client_address[SID])
            print(f"{hex(SID)} [{clients[SID]}] Session Closed due to lost packet")
            await session_removal(SID)
            return


        if CMD==0:
            header = make_header(clients[SID],SID,0)
            server.sendto(header, address)
            print(f"{hex(SID)} [{clients[SID]}] Session created")
            clients[SID] += 1

        elif CMD==1:
            print(f"{hex(SID)} [{clients[SID]}] {MSG}")
            header = make_header(clients[SID],SID,2)
            server.sendto(header,address)
            clients[SID] += 1

        elif CMD==3:
            header = make_header(clients[SID],SID,3)
            server.sendto(header, address)
            print(f"{hex(SID)} [{clients[SID]}] GOODBYE from client")
            print(f"{hex(SID)} session closed")
            session_removal(SID)

async def commands(server):
    while True:
        cmd = await aioconsole.ainput("")
        if cmd == 'q' :
            for c in clients:
                header = make_header(clients[c]+1, c, 3)
                server.sendto(header,client_address[c])
            server.close()
            pid = os.getpid()
            os.kill(pid,signal.SIGTERM)
            sys.exit()
    
async def check_timer(server):
    while True:
        
        await asyncio.sleep(timeout)
        current_time = asyncio.get_event_loop().time()
        temp_msg = last_msg.copy()
        for SID in temp_msg:
            if current_time - last_msg[SID] > timeout:
                header = make_header(clients[SID],SID,3)
                server.sendto(header,client_address[SID])
                print(f"{hex(SID)} [{clients[SID]}] Session Timeout")
                clients.pop(SID)
                client_address.pop(SID)
                last_msg.pop(SID)


async def main():

    port = int(sys.argv[1])
    client = ('localhost', port)
    server = await asyncudp.create_socket(local_addr=client)
    print(f'Listening on port {port}...')

    command = asyncio.create_task(commands(server))
    
    while True:
        data, address = await server.recvfrom()
        MAGIC, VER, CMD, SEQ, SID = struct.unpack('!HBBII', data[:12])

        if MAGIC != magic or VER != ver:
            print("Invalid Session")
            continue

        client_address[SID] = address
        
        if SID not in clients:
            clients[SID] = 0
            q[SID] = asyncio.Queue()
            responses = asyncio.create_task(server_response(SID, address, server))
            timer = asyncio.create_task(check_timer(server))
    
                    
        await q[SID].put((CMD, SEQ, SID, data[12:].decode()))

if __name__ == "__main__":
    server_run = asyncio.get_event_loop()
    server_run.run_until_complete(main())
    server_run.close()