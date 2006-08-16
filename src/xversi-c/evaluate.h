/*
# This file evaluate.h is part of xversi-c, a board game called reversi or othello.
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
