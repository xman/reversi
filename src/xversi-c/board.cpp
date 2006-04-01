#include "board.h"
#include <cassert>
#include <iostream>
#include <vector>

using namespace std ;

void board::reset(void)
{
  for(int i = 0 ; i < 64 ; i++) {
    data[i] = EMPTY ;
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
