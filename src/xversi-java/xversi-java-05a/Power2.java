public class Power2 {

private final static int POWER_SIZE = 64 ;
private static long[] power = new long[POWER_SIZE] ;

public static void Init() {

  int i ;
  long count = 1 ;

  for(i = 0 ; i < POWER_SIZE ; i++) {
    power[i] = count ;
    count *= 2 ;
  }

  System.out.println("count = " + power[63]) ;

}

public static long GetPower(int n) {
  
  if(n >= 0 && n <= POWER_SIZE) 
    return power[n] ;
  else
    return 0 ;

}

}
