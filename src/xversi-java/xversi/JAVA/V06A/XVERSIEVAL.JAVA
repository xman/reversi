public class XversiEval {
  
  public static long Evaluate(XversiBoard b , int c , int turn , int turnNum) {

    int i , j , k ;
    int count , count2 ;
    int score ;

    if(!XversiAgent.CheckSide(c))
      return -99999999 ;





    count = 0 ;
    count2= 0 ;
    for(i = 1 ; i <= XversiBoard.BOARD_SIZE ; i++) {
      for(j = 1 ; j <= XversiBoard.BOARD_SIZE ; j++) {
        if((k = b.GetBoard(i,j)) == 0) {
          count++ ;
        } else if(k == 1) {
          count2++ ;
        }

      }
    }

  

    if(c == 0)
      score = count - count2 ;
    else if(c == 1)
      score =  count2 - count ;

    score = 0 ;
    count = XversiAgent.CountMove(b,c) ;
    count2= XversiAgent.CountMove(b,(c+1)%2) ;

    if(c == 0)
      score += count - count2 ;
    else
      score += count2 - count ;

    return score ; 
  }

  public long Mobility(XversiBoard b , int c , int turn , int turnNum) {

    return 0 ;

  }
}

