package Game.AIPlay;

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
import java.util.ArrayList;
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
	int dirx;
	int diry;
	int size;
	int foodx,foody;
	void newSnek() {
		x.add((int)((size-10)*Math.random())+5);
		y.add((int)((size-10)*Math.random())+5);
	}
	void newDir() {
		double i =Math.random()%2;
		double j =Math.random()%2;
		if(i<0.5) {
			diry = 0;
			if(j<0.5) dirx = -1;
			else dirx = 1;
		}
		else {
			dirx=0;
			if(j<0.5) diry = -1;
			else diry = 1;
		}
	}
	void AImove() {
		x.set(0,x.get(0)+dirx);
		if(x.get(0)<0)
			x.set(0, size-1);
		else
			x.set(0, x.get(0)%size);
		y.set(0,y.get(0)+diry);
		if(y.get(0)<0)
			y.set(0, size-1);
		else
			y.set(0, y.get(0)%size);
	}
	void direction(int dx, int dy) {
		dirx = dx;
		diry = dy;
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
	public void AIthink(Vector<Vector<Integer>> maze, Vector<Vector<Boolean>> visited, Vector<Point> stack, Point current, Vector<Vector<Vector<Point>>> padosi, int min, Vector<Point> path) { // ALGO USED: DFS
		current.setLocation(x.get(0), y.get(0));
		stack.add(current);
		visited.get(current.y).set(current.x, true);
//		System.out.println(stack);
		while(!stack.isEmpty()) {
			//System.out.println("works");
			current.setLocation(stack.get(stack.size()-1).x, stack.get(stack.size()-1).y);
			stack.remove(stack.size()-1);
			int rand;
			int cnt =padosi.get(current.y).get(current.x).size();
			//System.out.println(cnt);
			int cnt2=0;
			while(cnt!=0) {
				cnt--;
				if(visited.get(padosi.get(current.y).get(current.x).get(cnt).y).get(padosi.get(current.y).get(current.x).get(cnt).x)==false) {
					cnt2++;
				}
			}
			//System.out.println(cnt2);
			if(cnt2>0) {
				do {
					rand = (new Random()).nextInt(padosi.get(current.y).get(current.x).size());
				}while(visited.get(padosi.get(current.y).get(current.x).get(rand).y).get(padosi.get(current.y).get(current.x).get(rand).x)==true);
				stack.add(current);
				visited.get(padosi.get(current.y).get(current.x).get(rand).y).set(padosi.get(current.y).get(current.x).get(rand).x,true);
				stack.add(padosi.get(current.y).get(current.x).get(rand));
				//System.out.println(stack);
			}
			//System.out.println(current.x+","+current.y+":"+foodx+","+foody);
			if(current.x==foodx&&current.y==foody&&stack.size()<min) {
				min = stack.size();
				path.clear();
				path.addAll(stack);
				path.add(new Point(foodx,foody));
				System.out.println(path);
				//System.out.println(path);
			}
		}//stack is empty now
		//System.out.println(foodx+","+foody);
		//AI will now make a decision
//		int controlI = y.get(0) - path.get(0).y;
//		int controlJ = x.get(0) - path.get(0).x;
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
	}//method over
}//class over
public class AI extends JComponent implements Runnable, KeyListener{
	private static final long serialVersionUID = 1L;
	AI(){
		addKeyListener(this);
		setFocusable(true);
	}
	static long startTime;
	static JFrame frame = new JFrame("Single Player");
	static JButton butt = new JButton("Foo");
	static String bSize,difficulty;
	static int size = 15, time =100;
	Player p1 = new Player(size);
	static Vector<Vector<Integer>> maze = new Vector<Vector<Integer>>();
	boolean start = false, end = false, sleeping=false,checkCollision;
	boolean food = false,newGame=false, paused=false,allowMove=true, skipMove=false;
	int itr = 0,score=0;
	int oldfoodx=0,oldfoody=0,olddx,olddy;
	static Vector<Vector<Boolean>> visited = new Vector<Vector<Boolean>>();
	static Vector<Point> stack = new Vector<Point>();
	Point current = new Point();
	static Vector<Vector<Vector<Point>>> padosi = new Vector<Vector<Vector<Point>>>();
	static int min = Integer.MAX_VALUE;
	static Vector<Point> path = new Vector<Point>();
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		do {
		System.out.print("Enter board size (small,medium,large): ");
		bSize = sc.nextLine().toLowerCase();
		if(bSize.equals("small")) {
			size =10;
			break;
		}
		else if(bSize.equals("medium")) {
			size =15;
			break;
		}
		else if(bSize.equals("large")) {
			size =20;
			break;
		}
		}while(true);
		sc.close();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0,0,1000,1000);
		frame.getContentPane().add(new AI());
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
		for(int i=0;i<size;i++) {//Neighbors of each cell (2-4 values depending on corner, edge, middle cell)
			padosi.add(new Vector<Vector<Point>>());
			for(int j=0;j<size;j++) {
				padosi.get(i).add(new Vector<Point>());
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
				if(maze.get(i).get(j+1==size?0:j+1)%2==0)
					padosi.get(i).get(j).add(new Point(i,j+1==size?0:j+1));
				if(maze.get(i).get(j-1==-1?size-1:j-1)%2==0)
					padosi.get(i).get(j).add(new Point(i,j-1==-1?size-1:j-1));
				if(maze.get(i+1==size?0:i+1).get(j)%2==0)
					padosi.get(i).get(j).add(new Point(i+1==size?0:i+1,j));
				if(maze.get(i-1==-1?size-1:i-1).get(j)%2==0)
					padosi.get(i).get(j).add(new Point(i-1==-1?size-1:i-1,j));
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
    	p1.AIthink(maze,visited,stack,current,padosi,min,path);
    	p1.AImove(); 
    	checkCollision = p1.checkCollision(food);
		if(p1.x.get(0)==p1.foodx&&p1.y.get(0)==p1.foody) {
			oldfoodx = p1.foodx;
			oldfoody = p1.foody;
			do {
			p1.newFood();
			} while(p1.x.contains(p1.foodx)&&p1.y.contains(p1.foody));
			score++;
			food = true;
			itr=0;
		}
		if(food) {
			itr++;
		}
		if(food&&itr==p1.x.size()) {
			p1.x.add(oldfoodx);
			p1.y.add(oldfoody);
			food=false;
			itr=0;
		}
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
		g2D.drawString("MODE: "+bSize+" , "+difficulty, 0,870 );
		long time = System.nanoTime()/1000000000-startTime;
		g2D.drawString("SCORE: "+score+", TIME: "+ time/60+"m "+ time%60 + "s", 0,-10 );
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				maze.get(i).set(j,0);
				visited.get(i).set(j, false);
				padosi.get(i).get(j).clear();
			}
		}
		stack.clear();
		path.clear();
		run();
		if(!checkCollision) {
			repaint();
		}
		else {
			System.out.println("SCORE: "+score+", TIME: "+time/60+"m "+time%60+"s");
			reset();
			newGame=false;
			repaint();
		}
		
	} else repaint();
	}
	public void run() {
		try {
			Thread.sleep(500);
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
				olddx = p1.dirx;
				olddy = p1.diry;
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
		stack.clear();
		padosi.clear();
		min = Integer.MAX_VALUE;
		path.clear();
		startTime = System.nanoTime()/1000000000;
	}
	public void keyReleased(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
	}
}
