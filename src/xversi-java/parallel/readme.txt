Distributed Parallel Xversi v0.8b Readme File - 9 April 2002

compilation:

> javac *.java -O
> rmic XversiEval
> make a copy for each compute server, one for client

---------------------------------------------------------

Run Server:

> java -Djava.security.policy=xversi.policy XversiEval localhost port

where localhost = Server IP
      port = port number used for rmi service

---------------------------------------------------------

Run Client:

> appletviewer -J-Djava.security.policy=xversi.policy test.html

---------------------------------------------------------

nodeinfo.txt

- first line contains number of hosts
- contains all the hosts (IP + port) providing rmi service

* if the server capable of running multiple threads,
  the host for the server can be duplicated for 
  running multiple jobs.

* the applet requires file access to nodeinfo.txt,
  therefore, appletviewer should be used, 
  or applet in browser should be granted permission
  for file access (if possible)

---------------------------------------------------------

xversi.policy

* granting file permission
* granting hosts and ports for rmi 


---------------------------------------------------------


