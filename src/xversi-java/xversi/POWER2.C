#include "power2.h"

void InitPower(void) {

  int i ;
  int count = 1 ; 

  for(i = 0 ; i < POWER_SIZE ; i++) {
    power[i] = count ;
    count *= 2 ;
  } 


}


int GetPower(int n) {

  if(n == 0) 
    return 1 ;
  else if(n > 0 && n <= POWER_SIZE)
    return power[n-1] ; 
  else 
    return 0 ;

}


