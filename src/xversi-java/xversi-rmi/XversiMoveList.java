public class XversiMoveList {

  private int num ;
  private int maxNum ;
  private int[] list ;

  public XversiMoveList(int n) {
    list = new int[n] ;
    maxNum = n - 1 ;
    num = -1 ;
  }

  public void ClearList() {
    num = -1 ;

  }

  public boolean ClearMove(int n) {
    int t ;
    if(n > num) return false ;
    list[n] = list[num] ;
    num-- ;
    return true ;
  }

  public int GetMaxNum() {
    return maxNum ;
  }

  public int GetNum() {
    return num ;
  }

  public int GetMove(int n) {
    if(n > num) return -1 ;
    return list[n] ;
  }

  public boolean InsertMove(int n) {
    if(num >= maxNum) {
      return false ; 
    } 
    num++ ;
    list[num] = n ;
    return true ;
  }

}
