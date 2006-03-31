/*
 * @(#) Othello.java 	
 *
 * Copyright (c) 1995 Yukio Hirai. All Rights Reserved.
 *
 * Author: Yukio Hirai <t94369yh@sfc.keio.ac.jp> 
 *
 * On condition that you do not modify this copyright notice,
 * You can use, copy, modify, and distribute this program
 * I am not responsible for  ANY damage caused by this program.
 * Use this program at YOUR OWN RISK!
 * Feel free to e-mail me some comments.
 *
 * 作者: 平井 幸夫 (ひらい ゆきお) <t94369yh@sfc.keio.ac.jp> 
 *
 * この文章自身を変更・削除しないという条件において、このプログ
 * ラムを自由に使用、複製、改造、配布して下さって構いません。
 * このプログラムによってもたらされた、いかなる損害についても、
 * 作者は責任を負いません。御自分の責任にて御使用下さい。 
 * 何かコメントがありましたら、是非メイルをお送り下さい。
 *
 */

import java.applet.*;
import java.lang.*;
import java.awt.*;
import java.net.*;

/**
 * This class is for the Othello game. This is the first applet I wrote.<br>
 * しごく単純な、オセロゲーム。これは私が作った最初のアプレットです。
 * @author Yukio Hirai (平井 幸夫) 
 * <a href="mailto:t94369yh@sfc.keio.ac.jp">(t94369yn@sfc.keio.ac.jp)</a><br>
 *  - URL <A HREF="http://www.sfc.keio.ac.jp/~t94369yh/">
 * http://www.sfc.keio.ac.jp/~t94369yh/</a>
 * @version 0.75  <i>Nov 1, 1995</i>
 */
public class Othello extends Applet implements Runnable {
  /**
   * The stete of a particular cell.<br>
   * オセロ盤の状態を表す
   */
  final int BLACK =  1;	
  final int WHITE = -1;
  final int EMPTY =  0;
  
  /**
   * Width of the board<br>
   * オセロ盤の大きさ
   */
  final int WIDTH = 480;
  
  /**
   * Space at bottom right<br>
   * 右下の空きスペース
   */
  final int SPACE = 80;
  
  /**
   * Directions<br>
   * 方向を表す定数
   */
  private final int UPPER 	= 0;
  private final int LOWER 	= 1;
  private final int RIGHT 	= 2;
  private final int LEFT  	= 3;
  private final int UPPERLEFT 	= 4;
  private final int UPPERRIGHT	= 5;
  private final int LOWERRIGHT	= 6;
  private final int LOWERLEFT	= 7;
  boolean direction[] = 
    {false, false, false, false, false, false, false, false};
  
  /**
   * Turn.<br>
   * 順番
   */
  public int turn;
  
  /**
   * The othello board. matrix of 8 * 8.<br>
   * オセロ盤、8 * 8マス。
   */
  protected int stone[][];

  /**
   * Number of stones on the board<br>
   * 盤上の石を数えるための変数
    */
  protected int counter_black = 0, counter_white = 0;

  /** 
   * column and row data for audio.<br>
   * 読み上げ用の変数。変数の渡し方があまりスマートではない
   * ので改良の余地あり。
   */
  int audioColumn, audioRow;

  /**
   * Computer player<br>
   * 対戦相手のコンピュータ。今は、石が置けるところに無作為に
   * 置いていくだけのアルゴリズムである。将来的には、このプレイヤを
   * 継承して、もっと強いアルゴリズムで対戦できるようにしたい。
   */
  OthelloPlayer computer;

