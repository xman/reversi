import java.rmi.* ;
import java.rmi.server.* ;



public class XversiEval extends UnicastRemoteObject implements XversiInterface {

  static Sema llock ;
 
  public int maxLevel = 4 ;
  public int lastnm = 13 ;
  private int side ;  


  public static void main(String[] args) {
    Power2.Init() ;
    String host = new String("") ;
    llock = new Sema(1) ;
    try {
      System.setSecurityManager(new RMISecurityManager()) ;
      XversiEval instance = new XversiEval() ;
	System.out.println("checkpoint") ;
      Naming.rebind("rmi://"+host+"9394/XversiEvalFunc" , instance) ;
      System.out.println("Registered XversiEvalFunc") ;

    } catch (Exception ex) {
      System.out.println(ex) ;
    }


  }

  public int getEvalScore(XversiBoard b , int c , int turn , int turnNum) throws RemoteException {

        
    int score ;
    score = Evaluate(b , c , turn , turnNum) ;
//    llock.down() ;
//    System.out.println("board: " + b.board) ;
//    System.out.println("boardFilled: " + b.boardFilled) ;
//    b.PrintBoard() ;    
//    System.out.println("score " + score) ;
//    llock.up() ;
    return score ;

  }


  public XversiEval() throws RemoteException { super() ; }
  

  public int Evaluate(XversiBoard b , int c , int turn , int turnNum) throws RemoteException {

    int score ;

    if(!XversiAgent.CheckSide(c))
      return -9999998 ;

    side = c ;

    if(64 - turnNum <= lastnm) 
      score = Eval(b , turn , 64 - turnNum , turnNum , -9999996) ;
    else
      score = Eval(b , turn , maxLevel , turnNum , -9999996) ;
    return score ; 
  }

  protected int Eval(XversiBoard b , int turn , int level , int turnNum , int prevScore) throws RemoteException {

    int i ;
    int numMove ;
    int move ;
    Spot s ;
    int score ;
    int bestScore ;
    XversiMoveList list ;
    XversiBoard bb ;

    if(level == 0) {
      if(64 == turnNum)
        return BinPieceCount(b) ;
      return ComputeEval(b,turn,turnNum) ;
    }
    list = XversiAgent.GenerateMove(b , turn) ; 
    if(list == null) {
      if(XversiAgent.GenerateMove(b , (turn+1)%2) == null) {
        return BinPieceCount(b) ;  
      }
      if(side == turn) {
	bestScore = -9999990 ;
        return Eval(b , (turn+1)%2 , level-1 , turnNum+1 , bestScore) ; 
      } else {
	bestScore = 9999990 ;
        return Eval(b , (turn+1)%2 , level-1 , turnNum+1 , bestScore) ; 
      }
    } else {


      bb = new XversiBoard() ;
      s = new Spot() ;
      numMove = list.GetNum() ;
      if(side == turn) {
        bestScore = -9999997 ; 
        for(i = 0 ; i <= numMove ; i++) {
           bb.Copy(b) ;
           move = list.GetMove(i) ; 
           XversiAgent.TranslatePosition(move , s) ;
           XversiAgent.MakeMove(bb , s.x , s.y , turn) ;
           score = Eval(bb , (turn+1) % 2 , level - 1 , turnNum+1 , bestScore) ;
           if(score >= prevScore)
             return score ;
           if(score > bestScore) {
             bestScore = score ;
           }
        }

      } else {
        bestScore = 9999997 ;
        for(i = 0 ; i <= numMove ; i++) {
           bb.Copy(b) ;
           move = list.GetMove(i) ;
           XversiAgent.TranslatePosition(move , s) ;
           XversiAgent.MakeMove(bb , s.x , s.y , turn) ;
           score = Eval(bb , (turn+1) % 2 , level - 1 , turnNum+1 , bestScore) ;
           if(score <= prevScore) {
             return score ;
           }
           if(score < bestScore) {
             bestScore = score ;
           }
        } 

      }

      return bestScore ;

    } 

  }

  protected int ComputeEval(XversiBoard b , int turn , int turnNum) throws RemoteException  {

    int score ;

    score = PieceCount(b , turn , turnNum) ;
    if(score > 9000000) return score ;
    score += Mobility(b , turn , turnNum) ;
    score += CheckCorner(b , turn) ;
    score += CenterCheck(b , turn) ;

    return score ;
  }

  protected int CenterCheck(XversiBoard b , int turn) throws RemoteException  {

    final int d = 1 ;
    int piece ;
    int opponent ;
    int score = 0 ;

    opponent = (side+1) % 2 ;

    piece = b.GetBoard(4,4) ;
    if(piece == side) 
      score += d ;
    else if(piece == opponent)
      score -= d ;

    piece = b.GetBoard(4,5) ;
    if(piece == side) 
      score += d ;
    else if(piece == opponent)
      score -= d ;

    piece = b.GetBoard(5,4) ;
    if(piece == side) 
      score += d ;
    else if(piece == opponent)
      score -= 1 ;
    piece = b.GetBoard(5,5) ;
    if(piece == side) 
      score += d ;
    else if(piece == opponent)
      score -= 1 ;

    return score ;

  }


