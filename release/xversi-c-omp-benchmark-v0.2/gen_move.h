/*
# This file gen_move.h is part of xversi-c, a board game called reversi or othello.
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
