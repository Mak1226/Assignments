# Team Member: Shailab Chauhan(112001038)
#             Mayank Rawat(112001024)

import socket
import threading
import datetime

PROXY_HOST = '127.0.0.1'
PROXY_PORT = 8002

# Handle the different client
def handle_client(client_proxy_socket):
    request = client_proxy_socket.recv(4096)

    # Process with the HTTPS request
    if request.startswith(b'CONNECT'):
        handle_https_request(request, client_proxy_socket)

    # Process with the HTTP request
    else:
        handle_http_request(request, client_proxy_socket)

# Handles the HTTPS request
def handle_https_request(request, client_proxy_socket):
    header = request.split()
    host, port = header[1].split(b':')

    try:
        # Make a connection with the destination server
        target_socket = establish_connection(host, int(port))
    except Exception:
        # Send a bad gateway response back to the client/browser
        send_bad_gateway_response(client_proxy_socket)
        client_proxy_socket.close()
        return

    # Send a OK response back to the client
    send_ok_response(client_proxy_socket)

    # Print the output in the required format
    log_request_info(header)

    # For transferring request and response from proxy to server and server to proxy resp.
    start_forwarding_threads(client_proxy_socket, target_socket)

# Handles the HTTP request
def handle_http_request(request, client_proxy_socket):
    if not request:
        return

    header, _, body = request.partition(b'\r\n\r\n')
    header = modify_http_header(header)

    request = header + b'\r\n\r\n' + body
    parts = request.split(b' ')

    # Print the output in the required format
    log_request_info(parts)

    host = parse_host(parts[1])
    port = 80

    try:
        # Make a connection with the destination server
        remote_socket = establish_connection(host, port)
    except:
        # Send a bad gateway response back to the client/browser
        send_bad_gateway_response(client_proxy_socket)
        client_proxy_socket.close()
        return
    
    # Send a modified request to the server
    remote_socket.send(request)

    # For transferring request and response from proxy to server and server to proxy resp.
    start_forwarding_threads(remote_socket, client_proxy_socket)

def establish_connection(host, port):
    socket_instance = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    socket_instance.connect((host, port))
    return socket_instance

def send_bad_gateway_response(client_proxy_socket):
    http_bad_response = b"HTTP/1.1 502 Bad Gateway\r\n\r\n"
    client_proxy_socket.send(http_bad_response)

def send_ok_response(client_proxy_socket):
    http_ok_response = b"HTTP/1.0 200 OK\r\n\r\n"
    client_proxy_socket.send(http_ok_response)

def modify_http_header(header):
    header = header.replace(b'Connection: keep-alive', b'Connection: close') \
                   .replace(b'Proxy-connection: keep-alive', b'Proxy-connection: close') \
                   .replace(b'HTTP/1.1', b'HTTP/1.0')
    return header

def parse_host(url):
    if url.startswith(b'http://'):
        return url.split(b'/')[2]
    return url

def forward(source, destination):
    while True:
        try:
            data = source.recv(4096)
            if not data:
                break
            try:
                destination.send(data)
            except BrokenPipeError:
                break 
        except ConnectionResetError:
            continue


def start_forwarding_threads(source, destination):
    client_to_target_thread = threading.Thread(target=forward, args=(source, destination))
    target_to_client_thread = threading.Thread(target=forward, args=(destination, source))

    client_to_target_thread.start()
    target_to_client_thread.start()

    client_to_target_thread.join()
    target_to_client_thread.join()

    source.close()
    destination.close()

def log_request_info(parts):
    now = datetime.datetime.now()
    print(now.strftime("%d %b %H:%M:%S") + " - >>> " + parts[0].decode() + " " + parts[1].decode())

def main():
    proxy_server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    proxy_server.bind((PROXY_HOST, PROXY_PORT)) # Proxy server
    proxy_server.listen(5)

    print(datetime.datetime.now().strftime("%d %b %H:%M:%S") + " - " + f"Proxy listening on {PROXY_HOST}:{PROXY_PORT}")

    while True:
        client_proxy_socket, addr = proxy_server.accept()
        clientThread = threading.Thread(target=handle_client, args=(client_proxy_socket,))
        clientThread.start()

if __name__ == "__main__":
    main()