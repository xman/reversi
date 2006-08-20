import java.awt.*;
import java.util.*;
import java.io.*;
/*
* full contact info:
* Sieuwert van Otterloo
* Rijnlaan 33bis
* 3522BB Utrecht
* homepage www.bluering.nl or go.to/sieuwert
* phone +31615524227
* coauthor Ernst van Rheenen
*/


/** 
* reversi.java is an applet for playing the reversi othello board game against the computer.
* Designed by 
* <a href="http://www.bluering.nl">bluering software development </a>. 
* You can send questions about this source to 
* <a href="mailto:sieuwert@bluering.nl">sieuwert@bluering.nl</a>
* this applet can be freely used for whatever you want if you first ask permission from
* the author.
* <h2>project overview</h2>
* This applet consists of several classes.We have put all the classes in one file 
* for easy downloading. To get the right 
* javadoc documentation, we had to make all classes and many methods public. You
* might want to put each class in a separate file with the filename equal to the class 
* name. You can also make all classes except reversi non-public (just remove the word public)
* to avoid errors. The drawback is that you will not see those classes in the documentation.
* <p>the javadoc files contain much of the comment, but not all of it. Check the sourcecode
* for the last details. The list of all classes is:
* <dl>
*<dt>reversi</dt><dd>the toplevel applet class. It contains and handles the buttons. </dd>
*<dt>newgamewindow</dt><dd>the window that allows you to set up a new game. It appears when 
* you press new game</dd>
*<dt>boardview</dt><dd> the class that paints the pieces. It also handles the mouse clicks.</dd>
*<dt>board </dt><dd>The class containing the state of the board. This class knows the rules
* of the game. Designers say it contains the game logic. It is in some way the most pure, and basic
* class, where all others rely on.</dd>
*<dt>player </dt><dd>the functions to calculate what move to do. This class contains
* the minimax algorithm with the alpha-beta heuristic. With these techniques a search is
* done to find the best position it can reach. The other classes are needed for estimating the
* winning value of positions.</dd>
*<dt>weightvector</dt><dd>This class contains the adjustable parameters that are needed for
* determining the values of positions.</dd>
*<dt>genetictrain </dt><dd>This class contains the genetic algorithm that can search the best
* weight vector. If you want to search a good weight vector, you must run this class, and
* in a few minutes it will select good weight vectors by letting them play against each other.</dd>
*<dt>file</dt><dd>This is just a utility class for reading and writing files in an easy way.</dd>
* </dl>
* <p>
*/


public class reversi extends java.applet.Applet{
  /**
  * the interface components in this applet. slabel means status label.
  */ 
  Label slabel=new Label("please do a move");
  board b=new board();
  boardview bview=new boardview(b,this);
  Button newgame=new Button("new game"),
        undo=new Button("undo move");
 
 /**
 * constructor for running this applet as an application
 */
 public reversi(){}
 
 /**
 * the init method is called when the applet is loaded by the browser. 
 */ 
 public void init(){
  setBackground(Color.white);//you can set the background color of the applet in this line
  //resize(bview.SX+30,bview.SY+100); //this line not needed in applets, they cannot resize
  Panel buttonpanel=new Panel();//panel to keep buttons together
  buttonpanel.setLayout(new GridLayout(1,3));//add three buttons beside each other
  buttonpanel.add(newgame);
  buttonpanel.add(undo);
  Panel superpanel=new Panel();
  superpanel.setLayout(new GridLayout(3,1));
  superpanel.add(slabel);
  superpanel.add(buttonpanel);
  setLayout(new BorderLayout());
  add("North",bview);
  add("South",superpanel); 
 }
 
 /**the start method of the applet. It is called any time the window re-appears on the
 * screen. All we do here is start the animation.
 */
 public void start()
    {bview.start();}
 /**
 * here we stop the animation
 */
 public void stop()
    {bview.stop();}
 /**
 * the action method is called when a button is pressed. It first checks whether
 * bview might be busy. In that case, it does nothing. Else it undoes a move or 
 * launches a new game window.
 * @return true to indicate success
 */
 public boolean action(Event ev, Object O){
  if(bview.wait)return true;
  if(ev.target==newgame)
    new newgamewindow(this);
  else if(ev.target==undo)
    undo();
  return true;
 }
 /**
 * this method is called by newgamewindow.  
 */
 public void newgame(){
  b.clear();//b.clear() restores the opening position on the board.
  bview.repaint();//this updates the screen
 }
 /**
 * this method undoes a move, if possible.
 */
 public void undo(){
  if(b.moves!=0)
    bview.undomove();
 }
 
 /**
 * sets a given text in the message label.
 * @param s the message to display.
 */
 public void message(String s){
  slabel.setText(s);//set a message in the status label.
 }
 
 /**
 * this method is quick & dirty trick for running this applet as
 * an application. It is not used when a browser runs this class
 * as an applet.
 */
 public static void main(String[] ps)
 {Frame f=new Frame("reversi");
  reversi r=new reversi();
  f.resize(500,500);
  f.add("Center",r);
  r.init();
  r.start();
  f.show();
 }
 
}

/**
* this is a window for popping up to create a new game. the user
* can set what kind of game he wants, and then press cancel or start.
*/

class newgamewindow extends Frame{
   reversi r;
   Choice[] cm={new Choice(),new Choice()};
   Button start=new Button("start"); 
   Button cancel=new Button("cancel"); 


