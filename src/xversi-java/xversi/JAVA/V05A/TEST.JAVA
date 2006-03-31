import java.applet.Applet; 
import java.awt.* ;
import java.awt.event.* ;

public class test extends Applet {
int i = 0 ;
public void start() {
try {
for( ; i < 10 ; i++) {
System.out.println(i) ;
Thread.sleep(400) ;
}
} catch (InterruptedException e) {}
}

public void stop() {
i = -1 ;
}
}

