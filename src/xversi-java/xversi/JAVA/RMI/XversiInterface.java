import java.rmi.* ;

public interface XversiInterface extends Remote {

  int getEvalScore(XversiBoard b , int c , int turn , int turnNum) throws RemoteException ;
  

}