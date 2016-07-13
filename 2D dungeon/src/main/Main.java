package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.util.ArrayList;

import javax.swing.JFrame;

import entity.Level;
import entity.Player;
import entity.Projectile;
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
	int currentfloor = 0;
	public int floorscreated = 0;
	long starttime = 0;
	float fps = 0;
	float fps_display = 0;
	Player player;
	Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
	public boolean mousepressed = false;
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	public Main() {
		createMap(1, 6000, 6000, 10, 40, 6);
		
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
				if(e.render){
					int x = e.x+xsc;
					int y = e.y+ysc;
					int size = Tile.size;
					int radius = 32*Tile.size;
					
					if(x > -size && x < ss.width && y > -size && y < ss.height){		
						float square_dist = (float) (Math.pow((player.x - x), 2) + Math.pow((player.y - y), 2));
						float A = 0;
						float R = 0;
						float G = 0;
						float B = 0;
						if(square_dist <= Math.pow(radius, 2)){
							A = (int)((float)(Math.sqrt(square_dist)/radius));
						}
						g.setColor(e.getColor());
						g.fillRect(x, y, size, size);
						g.setColor(new Color(A,R,G,B));
						g.fillRect(x, y, size, size);
					}			
				}
			}
		}
		for(Projectile p : projectiles){
			p.render(g);
		}
		player.render(g);
		g.setColor(Color.RED);
		g.drawRect(xsc, ysc, l.grid_x, l.grid_y);
		g.drawRect(f.getWidth()-202, 2, 200, 200);
		g.setColor(Color.GREEN);
		g.drawString("Floor: "+currentfloor, 50, 80);
		g.drawString("fps: "+fps_display, 50, 100);
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
		if(tickcount % 10 == 0){
			fps_display = fps;
		}
		key.tick(tickcount);
		for(Projectile p : new ArrayList<Projectile>(projectiles)){
			p.tick();
		}
	}
	public static void main(String[] args) {
		new Main();
	}
	public Level getCurrentLevel() {
		return floors.get(currentfloor);
	}
}
