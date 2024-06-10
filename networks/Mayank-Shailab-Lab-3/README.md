# Computer Networks Lab 3

### Repository/Folder Structure

```yaml

 Mayank-Shailab-Lab-3
 |
 +--A
 |  |
 |  +- client_non_thread.py
 |  +- server_thread.py
 |  +- client.sh
 |  +- server.sh
 |
 +--B
 |  |
 |  +- client_thread.py
 |  +- server_non_thread.py
 |  +- client.sh
 |  +- server.sh
 |
 +- README.md

```

### Table denotes the different Server-Client in folders

```yaml
+----------------+----------+----------+
|                |     A    |     B    |
+----------------+----------+----------+
|   Threading    |  Server  |  Client  |
+----------------+----------+----------+
| Non-Threading  |  Client  |  Server  |
+----------------+----------+----------+
```

### Threading Client & Threading Server

```bash
$ # Client
$ cd B
$ bash client.sh localhost 55555
$ # Server
$ cd A
$ bash server.sh 55555
```


### Non-Threading Client & Non-Threading Server

```bash
$ # Client
$ cd A
$ bash client.sh localhost 55555
$ # Server
$ cd B
$ bash server.sh 55555
```


### Threading Client & Non-Threading Server

```bash
$ # Client
$ cd B
$ bash client.sh localhost 55555
$ # Server
$ cd B
$ bash server.sh 55555
```


### Non-Threading Client & Threading Server

```bash
$ # Client
$ cd A
$ bash client.sh localhost 55555
$ # Server
$ cd A
$ bash server.sh 55555
```