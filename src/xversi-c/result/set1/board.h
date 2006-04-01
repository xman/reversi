#ifndef BOARD_H
#define BOARD_H

#include "location_list.h"
#include <cassert>
#include <cstring>
#include <set>


class board
{
public:
  enum piece { BLACK = 0, WHITE = 1, EMPTY = 2 } ;
  typedef location_list::location location ;

  std::set<int> cand_moves ;

  board() { reset() ; }  
  inline board& operator=(const board &b) ;
  inline int get_at(const location& loc) const ;
  inline int get_at(int x, int y) const ;
  bool check_move_at(const location& loc, int side) const ;  
  int  get_flip_dir(const location& loc, int side)  const ;
  void move_at(const location& loc, int side) ;
  // void unmove_at(location& loc, int side) ;
  inline int get_white_count(void) const { return white_count ; } 
  inline int get_black_count(void) const { return black_count ; } 
  void reset(void) ;
  void print(void) const ;

  //std::list<int> candidate_moves ;

protected: 

  inline void set_at(const location& loc, int side) ;
  inline void set_at(int x, int y, int side) ;
  
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


board& board::operator=(const board &b)
{
  // memcpy found to improve the performance slightly.
  memcpy(data, b.data, 64*sizeof(char)) ;
  // cand_moves = b.cand_moves ;
  white_count = b.white_count ;
  black_count = b.black_count ;
  return *this ;
}


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
  // data[loc.x+(loc.y<<3)] = side ;    
  data[x][y] = side ;    
}

#endif // BOARD_H
