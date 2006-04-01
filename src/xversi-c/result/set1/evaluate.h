#ifndef EVALUATE_H
#define EVALUATE_H

#include "board.h"
//#include <ext/hash_map>
#include <cstring>

//using namespace __gnu_cxx;

//struct myhash
//{
//  size_t operator()(unsigned long long int x) const
//  {
//    return x  ;
//  }
//};


class evaluate
{
public:
  typedef int score ;

  evaluate(int level) : level(level) { } 
  score eval(const board& b, board::piece side, int turn) ;

  int level ;
  static int rcount ;
  static int ecount ;
  static int ccount ;

protected:
  score recur_eval(const board& b, int turn, int level, int turn_num, score prev_s) ;
  score end_eval(const board&b , int turn) ;
  score comp_eval(const board& b, int turn, int turn_num) ;
  score piece_count(const board& b, int turn_num) ;
  score mobility(const board& b, int turn_num) ;
  int count_move(const board& b, int side) ;
  score corner(const board& b, int turn)   ;

private:
//  typedef struct { char data[39] ; } hash_value_type ;
//  typedef hash_map<unsigned long long int, score, myhash > hash_table ;

  board::piece side ;
//  hash_table e_board ;
//  hash_value_type hash_board(const board& b) ;
} ;


#endif // EVALUATE_H