  /**
   * Initialize this applet.
   * Resizing and preparing AudioData.<br>
   * アプレットを初期化する。
   */
  public void init() {
    /* 最初にAppletが作成される時に呼ばれる */
    //System.out.println("Othello: init() --- 初期化");
    
    /* オセロ盤を作る */
    stone = new int[8][8];
    
    /* オセロ盤を初期化 */
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
	stone[i][j] = EMPTY;
      }
    }
    stone[3][3] = BLACK; stone[4][3] = WHITE;
    stone[3][4] = WHITE; stone[4][4] = BLACK;
    
    countStone();		/* 盤上の石を数える */

    turn = BLACK;		/* 先手を黒番に設定 */
    
    /* Netscape2.0b1Jでは、resizeに不都合があるらしい */
    /*resize(WIDTH*2 + SPACE, 1000 + WIDTH*2 + SPACE);  appletの大きさを設定 */
    
    /* 音データの準備 */
    for (int i = 1; i <= 8; i++) {
      getAudioClip(getCodeBase(), "audio/" + i + ".au");
    }
    getAudioClip(getCodeBase(), "audio/black.au");
    getAudioClip(getCodeBase(), "audio/white.au");

    computer = new OthelloPlayer(this);	/* 対戦相手のコンピュータを作成 */
  }
  

  /**
   * Paint this applet.<br>
   * アプレットを描画する
   */
  public void paint(Graphics g) {
    //System.out.println("Othello: paint() --- 描画");

    drawBoard(g);	/* オセロ盤を描く */
    /* 石を描く */
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
	if (stone[i][j] != 0){
	  drawStone(i, j, g);
	}
      }
    }
    
    drawTurn(g); 	/* どちらの番かを盤の下に示す */
    drawCountStone(g);  /* 各々の石の数を描く */

    if (counter_black + counter_white == 64) {
      showWinner(g);
    }
  }
  

  /**
   * Draw the Othello board<br>
   * オセロ盤を描く
   */
  public void drawBoard (Graphics g) {
    /* バックの色指定 */
    setBackground(Color.green);
    
    /* 外枠を描く */
    g.setColor(Color.black);
    g.drawLine(0,0, 0,WIDTH);		
    g.drawLine(WIDTH,0, WIDTH,WIDTH);	
    g.drawLine(0,0, WIDTH,0);		
    g.drawLine(0,WIDTH, WIDTH,WIDTH);	
    
    for(int i = 1; i < 8; i++){
      /* 縦線を引く */
      g.drawLine(WIDTH*i/8,0, WIDTH*i/8,WIDTH);
      /* 横線を引く */
      g.drawLine(0,WIDTH*i/8, WIDTH,WIDTH*i/8);
    }
  }
  

  /**
   * Paint stones on the board.<br>
   * 石を描く
   */
  public void drawStone(int column, int row, Graphics g) {
    /* 色を設定 */
    if (stone[column][row] == BLACK) {
      g.setColor(Color.black);
    } else if (stone[column][row] == WHITE){
      g.setColor(Color.white);
    }
    g.fillOval(column * WIDTH / 8 + 10, row * WIDTH / 8 + 10, 
		  WIDTH / 12, WIDTH / 12);
  }
  

  /**
   * Display which trun.
   * 順番を表示
   */
  void drawTurn(Graphics g){
    String black = "Black";		
    String white = "White";		
    String comment = " player's turn";		
    
    g.setColor(Color.blue);
    if (turn == BLACK) {
      g.drawString(black + comment, WIDTH/2, WIDTH + 35);
      g.setColor(Color.black);
      showStatus("Your turn!");
    } else {
      g.drawString(white + comment, WIDTH/2, WIDTH + 35);
      g.setColor(Color.white);
      showStatus("Compurter's turn!");
    }
    /* 色を示すアイコン */
    g.fill3DRect(WIDTH/2 - 20, WIDTH+20, 20, 20, true);
  }
  

  /**
   * Show win or lose.<br>
   * 勝敗を表示する
   */
  void showWinner(Graphics g){
    /* フォントを設定 */
    //Font font = getFont("Courier", Font.BOLD, 96);
    //g.setFont(font);

    g.clearRect(0, 0, WIDTH+1, WIDTH+1);

    if (counter_black > counter_white) {
      for(int  i = 0; i < 255; i++){
	//g.setColor(getColor(i, i, i));
	g.fillRect(0, 0, WIDTH+1, WIDTH+1);
	//g.setColor(getColor(0, i, i));
	g.drawString("You", 150, 100);
	g.drawString("Won!", 130, 200);
      }
    } else if (counter_black < counter_white) {
      g.setColor(Color.black);
      g.fillRect(0, 0, WIDTH+1, WIDTH+1);
      for(int i = 255; i >=0; i--){
	//g.setColor(getColor(i, i, i));
	g.drawString("You", 150, 100);
	g.drawString("lost...", 75, 200);
      }
    } else {
      g.drawString("Draw", 100, 100);
      g.drawString("Game", 100, 200);
    }
  }

  /**
   * Count the number of stones.
   * 盤上の石を数える
   */
  void countStone() {
    counter_black = 0;
    counter_white = 0;
    
    /* 各々の石を数える */
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
	if(stone[i][j] == BLACK) counter_black++;
	if(stone[i][j] == WHITE) counter_white++;
      }
    }

    /* ゲーム終了判定 */
    if (counter_black + counter_white == 64) {
      //System.out.println("Othello: countStone(勝負あり！)");
      endGame();
      init();
    }
  }

  /**
   * Display winner. This method is called when the game is finished.
   */
  public void endGame(){
    if (counter_black > counter_white) {
      //System.out.println("Othello: endGame(黒の勝ち！)");
    } else if (counter_black < counter_white) {
      //System.out.println("Othello: endGame(白の勝ち！)");
    } else {
      //System.out.println("Othello: endGame(引き分け)");
    }
    repaint();
    try {
      Thread.sleep(500);
    } catch(Exception e){
    }
  }
  

  /**
   * Display the number of stones.<br>
   * 石の数を描く
   */
  void drawCountStone(Graphics g){
    g.setColor(Color.white);
    g.fill3DRect(WIDTH+15, 130, 30,20, false); /* 数字を書くボックス */
    g.fill3DRect(WIDTH+15, 190, 30,20, false); /* 数字を書くボックス */
    g.fill3DRect(WIDTH+5, 160, 20,20, true); /* 白アイコン */
    g.setColor(Color.black);
    g.fill3DRect(WIDTH+5, 100, 20,20, true); /* 黒アイコン */
    g.drawString("Black", WIDTH+30, 115);		
    g.drawString("White", WIDTH+30, 175);
    g.drawString(Integer.toString(counter_black), WIDTH+20, 145); 
    g.drawString(Integer.toString(counter_white), WIDTH+20, 205);
  }
  

  /**
   * When mouse button is released.<br>
   * マウスボタンが放された時の処理
   */
  public boolean mouseUp(Event e, int x, int y) {
    /* クリック座標からオセロ盤の行列に変換 */
    int column = (int)(x / (WIDTH / 8));
    int row	   = (int)(y / (WIDTH / 8));
    
    if (turn == BLACK) {
      /* 石を置いて相手の番にする */
      if (checkStone(column, row, turn) == true){
	turnStone(column, row, turn); /* 石を置き、裏返す */
	playAudio(column, row);       /* 読み上げる */

	turn = - turn;	/* 順番を交替 */
	countStone();   /* オセロ盤上の、各々の石の数を数える */
	//repaint();      /* 再描画 */
	update(getGraphics());
	
	try {
	  Thread.sleep(500);	/* 間を空ける */
	} catch (Exception excep){
	}
      }
    }

    if (turn == WHITE) {
      computer.decide();	/* コンピュータの手 */
      countStone(); /* オセロ盤上の、各々の石の数を数える */
      //repaint();    /* 再描画 */
      update(getGraphics());
    }
    return true;
  }

