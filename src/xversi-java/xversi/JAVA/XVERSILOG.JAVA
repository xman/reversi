import java.io.* ;

public class XversiLog { 

  public int totalLookup = 0 ;
  public final int size = 32 ;
  public XversiLogComponent[] xc  ;  
  public int pUsed = 0 ;
  public String filename = new String("logfile") ;
  public RandomAccessFile logFile ;
  public final int bytesPerComponent = 36 ;

  XversiLog() {
    try {
    System.out.println("xversilog created") ;
    xc = new XversiLogComponent[size] ;  
    int i ;
    for(i = 0 ; i < size ; i++) {
      xc[i] = new XversiLogComponent() ;
    }
    init() ;   
    } catch(IOException e) { System.out.println(e.getMessage()) ; } 
  }

  protected void init() throws IOException  {
    logFile = new RandomAccessFile(filename , "rwd") ;
  }

  public int find(long boardFilled , long board , int side)  throws IOException  {
    int i ;
    long n ;

    if(pUsed > size) System.out.println("exceed temp storage size") ;
    for(i = 0 ; i < pUsed ; i++) {
      if(xc[i].boardFilled == boardFilled && xc[i].board == board && xc[i].side == side) {
         totalLookup++ ;
         return i ; 
      }
    }

//    System.out.println("checkpoint") ;
    n = logFile.length() / (long)bytesPerComponent ;
    for( ; i < n + pUsed  ; i++) {
      if(findFile(i - pUsed , boardFilled , board , side) == true) {
         totalLookup++ ;
        return i ;
      }
    }    
//    System.out.println("checkpoint2") ;
    return -1 ;

  }

  public boolean findFile(int i , long boardFilled , long board , int side)  throws IOException  {
    long position ;
    position = (long) i * bytesPerComponent ;
    logFile.seek(position) ;
    if(logFile.readLong() != boardFilled) return false ;
    if(logFile.readLong() != board) return false ;
    logFile.readInt() ;
    logFile.readInt() ;
    if(logFile.readInt() != side) return false ;
    return true ;

  }


  public int getScore(int i)  throws IOException  {

    long position ;

    if(i < pUsed)
      return xc[i].score ;
    else {
      i -= pUsed ;
      position = (long) i * bytesPerComponent ;
      logFile.seek(position) ;
      logFile.readLong() ;
      logFile.readLong() ;
      logFile.readInt() ;
      logFile.readInt() ;
      logFile.readInt() ;
      logFile.readInt() ;
      return logFile.readInt() ;

    }
  }

  public boolean insert(long bf , long b , int bcount , int wcount , int s , int l , int sc)  throws IOException  {
    int i ;
    if(pUsed >= size) return false ;
    pUsed++ ;
    i = pUsed - 1 ;
//    System.out.println(" i = " + i + " ") ;

    xc[i].boardFilled = bf ;
    xc[i].board = b ;
    xc[i].bc = bcount ;
    xc[i].wc = wcount ;
    xc[i].side = s ;
    xc[i].level = l ;
    xc[i].score = sc ;
    return true ;

  }

  public boolean isFull() {
    if(pUsed >= size - 1) return true ;
    return false ;
  }

  public void write()  throws IOException  {

    long position ;
    position = logFile.length() ;
    logFile.seek(position) ;
    
    int i ;
    for(i = 0 ; i < pUsed ; i++) {
      logFile.writeLong(xc[i].boardFilled) ;
      logFile.writeLong(xc[i].board) ;
      logFile.writeInt(xc[i].bc) ;
      logFile.writeInt(xc[i].wc) ;
      logFile.writeInt(xc[i].side) ;
      logFile.writeInt(xc[i].level) ;
      logFile.writeInt(xc[i].score) ;

    }

    pUsed = 0 ;

  }

  public void close()   throws IOException  {
    logFile.close() ;
  }

}