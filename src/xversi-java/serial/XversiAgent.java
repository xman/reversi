/*
# This file XversiAgent.java is part of xversi-java, a board game called reversi or othello.
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

public class XversiAgent {

public void Init() {
}

 public static XversiMoveList GenerateMove(XversiBoard b , int side) {


    int x , y ;
    int numMove ;
    XversiMoveList list ;

    numMove = CountMove(b , side) ;
    if(numMove == 0) return null ;

    list = new XversiMoveList(numMove) ; 

    for(x = 1 ; x <= XversiBoard.BOARD_SIZE ; x++) {
      for(y = 1 ; y <= XversiBoard.BOARD_SIZE ; y++) {
        if(XversiAgent.CheckMove(b , x , y , side) > 0) {
          list.InsertMove(XversiAgent.TranslateXY(x,y)) ;
        }
      }
    }

    return list ;
  }

public static boolean CheckXY(int x , int y) {

  if(x <= 0 || x > XversiBoard.BOARD_SIZE || y <= 0 || y > XversiBoard.BOARD_SIZE)
    return false ;

  return true ;

}

public static boolean CheckPosition(int position) {

  if(position <= 0 || position > XversiBoard.BOARD_SQUARE)
    return false ;

  return true ;

}


public static boolean CheckSide(int c) {

  if(c == 1 || c == 0)
    return true ;

  return false ;

}


public static int CheckMove(XversiBoard b, int x , int y , int c) {

  int i , j ;
  int side ;
  int nextSide ;
  int count ;

  int result = 0 ;

  if(b.GetBoard(x , y) != -1)
    return 0 ;

  if(!CheckXY(x,y))
    return 0 ;
  if(!CheckSide(c))
    return 0 ; 
 
  nextSide = (c+1) % 2 ;
 
  /* right */ 
  count = 0 ;
  for(i = x + 1 , j = y ; CheckXY(i,j) == true ; i++) {
    side = b.GetBoard(i , j) ;
    if(side == c) { 
      if(count > 0) 
        result |= 1 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  } 

  /* lower right */
  count = 0 ;
  for(i = x + 1 , j = y - 1 ; CheckXY(i,j) == true ; i++ , j--) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 2 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }


  /* down */
  count = 0 ;
  for(i = x , j = y - 1 ; CheckXY(i,j) == true ; j--) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 4 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }


  /* lower left */
  count = 0 ;
  for(i = x - 1 , j = y - 1 ; CheckXY(i,j) == true ; i-- , j--) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 8 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }


  /* left */
  count = 0 ;
  for(i = x - 1 , j = y ; CheckXY(i,j) == true ; i--) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 16 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }

  /* upper left */
  count = 0 ;
  for(i = x - 1 , j = y + 1 ; CheckXY(i,j) == true ; i-- , j++) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 32 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }

  /* up */
  count = 0 ;
  for(i = x , j = y + 1 ; CheckXY(i,j) == true ; j++) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 64 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  } 


  /* upper right */
  count = 0 ;
  for(i = x + 1 , j = y + 1 ; CheckXY(i,j) == true ; i++ , j++) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 128 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }

  return result ;

}

public static boolean MakeMove(XversiBoard b, int x , int y , int c) {

  int allowMove ;
  int i , j ;
  int nextSide ;

  if(!CheckXY(x,y) || !CheckSide(c))
    return false ;

  nextSide = (c + 1) % 2 ;

  allowMove = CheckMove(b , x , y , c) ;
  b.SetBoard(x , y , c) ;

  /* right */
  i = x + 1 ; 
  j = y ;
  if((allowMove & 1) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i++ ;  
    }
  }


  /* lower right */
  i = x + 1 ;
  j = y - 1 ;
  if((allowMove & 2) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i++ ; 
      j-- ;
    }
  }
  /* down */
  i = x ;
  j = y - 1 ;
  if((allowMove & 4) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      j-- ; 
    }
  }
  /* lower left */
  i = x - 1 ;
  j = y - 1 ;
  if((allowMove & 8) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i-- ;
      j-- ; 
    }
  }
  /* left */
  i = x - 1 ;
  j = y ;
  if((allowMove & 16) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i-- ;
    }
  }
  /* upper left */
  i = x - 1 ;
  j = y + 1 ;
  if((allowMove & 32) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i-- ;
      j++ ; 
    }
  }
  /* up */
  i = x ;
  j = y + 1 ;
  if((allowMove & 64) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      j++ ; 
    }
  }
  /* upper right */
  i = x + 1 ;
  j = y + 1 ;
  if((allowMove & 128) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i++ ; 
      j++ ;
    }
  }

  return true ;

}


public static int TranslateXY(int x , int y) {

  int position ;

  if(!CheckXY(x,y))
    return 0 ;

  position = (y-1)*XversiBoard.BOARD_SIZE ;
  position += XversiBoard.BOARD_SIZE - x + 1 ;
  return position ;


}

public static boolean TranslatePosition(int position , Spot s) {

  if(!CheckPosition(position))
    return false ;

  s.x = XversiBoard.BOARD_SIZE - ((position-1) % XversiBoard.BOARD_SIZE)  ;
  s.y = (position-1) / XversiBoard.BOARD_SIZE + 1 ;
  return true ;

}


public static int CountMove(XversiBoard b , int side) {
  int count = 0 ;
  int x , y ;

  for(x = 1 ; x <= XversiBoard.BOARD_SIZE ; x++) {
    for(y = 1 ; y <= XversiBoard.BOARD_SIZE ; y++) {
      if(CheckMove(b , x , y , side) > 0) {
        count++ ;
      }
    }
  }

  return count ;
}


}
