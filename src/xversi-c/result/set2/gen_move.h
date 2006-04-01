#ifndef GEN_MOVE_H
#define GEN_MOVE_H

#include "board.h"
#include "location_list.h"

class gen_move
{
public:
  static void generate_move(const board& b, int side, location_list& valid_moves) ;
protected:
} ;

#endif // GEN_MOVE_H