/**
 * Speak out the column and the row by activating audio thread.
 */
public void playAudio(int column, int row){
    //System.out.print("Othello: playAudio(" + (column+1) + ", ");
    //System.out.print((row+1) + ", ");
    if (turn == BLACK) {
      //System.out.println("黒)");
    } else {
      //System.out.println("白)");
    }

    /* 音声再生methodに引数を渡す */
    audioColumn = column + 1;
    audioRow = row + 1;
    
    /* 音声再生用のThreadをforkする */
    (new Thread(this)).start();
  }    

  /**
   * Play the sound on another Thread.<br>
   * 別のThreadの上で、音声を鳴らす
   */
  public void run() {
    /* 列を読み上げる */
    play(getCodeBase(), "audio/" + audioColumn + ".au");
    try {
      Thread.sleep(500); 
    } catch (Exception e) {
    }
    /* 行を読み上げる */
    play(getCodeBase(),"audio/" + audioRow + ".au");
    //Thread.sleep(500); 
    /* どちらの手かを読み上げる */
    /*if (turn == BLACK){
      play(getCodeBase(), "audio/white.au");
    } else if (turn == WHITE) {
      play(getCodeBase(), "audio/black.au");
    }*/
    /* Threadを明示的に破棄することはこの場合、必要ない（らしい） */
  }

  /**
   * Check whether a stone can be put anywhere.
   * 
   * <jp 盤のどこかに石が置けるかどうか調べる。
   * 一つでも置ける場所があるなら true, 置けないなら false を返す>
   * 
   * @return boolean true if you can put a stone
   */
  public boolean checkAll(int turn) {
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
	if(checkStone(i, j, turn)){
	  return true;
	}
      }
    }
    return false;
  }

  /**
   * Check whether a stone can be put there.<br>
   * 石が置けるかどうか調べる。置けるなら true, 置けないなら false を返す
   * 
   * @return boolean true if you can put a stone there
   */
  public boolean checkStone(int column, int row, int color){
    //System.out.println("Othello: checkStone("+column+","+row+")");
    
    int i, j;
    
    /* 方向チェック配列を初期化 */
    for (i = 0; i < 8; i++){
      direction[i] = false;
    }
    
    if(stone[column][row] != 0) {
      /* もし、そこに石があったら、falseを返す */
      //System.out.println("Othello: checkStone(すでに石があるぞ)");
      //System.out.println("Othello: checkStone(ここには置けないぞ)");
      return false;
    } else { 
      /* 左 */
      if (column > 1 && stone[column-1][row] == -color) {
	for (i = column-2; i > 0 && stone[i][row] == -color; i--);
	if (stone[i][row] == color) {
	  //System.out.println("Othello: checkStone(左が裏返せるぞ)");
	  direction[LEFT] = true;
	}
      }
      /* 右 */
      if (column < 6 && stone[column+1][row] == -color) {
	for (i = column+2; i < 7 && stone[i][row] == -color; i++);
	if (stone[i][row] == color) {
	  //System.out.println("Othello: checkStone(右が裏返せるぞ)");
	  direction[RIGHT] = true;
	}
      } 
      /* 上 */
      if (row > 1 && stone[column][row-1] == -color) {
	for (j = row-2; j > 0 && stone[column][j] == -color; j--);
	if (stone[column][j] == color) {
	  //System.out.println("Othello: checkStone(上が裏返せるぞ)");
	  direction[UPPER] = true;
	}
      }
      /* 下 */
      if (row < 6 && stone[column][row+1] == -color) {
	for (j = row+2; j < 7 && stone[column][j] == -color; j++);
	if (stone[column][j] == color) {
	  //System.out.println("Othello: checkStone(下が裏返せるぞ)");
	  direction[LOWER] = true;
	}
      }
      /* 左上 */
      if (column > 1 && row > 1 && stone[column-1][row-1] == -color) {
	for (i = column-2, j = row-2; i > 0 && j > 0 
	     && stone[i][j] == -color; i--, j--);
	if (stone[i][j] == color) {
	  //System.out.println("Othello: checkStone(左上が裏返せるぞ)");
	  direction[UPPERLEFT] = true;
	}
      }
      /* 右上 */
      if (column < 6 && row > 1 && stone[column+1][row-1] == -color) {
	for (i = column+2, j = row-2; i < 7 && j > 0 
	     && stone[i][j] == -color; i++, j--);
	if (stone[i][j] == color) {
	  //System.out.println("Othello: checkStone(右上が裏返せるぞ)");
	  direction[UPPERRIGHT] = true;
	}
      }
      /* 右下 */
      if (column < 6 && row < 6 && stone[column+1][row+1] == -color) {
	for (i = column+2, j = row+2; i < 7 && j < 7 
	     && stone[i][j] == -color; i++, j++);
	if (stone[i][j] == color) {
	  //System.out.println("Othello: checkStone(右下が裏返せるぞ)");
	  direction[LOWERRIGHT] = true;
	}
      }
      /* 左下 */
      if (column > 1 && row < 6 && stone[column-1][row+1] == -color) {
	for (i = column-2, j = row+2; i > 0 && j < 7 
	     && stone[i][j] == -color; i--, j++);
	if (stone[i][j] == color) {
	  //System.out.println("Othello: checkStone(左下が裏返せるぞ)");
	  direction[LOWERLEFT] = true;
	}
      } 

      /* どこかの方向で裏返せるなら、trueを返す */
      for (i = 0; i < 8; i++){
	if (direction[i] == true){
	  return true;
	}
      }

      //System.out.println("Othello: checkStone(どこも裏返せないぞ)");
      //System.out.println("Othello: checkStone(ここには置けないぞ)");
      return false;
    }
  }

  /**
   * Turn the stone over.
   * <jp 実際に石をひっくり返す>
   */
  public void turnStone(int column, int row, int color) {
    /* まず、石を置く */
    stone[column][row] = color;

    /* i は列を、j は行を表すのに用いる */
    int i,j;

    /* 左 */
    if (direction[LEFT] == true){
      //System.out.println("Othello: turnStone(左を裏返すぞ)");
      for (i = column-1; stone[i][row] != color; i--){
	stone[i][row] = - stone[i][row];
      }
    }
    /* 右 */
    if (direction[RIGHT] == true){
      //System.out.println("Othello: turnStone(右を裏返すぞ)");
      for (i = column + 1; stone[i][row] != color; i++){
	stone[i][row] = - stone[i][row];
      }
    }
    /* 上 */
    if (direction[UPPER] == true){
      //System.out.println("Othello: turnStone(上を裏返すぞ)");
      for (j = row - 1; stone[column][j] != color; j--){
	stone[column][j] = - stone[column][j];
      }
    }
    /* 下 */
    if (direction[LOWER] == true){
      //System.out.println("Othello: turnStone(下を裏返すぞ)");
      for (j = row + 1; stone[column][j] != color; j++){
	stone[column][j] = - stone[column][j];
      }
    }
    /* 左上 */
    if (direction[UPPERLEFT] == true){
      //System.out.println("Othello: turnStone(左上を裏返すぞ)");
      for (i = column-1, j = row-1; stone[i][j] != color; i--, j--){
	stone[i][j] = - stone[i][j];
      }
    }
    /* 右上 */
    if (direction[UPPERRIGHT] == true){
      //System.out.println("Othello: turnStone(右上を裏返すぞ)");
      for (i = column+1, j = row-1; stone[i][j] != color; i++, j--){
	stone[i][j] = - stone[i][j];
      }
    }
    /* 右下 */
    if (direction[LOWERRIGHT] == true){
      //System.out.println("Othello: turnStone(右下を裏返すぞ)");
      for (i = column+1, j = row+1; stone[i][j] != color; i++, j++){
	stone[i][j] = - stone[i][j];
      }
    }
    /* 左下 */
    if (direction[LOWERLEFT] == true){
      //System.out.println("Othello: turnStone(左下を裏返すぞ)");
      for (i = column-1, j = row+1; stone[i][j] != color; i--, j++){
	stone[i][j] = - stone[i][j];
      }
    }

    //System.out.println("Othello: turnStone(裏返しは、おしまい)");    
  }
}