   /**
   * create a new newgamewindow. It shown immediately. You can use this
   * dialog only once.
   * @param ir the applet that wants a new game.
   */
   public newgamewindow(reversi ir){
    super("new game");
    r=ir;
    r.bview.wait=true;
    setLayout(new GridLayout(2,3));
    cm[0].add("human");
    cm[0].add("computer");
    cm[1].add("computer");
    cm[1].add("human");
    add(cm[0]);
    add(new Label(" versus "));
    add(cm[1]);
    add(start);
    add(cancel);
    resize(250,100);
    setLocation(100,100);
    show();
   }
   /**
   * this method is called any time a button is pressed or an option selected.
   */
   public boolean action(Event ev, Object o){
    if(ev.target==start){
     r.newgame();
     r.bview.setplayers(
        cm[0].getSelectedIndex()==1,
        cm[1].getSelectedIndex()==0);
     r.bview.wait=false;
     dispose();
    }
    else if(ev.target==cancel){
     r.bview.wait=false;   
     dispose();
    }
    return true;
   }
}

/**
* a boardview is a canvas (a rectangle that can be displayed on the screen and painted on)
* that can display a board. It can be used in two ways
* <ol>
* <li>by an applet. In this case the boardview listens to the mouse,
* and lets the players do moves. It also starts an animation for fading in any
* changes.
* <li>By genetictrain. Animation is no longer needed, and it does not listen to the mouse. 
* </ol>
*/
class boardview extends Canvas
implements Runnable
{
    board b;//the board that is viewed
    int fieldsize=32;//size in pixels of a field.
    int SX,SY;//the total size in pixels of this canvas
    reversi top;//the applet that uses this boardview
    boolean wait=false;//if wait==true the mouse will be ignored
    player[] players={null,null};
    boolean[] computer={false,true};
        //wether the computer plays that player.
    int[] oldboard; //a copy of the A table of the board.
    
    String statusbartext="";//the text under the applet
    
    Color[] r2g={Color.red,
        new Color(213,43,0),
        new Color(171,85,0),
        new Color(128,128,0),
        new Color(85,171,0),
        new Color(43,213,0),
        Color.green};//the colors for the morph
    int framenumber;//how far we are in morphing
    int frames=r2g.length;//the total number of frames
    Graphics g;
    Frame superframe;//only in non-applet mode
    
    /**
	* constructor for applet mode   
	*/
	public boardview(board ib,reversi ir){
     top=ir;
     b=ib;
     myresize(b.X,b.Y);
     players[0]=new player(b,0,this);
     players[1]=new player(b,1,this);
     statusbartext=b.statusmessage();
    }
    /**
    * constructor in non-applet mode   
    */    
    public boardview(board ib){
     b=ib;
     wait=true;
     myresize(b.X,b.Y);
     display();
    }
    /**
	* creates a frame that shows this boardview. Used in nonapplet-mode.   
	*/
    public void display(){
     
     superframe=new Frame("reversi");
     superframe.resize(SX+20,SY+20);
     superframe.add("Center",this);
     superframe.show();
    }
    /**
	* this method is called if the user clicked the mouse.
	* @param evt an object that contains details on the click.
	* @param x the horizontal position of the cursor
	* @param y the vertical position
	* @return true to indicate success
	*/

    public boolean mouseDown(Event evt,int x,int y){
     if(x>=SX||y>=SY||wait)
        return true;
     clicked(b.co(x/fieldsize,y/fieldsize));
     return true;
    }
    
    /**
	* do the given move on the board.
	* @param m the move to be done.
	*/
    public void domove(int m){
     highlightoff();//stops the morph
     b.domove(m);//do the move
     statusbartext=b.statusmessage();//set the message
     highlighton();//starts the morph
    }
    /**
    * undo the last move on the board.
    */
    public void undomove(){
     highlightoff();
     b.undomove();
     statusbartext=b.statusmessage();
     highlighton();
    }
    
    
    
    void clicked(int c){//called if user clicked field c
     if(!computer[b.getplayer()]){
      if(b.posmoves==0)
         domove(-1);
      else if(b.canmove(c))
        domove(c);
     }
     if(computer[b.getplayer()])
      computermove();
    }
    
    void computermove(){
      message("computer is thinking...");  
      wait=true;
      players[b.getplayer()].ask("Please do your move.");         
    /*the computer player will start calculating, and call
    answer with its answer.*/
    }

/**
* you can indicate here whether computer players must be used.
* @param a true if a computer player must be used for the first 
* player. False if the user operates this player.
* @param b same for second player.
*/
    public void setplayers(boolean a,boolean b){
     computer[0]=a;
     computer[1]=b;   
    }
    /*resize this object.*/
	void myresize(int x,int y){
     SX=x*fieldsize;
     SY=y*fieldsize;
     resize(SX,SY+20);//the 20 is room for the message under the screen.
    }

    boolean painting=false;
    /**
	* this method is called by the operating system if the frame must be redrawn, and by
	* the rest of the program if something changed on the board. It is synchronized 
	* because genetictrain calls this method very often, and we do not want these paints
	* to happen in parallel.
	*/
	public void paint(Graphics newg){
     synchronized(this){
      if(painting) return;
      painting=true;
     }
     g=newg;
     for(int j=0;j<b.Y;j++)
     for(int i=0;i<b.X;i++)
      paintfield(i,j);
     paintstatusbar();
     painting=false;
    }
	/**
	* paints the text under the board.
	*/
    void paintstatusbar(){
     g.setColor(Color.black);
     g.drawString(statusbartext,10,SY+10);
    }
    
/**
* this method determines if the stone on the given spot is changing color.
* @return 0 if it keeps the same color, 1 if fading from red to green, 2
* if it is fading from green to read..
*/
    int fading(int i,int j){
     if(oldboard==null)
        return 0;
     g=getGraphics();
     int x=b.co(i,j);
     if(oldboard[x]==b.P1&&b.get(x)==b.P2)
        return 1;
     if(oldboard[x]==b.P2&&b.get(x)==b.P1)
        return 2;
     return 0;   
    }/*0 means not, 1 r->g, 2 g->r*/

    Color back[]={new Color(128,128,128),new Color(100,100,100)};
    Color c1=new Color(255,0,0);
    Color c2=new Color(0,255,0);

 /**
 * paint one field 
 */
    void paintfield(int i,int j){
     paintback(i,j);   
     paintnormalstone(i,j);
    }

/**
* is called every 150 milliseconds. It must fade the colors of the stones that 
* recently changed.
*/    
    void fade(){
     if(framenumber==-1)
      return;
     if(framenumber>=frames){
      repaint();
      framenumber=-1;
      return;
     }
     for(int j=0;j<b.Y;j++)
      for(int i=0;i<b.X;i++)
       switch(fading(i,j)){
        case 0:break;  
        case 1:
         paintback(i,j);
         paintstone(i,j,r2g[framenumber]);
        break;
        case 2:         
         paintback(i,j);
         paintstone(i,j,r2g[frames-1-framenumber]);//r2g[frames-1-framenumber]
        break;
       }       
     framenumber++;
    }

/**
* paint the empty fields: just the background color. 
*/
    void paintback(int i,int j){
     int si=i*fieldsize,sj=j*fieldsize;
     g.setColor(back[(i+j)%2]);
     g.fillRect(si,sj,fieldsize,fieldsize);        
    }
/**
* paint a stone in the given color.
*/    
    void paintstone(int i,int j,Color c){
     int si=i*fieldsize,sj=j*fieldsize;
     g.setColor(c);
     g.fillRect(si+2,sj+2,fieldsize-4,fieldsize-4);        
    }
/**
* paint a field with a stone if any, or display the
* number of flips for that field.
*/
    void paintnormalstone(int i,int j){
     int v;   
     boolean canmove;
     int flips;
     synchronized(this){
      v=b.get(i,j);
      canmove=b.canmove(b.co(i,j));
      flips=b.getflips(b.co(i,j));
     }
     if(v==b.P1)
      paintstone(i,j,c1);
     else if(v==b.P2)
      paintstone(i,j,c2);
     else if(canmove){
      int si=i*fieldsize,sj=j*fieldsize;
      g.setColor(Color.black);
      g.drawString(""+flips,si+fieldsize/2,sj+fieldsize/2);      
     }
    }

 void message(String s)
    {top.message(s);}
 /**
 * this method is called by movers to return return the move they want to do.
 * @param s s is not used. It is a little joke: it can be used by callers to give
 * a comment to this function.
 * @param m the move to be done.
 */
 public void answer(String s,int m){
  oldboard=(int[])b.A.clone();
  domove(m);
  wait=false;
  message("");
 }

 /**
 * stops the animation (called when another move is already done).
 */
 void highlightoff(){
  oldboard=(int[])b.A.clone();
  repaint();       
 }
 /**
 * start the colorfade animation. called after something changed.
 */
 void highlighton(){
  repaint();
  framenumber=0;
 }
 
 boolean keeponrunning;
 
 
 /**
 * is called if a new thread is created for the animation. 
 */
 public void start()
   {keeponrunning=true;
    new Thread(this).start();
   }
/**
* is called if someone wants the thread to stop.
*/   
 public void stop()
   {keeponrunning=false;}

/**
* this method is the one that runs in its own thread to do the animation.
* it calls fade() every 150 milliseconds.
*/ 
 public void run(){
  while(keeponrunning){
   try{Thread.sleep(150);}catch(Exception e){}
   fade();
  }
 }

}

