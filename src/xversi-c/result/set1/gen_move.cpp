#include "gen_move.h"
#include "board.h"
#include "location_list.h"
//#include <list>

//using namespace std ;

void gen_move::generate_move(const board& b, int side, location_list& valid_moves)
{
//  board& bb = const_cast<board&>(b) ;
  location_list::location m ;

//  set<int>::iterator move ;
//  list<int> cset = b.candidate_moves ;

  for(m.y = 0 ; m.y < 8 ; m.y++) {
    for(m.x = 0 ; m.x < 8 ; m.x++) {
//    for(move = bb.cand_moves.begin() ; move != bb.cand_moves.end() ; move++) {
      if(b.get_at(m) == board::EMPTY && b.check_move_at(m, side)) {
//      m.y = (*move) >> 3 ;
//      m.x = (*move) - m.y ;
//      if(b.check_move_at(m, side)) {
        valid_moves.insert(m) ;
      }
    }
//    }
  }
}

