public class Sema {
    private int n ;
    
    public Sema(int m) {
        n = m ;
    }
    
    public synchronized void down() {
        
        while(n == 0) {
            try{
                wait() ;
            } catch (InterruptedException e) { }
        }
        
        
        n-- ;
    }
    
    public synchronized void up() {
        n++ ;
        notifyAll() ;
    }
    
}