/**
* a board contains the current game situation: where the stones are, what color they have
* and who should move. It also contains a history so moves can be undone.
*/
class board{
/**
* the array A contains the stones. You would expect A to be a 2-dimensional array, but
* it turns out that it is faster to make it a one-dimensional array. A is larger than the
* nulber of fields, because this prevents bounds tracking. If you start on a valid field,
* and you start walking in any direction, you will first encounter a field with the value
* OUT before you cross a border.<p>
* because A is onedimensional, you just need one integer to indicate a field. 
* So instead of an object move all methods dealing with moves just use int's
* (note the huge speed improvement).
*/  
 public int[] A;
 static int[] all;//all indices of visible fields.
 static int X=8,Y=8;/*the size of the visible board. You can play on larger
 boards by changing these values. Note that the computer AI will be confused if 
 you do so. Try to keep X==Y (or modify the kinds code) and recompile all classes
 after you did, and retrain the player.*/
 static int N=X*Y;//the number of visible fields.
 static int BASE=X+2;/*the number used in converting 1dimensional and
 twodimensional coordinates*/
 
 //the values A can have.
 public static int OUT=88,P1=0,P2=1,FREE=3;
 
 /*the information needed to undo a move.*/
 //the moves already done.
 int[] move=new int[128];
 //the number of stones flipped in that move.
 int[] undoflips=new int[128];
 //the positions of the stones flipped in that move.
 int[][] undoflip=new int[128][24];
 
