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
  
  // srand(time(NULL)) ;
  srand(100) ;
  
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
    
    //cin >> m.x >> m.y ;        
    // cout << "Move at " << m.x << " " << m.y << endl ;
    //if(m.x >= 0 && m.y >= 0) {
    //  b.move_at(m, value) ;
    //  b.print() ; 
    //}
    //if(value == board::WHITE) value = board::BLACK ;
    //else value = board::WHITE ;
  } 
  
  cout << "White: " << b.get_white_count() << " Black: " << b.get_black_count() << endl ;
  cout << "Recursive evaluate: " << evaluate::rcount << endl ;
  cout << "Recursive end eval: " << evaluate::ecount << endl ;
  cout << "Evaluation: " << evaluate::ccount << endl ;
  
  return 0 ;
}
