package Game.SnakeAI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
 difficulty - easy=200 medium=150 hard=100 (sleep time)
 score tracking - simple=+1 increased=+snakeLength
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
		//x.add(x.get(0)+1); initial length of snake
		y.add((int)((size-10)*Math.random())+5);
		//y.add(y.get(0));
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
	void move() {
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
//		try {
//			Thread.sleep(50);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	void newFood() {
		foodx = (int) ((size)*Math.random());
		foody = (int) ((size)*Math.random());
	}
	public boolean checkCollision(boolean food) {
		int lim = x.size();
		//if(food) lim--;
		if(lim<=4) return false;
		for(int j=1;j<lim;++j) {
			if(x.get(0)==x.get(j)&&y.get(0)==y.get(j))
				return true;
		}
		return false;
	}
}
public class Snek extends JComponent implements Runnable, KeyListener, ActionListener{
	private static final long serialVersionUID = 1L;
	Snek(){
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
		do {
			System.out.print("Enter difficulty (easy,medium,hard): ");
			difficulty = sc.nextLine().toLowerCase();
			if(difficulty.equals("easy")) {
				time=150;
				break;
			}
			else if(difficulty.equals("medium")) {
				time=100;
				break;
			}
			else if(difficulty.equals("hard")) {
				time=50;
				break;
			}
			}while(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0,0,1000,1000);
		frame.getContentPane().add(new Snek());
		frame.getContentPane().setBackground(Color.DARK_GRAY);
//		frame.add(butt);
//		frame.addWindowFocusListener(new WindowAdapter() {
//		    public void windowGainedFocus(WindowEvent e) {
//		        butt.requestFocusInWindow();
//		    }
//		});
//		frame.remove(butt);
		frame.setVisible(true);
	}
	private static void Initialize() {
		for(int i=0;i<size;i++) {//adding 4 walls to each cell
			maze.add(new Vector<Integer>());
			for(int j=0;j<size;j++) {
				maze.get(i).add(0);
			}
		}
		
	}
	double board = 850;
	double thick = board/size;
	
	public void paint(Graphics g)
	{
//		frame.getContentPane().transferFocus();
//		frame.setEnabled(true);
//		frame.transferFocus();
//		frame.add(butt);//to draw focus here
//		frame.remove(butt);
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
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				maze.get(i).set(j,0);
			}
		}
    	
    	//run();
    	if(!skipMove) {
    		for(int i=p1.x.size()-1;i>0;i--) {
    			p1.x.set(i, p1.x.get(i-1));
    			p1.y.set(i, p1.y.get(i-1));
    		}
    		p1.move(); 
    	}
    	//skipMove=false;
		//skipMove=false;
    	//allowMove=false;
    	//run();
    	checkCollision = p1.checkCollision(food);
		//g2D.draw(new Rectangle2D.Double(0, 0, size*thick, size*thick));
		for(int i=0;i<p1.x.size();i++)
			maze.get(p1.y.get(i)).set(p1.x.get(i), 1);
		//maze.get(p1.foody).set(p1.foodx, 2);
		
		//run();
		maze.get(p1.foody).set(p1.foodx, 2);
		//System.out.println(p1.foodx+","+p1.foody);
		if(p1.x.get(0)==p1.foodx&&p1.y.get(0)==p1.foody) {
			oldfoodx = p1.foodx;
			oldfoody = p1.foody;
			do {
			p1.newFood();
			} while(p1.x.contains(p1.foodx)&&p1.y.contains(p1.foody));
//			for(int i=0;i<p1.x.size();i++)
//				System.out.println(p1.x.get(i)+","+p1.y.get(i));
			score++;
			food = true;
			itr=0;
		}
		if(food) {
			//System.out.println(itr);
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
		g2D.fill(new Rectangle2D.Double(p1.foodx*thick+thick/8.000,p1.foody*thick+thick/8.000,3*thick/4.000,3*thick/4.000));
		for(int i=p1.x.size()-1;i>=0;i--) {
			g2D.setColor(Color.getHSBColor((float) (45.900/360.000), (float)(i/2.00+p1.x.size()/2.00)/p1.x.size(), (float) 1));
			if(i==0) g2D.setColor(Color.getHSBColor((float) (10.600/360.000), (float) 0.9, (float) 1));
			g2D.fill(new Rectangle2D.Double(p1.x.get(i)*thick+thick/8.000,p1.y.get(i)*thick+thick/8.000,3*thick/4.000,3*thick/4.000));
		}
		g2D.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g2D.setColor(Color.RED);
		g2D.drawString("MODE: "+bSize+" , "+difficulty, 0,870 );
		g2D.drawString("SCORE: "+score+", TIME: "+(System.nanoTime()/1000000000-startTime)+"s", 0,-10 );
		run();
		if(!checkCollision) {
			//System.out.println("Collision");
			skipMove = false;
			repaint();
		}
		else {
			System.out.println("SCORE: "+score+", TIME: "+(System.nanoTime()/1000000000-startTime)+"s");
			reset();
			newGame=false;
			repaint();
		}
		
	} else repaint();
	}
	public void run() {
		sleeping = true;
		try {
			Thread.sleep((long) (time));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sleeping = false;
	}
	public void keyPressed(KeyEvent e) {
//		try {
//			Thread.sleep(0);
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
		if(!sleeping&&allowMove) {
			if (e.getKeyCode()==KeyEvent.VK_UP && p1.diry!=1 && p1.diry!=-1) {
        		p1.direction(0, -1);
        		if(!skipMove) {
        			for(int i=p1.x.size()-1;i>0;i--) {
            			p1.x.set(i, p1.x.get(i-1));
            			p1.y.set(i, p1.y.get(i-1));
            		}
            		p1.move();
            		skipMove=true;
            		//repaint();
            	}
        		//run();
        	}
        	else if (e.getKeyCode()==KeyEvent.VK_DOWN && p1.diry!=-1 && p1.diry!=1) {
        		p1.direction(0, 1);
        		if(!skipMove) {
        			for(int i=p1.x.size()-1;i>0;i--) {
            			p1.x.set(i, p1.x.get(i-1));
            			p1.y.set(i, p1.y.get(i-1));
            		}
            		p1.move();
            		skipMove=true;
            		//repaint();
            	}
        		//run();
        	}
        	else if (e.getKeyCode()==KeyEvent.VK_RIGHT && p1.dirx!=-1 && p1.dirx!=1) {
        		p1.direction(1, 0);
        		if(!skipMove) {
        			for(int i=p1.x.size()-1;i>0;i--) {
            			p1.x.set(i, p1.x.get(i-1));
            			p1.y.set(i, p1.y.get(i-1));
            		}
            		p1.move();
            		skipMove=true;
            		//repaint();
            	}
        		//run();
        	}
        	else if (e.getKeyCode()==KeyEvent.VK_LEFT && p1.dirx!=1 && p1.dirx!=-1) {
        		p1.direction(-1, 0);
        		if(!skipMove) {
        			for(int i=p1.x.size()-1;i>0;i--) {
            			p1.x.set(i, p1.x.get(i-1));
            			p1.y.set(i, p1.y.get(i-1));
            		}
            		p1.move();
            		skipMove=true;
            		//repaint();
            	}
        		//run();
        	}
		}
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
		startTime = System.nanoTime()/1000000000;
	}
	public void keyReleased(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
	}
	public void actionPerformed(ActionEvent arg0) {
//          for(Window window :Window.getWindows())
//        	  window.toFront();
	}	
}