 int moves; //number of moves done.
 int[] can;//the number of flips if you move here,
 int posmoves;//the number of moves possible. if it is zero the current player must pass.
 int thisp;//the player that is currently moving. valid values are P1 and P2.
 int[] posmove=new int[N];/*the moves that are possible.*/
 int[] points={0,0};//the points for each player.
 int free;//the number of free fields. 
 int status; /*values:*/final static int RUNNING=1,FINISHED=2;

 /*these fields are needed by the computer AI:*/
 static int[] kind;//the 'kind' of each field
 static int kinds;//the total number of different kinds.
 static int evalvectorlength;//the length of an evaluation vector.

 static{/*preparations for this class*/
  all=new int[N];
  int cur=0;
  for(int j=0;j<Y;j++)
  for(int i=0;i<X;i++)
	all[cur++]=co(i,j);
  kind=new int[(X+2)*(Y+2)];
  int k=0;
  for(int i=0;i<X/2;i++)  
   for(int j=0;j<=i;j++){
    kind[co(i,j)]=k;
    kind[co(X-1-i,j)]=k;
    kind[co(i,Y-1-j)]=k;
    kind[co(X-1-i,Y-1-j)]=k;
    kind[co(j,i)]=k;
    kind[co(j,X-1-i)]=k;
    kind[co(X-1-j,i)]=k;
    kind[co(X-1-j,Y-1-i)]=k;
    k++;
   }
  kinds=k;
  evalvectorlength=kinds+3;
  /*the kinds indicate the fields that are really different,
  */
 }


 /* help functions:*/
 static int co(int i,int j)	{return (i+1)+((j+1)*BASE);}
 //convert from 2dimensional field coordinates to 1dimensional field number.
 static int p1(int r)		{return r%BASE-1;}//get the x coordinate of a field number
 static int p2(int r)		{return r/BASE-1;}//get the y coordinate.
 static int opponent(int p)	{return 1-p;}//returns P1 if given P2 and vice versa.

 /**
 * returns an exact copy of this board.
 */
 public board copy(){
  board r=new board();
  r.moves=moves;
  r.posmoves=posmoves;
  r.thisp=thisp;
  r.A=samearray(A);
  r.points=samearray(points);
  r.move=samearray(move);
  r.posmove=samearray(posmove);
  r.can=samearray(can);
  return r;  
 }
 /*make copy of given array*/
 int[] samearray(int[] a)
    {return (int[])a.clone();}

 /*create a new board.*/
 public board(){
  A=new int[(X+2)*(Y+2)];
  can=new int[(X+2)*(Y+2)];
  for(int i=0;i<A.length;i++)
	A[i]=OUT;
  clear();
 }
 
 /*info functions*/
 
 /**
 * get the player currently moving: P1 or P2.
 */
 public int getplayer()	{return moves%2;}
 /**
 * set the given field with the given value
 */
 public void set(int i,int value)	{A[i]=value;}
 /**
 *get the value of the given field
 */
 public int  get(int i)		{return A[i];}
 /**
 * get the value of the field indicated in 2dimensional coordinates.
 */
 public int  get(int i,int j)	{return A[co(i,j)];}

 /**
 * resetup the board to the initial situation. Chooses randomly between
 * the two possible openings.
 */
 public void clear(){
  moves=0;
  for(int i=0;i<N;i++)
   A[all[i]]=FREE;
  if(2*Math.random()>1){
   set(co(3,3),P1);
   set(co(3,4),P1);
   set(co(4,3),P2);
   set(co(4,4),P2);
  }else{
   set(co(3,3),P2);
   set(co(3,4),P1);
   set(co(4,3),P1);
   set(co(4,4),P2);  
  }
  setcan();
  status=RUNNING;
 }
 
 /*sets the can array with the right values
 (and) status, free, etc... call this method after each
 change.*/
 void setcan(){
  points[P1]=points[P2]=posmoves=0;
  free=0;
  thisp=getplayer();
  for(int i=0;i<N;i++)
    if(A[all[i]]==P1)
        {points[P1]++;can[all[i]]=0;}
    else if(A[all[i]]==P2)
        {points[P2]++;can[all[i]]=0;}
    else
    {
     free++;
     can[all[i]]=flips(all[i]);
     if(can[all[i]]>0)
     {
      posmove[posmoves++]=all[i];
     }
    }
  if(finished())
    status=FINISHED;
  else
   status=RUNNING; 
 }

/**
* do the given move on this board.
*/
 public void domove(int c){
 /*do a move on this board.*/
  undoflips[moves]=0;
  if(status==FINISHED)
    return;
  if(c!=-1)
  {int otherp=opponent(thisp);
   int i,dir,d;
   A[c]=thisp;
   for(i=0;i<8;i++){
     dir=around[i];
     if(count(otherp,c,dir)>0)
         for(d=dir+c;A[d]==otherp;d+=dir){
          A[d]=thisp;
          undoflip[moves][undoflips[moves]++]=d;
         }
   }
  }
   move[moves++]=c;  
   setcan();
 }

