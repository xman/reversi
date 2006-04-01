#ifndef EVALUATE_H
#define EVALUATE_H

#include "board.h"
#include <cstring>

class evaluate
{
public:
  typedef int score ;

  evaluate(int level) : level(level) { } 
  score eval(const board& b, board::piece side, int turn) ;

  int level ; /// Number of evaluation levels.
  static int rcount ; /// Number of evaluation recursions.
  static int ecount ; /// Number of full end-game evaluation.
  static int ccount ; /// Number of evaluation computation.

protected:
  score recur_eval(const board& b, int turn, int level, int turn_num, score prev_s) ;
  score end_eval(const board&b , int turn) ;
  score comp_eval(const board& b, int turn, int turn_num) ;
  score piece_count(const board& b, int turn_num) ;
  score mobility(const board& b, int turn_num) ;
  int count_move(const board& b, int side) ;
  score corner(const board& b, int turn)   ;

private:
  board::piece side ;

} ;


#endif // EVALUATE_H
