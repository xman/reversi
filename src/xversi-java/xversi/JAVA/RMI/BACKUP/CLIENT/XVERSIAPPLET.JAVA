import java.applet.Applet;
import java.lang.Math ;
import java.awt.* ;
import java.awt.event.* ;
import java.io.Serializable ;

import java.rmi.* ;

public class XversiApplet extends Applet implements ActionListener,MouseListener  {
    
    protected int numThreads = 1 ;

    protected int numComputeNode = 1 ;

    
    private Spot s = new Spot() ;
    
    XversiBoard b = new XversiBoard() ;
    XversiAgent agent = new XversiAgent() ;
    XversiPlayer player = new XversiPlayer(0) ;
    
    private boolean paintNeeded ;
    private int turn ;
    private int turnNum ;
    private boolean progress ;
    private int lastMove ;
    //private int bWin = 0 ;
    //private int wWin = 0 ;
    //private int bwDraw = 0 ;
    
    Button resetButton ;
    Button switchButton ;
    Label turnLabel ;
    Label humanLabel ;
    Label blackLabel ;
    Label whiteLabel ;
    //Label blackWin ;
    //Label whiteWin ;
    //Label draw ;
    
    protected XversiInterface evalInterface[] = new XversiInterface[numComputeNode] ;
    //protected XversiInterface evalInterface2;
    
    public void init() {
        
        
        //  bWin = 0 ;
        //  wWin = 0 ;
        //  bwDraw = 0 ;
        
        try {
            evalInterface[0] = (XversiInterface) Naming.lookup("rmi://155.69.146.48:9394/XversiEvalFunc") ;
            //evalInterface = (XversiInterface) Naming.lookup("rmi://xman/XversiEvalFunc2") ;
        } catch (Exception e) { System.out.println(e) ; }
        
        Power2.Init() ;
        resetButton = new Button("Reset") ;
        add(resetButton) ;
        switchButton = new Button("Switch") ;
        add(switchButton) ;
        blackLabel = new Label("Black: 2") ;
        add(blackLabel) ;
        whiteLabel = new Label("White: 2") ;
        add(whiteLabel) ;
        humanLabel = new Label("You: Black") ;
        add(humanLabel) ;
        turnLabel = new Label("Turn: Black") ;
        add(turnLabel) ;
        //blackWin  = new Label("Bwin: 0") ;
        //add(blackWin) ;
        //whiteWin  = new Label("Wwin: 0") ;
        //add(whiteWin) ;
        //draw = new Label("Draw: 0") ;
        //add(draw) ;
        
        resetButton.addActionListener(this) ;
        switchButton.addActionListener(this) ;
        addMouseListener(this) ;
        
        reset() ;
    }
    
    private void reset() {
        b.Init() ;
        agent.Init() ;
        player.Init(1) ;
        
        turn = 0 ;
        turnNum = 4 ;
        humanLabel.setText("You: Black") ;
        turnLabel.setText("Turn: Black") ;
        progress = true ;
        lastMove = -1 ;
        paintNeeded = true ;
        repaint() ;
    }
    
