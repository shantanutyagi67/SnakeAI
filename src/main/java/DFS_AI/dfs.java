package DFS_AI;

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
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
//import matlabcontrol.*;
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
		x.set(0,x.get(0)+dirx.get(0));
		if(x.get(0)<0)
			x.set(0, size-1);
		else
			x.set(0, x.get(0)%size);
		y.set(0,y.get(0)+diry.get(0));
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
		if(lim<=4) return false;
		for(int j=1;j<lim;++j) {
			if(x.get(0)==x.get(j)&&y.get(0)==y.get(j))
				return true;
		}
		return false;
	}
	public void AIthink(int min, Vector<Vector<Integer>> maze, Vector<Vector<Boolean>> visited, int currentCellI, int currentCellJ, Vector<Vector<Vector<Integer>>> padosiI,Vector<Vector<Vector<Integer>>> padosiJ,Vector<Integer> stackI, Vector<Integer> stackJ, Vector<Integer> pathI, Vector<Integer> pathJ, boolean think) { // ALGO USED: DFS
		currentCellI = y.get(0);
		currentCellJ = x.get(0);
		visited.get(currentCellI).set(currentCellJ,true);//set initial cell as visited
		stackI.add(currentCellI);//add initial cell to stack
		stackJ.add(currentCellJ);
		while(!stackI.isEmpty()) {//loop till stack is empty
			//pop stack and set the popped value to current cell
			currentCellI=stackI.get(stackI.size()-1);//setting  current cell 
			currentCellJ=stackJ.get(stackJ.size()-1);
			//solved.get(currentCellI).set(currentCellJ,true);
			stackI.remove(stackI.size()-1);//popping of stack
			stackJ.remove(stackJ.size()-1);
			int rand;
			int cnt =padosiI.get(currentCellI).get(currentCellJ).size();//total neighbours of current cell
			int cnt2=0;//number of unvisited neighbours current cell has
			while(cnt!=0) {
				cnt--;
				if(visited.get(padosiI.get(currentCellI).get(currentCellJ).get(cnt)).get(padosiJ.get(currentCellI).get(currentCellJ).get(cnt))==false) {
					cnt2++;
				}
			}
			//if current cell has unvisited neighbours
			if(cnt2>0) {
				do {//find random unvisited neighbour of current cell and storing its index in rand variable
					rand = (new Random()).nextInt(padosiI.get(currentCellI).get(currentCellJ).size());
				}while(visited.get(padosiI.get(currentCellI).get(currentCellJ).get(rand)).get(padosiJ.get(currentCellI).get(currentCellJ).get(rand))==true);   
				stackI.add(currentCellI);//push current cell to stack
				stackJ.add(currentCellJ);
				//Mark the chosen neighbour as visited and push it to the stack
				visited.get(padosiI.get(currentCellI).get(currentCellJ).get(rand)).set(padosiJ.get(currentCellI).get(currentCellJ).get(rand),true);
				stackI.add(padosiI.get(currentCellI).get(currentCellJ).get(rand));
				stackJ.add(padosiJ.get(currentCellI).get(currentCellJ).get(rand));
			}
			//maze solving logic. solution is equal to the current stack values when we reach the destination node
			//if we reach destination multiple times then select the shortest path by updating the solution list
			if(currentCellI==foody&&currentCellJ==foodx&&stackI.size()<min) {
				min = stackI.size();
				pathI.clear();
				pathJ.clear();
				pathI.addAll(stackI);
				pathI.add(foody);
				pathI.remove(0);
				pathJ.addAll(stackJ);
				pathJ.add(foodx);
				pathJ.remove(0);
			}
		}
//		System.out.println(pathI);
//		System.out.println(pathJ);
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
public class dfs extends JComponent implements Runnable, KeyListener{
	private static final long serialVersionUID = 1L;
	dfs(){
		addKeyListener(this);
		setFocusable(true);
	}
	static long startTime;
	static JFrame frame = new JFrame("Single Player");
	static JButton butt = new JButton("Foo");
	static String bSize,difficulty;
	static int size = 10, time =150;
	Player p1 = new Player(size);
	//MatlabControl mc = new MatlabControl();
	static Vector<Vector<Integer>> maze = new Vector<Vector<Integer>>();
	boolean start = false, end = false, sleeping=false,checkCollision,think=true;
	boolean food = false,newGame=false, paused=false,allowMove=true, skipMove=false;
	int itr = 0,score=0;
	int oldfoodx=0,oldfoody=0,olddx,olddy;
	static Vector<Vector<Vector<Integer>>> padosiI = new Vector<Vector<Vector<Integer>>>();
	static Vector<Vector<Vector<Integer>>> padosiJ = new Vector<Vector<Vector<Integer>>>();
	static Vector<Vector<Boolean>> visited = new Vector<Vector<Boolean>>();
	static Vector<Integer> stackI = new Vector<Integer>();
	static Vector<Integer> stackJ = new Vector<Integer>();
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
		frame.getContentPane().add(new dfs());
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setVisible(true);
	}
	private static void Initialize() {
		for(int i=0;i<size;i++) {
			visited.add(new Vector<Boolean>());
			for(int j=0;j<size;j++) {
				visited.get(i).add(false);
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
    	for(int i=p1.x.size()-1;i>0;i--) {
    		p1.x.set(i, p1.x.get(i-1));
    		p1.y.set(i, p1.y.get(i-1));
    	}
    	if(think) {
    		p1.AIthink(min,maze,visited,currentCellI,currentCellJ,padosiI,padosiJ,stackI,stackJ,pathI,pathJ,think);
    		think=false;
//    		System.out.println(pathI);
//    		System.out.println(pathJ);
//    		System.out.println(p1.y+","+p1.x);
//    		System.out.println(p1.foody+","+p1.foodx);
    	}
    	//p1.AImove(tmp);
    	if(tmp!=pathI.size()) {
    	p1.x.set(0, pathJ.elementAt(tmp));
    	p1.y.set(0, pathI.elementAt(tmp));
    	}
    	tmp++;
    	checkCollision = p1.checkCollision(food);
		if(p1.x.get(0)==p1.foodx&&p1.y.get(0)==p1.foody) {
			oldfoodx = p1.foodx;
			oldfoody = p1.foody;
			do {
			p1.newFood();
			} while(maze.get(p1.foody).get(p1.foodx)!=0);
			score++;
			food = true;
			itr=0;
			think=true;
			tmp=0;
			p1.x.add(oldfoodx);
			p1.y.add(oldfoody);
			food=false;
			p1.dirx.clear();
			p1.diry.clear();
			pathI.clear();
			pathJ.clear();
			min = Integer.MAX_VALUE;
		}
//		if(food) {
//			itr++;
//		}
//		if(food) {//&&itr==p1.x.size()) {
//			p1.x.add(oldfoodx);
//			p1.y.add(oldfoody);
//			food=false;
//			itr=0;
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
			}
		}
		p1.dirx.clear();
		p1.diry.clear();
//		pathI.clear();
//		pathJ.clear();
//		stackI.clear();
//		stackJ.clear();
		run();
		if(!checkCollision) {
			repaint();
		}
		else {
			System.out.println("SCORE: "+score+", TIME: "+time/60+"m "+time%60+"s");
			try {
			      File myObj = new File("MATLAB/ScoreDataDFS.txt");
			      myObj.createNewFile();
			      FileWriter myWriter = new FileWriter(myObj,true);// true means don't overwrite
			      myWriter.write(score+";"+"\n");
			      myWriter.close();
			    } catch (IOException e) {
			    	e.printStackTrace();
			      }
//			MatlabProxyFactoryOptions options =
//	                new MatlabProxyFactoryOptions.Builder()
//	                    .setUsePreviouslyControlledSession(true)
//	                    .build();
//
//	    MatlabProxyFactory factory = new MatlabProxyFactory(options);
//	            MatlabProxy proxy = null;
//				try {
//					proxy = factory.getProxy();
//				} catch (MatlabConnectionException e) {
//					e.printStackTrace();
//				}
//				// call user-defined function (must be on the path)
//	            try {
//	            	proxy.eval("MATLAB\\graphDFS.m");
//				} catch (MatlabInvocationException e) {
//					e.printStackTrace();
//				}
//	            // close connection
//	            proxy.disconnect();
			reset();
			newGame=true;
			repaint();
		}
		
	} else repaint();
	}
	public void run() {
		try {
			Thread.sleep(150);
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
//				olddx = p1.dirx.get(p1.dirx.size());
//				olddy = p1.diry.get(p1.diry.size());
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
		stackI.clear();
		padosiI.clear();
		stackJ.clear();
		padosiJ.clear();
		min = Integer.MAX_VALUE;
		pathI.clear();
		pathJ.clear();
		tmp=0;
		think = true;
		p1.dirx.clear();
		p1.diry.clear();
		newGame = true;
		startTime = System.nanoTime()/1000000000;
	}
	public void keyReleased(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
	}
}