 //all directions you can flips stones in. Given as the difference in 
 //1dimensional field numbers.
 int[] around={1,-1,BASE,-BASE,BASE+1,BASE-1,-BASE+1,-BASE-1};
 
 /**
 * calculate the number of stones that would flip if you put
 * a stone at the given position.
 */
 int flips(int c){
  if(A[c]!=FREE) return 0;
  int ret=0;
  int otherp=opponent(thisp);
  for(int i=0;i<8;i++)
    ret+=count(otherp,c,around[i]);
  return ret;
 }
 /**
 * count the flips that would happen if you put a 
 * stone at position c and looking in the direction dir.
 */
 int count(int otherp,int c,int dir){
  int ret=0;
  if(A[dir+c]!=otherp)
    return 0;
  for(c=dir+c;A[c]==otherp;c+=dir)
    ret++;
  if(A[c]==thisp)
    return ret;
  return 0;
 }
 
 /**
 * returns whether the current player is allowed to place a stone at c.
 */
 public boolean canmove(int c)
    {return can[c]>0;}
 /**
 * returns true iff the game is finished.
 */
 public boolean finished()
    {return free==0||(posmoves==0&&move[moves-1]==-1);}
 /**
 * returns true if player 1 won the game.
 */
 public boolean P1wins()
    {return points[0]>points[1];}
 /**
 * returns true if player 2 won the game.
 */
 public boolean P2wins()
    {return points[0]<points[1];}
 
 /**
 * return the number of stones that would flip when playing c.
 */   
 public int getflips(int c){return can[c];}
 /**
 *return the number of visible fields that are not free.
 */
 public int getcoverage()
    {return points[0]+points[1];}
 
 /* for position evaluation */
 int getbalance(){
  return points[thisp]-points[1-thisp];
 }
 /**
 *return the total number of stones that can be flipped
 */
 int getflipsum(){
  int f=0;
  for(int i=0;i<N;i++)
    f+=can[all[i]];
  return f;
 }
 /**
 * return the number of moves that can be done.
 */
 int getmovecount()
    {return posmoves;}
    
/**
* returns a certain set of number that will be used
* to evaluate the situation. Currently, it returns 13 numbers:
* the first 10 numbers returns the number of counters in a certain
* kind of field. There are ten kind of fields due to symmetries.
* The kinds are in the kind table. This table looks like this <pre>
*       0   1   3   6   6   3   1   0
*       1   2   4   7   7   4   2   1
*       3   4   5   8   8   5   4   3
*       6   7   8   9   9   8   7   6
*       6   7   8   9   9   8   7   6
*       3   4   5   8   8   5   4   3
*       1   2   4   7   7   4   2   1
*       0   1   3   6   6   3   1   0</pre>
* But you should not care about how this table looks: What is important
* is that we value counters regarding their position, but using symmetries to
* limit the number of situations.
* the 11th number (that is out[10] or out[kinds])
* return how many counters you are ahead (negative if behind)
* the twelth number return the number of moves you can do
* the last number tells you the number of counters you can flip in total.
* <p>    
* If you add a certain feature that you think is important for
* winning chances, do it here and change evalvectorlength. The
* genetictrain and weightvectorclasses will automatically adjust if you recompile them.
* you must retrain before you use the applet, beacuse the standard weigth vector in the
* player class will then be too short.
* @param out an optional array you want the result in (reusing memory improves the speed)
* You can make it null if you do not have such array.
* @return returns an evalvector. It will be out if that was not null.
*/
 public int[] getevalvector(int[] out){
  if(out==null)
    out=new int[evalvectorlength];
  for(int i=0;i<kinds;i++)  
    out[i]=0;
  for(int i=0;i<N;i++){
   int f=A[all[i]];
   if(f==thisp)
    out[kind[all[i]]]++;
   else if(f==1-thisp)
    out[kind[all[i]]]--;
  }
  out[kinds]=getbalance();
  out[kinds+1]=getmovecount();
  out[kinds+2]=getflipsum();
  return out; 
 }
 
/**
* undo the last move. The board remembers what move that was, so you do not
* have to give it.
*/ 
 public void undomove(){
  moves--;
  int opp=opponent(getplayer());
  if(move[moves]!=-1)
    A[move[moves]]=FREE;
  for(undoflips[moves]--;undoflips[moves]>=0;undoflips[moves]--)
    A[undoflip[moves][undoflips[moves]]]=opp;
  setcan();
 }
 
/**
*returns a line of text describing the situation.
*/ 
 public String statusmessage(){
  if(finished()){
   if(P1wins())
    return "RED wins with "+points[P1]+" against "+points[P2];
   if(P2wins())
    return "GREEN wins with "+points[P2]+" against "+points[P1];
   return "The game ended in DRAW";
  } 
  if(thisp==P1)
   return "RED to move:"+points[P1]+" (green: "+points[P2]+")";
  return "GREEN to move:"+points[P2]+" (red: "+points[P1]+")";
 }
 
}

/**
* this class is a computer player, that calculates what move to do
* in a given situation.
*/
class player implements Runnable{
 /*general variables*/
 board realb,b;
 int side;
 int maxlevel;
 
