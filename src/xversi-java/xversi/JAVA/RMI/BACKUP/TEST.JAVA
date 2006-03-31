import java.applet.Applet; 
import java.awt.* ;
import java.awt.event.* ;

public class test extends Applet {
int i = 0 ;
int j = 0 ;
double x[][] = new double[2048][2048] ;


public void start() {

for( i=0; i < 2048 ; i++) {
for(j=0 ; j < 2048 ; j++) {

x[i][j] = i+j ;
System.out.println(x[i][j]) ;




}
}
}

public void stop() {
i = -1 ;
}
}

