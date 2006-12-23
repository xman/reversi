/*
# This file readme.txt is part of xversi-java-parallel, a board game called
# reversi or othello.
#
# Copyright (c) 2002-2006 Chung Shin Yee <cshinyee@gmail.com>
#
#       http://myxman.org
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License as
# published by the Free Software Foundation; either version 2 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
# USA.
#
# The GNU General Public License is contained in the file COPYING.
#
*/

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


