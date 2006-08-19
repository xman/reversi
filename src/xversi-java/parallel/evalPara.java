/*
# This file evalPara.java is part of xversi-java-parallel, a board game called reversi or othello.
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

/* encapsulate parameters from main thread to threads */
public class evalPara {
 
    public Integer bM ;
    public Integer bS ;
    public Integer s  ;
    public Sema lock  ;
    public Integer counter ;
    public XversiMoveList bList ;
    public int move ;
   
    public evalPara(int Move , Integer bestMove , Integer bestScore , Integer score , Sema lk , Integer count , XversiMoveList bestList) {
        bM = bestMove ;
        bS = bestScore ;
        s = score ;
        lock = lk ;
        counter = count ;
        bList = bestList ;
        move = Move ;
    }
    
    
}