    public void start() {
        
        XversiMoveList list ;
        XversiMoveList bestList ;
        XversiMoveList tempList ;
        
        int i  ;
        //int bestMove = 0  ;
        Integer bestMove = new Integer(0) ;
        //int bestScore = -9999999 ;
        Integer bestScore = new Integer(0) ;
        //Integer maxScore = new Integer(-999999) ;
        Sema lock = new Sema(1) ;
        int move ;
        //int score ;
        Integer score = new Integer(0) ;
        Integer count = new Integer(0) ;
        int totalM = 0 ;
        
        //    player.SwitchSide() ;
        //    if(player.GetSide() == 0) {
        //      humanLabel.setText("You: Black") ;
        //    } else {
        //      humanLabel.setText("You: White") ;
        //    }
        
        
        
        if(paintNeeded == true) {
            repaint() ;
            return ;
        }
        
        if(progress && player.GetSide() == turn) {
            if((list = XversiAgent.GenerateMove(b , turn)) != null) {
                
                totalM = list.GetNum() + 1 ;
                bestScore = new Integer(-9999999) ;
                bestList = new XversiMoveList(totalM) ;
                
                // evalPara(int Move , Integer bestMove , Integer bestScore , Integer score , Sema lk , XversiInterface intface , Integer count , XversiBoard bb , int cc , int turn , int turnNum , XversiMoveList bestList
                evalPara parameters = new evalPara(0 , bestMove , bestScore , score , lock , count , bestList) ;
                
                for(i = 0 ; i < totalM ; i++) {
                    XversiBoard bb = new XversiBoard() ;
                    move = list.GetMove(i) ;
                    XversiAgent.TranslatePosition(move , s) ;
                    bb.Copy(b) ;
                    XversiAgent.MakeMove(bb , s.x , s.y , turn) ;
                    
                    // parameters.move = move ;
                    score = new Integer(-999999) ;
                    
                    try {
                        evalThread ethread = new evalThread(parameters , bb , turn , (turn+1)%2 , turnNum+1 , move , evalInterface[i%numComputeNode])  ;
                        ethread.start() ;
                        
                        
                        //score = evalInterface.getEvalScore(bb , turn , (turn+1)%2 , turnNum+1) ;
                        
                        
                    } catch (Exception e) { System.out.println(e) ; }
                    
                    

                    while(true) {
                        try {
                        Thread.sleep(400) ;
                        } catch(InterruptedException e) { }
                        lock.down() ;
                        //System.out.println("count val : " + parameters.counter.intValue()) ;
                        //System.out.println("i " + i) ;
                        //System.out.println("score : " + parameters.s.intValue() ) ;
                        if(parameters.counter.intValue() >= totalM || parameters.counter.intValue() + numThreads - 1 > i ) {
                            
                            lock.up() ;
                            break ;
                        }
                        lock.up() ;
                    }


                    //System.out.println("Move : x = " + s.x + " y = " + s.y + " score = " + score) ;
                    
                    
                    
      /*if(score > bestScore) {
        bestScore = score ;
        bestMove = move ;
        bestList.ClearList() ;
        bestList.InsertMove(bestMove) ;
        if(bestScore > 9000000)
          break ;
       
      } else if(score == bestScore) {
        bestList.InsertMove(move) ;
      }*/
                }
                
                try {
                    while(true) {
                        Thread.sleep(400) ;
                        lock.down() ;
                        //System.out.println("count val : " + parameters.counter.intValue()) ;
                        //System.out.println("totalM " + totalM) ;
                        if(parameters.counter.intValue() >= totalM) {
                            
                            lock.up() ;
                            break ;
                        }
                        lock.up() ;
                        //                        lock.down() ;
                        //                        if(count.intValue() < totalM) {
                        //                            lock.up() ;
                        
                        //                            this.wait() ;
                        //                        } else break ;
                    }
                } catch (InterruptedException e) { }


		System.out.println("best score :  " + parameters.bS.intValue()) ;		
                
                int k ;
                k = (int)(Math.random() * (bestList.GetNum()+1)) ;
                k = bestList.GetMove(k) ;

                lastMove = k ;
                XversiAgent.TranslatePosition(k , s) ;
                System.out.println("Move take: " + k + " x = " + s.x + " y = " + s.y) ;
                XversiAgent.MakeMove(b , s.x , s.y , turn) ;
                turnNum++ ;
                
                paintNeeded = true ;
                repaint() ;
            }
            
            if(XversiAgent.CountMove(b , (turn+1)%2) > 0) {
                turn = (turn + 1) % 2 ;
            } else {
                if(XversiAgent.CountMove(b , turn) > 0) {
                    start() ;
                } else {
                    progress = false ;
                }
                
            }
            if(turn == 0) {
                turnLabel.setText("Turn: Black") ;
            } else {
                turnLabel.setText("Turn: White") ;
            }
            
        }
        
        
        
    }
    
