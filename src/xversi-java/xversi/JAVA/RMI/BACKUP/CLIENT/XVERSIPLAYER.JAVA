public class XversiPlayer {

  private int side ;

  public XversiPlayer(int s) {
    Init(s) ;
  }

  public void Init(int s) {
    side = s ;
  }

  public boolean SetSide(int c) {
    if(XversiAgent.CheckSide(c)) {
      side = c ; 
      return true ;
    } else {
      return false ;
    }

  }

  public void SwitchSide() {
    side = (side + 1) % 2 ;
  }

  public int GetSide() {
    return side ;
  }

}

