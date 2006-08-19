import java.io.Serializable ;


/* providing look up for values of 2^power */
public class Power2 implements Serializable {

private final static int POWER_SIZE = 64 ;
private static long[] power = new long[POWER_SIZE] ;

public static void Init() {

  int i ;
  long count = 1 ;

  for(i = 0 ; i < POWER_SIZE ; i++) {
    power[i] = count ;
    count *= 2 ;
  }

}

public static long GetPower(int n) {
  
  if(n >= 0 && n <= POWER_SIZE) 
    return power[n] ;
  else
    return 0 ;

}

}