    public void paint(Graphics g) {
        
        int i,j ;
        int bc , wc ;
        int w ;
        int h ;
        
        
        bc = wc = 0 ;
        
        g.setColor(Color.gray) ;
        g.drawRect(1,1, getSize().width - 1 , getSize().height - 1) ;
        
        g.setColor(Color.black) ;
        for(i = 0 ; i <= 8 ; i++) {
            g.drawLine((getSize().width-1)* i / 8 , getSize().height / 9 , (getSize().width-1) * i / 8  , getSize().height) ;
            g.drawLine(0 , ((getSize().height - 1) * (i+1)) / 9 , getSize().width , ((getSize().height - 1) * (i+1)) / 9) ;
        }
        
        w = getSize().width / 8 ;
        h = getSize().height / 9 ;
        for(i = 1 ; i <= XversiBoard.BOARD_SIZE ; i++) {
            for(j = 1 ; j <= XversiBoard.BOARD_SIZE ; j++) {
                if(b.GetBoard(i,j) >= 0) {
                    if(b.GetBoard(i,j) == 0) {
                        g.setColor(Color.black) ;
                        bc++ ;
                    } else {
                        g.setColor(Color.red) ;
                        wc++ ;
                    }
                    s.x = (i-1) * w + w/2 ;
                    s.y = j * h + h/2 ;
                    g.fillOval(s.x - w*4/10 , s.y - h*4/10 , w*4/5 ,h*4/5) ;
                }
            }
        }
        
        if(lastMove > 0) {
            XversiAgent.TranslatePosition(lastMove , s) ;
            g.setColor(Color.green) ;
            s.x = (s.x-1) * (getSize().width / 8) + (getSize().width/16) ;
            s.y = s.y * (getSize().height / 9) + (getSize().height/18) ;
            g.fillOval(s.x - 2 , s.y - 2, 2*2 , 2*2) ;
        }
        
        whiteLabel.setText("White: " + wc ) ;
        blackLabel.setText("Black: " + bc ) ;
        
        //  if(progress == false) {
        
        //       if(wc > bc) {
        //          wWin ++ ;
        //          whiteWin.setText("WWin: " + wWin) ;
        //        }
        //        else if(wc < bc) {
        //          bWin ++  ;
        //          blackWin.setText("BWin: " + bWin) ;
        //        }
        //        else if(wc == bc){
        //          bwDraw ++ ;
        //          draw.setText("Draw: " + bwDraw) ;
        //        }
        
        
        
        
        //    try {
        //    Thread.sleep(5000) ;
        //    } catch (InterruptedException e) { }
        
        //    reset() ;
        //  }
        
        if(paintNeeded == true) {
            paintNeeded = false ;
            start() ;
        }
        
    }
    
    public void mousePressed(MouseEvent event) {}
    public void mouseClicked(MouseEvent event) {
        
        int x , y ;
        
        if(progress && player.GetSide() != turn) {
            
            x = event.getX() / (getSize().width / 8) + 1 ;
            y = event.getY() / (getSize().height / 9) ;
            
            if(XversiAgent.CheckXY(x,y)) {
                
                if(XversiAgent.CheckMove(b , x , y , turn) > 0) {
                    XversiAgent.MakeMove(b , x , y , turn) ;
                    lastMove = -1 ;
                    turnNum ++ ;
                    paintNeeded = true ;
                    repaint() ;
                    if(XversiAgent.CountMove(b , (turn + 1)%2) > 0) {
                        turn = (turn+1) % 2 ;
                        if(turn == 0) {
                            turnLabel.setText("Turn: Black") ;
                        } else {
                            turnLabel.setText("Turn: White") ;
                        }
                        start() ;
                        
                    } else {
                        if(XversiAgent.CountMove(b , turn) <= 0) {
                            progress = false ;
                        }
                    }
                    
                }
                
            } else { start() ; }
        } else {
            repaint() ;
        }
        
    }
    
    public void stop() {
        paintNeeded = true ;
        repaint() ;
        
    }
    
    public void mouseReleased(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == resetButton) {
            reset() ;
        } else if(e.getSource() == switchButton) {
            player.SwitchSide() ;
            if(player.GetSide() == 1) {
                // computer is white
                humanLabel.setText("You: Black") ;
            } else {
                // computer is black
                humanLabel.setText("You: White") ;
            }
            paintNeeded = true ;
            repaint() ;
            
        }
        
    }
    
    
}
