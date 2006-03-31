/* send request and receive result */
public class evalThread extends Thread {
    
    evalPara para ;
    XversiBoard b ;
    int side ;
    int t ;
    int tn ;
    XversiInterface interf ;
    int move ;
    String host ;
 
    
    public evalThread(evalPara param , XversiBoard board , int c , int turn , int turnNum , int m , XversiInterface intface , String h) {
        super() ;
        para = param ;
        b = board ;
        side = c ;
        t = turn ;
        tn = turnNum ;
        interf = intface ;
        move = m ;
	host = new String(h.toString()) ;
    }
    
    public void run() {
        int score = -999999 ;
        
        try {
            score = interf.getEvalScore(b , side , t , tn) ;
        } catch(Exception e) { System.out.println("error: " + e.getMessage()) ; }
	System.out.println("host: " + host + " move = " + move + " score = " + score) ;
        para.lock.down() ;
        
        //para.s = new Integer(score) ;
        if(score > para.bS.intValue()) {
            para.bS = new Integer(score)  ;
            para.bM = new Integer(move) ;
            para.bList.ClearList() ;
            para.bList.InsertMove(para.bM.intValue()) ;
            
        } else if(score == para.bS.intValue()) {
            para.bList.InsertMove(move) ;
        }
        para.counter = new Integer(para.counter.intValue() + 1) ;
        para.lock.up() ;
        //applet.notifyAll()  ;
    }
    
    
}