  protected int BinPieceCount(XversiBoard b) throws RemoteException {

    int count , count2 ;
    int temp ;
    int i , j ; 
    int k ;
    int score ;

    if(side == 0) {
      count  = b.GetBC() ;
      count2 = b.GetWC() ;
    } else if(side == 1) {
      count  = b.GetWC() ;
      count2 = b.GetBC() ;
    } else {
      return -9999991 ;
    }

      if(count > count2) {
        return 9999994 ;
      } else if(count < count2) {
        return -9999994;
      } else {
        return 0 ; 
      }

  }



  protected int PieceCount(XversiBoard b , int turn , int turnNum) throws RemoteException  {

    int count , count2 ;
    int temp ;
    int i , j ; 
    int k ;
    int score ;

    if(side == 0) {
      count  = b.GetBC() ;
      count2 = b.GetWC() ;
    } else if(side == 1) {
      count  = b.GetWC() ;
      count2 = b.GetBC() ;
    } else {
      return -9999991 ;
    }

    if(count == 0) return -9999984 ;
    if(count2 ==0) return 9999984  ;

    if(XversiBoard.BOARD_SQUARE == turnNum) {
      if(count > count2) {
        return 9999974 ;
      } else if(count < count2) {
        return -9999974;
      } else {
        return 0 ; 
      }
    }


    score = count - count2 ;
    if(turnNum < 45) {
      if((count+count2) / count > count + count2 - 8)  {
        score *= 2 ;
      } else {
        score *= -2 ;
      }
    } else {
      score *= 3 ;
    } 
    //System.out.println("piece score: " + score) ;
    return score ;
  }


  protected int Mobility(XversiBoard b , int turn , int turnNum) throws RemoteException  {

    int count ;
    int count1;
    int count2;
    
    int score ;

    count1 = XversiAgent.CountMove(b,turn) ;
    count2= 0 ; //XversiAgent.CountMove(b,(turn+1)%2) ;
    if(turn == side) {
      count = count1 - count2 ;
      if(count > 5) count = 5 ;
      score = 4*count ;
    } else {
      count = count2 - count1 ;
      if(count > 5) count = 5 ;
      if(turnNum <= 55) {
//      if(count1 == 3)
//        score = 4 ;
//      else if(count1 == 2) 
//        score = 10 ;
//      else if(count1 == 1)
//        score = 20 ;      
//      else
        score = -4*count ;
      } else 
	score = -5*count ;
       
    }
    
    return score ;
  }

  protected int CheckCorner(XversiBoard b , int turn) throws RemoteException  {
    
    int count  ;
    int i , j ;
    int c[] = new int[4] ;
    int d ;
    int ch1, ch2 ;
    int score ;

    score = 0 ;
    count = 0 ;
    for(i = 1 ; i <= 8 ; i+=7) {
      for(j = 1 ; j <= 8 ; j+=7) {
        if((c[count] = b.GetBoard(i,j)) == side) {
          score += 20 ;
        } else if(c[count] == (side+1) % 2) {
          score -= 25 ;
        } else {
          ch1 = XversiAgent.CheckMove(b , i , j , side) ;
          ch2 = XversiAgent.CheckMove(b , i , j , (side+1) % 2) ;
          if(ch1 > 0 && (turn == side || ch2 <= 0)) {
            score += 20 ;
          } else if(ch2 > 0 && (turn != side || ch1 <= 0)) {
            score -= 25 ;
          }
          c[count] = -1 ;
        } 
        count++ ;
      }
    } 

    if(c[0] == -1) {
      if((d = b.GetBoard(2,2)) == side) {
        score -= 25 ;
      } else if(d == (side + 1) % 2) {
        score += 20 ;
      }
    } 

    if(c[1] == -1) {
      if((d = b.GetBoard(2,7)) == side) {
        score -= 25 ;
      } else if(d == (side + 1) % 2) {
        score += 20 ;
      }
    }

    if(c[2] == -1) {
    if((d = b.GetBoard(7,2)) == side) {
        score -= 25 ;
      } else if(d == (side + 1) % 2) {
        score += 20 ;
      }
    }
  
    if(c[3] == -1) {
      if((d = b.GetBoard(7,7)) == side) {
        score -= 25 ;
      } else if(d == (side + 1) % 2) {
        score += 20 ;
      }

    }
    //System.out.println("Corner: " + score) ;
    return score ;
 
  }

}

