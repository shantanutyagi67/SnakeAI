package BFS_AI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
/*
 board size - small=10 medium=15 large=20 (size)
 score tracking - score=snake length -1
 */
class Player{
	Player(int size){
		this.size = size;
		
		foodx = (int) ((size)*Math.random());
		foody = (int) ((size)*Math.random());
	}
	ArrayList<Integer> x = new ArrayList<Integer>();
	ArrayList<Integer> y = new ArrayList<Integer>();
	Vector<Integer>dirx = new Vector<Integer>();
	Vector<Integer>diry = new Vector<Integer>();
	int size;
	int foodx,foody;
	void newSnek() {
		x.add((int)((size-10)*Math.random())+5);
		y.add((int)((size-10)*Math.random())+5);
	}
	void newDir() {
		dirx.clear();
		diry.clear();
		double i =Math.random()%2;
		double j =Math.random()%2;
		if(i<0.5) {
			diry.add(0);
			if(j<0.5) dirx.add(-1);
			else dirx.add(1);
		}
		else {
			dirx.add(0);
			if(j<0.5) diry.add(-1);
			else diry.add(1);
		}
	}
	void AImove(int tmp) {
		x.set(0,x.get(0)+dirx.get(tmp));
		if(x.get(0)<0)
			x.set(0, size-1);
		else
			x.set(0, x.get(0)%size);
		y.set(0,y.get(0)+diry.get(tmp));
		if(y.get(0)<0)
			y.set(0, size-1);
		else
			y.set(0, y.get(0)%size);
	}
	void direction(int dx, int dy) {
		dirx.add(dx);
		diry.add(dy);
	}
	void newFood() {
		foodx = (int) ((size)*Math.random());
		foody = (int) ((size)*Math.random());
	}
	public boolean checkCollision(boolean food) {
		int lim = x.size();
		if(lim==1) return false;
		for(int j=1;j<lim;++j) {
			if(x.get(0)==x.get(j)&&y.get(0)==y.get(j))
				return true;
		}
		return false;
	}
	public void AIthink(int min, Vector<Vector<Integer>> maze, Vector<Vector<Boolean>> visited, int currentCellI, int currentCellJ, Vector<Vector<Vector<Integer>>> padosiI,Vector<Vector<Vector<Integer>>> padosiJ,Vector<Integer> queueI, Vector<Integer> queueJ, Vector<Integer> pathI, Vector<Integer> pathJ, boolean think, Vector<Vector<Integer>> parentI, Vector<Vector<Integer>> parentJ, Vector<Vector<Integer>> depth) {
		// ALGO USED: BFS
		currentCellI = y.get(0);
		currentCellJ = x.get(0);
		visited.get(currentCellI).set(currentCellJ,true);//set initial cell as visited
		queueI.add(currentCellI);//add initial cell to stack
		queueJ.add(currentCellJ);
		while(!queueI.isEmpty()) {//loop till stack is empty
			//pop stack and set the popped value to current cell
			currentCellI=queueI.get(0);//setting  current cell 
			currentCellJ=queueJ.get(0);
			//solved.get(currentCellI).set(currentCellJ,true);
			if(currentCellI==foody&&currentCellJ==foodx&&queueI.size()<min) {
				min = queueI.size();
				pathI.clear();
				pathJ.clear();
				int crawlI = foody;
				int crawlJ = foodx;
				pathI.add(foody);
				pathJ.add(foodx);
				while(pathI.get(pathI.size()-1)!=y.get(0)||pathJ.get(pathJ.size()-1)!=x.get(0)) {
					pathI.add(parentI.get(crawlI).get(crawlJ));
					pathJ.add(parentJ.get(crawlI).get(crawlJ));
					if(parentI.get(crawlI).get(crawlJ)==-1&& parentJ.get(crawlI).get(crawlJ)==-1) break;
					crawlI = parentI.get(crawlI).get(crawlJ);
					crawlJ = parentJ.get(crawlI).get(crawlJ);
				}
			}
			queueI.remove(0);//popping of stack
			queueJ.remove(0);
			int cnt =padosiI.get(currentCellI).get(currentCellJ).size();//total neighbours of current cell
			while(cnt!=0) {
				cnt--;
				if(!visited.get(padosiI.get(currentCellI).get(currentCellJ).get(cnt)).get(padosiJ.get(currentCellI).get(currentCellJ).get(cnt))) {
//					depth.get(padosiI.get(currentCellI).get(currentCellJ).get(cnt)).set(padosiJ.get(currentCellI).get(currentCellJ).get(cnt),
//							depth.get(currentCellI).get(currentCellJ)+1);
					visited.get(padosiI.get(currentCellI).get(currentCellJ).get(cnt)).set(padosiJ.get(currentCellI).get(currentCellJ).get(cnt),true);
					parentI.get(padosiI.get(currentCellI).get(currentCellJ).get(cnt)).set(padosiJ.get(currentCellI).get(currentCellJ).get(cnt),currentCellI);
					parentJ.get(padosiI.get(currentCellI).get(currentCellJ).get(cnt)).set(padosiJ.get(currentCellI).get(currentCellJ).get(cnt),currentCellJ);
					queueI.add(padosiI.get(currentCellI).get(currentCellJ).get(cnt));
					queueJ.add(padosiJ.get(currentCellI).get(currentCellJ).get(cnt));
				}
			}
//				System.out.println(foody+","+foodx);
//				System.out.println(pathI);
//				System.out.println(pathJ);
			
		}
		Collections.reverse(pathI);
		Collections.reverse(pathJ);
// hardcoding the solution to diagonal movement by inserting missing piece
//		for(int i=0;i<pathI.size()-1;i++) {
//			if( Math.abs(pathI.get(i)-pathI.get(i+1)) + Math.abs(pathJ.get(i)-pathJ.get(i+1)) == 2 ) {
//				pathI.insertElementAt(pathI.get(i), i+1);
//				pathJ.insertElementAt(pathJ.get(i), i+1);
//			}
//		}
//		System.out.println(y.get(0)+","+x.get(0));
//		System.out.println(foody+","+foodx);
//		System.out.println(pathI);
//		System.out.println(pathJ);
		//not needed
		//only needed when move method is to be called
		//but better approach was found out
//		int controlI = y.get(0) - pathI.get(1);
//		if(controlI==9) controlI=1;
//		if(controlI==-9) controlI=-1;
//		int controlJ = x.get(0) - pathJ.get(1);
//		if(controlJ==9) controlJ=1;
//		if(controlJ==-9) controlJ=-1;
//		if(controlI==0 && controlJ==1) {//move right
//			direction(1,0);
//		}
//		else if(controlI==0 && controlJ==-1) {//move left
//			direction(-1,0);
//		}
//		else if(controlI==-1 && controlJ==0) {//move up
//			direction(0,-1);
//		}
//		else if(controlI==1 && controlJ==0) {//move down
//			direction(0,1);
//		}
	}
}//class over
public class bfs extends JComponent implements Runnable, KeyListener{
	private static final long serialVersionUID = 1L;
	bfs(){
		addKeyListener(this);
		setFocusable(true);
	}
	static long startTime;
	static JFrame frame = new JFrame("Single Player");
	static JButton butt = new JButton("Foo");
	static String bSize,difficulty;
	static int size = 10, time =150;
	Player p1 = new Player(size);
	static Vector<Vector<Integer>> maze = new Vector<Vector<Integer>>();
	boolean start = false, end = false, sleeping=false,checkCollision,think=true, noPath=false;
	boolean food = false,newGame=false, paused=false,allowMove=true, skipMove=false;
	int itr = 0,score=0;
	int oldfoodx=0,oldfoody=0,olddx,olddy;
	static Vector<Vector<Vector<Integer>>> padosiI = new Vector<Vector<Vector<Integer>>>();
	static Vector<Vector<Vector<Integer>>> padosiJ = new Vector<Vector<Vector<Integer>>>();
	static Vector<Vector<Boolean>> visited = new Vector<Vector<Boolean>>();
	static Vector<Vector<Integer>> parentI = new Vector<Vector<Integer>>();
	static Vector<Vector<Integer>> parentJ = new Vector<Vector<Integer>>();
	static Vector<Vector<Integer>> depth = new Vector<Vector<Integer>>();
	static Vector<Integer> queueI = new Vector<Integer>();
	static Vector<Integer> queueJ = new Vector<Integer>();
	static Vector<Integer> pathI = new Vector<Integer>();
	static Vector<Integer> pathJ = new Vector<Integer>();
	static int currentCellI=0, currentCellJ=0;//select initial cell
	static int min = Integer.MAX_VALUE,tmp=0;
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
//		do {
//		System.out.print("Enter board size (small,medium,large): ");
//		bSize = sc.nextLine().toLowerCase();
//		if(bSize.equals("small")) {
//			size =10;
//			break;
//		}
//		else if(bSize.equals("medium")) {
//			size =15;
//			break;
//		}
//		else if(bSize.equals("large")) {
//			size =20;
//			break;
//		}
//		}while(true);
		size=10;
		sc.close();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0,0,1000,1000);
		frame.getContentPane().add(new bfs());
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setVisible(true);
	}
	private static void Initialize() {
		for(int i=0;i<size;i++) {
			visited.add(new Vector<Boolean>());
			parentI.add(new Vector<Integer>());
			parentJ.add(new Vector<Integer>());
			depth.add(new Vector<Integer>());
			for(int j=0;j<size;j++) {
				visited.get(i).add(false);
				//-1 for root node, -2 for unvisited nodes
				//after search complete no -1 value node remains
				parentI.get(i).add(-1);
				parentJ.get(i).add(-1);
				parentJ.get(i).add(0);
			}
		}
		for(int i=0;i<size;i++) {
			maze.add(new Vector<Integer>());
			for(int j=0;j<size;j++) {
				maze.get(i).add(0);
			}
		}
		for(int i=0;i<size;i++) {
			padosiI.add(new Vector<Vector<Integer>>());
			padosiJ.add(new Vector<Vector<Integer>>());
			for(int j=0;j<size;j++) {
				padosiI.get(i).add(new Vector<Integer>());
				padosiJ.get(i).add(new Vector<Integer>());
			}
		}
	}
	double board = 850;
	double thick = board/size;
	
	public void paint(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		g2D.translate(frame.getWidth()/2-board/2, frame.getHeight()/2-board/2);		
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    	rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    	g2D.setRenderingHints(rh);
    	g2D.setStroke(new BasicStroke(4f));
		g2D.setColor(Color.WHITE);
		g2D.fill(new Rectangle2D.Double(0, 0, size*thick, size*thick));
		g2D.setStroke(new BasicStroke(1f));
		g2D.setColor(Color.BLACK);
		for(int i=0;i<=size;i++)
			g2D.draw(new Line2D.Double(0, i*thick, size*thick, i*thick));
		for(int j=0;j<=size;j++)
			g2D.draw(new Line2D.Double(j*thick, 0, j*thick, size*thick));
		if(newGame) {
		if(!start) {
			startTime = System.nanoTime()/1000000000;
			p1.newSnek();
			p1.newDir();
			Initialize();
			start = true;
		}
		if(!paused) {
		for(int i=0;i<p1.x.size();i++)
			maze.get(p1.y.get(i)).set(p1.x.get(i), 1);
		maze.get(p1.foody).set(p1.foodx, 2);
//		for(int i=0;i<size;i++)
//			System.out.println(maze.get(i));
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				if(maze.get(i).get(j+1==size?0:j+1)%2==0) {
					padosiJ.get(i).get(j).add(j+1==size?0:j+1);
					padosiI.get(i).get(j).add(i);
				}
				if(maze.get(i).get(j-1==-1?size-1:j-1)%2==0) {
					padosiJ.get(i).get(j).add(j-1==-1?size-1:j-1);
					padosiI.get(i).get(j).add(i);
				}
				if(maze.get(i+1==size?0:i+1).get(j)%2==0) {
					padosiI.get(i).get(j).add(i+1==size?0:i+1);
					padosiJ.get(i).get(j).add(j);
				}
				if(maze.get(i-1==-1?size-1:i-1).get(j)%2==0) {
					padosiI.get(i).get(j).add(i-1==-1?size-1:i-1);
					padosiJ.get(i).get(j).add(j);
				}
			}
		}
//		for(int i=0;i<size;i++) {
//			for(int j=0;j<size;j++) {
//				System.out.print(padosi.get(i).get(j));
//			}
//			System.out.println();
//		}
		//System.out.println("test1");
    	if(think) {
    		p1.AIthink(min,maze,visited,currentCellI,currentCellJ,padosiI,padosiJ,queueI,queueJ,pathI,pathJ,think,parentI,parentJ,depth);
    		//think=false;
    	}
    	//System.out.println("test2");
    	if(pathI.size()>1) {
    		for(int i=p1.x.size()-1;i>0;i--) {
    			p1.x.set(i, p1.x.get(i-1));
    			p1.y.set(i, p1.y.get(i-1));
    		}
    		//p1.AImove(tmp);
    		//if(tmp+1!=pathI.size()) {
    		p1.x.set(0, pathJ.elementAt(1));
    		p1.y.set(0, pathI.elementAt(1));
    		//}
    	}
    	else noPath = true;
    	//System.out.println("test3");
    	tmp++;
    	checkCollision = p1.checkCollision(food);
    	if(noPath) checkCollision=true;
		//System.out.println("test4");
		
		if(p1.x.get(0)==p1.foodx&&p1.y.get(0)==p1.foody) {
			p1.x.add(p1.foodx);
			p1.y.add(p1.foody);
			//System.out.println("test4.5");
			do {
			p1.newFood();
			//System.out.println("newfood: "+p1.foodx+","+p1.foody);
			} while(maze.get(p1.foody).get(p1.foodx)!=0);
			//System.out.println("test5");
			score++;
			itr=0;
			//think=true;
			tmp=0;
//			p1.dirx.clear();
//			p1.diry.clear();
			food=false;
			
		}
		
//		if(food) {
//			itr++;
//		}
//		if(food) {//&&itr==p1.x.size()) {
//			p1.x.add(oldfoodx);
//			p1.y.add(oldfoody);
//			food=false;
//			itr=0;
//			pathI.clear();
//			pathJ.clear();
//			min = Integer.MAX_VALUE;
//		}
		}
		g2D.setColor(Color.getHSBColor((float) (340.000/360.000), (float) 1, (float) 0.8));
		g2D.fill(new Ellipse2D.Double(p1.foodx*thick+thick/4.00,p1.foody*thick+thick/4.00,thick/2.00,thick/2.00));
		for(int i=p1.x.size()-1;i>=0;i--) {
			g2D.setColor(Color.getHSBColor((float) (45.900/360.000), (float)(i/2.00+p1.x.size()/2.00)/p1.x.size(), (float) 1));
			if(i==0) g2D.setColor(Color.getHSBColor((float) (10.600/360.000), (float) 0.9, (float) 1));
			g2D.fill(new Rectangle2D.Double(p1.x.get(i)*thick+thick/8.000,p1.y.get(i)*thick+thick/8.000,3*thick/4.000,3*thick/4.000));
		}
		g2D.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g2D.setColor(Color.RED);
		g2D.drawString("MODE: "+size+" , AI", 0,870 );
		long time = System.nanoTime()/1000000000-startTime;
		g2D.drawString("SCORE: "+score+", TIME: "+ time/60+"m "+ time%60 + "s", 0,-10 );
		
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				maze.get(i).set(j,0);
				visited.get(i).set(j, false);
				padosiI.get(i).get(j).clear();
				padosiJ.get(i).get(j).clear();
				parentI.get(i).set(j, -1);
				parentJ.get(i).set(j, -1);
				//depth.get(i).set(j, 0);
			}
		}
		p1.dirx.clear();
		p1.diry.clear();
		pathI.clear();
		pathJ.clear();
		queueI.clear();
		queueJ.clear();
		min = Integer.MAX_VALUE;
		//System.out.println("test6");
		run();
		if(!checkCollision) {
			repaint();
		}
		else {
			System.out.println("SCORE: "+score+", TIME: "+time/60+"m "+time%60+"s");
			try {
			      File myObj = new File("MATLAB/ScoreDataBFS.txt");
			      myObj.createNewFile();
			      FileWriter myWriter = new FileWriter(myObj,true);
			      myWriter.write(score+";"+"\n");
			      myWriter.close();
			    } catch (IOException e) {
			    	e.printStackTrace();
			      }
			reset();
			newGame=true;
			repaint();
		}
		
	} else repaint();//for paused
	}
	public void run() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER) {
			reset();
			newGame=false;
		}
		if (e.getKeyCode()==KeyEvent.VK_SPACE) {
			newGame = true;
		}
		if (e.getKeyCode()==KeyEvent.VK_P) {
			if(!paused) {
				paused = true;
				olddx = p1.dirx.get(p1.dirx.size()-1);
				olddy = p1.diry.get(p1.diry.size()-1);
				p1.direction(0, 0);
			}
			else {
				paused = false;
				p1.direction(olddx,olddy);
			}
		}
	}
	private void reset() {
		maze.clear();
		p1.x.clear();
		p1.y.clear();
		p1.newFood();
		start = false;
		end = false;
		score = 0;
		allowMove=true;
		paused = false;
		skipMove=false;
		visited.clear();
		queueI.clear();
		padosiI.clear();
		queueJ.clear();
		padosiJ.clear();
		min = Integer.MAX_VALUE;
		pathI.clear();
		pathJ.clear();
		tmp=0;
		think = true;
		p1.dirx.clear();
		p1.diry.clear();
		newGame = true;
		parentI.clear();
		parentJ.clear();
		depth.clear();
		noPath=false;
		startTime = System.nanoTime()/1000000000;
	}
	public void keyReleased(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
	}
}
