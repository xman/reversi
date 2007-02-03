/*
# This file board.cpp is part of xversi-c, a board game called reversi or othello.
#
# Copyright (c) 2005-2006 Chung Shin Yee <cshinyee@gmail.com>
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

#include "board.h"
#include <cassert>
#include <iostream>
#include <vector>

using namespace std ;

board& board::operator=(const board &b)
{
  // memcpy found to improve the performance slightly.
  memcpy(data, b.data, 64*sizeof(char)) ;
  white_count = b.white_count ;
  black_count = b.black_count ;
  return *this ;
}

void board::set_at(const location& loc, int side) 
{ 
  assert(loc.x >= 0 && loc.x <= 7 && loc.y >= 0 && loc.y <= 7) ;
  assert(side == BLACK || side == WHITE) ;
  assert(get_at(loc) != side) ;
    
  if(side == BLACK) {
    if(get_at(loc) == WHITE) {
      white_count-- ;
    }
    black_count++ ;      
  } else if(side == WHITE) {
    if(get_at(loc) == BLACK) {
      black_count-- ;
    }
    white_count++ ;
  }
  // data[loc.x+(loc.y<<3)] = side ;    
  data[loc.x][loc.y] = side ;    
}


void board::set_at(int x, int y, int side) 
{ 
  assert(x >= 0 && x <= 7 && y >= 0 && y <= 7) ;
  assert(side == BLACK || side == WHITE) ;
  assert(get_at(x,y) != side) ;
    
  if(side == BLACK) {
    if(get_at(x,y) == WHITE) {
      white_count-- ;
    }
    black_count++ ;      
  } else if(side == WHITE) {
    if(get_at(x,y) == BLACK) {
      black_count-- ;
    }
    white_count++ ;
  }
  // data[x+(y<<3)] = side ;    
  data[x][y] = side ;    
}


void board::reset(void)
{
  for(int i = 0 ; i < 8 ; i++) {
  	for(int j = 0 ; j < 8 ; j++) {
		data[i][j] = EMPTY ;
  	}
  }

  set_at(3, 3, WHITE) ;
  set_at(4, 4, WHITE) ;
  set_at(4, 3, BLACK) ;
  set_at(3, 4, BLACK) ;

  black_count = 2 ;
  white_count = 2 ;    
}

void board::print(void) const {
  location m ;
  for(m.y = 7 ; m.y >= 0 ; m.y--) {
    cout << m.y << " " ;
    for(m.x = 0 ; m.x < 8 ; m.x++) {
      switch(get_at(m)) {
        case EMPTY:
          cout << ". " ; 
	  break ;
	case BLACK:
	  cout << "0 " ;
	  break ;
	case WHITE:
	  cout << "1 " ;
	  break ;
	default:
	  cout << "? " ;
      }
    }
    cout << endl ;
  }
  cout << "  " ;
  for(m.x = 0 ; m.x < 8 ; m.x++) {
    cout << m.x << " " ;
  }
  cout << endl << endl ; 
}


void board::move_at(const location& loc, int side)
{
  int allowed_move ;
  int opponent ;
  location tv_loc ; // traverse location

  assert(loc.x >= 0 && loc.x <= 7 && loc.y >= 0 && loc.y <= 7) ;
  assert(side == BLACK || side == WHITE) ;
  assert(get_at(loc) == EMPTY) ;
  
  if(side == BLACK) opponent = WHITE ;
  else opponent = BLACK ;

  allowed_move = get_flip_dir(loc, side) ;
  set_at(loc, side) ;

  /* right */
  tv_loc.x = loc.x + 1 ; 
  tv_loc.y = loc.y ;
  if((allowed_move & RIGHT) > 0) {
    while(tv_loc.x < 8 && get_at(tv_loc) == opponent) {
      set_at(tv_loc, side) ; 
      tv_loc.x++ ;  
    }
  }

  /* lower right */
  tv_loc.x = loc.x + 1 ;
  tv_loc.y = loc.y - 1 ;
  if((allowed_move & LRIGHT) > 0) {
    while(tv_loc.x < 8 && tv_loc.y >= 0 && get_at(tv_loc) == opponent) {
      set_at(tv_loc, side) ; 
      tv_loc.x++ ; 
      tv_loc.y-- ;
    }
  }
  
  /* down */
  tv_loc.x = loc.x ;
  tv_loc.y = loc.y - 1 ;
  if((allowed_move & DOWN) > 0) {
    while(tv_loc.y >= 0 && get_at(tv_loc) == opponent) {
      set_at(tv_loc, side) ; 
      tv_loc.y-- ; 
    }
  }
  
  /* lower left */
  tv_loc.x = loc.x - 1 ;
  tv_loc.y = loc.y - 1 ;
  if((allowed_move & LLEFT) > 0) {
    while(tv_loc.x >= 0 && tv_loc.y >= 0 && get_at(tv_loc) == opponent) {
      set_at(tv_loc, side) ; 
      tv_loc.x-- ;
      tv_loc.y-- ;
    }
  }
  
  /* left */
  tv_loc.x = loc.x - 1 ;
  tv_loc.y = loc.y ;
  if((allowed_move & LEFT) > 0) {
    while(tv_loc.x >= 0 && get_at(tv_loc) == opponent) {
      set_at(tv_loc, side) ; 
      tv_loc.x-- ;
    }
  }
  
  /* upper left */
  tv_loc.x = loc.x - 1 ;
  tv_loc.y = loc.y + 1 ;
  if((allowed_move & ULEFT) > 0) {
    while(tv_loc.x >= 0 && tv_loc.y < 8 && get_at(tv_loc) == opponent) {
      set_at(tv_loc, side) ; 
      tv_loc.x-- ;
      tv_loc.y++ ; 
    }
  }
  
  /* up */
  tv_loc.x = loc.x ;
  tv_loc.y = loc.y + 1 ;
  if((allowed_move & UP) > 0) {
    while(tv_loc.y < 8 && get_at(tv_loc) == opponent) {
      set_at(tv_loc, side) ; 
      tv_loc.y++ ; 
    }
  }
  
  /* upper right */
  tv_loc.x = loc.x + 1 ;
  tv_loc.y = loc.y + 1 ;
  if((allowed_move & URIGHT) > 0) {
    while(tv_loc.x < 8 && tv_loc.y < 8 && get_at(tv_loc) == opponent) {
      set_at(tv_loc, side) ; 
      tv_loc.x++ ;
      tv_loc.y++ ; 
    }
  }

}

