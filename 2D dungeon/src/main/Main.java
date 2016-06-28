package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import entity.Level;
import entity.Player;
import entity.Room;
import entity.Tile;

public class Main {
	
	ArrayList<Level> floors = new ArrayList<Level>();
	BufferStrategy bs;
	public JFrame f;
	Key key;
	public int mousex;
	public int mousey;
	public int xsc = 0;
	public int ysc = 0;
	public double zoomlevel = 0.5;
	int currentfloor = 0;
	public int floorscreated = 0;
	long starttime = 0;
	Player player;
	Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
	
	public Main() {
		createMap(1, 6000, 6000, 25, 20, 3);
		
		f = new JFrame();
		f.setSize(ss.width, ss.height);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.createBufferStrategy(3);
		
		key = new Key(this);
		f.addKeyListener(key);
		f.addMouseMotionListener(key);
		f.addMouseListener(key);
		f.addMouseWheelListener(key);
		bs = f.getBufferStrategy();
		new TimeHandler(this, 64).start();
		new Graphics(this).start();
		
		StartGame();
	}
	private void StartGame() {
		player = new Player(this, getCurrentLevel().rooms.get(0).x, getCurrentLevel().rooms.get(0).y);
	}
	private void createMap(int numberfloors, int width, int height, int roomcount, int rmax, int rmin) {
		starttime = System.currentTimeMillis();
		double prevtime = 0;
		for(int i = 0; i < numberfloors; i++){
			floors.add(new Level(this, width, height, roomcount, rmax, rmin));
			double time = ((System.currentTimeMillis()-starttime)/1000.0);
			System.out.println(""+floors.size()+" / "+numberfloors+" : "+(time-prevtime));
			prevtime = time;
		}
		double time = ((System.currentTimeMillis()-starttime)/1000.0);
		System.out.println("Floors: "+floorscreated+" Time: "+time);
		double rate = time / ((double)floorscreated);
		System.out.println("Rate: "+rate);
	}
	private BufferedImage GetMinimap(Level l) {
		return l.minimap;
	}
	public void render(Graphics2D g) {
		Level l = getCurrentLevel();
		if(l.grid != null){
			for(Tile e : new ArrayList<Tile>(l.grid)){
				int x = e.x+xsc;
				int y = e.y+ysc;
				int size = Tile.size-1;
				
				//if(x > 0 && x+size < ss.width && y > 0 && y+size < ss.height){
					g.setColor(e.getColor());
					g.fillRect(x, y, size, size);
					g.setColor(e.getLighting());
					g.fillRect(x, y, Tile.size-1, Tile.size-1);
				//}
			}
		}
		player.render(g);
		g.setColor(Color.RED);
		g.drawRect(xsc, ysc, l.grid_x, l.grid_y);
		g.drawRect(f.getWidth()-202, 2, 200, 200);
		g.setColor(Color.GREEN);
		g.drawString("Floor: "+currentfloor, 50, 80);
	}
	public static Color randomColor() {
		return new Color((float)randomBiased(0.6), (float)randomBiased(0.6), (float)randomBiased(0.6));
	}
	public static double randomBiased (double bias) {
	     double v = Math.pow(Math.random(), bias); 
	     return v;
	}
	public int roundm(double n) {
		int m = Tile.size;
		return (int) (Math.floor(((n + m - 1)/m))*m);
	}
	public void tick(int tickcount) {	
		key.tick(tickcount);
		
		
		for(Tile t : getCurrentLevel().grid){
			int xp = t.x;
			int yp = t.y;
			int x = player.x;
			int y = player.y;
			int radius = 16*Tile.size;
		    double square_dist = (float) (Math.pow((x - xp),2) + Math.pow((y - yp), 2));
			if(square_dist <= Math.pow(radius, 2)){
				float b = 1f - (float) (square_dist/Math.pow(radius, 2));
				if(t.brightness < b){
					t.brightness = b;
				}
			}
		}
	}
	public static void main(String[] args) {
		new Main();
	}
	public Level getCurrentLevel() {
		return floors.get(currentfloor);
	}
}
