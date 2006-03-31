#define BOARD_SIZE 8

void InitBoard(void) ;
int SetBoard(int , int , int) ;
int GetBoard(int , int) ;
int GetBoardSize(void) ;
int CheckXY(int , int) ;
int TranslateXY(int , int) ;
int TranslatePosition(int , int * , int *)  ;

void PrintBoard(void) ;

int board[2] ;
int boardFilled[2] ;

