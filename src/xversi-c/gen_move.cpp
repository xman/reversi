#include "gen_move.h"
#include "board.h"
#include "location_list.h"

void gen_move::generate_move(const board& b, int side, location_list& valid_moves)
{
  location_list::location m ;

  for(m.y = 0 ; m.y < 8 ; m.y++) {
    for(m.x = 0 ; m.x < 8 ; m.x++) {
      if(b.get_at(m) == board::EMPTY && b.check_move_at(m, side)) {
        valid_moves.insert(m) ;
      }
    }
  }
}

