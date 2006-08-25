/*
# This file evalThread.java is part of xversi-java-parallel, a board game called reversi or othello.
#
# Copyright (c) 2002-2006 Chung Shin Yee <cshinyee@gmail.com>
#
#       http://cshinyee.blogspot.com/index.html
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

/* send request and receive result */
public class evalThread extends Thread {
    
    evalPara para ;
    XversiBoard b ;
    int side ;
    int t ;
    int tn ;
    XversiInterface interf ;
    int move ;
    String host ;
 
    
    public evalThread(evalPara param , XversiBoard board , int c , int turn , int turnNum , int m , XversiInterface intface , String h) {
        super() ;
        para = param ;
        b = board ;
        side = c ;
        t = turn ;
        tn = turnNum ;
        interf = intface ;
        move = m ;
	host = new String(h.toString()) ;
    }
    
    public void run() {
        int score = -111111 ;
        
        try {
            score = interf.getEvalScore(b , side , t , tn) ;
        } catch(Exception e) { System.out.println("error: " + e.getMessage()) ; }
	System.out.println("host: " + host + " move = " + move + " score = " + score) ;
        para.lock.down() ;
        
        //para.s = new Integer(score) ;
        if(score > para.bS.intValue()) {
            para.bS = new Integer(score)  ;
            para.bM = new Integer(move) ;
            para.bList.ClearList() ;
            para.bList.InsertMove(para.bM.intValue()) ;
            
        } else if(score == para.bS.intValue()) {
            para.bList.InsertMove(move) ;
        }
        para.counter = new Integer(para.counter.intValue() + 1) ;
        para.lock.up() ;
        //applet.notifyAll()  ;
    }
    
    
}
