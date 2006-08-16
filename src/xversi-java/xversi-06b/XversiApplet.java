/*
# This file XversiApplet.java is part of xversi-java, a board game called reversi or othello.
#
# Copyright (c) 2002-2006 Chung Shin Yee <cshinyee@gmail.com>
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

import java.applet.Applet; 
import java.lang.Math ;
import java.awt.* ;
import java.awt.event.* ;

public class XversiApplet extends Applet implements ActionListener,MouseListener {

private static final int RADIUS = 15 ;
private Spot s = new Spot(RADIUS) ;

XversiBoard b = new XversiBoard() ;
XversiAgent agent = new XversiAgent() ;
XversiPlayer player = new XversiPlayer(0) ;

private boolean paintNeeded ;
private int turn ;
private int turnNum ;
private boolean progress ;

Button resetButton ;
Button switchButton ;
Label turnLabel ;
Label humanLabel ;
Label blackLabel ;
Label whiteLabel ;

public void init() {


  Power2.Init() ;
  resetButton = new Button("Reset") ;
  add(resetButton) ;
  switchButton = new Button("Switch") ;
  add(switchButton) ;
  blackLabel = new Label("Black: 2") ;
  add(blackLabel) ;
  whiteLabel = new Label("White: 2") ;
  add(whiteLabel) ;
  humanLabel = new Label("You: Black") ; 
  add(humanLabel) ;
  turnLabel = new Label("Turn: Black") ; 
  add(turnLabel) ; 

  resetButton.addActionListener(this) ;
  switchButton.addActionListener(this) ;
  addMouseListener(this) ;

  reset() ;
}

private void reset() {
  b.Init() ;
  //b.PrintBoard() ;
  agent.Init() ;
  player.Init(0) ;
  paintNeeded = false ;
  turn = 1 ;
  turnNum = 4 ;
  humanLabel.setText("You: Black") ;
  turnLabel.setText("Turn: Black") ;
  progress = true ;
  repaint() ;
}

public void start() {

  XversiMoveList list ;
  XversiMoveList bestList ;
  XversiBoard bb = new XversiBoard() ;
  int i ;
  int bestMove = 0  ;
  int bestScore = -9999999 ;
  int move ;
  int score ;

  if(paintNeeded == true) return ;

  if(progress && player.GetSide() == turn) {
    if((list = XversiAgent.GenerateMove(b , turn)) != null) { 
    bestList = new XversiMoveList(list.GetMaxNum() + 1) ;
    for(i = 0 ; i <= list.GetMaxNum() ; i++) {
      move = list.GetMove(i) ;
      XversiAgent.TranslatePosition(move , s) ;
      bb.Copy(b) ;
      XversiAgent.MakeMove(bb , s.x , s.y , turn) ;
      score = XversiEval.Evaluate(bb , turn , (turn+1)%2 , turnNum+1) ; 
      System.out.println("Move : x = " + s.x + " y = " + s.y + " score = " + score) ;
      if(score > bestScore) {
        bestScore = score ;
        bestMove = move ;
        bestList.ClearList() ;
        bestList.InsertMove(bestMove) ;
        
      } else if(score == bestScore) {
        bestList.InsertMove(move) ;
      }
    }     

    int k ;
    k = (int)(Math.random() * (bestList.GetNum()+1)) ;
    k = bestList.GetMove(k) ;
    XversiAgent.TranslatePosition(k , s) ;
    System.out.println("Move take: x = " + s.x + " y = " + s.y) ;
    XversiAgent.MakeMove(b , s.x , s.y , turn) ; 
    turnNum++ ;
    repaint() ;
    }

    if(XversiAgent.CountMove(b , (turn+1)%2) > 0) {
      turn = (turn + 1) % 2 ;
    } else {
      if(XversiAgent.CountMove(b , turn) > 0) {
        start() ;
      } else {
        progress = false ;
      }

    }
    if(turn == 1) {
      turnLabel.setText("Turn: Black") ;
    } else {
      turnLabel.setText("Turn: White") ;
    }

  }


}

public void paint(Graphics g) {

int i,j ;
int bc , wc ;

bc = wc = 0 ;

g.setColor(Color.gray) ;
g.drawRect(0,0, getSize().width - 1 , getSize().height - 1) ;

g.setColor(Color.black) ;
for(i = 0 ; i <= 8 ; i++) {
  g.drawLine((getSize().width-1)* i / 8 , getSize().height / 9 , (getSize().width-1) * i / 8  , getSize().height) ;
  g.drawLine(0 , ((getSize().height - 1) * (i+1)) / 9 , getSize().width , ((getSize().height - 1) * (i+1)) / 9) ;
}

  for(i = 1 ; i <= XversiBoard.BOARD_SIZE ; i++) {
    for(j = 1 ; j <= XversiBoard.BOARD_SIZE ; j++) {      
      if(b.GetBoard(i,j) >= 0) {
      if(b.GetBoard(i,j) == 1) {
        g.setColor(Color.black) ;
        bc++ ;
      } else {
        g.setColor(Color.red) ;
        wc++ ;
      }
        s.x = (i-1) * (getSize().width / 8) + (getSize().width/16) ;
        s.y = j * (getSize().height / 9) + (getSize().height/18) ;
        g.fillOval(s.x - RADIUS , s.y - RADIUS, RADIUS*2,RADIUS*2) ;
      }
    }
  }

  whiteLabel.setText("White: " + wc ) ;
  blackLabel.setText("Black: " + bc ) ; 
  if(paintNeeded == true) {
    paintNeeded = false ;
    start() ;
  }

}

public void mousePressed(MouseEvent event) {}
public void mouseClicked(MouseEvent event) {

  int x , y ;

  if(progress && player.GetSide() != turn) {

    x = event.getX() / (getSize().width / 8) + 1 ;
    y = event.getY() / (getSize().height / 9) ;

    if(XversiAgent.CheckXY(x,y)) {

      if(XversiAgent.CheckMove(b , x , y , turn) > 0) {
        XversiAgent.MakeMove(b , x , y , turn) ; 
        turnNum ++ ; 
        paintNeeded = true ;
        repaint() ;
        if(XversiAgent.CountMove(b , (turn + 1)%2) > 0) {
          turn = (turn+1) % 2 ; 
          if(turn == 1) {
            turnLabel.setText("Turn: Black") ;
          } else {
            turnLabel.setText("Turn: White") ;
          }    
          start() ;
       
        } else {
          if(XversiAgent.CountMove(b , turn) <= 0) {
            progress = false ;
          }
        }
       
      }

    } else { start() ; } 
  } else {
    repaint() ;
  }

}
public void mouseReleased(MouseEvent event) {}
public void mouseEntered(MouseEvent event) {}
public void mouseExited(MouseEvent event) {}



public void actionPerformed (ActionEvent e) {
  if(e.getSource() == resetButton) {
    reset() ;
  } else if(e.getSource() == switchButton) {
    player.SwitchSide() ;
    if(player.GetSide() == 0) {
      humanLabel.setText("You: Black") ;
    } else {
      humanLabel.setText("You: White") ;
    }
   
    start() ;
  }

}

}
