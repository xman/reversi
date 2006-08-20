/*
# This file gen_move.cpp is part of xversi-c, a board game called reversi or othello.
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

