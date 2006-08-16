/*
# This file main.cpp is part of xversi-c, a board game called reversi or othello.
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

#include <iostream>
#include <cstdlib>
#include <ctime>
#include "board.h"
#include "location_list.h"
#include "evaluate.h"
#include "gen_move.h"

using namespace std ;

int main(int argc, char** argv)
{
  board b ;
  board buffer_b ;

  board::piece value ;
  location_list::location m ;
  location_list::location best_move ;
  evaluate::score best_s ;
  evaluate::score s ;
  evaluate e(5) ;
  location_list vmoves ;
  location_list::iterator iter ;
  
  srand(time(NULL)) ;
  // srand(100) ;
  
  value = board::WHITE ;
  while(true) {

    vmoves.clear() ;  
    gen_move::generate_move(b, value, vmoves) ;
    if(vmoves.is_empty()) {
      if(value == board::WHITE) value = board::BLACK ;
      else value = board::WHITE ;
      vmoves.clear() ;
      gen_move::generate_move(b, value, vmoves) ;
      if(vmoves.is_empty())      
        break ;     
    }
        
    best_s = -99999999 ;
    for(iter = vmoves.begin() ; iter != vmoves.end() ; iter++) {
      m = *iter ;     
      buffer_b = b ; 
      buffer_b.move_at(m, value) ;      
      s = e.eval(buffer_b, value, (value+1)%2) ; 
      cout << "(" << m.x << "," << m.y << "): " << s << endl ;
      if(s > best_s) {
        best_s = s ;
	best_move = m ;
      }          
    }             
    b.move_at(best_move, value) ;
    b.print() ;
    if(value == board::WHITE) value = board::BLACK ;
    else value = board::WHITE ;
    
    // FIXME: I should check if the input is valid move.
    cout << "Please provide coordinate in \"x y\": " ;
    cin >> m.x >> m.y ;        
    cout << "Move at " << m.x << " " << m.y << endl ;
    if(m.x >= 0 && m.y >= 0) {
      b.move_at(m, value) ;
      b.print() ; 
    }
    if(value == board::WHITE) value = board::BLACK ;
    else value = board::WHITE ;
  } 
  
  cout << "White: " << b.get_white_count() << " Black: " << b.get_black_count() << endl ;
  cout << "Recursive evaluate: " << evaluate::rcount << endl ;
  cout << "Recursive end eval: " << evaluate::ecount << endl ;
  cout << "Evaluation: " << evaluate::ccount << endl ;
  
  return 0 ;
}
