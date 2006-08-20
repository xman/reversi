/*
# This file board.h is part of xversi-c, a board game called reversi or othello.
#
# Copyright (c) 2005-2006 Chung Shin Yee <cshinyee@gmail.com>
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

#ifndef BOARD_H
#define BOARD_H

#include "location_list.h"
#include <cassert>
#include <cstring>


class board
{
public:
  enum piece { BLACK = 0, WHITE = 1, EMPTY = 2 } ;
  typedef location_list::location location ;
  board() { reset() ; }  
  board& operator=(const board &b) ;
  inline int get_at(const location& loc) const ;
  inline int get_at(int x, int y) const ;
  inline bool check_move_at(const location& loc, int side) const ;  
  int  get_flip_dir(const location& loc, int side)  const ;
  void move_at(const location& loc, int side) ;
  inline int get_white_count(void) const { return white_count ; } 
  inline int get_black_count(void) const { return black_count ; } 
  void reset(void) ;
  void print(void) const ;

protected: 
  void set_at(const location& loc, int side) ;
  void set_at(int x, int y, int side) ;
  
  // Changes of datatype of data will affect board() and operator=().
  char data[8][8] ;
  unsigned int white_count ;
  unsigned int black_count ;  
  
  static const int RIGHT  = 1 ;
  static const int LRIGHT = 2 ;
  static const int DOWN  = 4 ;
  static const int LLEFT = 8 ;
  static const int LEFT  = 16 ;
  static const int ULEFT = 32 ;
  static const int UP = 64 ;
  static const int URIGHT = 128 ;
} ;


int board::get_at(const location& loc) const
{ 
  assert(loc.x >= 0 && loc.x <= 7 && loc.y >= 0 && loc.y <= 7) ;
  // return data[loc.x+(loc.y<<3)] ;  
  return data[loc.x][loc.y] ;  
}

int board::get_at(int x, int y) const
{
  assert(x >= 0 && x <= 7 && y >= 0 && y <= 7) ;
  // return data[x+(y<<3)] ;
  return data[x][y] ;
}


bool board::check_move_at(const location& loc, int side) const
{
  location tv_loc ;
  int b_side  ; // side of the piece at (x,y)
  int opponent ;
  int count ;

  assert(loc.x >= 0 && loc.x <= 7 && loc.y >= 0 && loc.y <= 7) ;
  assert(side == BLACK || side == WHITE) ;
  assert(get_at(loc) == EMPTY) ;

  if(side == BLACK) opponent = WHITE ;
  else opponent = BLACK ;

  /* right */
  count = 0 ;
  for(tv_loc.x = loc.x+1, tv_loc.y = loc.y ; tv_loc.x < 8 ; tv_loc.x++) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) return true ;
      break ;
    } else if(b_side == opponent) {
      count ++ ;
    } else {
      break ;
    }
  }

  /* lower right */
  count = 0 ;
  for(tv_loc.x = loc.x+1, tv_loc.y = loc.y-1 ; tv_loc.x < 8 && tv_loc.y >= 0 ; tv_loc.x++, tv_loc.y--) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) return true ;
      break ;
    } else if(b_side == opponent) {
      count ++ ;
    } else {
      break ;
    }
  }

  /* down */
  count = 0 ;
  for(tv_loc.x = loc.x, tv_loc.y = loc.y-1 ; tv_loc.y >= 0 ; tv_loc.y--) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) return true ;
      break ;
    } else if(b_side == opponent) {
      count ++ ;
    } else {
      break ;
    }
  }

  /* lower left */
  count = 0 ;
  for(tv_loc.x = loc.x-1, tv_loc.y = loc.y-1 ; tv_loc.x >= 0 && tv_loc.y >= 0 ; tv_loc.x--,tv_loc.y--) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) return true ;
      break ;
    } else if(b_side == opponent) {
      count ++ ;
    } else {
      break ;
    }
  }

  /* left */
  count = 0 ;
  for(tv_loc.x = loc.x-1, tv_loc.y = loc.y ; tv_loc.x >= 0 ; tv_loc.x--) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) return true ;
      break ;
    } else if(b_side == opponent) {
      count ++ ;
    } else {
      break ;
    }
  }
  /* upper left */
  count = 0 ;
  for(tv_loc.x = loc.x-1, tv_loc.y = loc.y+1 ; tv_loc.x >= 0 && tv_loc.y < 8 ; tv_loc.x--, tv_loc.y++) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) return true  ;
      break ;
    } else if(b_side == opponent) {
      count ++ ;
    } else {
      break ;
    }
  }

  /* up */
  count = 0 ;
  for(tv_loc.x = loc.x , tv_loc.y = loc.y+1 ; tv_loc.y < 8 ; tv_loc.y++) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) return true ;
      break ;
    } else if(b_side == opponent) {
      count ++ ;
    } else {
      break ;
    }
  }

  /* upper right */
  count = 0 ;
  for(tv_loc.x = loc.x+1, tv_loc.y = loc.y+1 ; tv_loc.x < 8 && tv_loc.y < 8 ; tv_loc.x++ , tv_loc.y++) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) return true ;
      break ;
    } else if(b_side == opponent) {
      count ++ ;
    } else {
      break ;
    }
  }

  return false ;
  
}

#endif // BOARD_H
