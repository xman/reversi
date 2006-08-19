import java.io.Serializable ;

public class XversiBoard implements Serializable {

public final static int BOARD_SIZE = 8 ;
public final static int BOARD_SQUARE = 64 ;

protected long board ;
protected  long boardFilled ;
public  int wc ;
public  int bc ;

public void Init() {

  board = 0 ;
  boardFilled = 0 ;
  wc = 2 ;
  bc = 2 ;

  board |= Power2.GetPower(28) | Power2.GetPower(35) ;
  boardFilled |= Power2.GetPower(27) | Power2.GetPower(28) | Power2.GetPower(35) | Power2.GetPower(36) ;

}

public void Copy(XversiBoard b) {
 board = b.board ;
 boardFilled = b.boardFilled ; 
 wc = b.wc  ;
 bc = b.bc  ;

}

public int GetWC() {
  return wc ;
}

public int GetBC() {
  return bc ;
}
public boolean SetBoard(int x , int y , int c) {

  int position ;
  long power ;
  int t ; 
  
  if(!XversiAgent.CheckXY(x,y))
    return false ;

  if(c == 0) {
    if((t = GetBoard(x,y)) == 1) {
      bc++ ;
      wc-- ;
    } else if(t == -1) {
      bc++ ;
    }

  } else if(c == 1) {
    if((t = GetBoard(x,y)) == 0) {
      wc++ ;
      bc-- ;
    } else if(t == -1) {
      wc++ ;
    }
  } else return false ;

  position = XversiAgent.TranslateXY(x,y) ;
  power = Power2.GetPower(position-1) ;
  if(c == 0) {
    boardFilled |= power ;
    board &= ~power ;
  } else if(c == 1) {
    boardFilled |= power ;
    board |= power ;
  } else {
    boardFilled &= ~power ;
  }

  return true ;

}
public int GetBoard(int x , int y) {

  int position ;
  long power ;

  if(!XversiAgent.CheckXY(x,y)) 
    return -1 ;

  position = XversiAgent.TranslateXY(x,y) ;
  power = Power2.GetPower(position-1) ;
  if((boardFilled & power) == 0)
    return -1 ;
  else if((board & power) == 0)
    return 0 ;
  else return 1 ;


}

public void PrintBoard() {

  int x , y  ;

  for(y = BOARD_SIZE ; y > 0 ; y--) {
    for(x = 1 ; x <= BOARD_SIZE ; x++) {
      if(GetBoard(x,y) < 0)
        System.out.print('.') ;
      else if(GetBoard(x,y) == 1)
        System.out.print('X') ;
      else if(GetBoard(x,y) == 0)
        System.out.print('O') ;
      else
        System.out.print('?') ; 
    }
    System.out.println(' ') ;
  } 
  System.out.println(' ') ;
}

}
