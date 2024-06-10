#!bin/bash
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <portnum>"
    exit 1
fi

portnum="$1"

python3 server_thread.py "$portnum"