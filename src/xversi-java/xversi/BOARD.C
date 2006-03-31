#include "board.h"


void InitBoard(void) {

  board[0] = 0 ;
  board[1] = 0 ;

  boardFilled[0] = 0 ;
  boardFilled[1] = 0 ;

  board[0] |= GetPower(29) ;
  board[1] |= GetPower(4) ;

  boardFilled[0] |= GetPower(28) | GetPower(29) ;
  boardFilled[1] |= GetPower(4)  | GetPower(5)  ; 


}


int SetBoard(int x , int y , int c) {

  int position ;
  int power ;

  if(!CheckXY(x,y))
    return 0 ;

  position = TranslateXY(x,y) ;
  if(position <= 32) {
    power = GetPower(position) ;
    if(c == 0) {
      boardFilled[0] |= power ;
      board[0] &= ~power ;  
    } else if(c == 1) {
      boardFilled[0] |= power ;
      board[0] |= power ;
    } else {
      boardFilled[0] &= ~power ; 
    }
  } else {
    position -= 32 ;
    power = GetPower(position) ;
    if(c == 0) {
      boardFilled[1] |= power ;
      board[1] &= ~power ;
    } else if(c == 1) {
      boardFilled[1] |= power ;
      board[1] |= power ;
    } else {
      boardFilled[1] &= ~power ;
    }
  } 

  return 1 ;

}


int GetBoard(int x , int y) {

  int position ;
  int power ;

  if(!CheckXY(x,y))
    return -1 ;

  position = TranslateXY(x,y) ;
  if(position <= 32) {
    power = GetPower(position) ;
    if((boardFilled[0] & power) == 0)
      return -1 ;
    else if((board[0] & power) == 0)
      return 0 ;
    else return 1 ; 
  } else {
    position -= 32 ;
    power = GetPower(position) ;
    if((boardFilled[1] & power) == 0)
      return -1 ;
    else if((board[1] & power) == 0)
      return 0 ;
    else return 1 ;

  } 



}


int GetBoardSize(void) {

  return BOARD_SIZE ;

}


int CheckXY(int x , int y) {

  if(x <= 0 || x > BOARD_SIZE || y <= 0 || y > BOARD_SIZE)
    return 0 ;

  return 1 ;


}


int CheckPosition(int position) {

  if(position <= 0 || position > 32)
    return 0 ;

  return 1 ;

}


int CheckSide(int c) {
  if(c == 1 || c == 0)
    return 1 ;

  return 0 ;

}


int TranslateXY(int x , int y) {

  int position ;

  if(!CheckXY(x,y))
    return 0 ;

  position = (y-1)*BOARD_SIZE ;  
  position += BOARD_SIZE - x + 1 ;
  return position ;
 
}


int TranslatePosition(int position , int *x , int *y) {

  if(!CheckPosition(position))
    return 0 ;

  *x = BOARD_SIZE - ((position-1) % BOARD_SIZE)  ;
  *y = (position-1) / BOARD_SIZE + 1 ; 
  return 1 ;

}


void PrintBoard(void) {

  int x , y  ;

  for(y = BOARD_SIZE ; y > 0 ; y--) {
    for(x = 1 ; x <= BOARD_SIZE ; x++) {
      if(GetBoard(x,y) < 0)
        putchar('.') ;
      else if(GetBoard(x,y) == 1)
        putchar('X') ;
      else if(GetBoard(x,y) == 0)
        putchar('O') ;
      else
        putchar('?') ; 
    }
    putchar('\n') ;
  } 

}