/**
 * This is a class which define the strategy of computer othello player.<br>
 * 今は、石を置けるマスを無作為に選んで、石を置くだけのアルゴリズム。
 * 将来的には、このクラスを拡張することによって自分のプレイヤーを
 * つくって、大会を開いてみたい。
 *
 * @author Yukio Hirai (平井 幸夫)
 * <a href="mailto:t94369yh@sfc.keio.ac.jp">(t94369yn@sfc.keio.ac.jp)</a>
 *  - URL <A HREF="http://www.sfc.keio.ac.jp/~t94369yh/">
 * http://www.sfc.keio.ac.jp/~t94369yh/</a>
 * @version 0.1  <i>Sep 3, 1995</i>
 */
class OthelloPlayer {
  Othello parent;

  int i,j;

  OthelloPlayer(Othello parent){
    this.parent = parent;
  }

  public void decide(){
    if (parent.checkAll(parent.turn) == true) {
      do {
	//Math.srandom(System.currentTimeMillis());
	i = (int)(Math.random() * 8);
	//Math.srandom(System.currentTimeMillis() * System.currentTimeMillis());
	j = (int)(Math.random() * 8);
      } 
      while (! parent.checkStone(i, j, parent.turn));
      
      parent.turnStone(i, j, parent.turn); /* 石を置き、裏返す */
      //parent.playAudio(i, j);              /* 読み上げる */
    }
    parent.turn = - parent.turn;
  }
}