 /*The Artificial intelligence used here is not very complictated
 I use a alphabeta algorithm to search a few moves deep. Then
 I do a simple evaluation with a number of adjustable parameters.
 To find good values for these parameters I used a genetic algorithm.
 This genetic algorithm is not used while your playing: to slow. */
 
 /*evaluation variables*/
 
 /**
 * The string W is the evaluation string used to evaluate situations. It was 
 * calculate with the genetictrain class, and then inserted in this sourcecode.
 */
 public String W="8 0 -7 2 -8 2 0 -2 1 -6 2 3 1 -2 1 1 -2 0 -1 0 1 -2 -1 4 3 5 53 best";
 int[] weight=new weightvector(W).a;
 int offset;
 /*if you used genetictrain to calculate a new weightvector,
 change the line String W to hold a line of the new output file.
 typically, pick the first or last line. */
 
 
 /*for use in a parallel thread (applet mode)*/
 boardview bv;
 long time;
 
 
/**
* determines how far the computer looks ahead, higher value plays
* better, but takes more time. I think even values are better, because
* that gives the opponent the last move, but that is personal.*/
 public void setstrength(int l){
  if(l>6) l=6;
  if(l<1) l=1;
  maxlevel=l;
 }
 
/**
* set the weight vector this player should use.
*/
 public void setweight(int[] w)
    {weight=w;}

/**
* create a player.
* @param ib the board that is used. 
* @param iside the side the player takes: board.P1 or board.P2
*/ 
 public player(board ib,int iside)
    {this(ib,iside,null);}

 
 
/**
* create a player for applet mode: the player calculates in a separate thread,
* and wait 4 seconds before answering.
* @param ib the board that is used. 
* @param iside the side the player takes: board.P1 or board.P2
* @param ibv the boardview it must answer to.
*/ 
 public player(board ib,int iside,boardview ibv){
  realb=ib;
  side=iside;
  bv=ibv;
  setstrength(4);
 }

/**
* the method you can call if you want this player to think of a move in a 
* separate thread. This function returns immediately, so the caller can do something
* different. The answers will be given after 4 seconds or longer to the 
* boardview by the method answer.
* @param s a string that is not used. It can be used for comment by a caller. Do 
* not take it too seriously: it is an inside joke to include comment in extra parameters.
*/
 public void ask(String s){
  new Thread(this).start();
 }
  
 /*for timing*/
 void timestart()
    {time=System.currentTimeMillis();}
 int gettime()
    {return (int)(System.currentTimeMillis()-time);}
 
/**
* the method that will run in a separate thread. Do not call this function yourself,
* but use ask.
*/
 public void run(){
  timestart();
  int move=bestmove();
  while(gettime()<4000)
    {try{Thread.sleep(400);}catch(Exception e){}}
  bv.answer("Well, this is my move:",move);
 }
 
/**
* this functions returns the move that this player thinks is best. Use this function
* if you just want the best move, no extra threads and no delays.
* @return a move
*/
 public int bestmove(){
  b=realb.copy();  
  if(b.posmoves==0)
    return -1;
  setoffset();
  int s,best=-1,alpha=-100000;
  int k=b.posmoves;
  for(int i=0;i<k;i++)
  { b.domove(b.posmove[i]);
    s=-prognosis(-100000,-alpha,1);
    b.undomove();
    if(s>alpha)
        {alpha=s;
         best=i;
        }
  }
  return b.posmove[best];
 }
 
 /*returns how good the current situation looks.*/
 int prognosis(int alpha, int beta, int level)
 {
  if(level>=maxlevel||b.posmoves==0)
        return simplescore();
  int s;
  /*try all moves and return the estimate
  we get when doing the best move*/
  for(int i=0;i<b.posmoves;i++)
  {  b.domove(b.posmove[i]);
        s=-prognosis(-beta,-alpha,level+1);
     b.undomove();
     if(s>beta)
        return s;
     if(s>alpha)
        alpha=s;
  }
  return alpha;
 }
 
 
 void setoffset(){
  /*determines which part of the eval vector,the first or the last, to use.*/  
  if(b.getcoverage()<weight[weight.length-1])
    offset=0;
  else 
    offset=out.length;
 }
 
 int[] out;/*this vector can be used again and 
 again to get an evalvector in. this saves memory. */
 
 int simplescore(){
  int score=0;
  out=b.getevalvector(out);
  for(int i=0;i<out.length;i++)
    score+=weight[offset+i]*out[i];
  return score;
 }
}


/**
* this class is not used 
* by the applet, but only by its own main function. <p>
* A genetic algorithm is a method for solving an optimisation proble. Our
* problem is to find the best weight vector. The methods works much like
* biological evolution: random weight vectors are calculated, and in each
round they play against a randomly selected opponent and the winners survive. 
* The winners are then copied (with small changes), or two
* winners are mixed (called cross-over) and a new round starts: they are again tested 
* against each other... until you say it is enough. The main advantage of a genetic algorithm 
* is that it works whithout assuming anything about the problem. It just let two 
* 'individuals' against each other, does not care what they do exactly, but only needs
* to know the winner.<p>
* The exact description of a round is this: there is a fixed population size 
* (the number of weightvectors). They are randomly ordered, and the odd even numbers
* play against the next odd number. The winner is stored in the even position, the odd
* position is empty. Now per group of four a random decision is made: if they mutate, 
* the odd positions are filled with mutated copies of the winners. If crossover is chosen,
* the winners are crossed over, producing two children that are random mixtures of the 
* parents. Remember that individuals are just lists of numbers. For example:<pre>
* parent1:-3 -5  2  6 0
* parent2 -7  0 -4  1 2
* could give:
* child1: -3  0 -4  6 2
* child2: -7 -5  2  1 0 </pre>
* In the literature this is called uniform crossover. There are other kinds of crossover.
* the last position (which is odd) is filled with the average of all other individuals.
*/

