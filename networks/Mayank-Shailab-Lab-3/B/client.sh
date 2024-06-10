#!bin/bash
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <hostname> <portnum>"
    exit 1
fi

hostname="$1"
portnum="$2"

python3 client_thread.py "$hostname" "$portnum"