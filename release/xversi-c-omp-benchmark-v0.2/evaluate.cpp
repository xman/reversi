/*
# This file evaluate.cpp is part of xversi-c, a board game called reversi or othello.
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

#include "evaluate.h"
#include "board.h"
#include "gen_move.h"
#include "location_list.h"

using namespace std ;

int evaluate::ccount = 0 ;
int evaluate::rcount = 0 ;
int evaluate::ecount = 0 ;

evaluate::score evaluate::eval(const board& b, board::piece side, int turn)
{
  score s ;
  int turn_num ;

  assert(side == board::BLACK || side == board::WHITE) ;

  turn_num = b.get_black_count() + b.get_white_count() ;
  this->side = side ;
  if(turn_num >= 48) return end_eval(b, turn) ;
  s = recur_eval(b, turn, level, turn_num, -9999999) ;
  return s ;
}


evaluate::score evaluate::recur_eval(const board& b, int turn, int level, 
                                     int turn_num, score prev_s) 
{
    score s ;
    score best_s ;
    location_list::location move ;
    location_list::iterator iter ;
    location_list move_list ;
    board buffer_b ;

    #pragma omp atomic
    evaluate::rcount++ ;

    if(level == 0) return comp_eval(b,turn,turn_num) ;    
    move_list.clear() ;
    gen_move::generate_move(b , turn, move_list) ; 
    if(move_list.is_empty()) {
      return comp_eval(b , turn, turn_num) ; 
    } else {
      if(side == turn) {
        best_s = -9999997 ; 
        for(iter = move_list.begin() ; iter != move_list.end() ; iter++) {
           buffer_b = b ;
           move = *iter ;    
           buffer_b.move_at(move, turn)  ;
           s = recur_eval(buffer_b, (turn+1)%2, level-1, turn_num+1, best_s) ;
           if(s >= prev_s)
             return s ;
           if(s > best_s) {
             best_s = s ;
           }
        }
      } else {
        best_s = 9999997 ;
        for(iter = move_list.begin() ; iter != move_list.end() ; iter++) {
           buffer_b = b ;
           move = *iter ;
           buffer_b.move_at(move, turn) ;
           s = recur_eval(buffer_b, (turn+1)%2, level-1, turn_num+1, best_s) ;
           if(s <= prev_s) {
             return s ;
           }
           if(s < best_s) {
             best_s = s ;
           }
        } 
      }
      return best_s ;
    } 
}


evaluate::score evaluate::end_eval(const board&b , int turn)
{
  score s ;
  location_list::location move ;
  location_list::iterator iter ;
  location_list move_list ;
  board buffer_b ;

  #pragma omp atomic
  evaluate::ecount++ ;

  gen_move::generate_move(b, turn, move_list) ;
  if(move_list.is_empty()) {
    gen_move::generate_move(b, (turn+1)%2, move_list) ;
    if(move_list.is_empty()) {    
      if(side == board::BLACK) 
        return b.get_black_count() >= b.get_white_count() ? 9999990 : -9999990 ;
      else 
        return b.get_white_count() >= b.get_black_count() ? 9999990 : -9999990 ;
    } else {
      turn = (turn + 1) % 2 ;
    }
  }

  if(side == turn) {
    for(iter = move_list.begin() ; iter != move_list.end() ; iter++) {
      buffer_b = b ;
      move = *iter ;
      buffer_b.move_at(move, turn)  ;
      s = end_eval(buffer_b, (turn+1)%2) ;
      if(s > 0) return s ;
    }
    return -9999990 ;
  } else {
    for(iter = move_list.begin() ; iter != move_list.end() ; iter++) {
      buffer_b = b ;
      move = *iter ;
      buffer_b.move_at(move, turn)  ;
      s = end_eval(buffer_b, (turn+1)%2) ;
      if(s < 0) return s ;
    }
    return 9999990 ;
  }
}


evaluate::score evaluate::comp_eval(const board& b, int turn, int turn_num)
{
  score s ;
  #pragma omp atomic
  evaluate::ccount++ ;
  s  = piece_count(b, turn_num) ;
  s += mobility(b, turn_num) ;
  s += corner(b, turn) ;
  return s ;
}

evaluate::score evaluate::piece_count(const board& b, int turn_num)
{
  int count, count2 ;
  score s ;

  if(side == board::BLACK) {
    count  = b.get_black_count() ;
    count2 = b.get_white_count() ;
  } else if(side == board::WHITE) {
    count  = b.get_white_count() ;
    count2 = b.get_black_count() ;
  } else {
    return -9999991 ;
  }

  if(count == 0) return -9999994 ;
  if(count2 ==0) return 9999994  ;

  if(turn_num == 64) {
    if(count > count2) {
      return 9999994 ;
    } else if(count < count2) {
      return -9999994;
    } else {
      return 0 ; 
    }
  }
  s = count - count2 ;
  if(turn_num < 40) {
    if((count+count2) / count > count + count2 - 10) {
      s *= 2 ;
    } else {
      s *= -2 ;
    }
  } else {
    s *= 3 ;
  } 
  return s ;
}


evaluate::score evaluate::mobility(const board& b, int turn_num) {

  int count ;
  score s ;

  count = count_move(b,side) ;
  if(turn_num < 40) {
    s = 2*count  ;
  }else {
    s = 3*count  ;
  }
  return s ;
}

int evaluate::count_move(const board& b, int side) 
{
  int count = 0 ;
  location_list::location m ;

  for(m.y = 0 ; m.y < 8 ; m.y++) {
    for(m.x = 0 ; m.x < 8 ; m.x++) {
      if(b.get_at(m) == board::EMPTY && b.check_move_at(m, side)) {
        count++ ;
      }
    }
  }
  return count ;
}


evaluate::score evaluate::corner(const board& b, int turn) {
    
  location_list::location m ;
  int count  ;
  int c[4] ;
  int d ;
  int ch1, ch2 ;
  score s ;

  s = 0 ;
  count = 0 ;
  for(m.y = 0 ; m.y < 8 ; m.y+=7) {
    for(m.x = 0 ; m.x < 8 ; m.x+=7) {
      if((c[count] = b.get_at(m)) == side) {
        s += 20 ;
      } else if(c[count] == (side+1) % 2) {
        s -= 25 ;
      } else {
        ch1 = b.check_move_at(m, side) ;
        ch2 = b.check_move_at(m, (side+1)%2) ;
        if(ch1 && (turn == side || (!ch2))) {
          s += 20 ;
        } else if(ch2 && (turn != side || (!ch1))) {
          s -= 25 ;
        }
        c[count] = -1 ;
      } 
      count++ ;
    }
  } 

  if(c[0] == -1) {
    m.x = 1 ;
    m.y = 1 ;
    if((d = b.get_at(m)) == side) {
      s -= 25 ;
    } else if(d == (side + 1) % 2) {
      s += 20 ;
    }
  } 

  if(c[1] == -1) {
    m.x = 1 ;
    m.y = 6 ;
    if((d = b.get_at(m)) == side) {
      s -= 25 ;
    } else if(d == (side + 1) % 2) {
      s += 20 ;
    }
  }

  if(c[2] == -1) {
    m.x = 6 ;
    m.y = 1 ;
    if((d = b.get_at(m)) == side) {
      s -= 25 ;
    } else if(d == (side + 1) % 2) {
      s += 20 ;
    }
  }
  
  if(c[3] == -1) {
    m.x = 6 ;
    m.y = 6 ;
    if((d = b.get_at(m)) == side) {
      s -= 25 ;
    } else if(d == (side + 1) % 2) {
      s += 20 ;
    }
  }
  return s ; 
}

// eof