class genetictrain
{
 int popsize;
 weightvector[] pop;
 board b=new board();
 boardview bv;
 player[] P={new player(b,b.P1),new player(b,b.P2)};
 boolean onscreen=true;
 
 /**
 * @param p the population size for the genetic algorithm.
 */
 public genetictrain(int p){
  setpopsize(p);
  for(int i=0;i<popsize;i++)
    pop[i]=new weightvector(); 
  for(int i=0;i<2;i++)  
    P[i].setstrength(2);
  if(onscreen)
    bv=new boardview(b);
 }
 
 /**
 * change the population size.
 * @param the size wanted it will be rounded to a multiple of four,
 * because that is required by the algorithm. A reasonable value is between
 * 16 and 1600.
 */
 void  setpopsize(int n){
  popsize=4*(n/4);
  pop=new weightvector[popsize];    
 }

 /**
 *read in a population from a file. The file format is like this:
 * <ul><li>first line: the population size.
 * <li>one line per weightvector in the population
 * each line has the weights on it, and can end with a comment.</ul>
 * @param filename the file to open.
 */
 public void read(String filename){
 /*read a file back in*/
  file f=new file();
  f.readopen(filename);
  setpopsize(f.num(f.read()));
  for(int i=0;i<popsize;i++)
    pop[i]=new weightvector(f.read());
  f.readclose();  
 }
 /**
 * the file to write. The same format is used as for read.
 */
 public void write(String filename,String comment){
  file f=new file();
  f.writeopen(filename);
  f.write(""+popsize);
  for(int i=0;i<popsize;i++)
    f.write(""+pop[i]);
  f.write(comment+" "+new Date());
  f.writeclose();  
 }
  
 /**
 * let the genetic algorithm run the given number of rounds. It prints
 * the current round on the java console.
 * @param rounds the number of rounds. reasonable values are between
 * 1 and 1000 (although that may take some time.)
 */
 public void run(int rounds){
  try{
  for(int i=rounds-1;i>=0;i--){
   System.out.println(""+i);  
   run();      
  }}catch(Exception e){e.printStackTrace();}
 }
 
 /*does one round of the algroithm.*/
 void run(){
  int crossoverchance=25;//percentage
  /*raise this parameter if you think crossover is usefull.
  set to zero if you only want mutation to happen*/
  randomorder();
  for(int i=0;i+1<popsize;i+=2){
    pop[i]=winner(pop[i],pop[i+1]);
    pop[i+1]=null;
  }
  for(int i=0;i+1<popsize;i+=4){
    if(maybe(crossoverchance)){
     weightvector[] r=pop[i].crossover(pop[i+2]);
     pop[i+1]=r[0];
     pop[i+3]=r[1];
    }else{
     pop[i+1]=new weightvector(pop[i]);
     pop[i+3]=new weightvector(pop[i+2]);
    }
  }  
  pop[popsize-1]=new weightvector(pop); 
 }
 
 /**
 * reshuffle the population in a random order.
 */
 void randomorder(){
  for(int i=popsize-1;i>1;i--)
    swap(i,rand(i+1));
 }
 /**
 * swap to elements of the population.
 */
 void swap(int a,int b){
  weightvector dummy=pop[a];
  pop[a]=pop[b];
  pop[b]=dummy;
 }
 
 /**
 * this method lets two weightvectors play against each other, and
 * returns the winner (or w1 if in draw)
 */
 weightvector winner(weightvector w1,weightvector w2){
  P[0].setweight(w1.a);
  P[1].setweight(w2.a);
  b.clear();
  int onturn=0;
  while(!b.finished()){
   b.domove(P[onturn].bestmove());
   if(onscreen)
    bv.repaint();
   onturn=1-onturn;
  }
  if(b.P1wins())
    return w1;
  return w2;
 }
 
 
 /*return a random integer between 0 and r (r not included)*/
 int rand(int r)
    {return (int)(Math.random()*r);}
 /*returns true with the given chance*/
 boolean maybe(int percent)
  {return rand(100)<percent;}

 /*clean up the frames that were made.*/
 public void cleanup(){
  if(onscreen){
   bv.superframe.hide();
   bv.superframe.dispose();
  }
 }
 
 /**
 * the main function. Change this function so it does what you want, recompile
 * and run it to calculate a better weight vector. 
 */
 public static void main(String[] ps){
  int pop=2;
  int rounds=3;
  genetictrain g=new genetictrain(pop);//make new set
  //g.read("myoutput.txt");//remove this line if you have no inputs yet
  g.run(rounds);//do some processing
  g.write("newoutput.txt",rounds+" rounds");//write the output
  g.cleanup();//remove the window (if any)
  System.exit(0);
 }
 
}

