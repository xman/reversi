/*
# This file XversiMoveList.java is part of xversi-java-parallel, a board game called reversi or othello.
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

/* list of moves */
public class XversiMoveList {

  private int num ;
  private int maxNum ;
  private int[] list ;

  public XversiMoveList(int n) {
    list = new int[n] ;
    maxNum = n - 1 ;
    num = -1 ;
  }

  public void ClearList() {
    num = -1 ;

  }

  public boolean ClearMove(int n) {
    int t ;
    if(n > num) return false ;
    list[n] = list[num] ;
    num-- ;
    return true ;
  }

  public int GetMaxNum() {
    return maxNum ;
  }

  public int GetNum() {
    return num ;
  }

  public int GetMove(int n) {
    if(n > num) return -1 ;
    return list[n] ;
  }

  public boolean InsertMove(int n) {
    if(num >= maxNum) {
      return false ; 
    } 
    num++ ;
    list[num] = n ;
    return true ;
  }

}
