/*
# This file XversiPlayer.java is part of xversi-java, a board game called reversi or othello.
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

public class XversiPlayer {

  private int side ;

  public XversiPlayer(int s) {
    Init(s) ;
  }

  public void Init(int s) {
    side = s ;
  }

  public boolean SetSide(int c) {
    if(XversiAgent.CheckSide(c)) {
      side = c ; 
      return true ;
    } else {
      return false ;
    }

  }

  public void SwitchSide() {
    side = (side + 1) % 2 ;
  }

  public int GetSide() {
    return side ;
  }

}