/**
* this is a data-holding object that mainly just contains numbers
* that are used by the evaluation function. You could reuse this class
* for solving other problems with genetic algorithms. Note that the
* static members of this class determine what the weight vectors look like,
* so you would have to edit this part and recompile.
*/

class weightvector{
/**
*the most important thing is a: it contains
* the weights we are looking for. name is just a comment. At the moment 
* it stores the operators used to et this weight vector: M for mutation,
* avg for average of the rest of the population, 
*/
 static int n=2*board.evalvectorlength+1;   
 int[] a=new int[n];
 String name;
 static int[] min=new int[n];
 static int[] max=new int[n];

 static{
  for(int i=0;i<n-1;i++){
   min[i]=-10;
   max[i]=10;
  }
  min[n-1]=5;
  max[n-1]=60;
 }
 /**
 * create a new weigth vector filled with random numbers.
 */
 
 public weightvector(){/*random initialisation*/
  for(int i=0;i<n;i++)
    a[i]=rand(min[i],max[i]+1);    
  name=randomname();
 }
 /**
 * create a random name for the comment
 */
 String randomname(){
  String r="_";
  for(int i=0;i<2;i++)
   r+=(char)(rand(26)+'a');
  return r;
 }
 /**
 * make a copy of the given vector with small changes.
 */
 public weightvector(weightvector w){/*mutated copy*/
  for(int i=0;i<n;i++)
    a[i]=bound(w.a[i]-1+rand(0,3),min[i],max[i]);
  name="M"+w.name;
 }
 /**
 * creates a new vector with the average of the given population.
 */
 public weightvector(weightvector[] pop){
  for(int i=0;i<n;i++)
   a[i]=0;
  for(int p=0;p<pop.length;p++)
   for(int i=0;i<n;i++)
    a[i]+=pop[p].a[i];
  for(int i=0;i<n;i++)
   a[i]=a[i]/pop.length;
  name="avg";
 }
 
 /**
 *create a new weight vector from the given string. This constructor is
 * compatible with the toString() method.
 */
 public weightvector(String s){
  StringTokenizer st=new StringTokenizer(s);
  for(int i=0;i<n;i++)
    a[i]=file.num(st.nextToken());
  name=st.nextToken();
 }
 public String toString(){/*convert to string*/
  String s=""+a[0];
  for(int i=1;i<n;i++)
    s+=" "+a[i];
  s+=" "+name;
  return s;
 }
 /**
 *crosses this weight vector with the given weight vector. It returns
 * an array with two new weight vectors, that are random mixtures of the
 * two parents.
 */ 
 public weightvector[] crossover(weightvector w){
  weightvector[] r={new weightvector(),new weightvector()};
  for(int i=0;i<n;i++){
   int x=rand(0,2);
   r[x].a[i]=a[i];
   r[1-x].a[i]=w.a[i];
  }
  String s=randomname();
  r[0].name="X"+s;
  r[1].name="x"+s;
  return r;
 }

 int rand(int a,int b)//return random number r:a<=r<b
    {return a+rand(b-a);}
 int rand(int r)//return random number r:0<=x<r
    {return (int)(Math.random()*r);}
 int bound(int x,int min,int max){
  if(x<min) return min;
  if(x>max) return max;
  return x;
 }
    
}

 /**This is a class for reading and writing textfiles.
* basic use : <ul>
* <li>make a file object
* <li>do readopen
* <li>do some read
*  <li>do close
*  <li>or:
*  <li>make an object
*  <li>do writeopen
*  <li>do some writing
*  <li>do close 
* </ul>
*/

class file{
 BufferedReader in;
 PrintWriter out;
 boolean OK;

/**
* open a file for reading.
*/
 public void readopen(String f1)
    {readopen(new File(f1));}
/**
* open a file for reading.
*/

 public void readopen(File f1){
 try{
 in=new BufferedReader(new FileReader(f1));
 OK=true;
 }catch(Exception e)
        {e.printStackTrace();OK=false;}
 }
/**
* read one line of the input file.
*/
 public String read(){
     if(!OK)
        return null;
     try{
        return in.readLine().trim();
     }catch(Exception e)
        {e.printStackTrace();}
     return null;
 }

/**
* close the file opened for reading.
*/
 public void readclose(){
  try{
  if(in!=null)in.close();
  }catch(Exception e)
        {e.printStackTrace();}
 }

/**
* open a file for writing. Note that it is allowed to have both a file to
* read and a file to write with the same file object.
*/
 public void writeopen(String f1)
    {writeopen(new File(f1));}
/**
* open a file for writing. Note that it is allowed to have both a file to
* read and a file to write with the same file object.
*/

 public void writeopen(File f1){
 try{
 out=new PrintWriter(new FileOutputStream(f1));
 OK=true;
 }catch(Exception e)
        {e.printStackTrace();OK=false;}
 }
/**
* Write out the given string as a separate line.
*/

 public void write(String s)
    {if(OK)out.println(s);}

/**
* close the writefile
*/
 void writeclose()
    {if(out!=null)out.close();}
/**
* close both the file opened for reading and the one for writing (if any)
*/
 public void close()
    {readclose();writeclose();}

/**
* converts a String to an int.
*/
 public static int num(String s){
  return Integer.parseInt(s.trim());
 }

/**
* converts a String to a double.
*/
 public static double dnum(String s){
  return new Double(s).doubleValue();
 }
}