int board::get_flip_dir(const location& loc, int side) const
{
  location tv_loc ;
  int b_side ;
  int opponent ;
  int count ;
  int result  ;
  
  assert(loc.x >= 0 && loc.x <= 7 && loc.y >= 0 && loc.y <= 7) ;
  assert(side == BLACK || side == WHITE) ;  
  assert(get_at(loc) == EMPTY) ;

  if(side == BLACK) opponent = WHITE ;
  else opponent = BLACK ;
  result = 0 ;
  
  /* right */ 
  count = 0 ;
  for(tv_loc.x = loc.x+1, tv_loc.y = loc.y ; tv_loc.x < 8 ; tv_loc.x++) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) result |= RIGHT ;
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
      if(count > 0) result |= LRIGHT ;
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
      if(count > 0) result |= DOWN ;
      break ;
    } else if(b_side == opponent) {
      count ++ ; 
    } else {
      break ;
    }
  }

  /* lower left */
  count = 0 ;
  for(tv_loc.x = loc.x-1, tv_loc.y = loc.y-1 ; tv_loc.x >= 0 && tv_loc.y >= 0 ; tv_loc.x-- , tv_loc.y--) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) result |= LLEFT ;
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
      if(count > 0) result |= LEFT ;
      break ;
    } else if(b_side == opponent) {
      count ++ ; 
    } else {
      break ;
    }
  }

  /* upper left */
  count = 0 ;
  for(tv_loc.x = loc.x-1, tv_loc.y = loc.y+1 ; tv_loc.x >= 0 && tv_loc.y < 8 ; tv_loc.x-- , tv_loc.y++) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) result |= ULEFT ;
      break ;
    } else if(b_side == opponent) {
      count ++ ; 
    } else {
      break ;
    }
  }

  /* up */
  count = 0 ;
  for(tv_loc.x = loc.x, tv_loc.y = loc.y+1 ; tv_loc.y < 8 ; tv_loc.y++) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) result |= UP ;
      break ;
    } else if(b_side == opponent) {
      count ++ ; 
    } else {
      break ;
    }
  } 

  /* upper right */
  count = 0 ;
  for(tv_loc.x = loc.x+1, tv_loc.y = loc.y+1 ; tv_loc.x < 8 && tv_loc.y < 8 ; tv_loc.x++, tv_loc.y++) {
    b_side = get_at(tv_loc) ;
    if(b_side == side) {
      if(count > 0) result |= URIGHT ;
      break ;
    } else if(b_side == opponent) {
      count ++ ; 
    } else {
      break ;
    }
  }
  return result ;
}
