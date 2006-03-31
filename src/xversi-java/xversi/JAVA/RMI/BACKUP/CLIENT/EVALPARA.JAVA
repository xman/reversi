public class evalPara {
 
    public Integer bM ;
    public Integer bS ;
    public Integer s  ;
    public Sema lock  ;
    public Integer counter ;
    public XversiMoveList bList ;
    public int move ;
   
    public evalPara(int Move , Integer bestMove , Integer bestScore , Integer score , Sema lk , Integer count , XversiMoveList bestList) {
        bM = bestMove ;
        bS = bestScore ;
        s = score ;
        lock = lk ;
        counter = count ;
        bList = bestList ;
        move = Move ;
    }
    
    
}