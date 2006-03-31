#include "agent.h"

void InitAgent(void) {

  moveList[0] = -1 ;

}


int CheckMove(int x , int y , int c) {

  int i , j ;
  int side ;
  int count ;

  int result = 0 ;

  if(!CheckXY(x,y))
    return 0 ;
  if(!CheckSide(c))
    return 0 ; 
  
  /* right */ 
  count = 0 ;
  for(i = x + 1 , j = y ; CheckXY(i,j) > 0 ; i++) {
    side = GetBoard(i , j) ;
    if(side == c) { 
      if(count > 0) 
        result |= 1 ;
      break ;
    } else if(side == ((c+1) % 2)) {
      count ++ ; 
    } else
      break ;
  } 

  /* lower right */
  count = 0 ;
  for(i = x + 1 , j = y - 1 ; CheckXY(i,j) > 0 ; i++ , j--) {
    side = GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 2 ;
      break ;
    } else if(side == ((c+1) % 2)) {
      count ++ ; 
    } else
      break ;
  }


  /* down */
  count = 0 ;
  for(i = x , j = y - 1 ; CheckXY(i,j) > 0 ; j--) {
    side = GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 4 ;
      break ;
    } else if(side == ((c+1) % 2)) {
      count ++ ; 
    } else
      break ;
  }


  /* lower left */
  count = 0 ;
  for(i = x - 1 , j = y - 1 ; CheckXY(i,j) > 0 ; i-- , j--) {
    side = GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 8 ;
      break ;
    } else if(side == ((c+1) % 2)) {
      count ++ ; 
    } else
      break ;
  }


  /* left */
  count = 0 ;
  for(i = x - 1 , j = y ; CheckXY(i,j) > 0 ; i--) {
    side = GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 16 ;
      break ;
    } else if(side == ((c+1) % 2)) {
      count ++ ; 
    } else
      break ;
  }

  /* upper left */
  count = 0 ;
  for(i = x - 1 , j = y + 1 ; CheckXY(i,j) > 0 ; i-- , j++) {
    side = GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 32 ;
      break ;
    } else if(side == ((c+1) % 2)) {
      count ++ ; 
    } else
      break ;
  }

  /* up */
  count = 0 ;
  for(i = x , j = y + 1 ; CheckXY(i,j) > 0 ; j++) {
    side = GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 64 ;
      break ;
    } else if(side == ((c+1) % 2)) {
      count ++ ; 
    } else
      break ;
  } 


  /* upper right */
  count = 0 ;
  for(i = x + 1 , j = y + 1 ; CheckXY(i,j) > 0 ; i++ , j++) {
    side = GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 128 ;
      break ;
    } else if(side == ((c+1) % 2)) {
      count ++ ; 
    } else
      break ;
  }

  return result ;

}


int MakeMove(int x , int y , int c) {

  int allowMove ;
  int i , j ;

  if(!CheckXY(x,y) || !CheckSide(c))
    return 0 ;



  allowMove = CheckMove(x , y , c) ;
  SetBoard(x , y , c) ;

  /* right */
  i = x + 1 ; 
  j = y ;
  if((allowMove & 1) != 0) {
    while(GetBoard(i,j) == ((c+1)%2) && CheckXY(x,y) > 0) {
      SetBoard(i , j , c) ; 
      i++ ;  
    }
  }


  /* lower right */
  i = x + 1 ;
  j = y - 1 ;
  if((allowMove & 2) != 0) {
    while(GetBoard(i,j) == ((c+1)%2) && CheckXY(x,y) > 0) {
      SetBoard(i , j , c) ; 
      i++ ; 
      j-- ;
    }
  }
  /* down */
  i = x ;
  j = y - 1 ;
  if((allowMove & 4) != 0) {
    while(GetBoard(i,j) == ((c+1)%2) && CheckXY(x,y) > 0) {
      SetBoard(i , j , c) ; 
      j-- ; 
    }
  }
  /* lower left */
  i = x - 1 ;
  j = y - 1 ;
  if((allowMove & 8) != 0) {
    while(GetBoard(i,j) == ((c+1)%2) && CheckXY(x,y) > 0) {
      SetBoard(i , j , c) ; 
      i-- ;
      j-- ; 
    }
  }
  /* left */
  i = x - 1 ;
  j = y ;
  if((allowMove & 16) != 0) {
    while(GetBoard(i,j) == ((c+1)%2) && CheckXY(x,y) > 0) {
      SetBoard(i , j , c) ; 
      i-- ;
    }
  }
  /* upper left */
  i = x - 1 ;
  j = y + 1 ;
  if((allowMove & 32) != 0) {
    while(GetBoard(i,j) == ((c+1)%2) && CheckXY(x,y) > 0) {
      SetBoard(i , j , c) ; 
      i-- ;
      j++ ; 
    }
  }
  /* up */
  i = x ;
  j = y + 1 ;
  if((allowMove & 64) != 0) {
    while(GetBoard(i,j) == ((c+1)%2) && CheckXY(x,y) > 0) {
      SetBoard(i , j , c) ; 
      j++ ; 
    }
  }
  /* upper right */
  i = x + 1 ;
  j = y + 1 ;
  if((allowMove & 128) != 0) {
    while(GetBoard(i,j) == ((c+1)%2) && CheckXY(x,y) > 0) {
      SetBoard(i , j , c) ; 
      i++ ; 
      j++ ;
    }
  }

  return 1 ;

}

