#include "main.h"

int main(void) {

  int i , j , k ;
  int x , y ;
  int m ;
  int turn ;

  InitPower() ;
  InitBoard() ;
  PrintBoard() ;

  for(i = 1 ; i <= GetBoardSize() ; i++) {
    for(j = 1 ; j <= GetBoardSize() ; j++) {

      printf("x = %d y = %d -> %d\n" , i , j , k = TranslateXY(i,j)) ;
/*      TranslatePosition(k , &x , &y) ;
      printf("k = %d -> x = %d , y = %d \n" , k , x , y) ;
*/
      if((m = CheckMove(i,j,0)) != 0)
        printf("0 move eligible with %d\n" , m) ;
      if((m = CheckMove(i,j,1)) != 0)
        printf("X move eligible with %d\n" , m) ;
/*      printf("status %d\n\n" , GetBoard(i,j)) ;
*/
      putchar('\n') ;
    }
  }

  PrintBoard() ;
  turn = 0 ;

  while(1) {
    scanf("%d %d" , &x , &y) ;
    if(!CheckXY(x,y)) break ;
    if(CheckMove(x , y , turn)) {
      MakeMove(x , y , turn) ; 
      turn = (turn+1) % 2 ; 
    }
    PrintBoard() ;
  }


  return 0 ;

}

