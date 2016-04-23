package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import entity.Level;
import entity.Room;
import entity.Tile;

public class Main {
	
	ArrayList<Level> floors = new ArrayList<Level>();
	BufferStrategy bs;
	JFrame f;
	Key key;
	public int mousex;
	public int mousey;
	public int xsc = 0;
	public int ysc = 0;
	public double zoomlevel = 1;
	int currentfloor = 0;
	public int floorscreated = 0;
	long starttime = 0;
	public Main() {
		f = new JFrame();
		f.setSize(800, 600);
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
		
		createMap(200, 1000, 1000, 30, 20, 3);
	}
	private void createMap(int numberfloors, int width, int height, int roomcount, int rmax, int rmin) {
		starttime = System.currentTimeMillis();
		for(int i = 0; i < numberfloors; i++){
			floors.add(new Level(this, width, height, roomcount, rmax, rmin));
		}
		System.out.println("Floors: "+floorscreated+" Time: "+((System.currentTimeMillis()-starttime)/1000.0));
		System.out.println("Rate: "+(floorscreated/((System.currentTimeMillis()-starttime)/1000.0)));
		//System.exit(0);
	}
	private BufferedImage GetMinimap(Level l) {
		return l.minimap;
	}
	public void render(Graphics2D g) {
		Level l = floors.get(currentfloor);
		if(l.grid != null){
			for(Room e : new ArrayList<Room>(l.rooms)){
				g.setColor(Color.CYAN);
				g.fillRect(e.x+xsc, e.y+ysc, e.size_x, e.size_y);
			}
			for(Tile e : new ArrayList<Tile>(l.grid)){
				g.setColor(e.getColor());
				g.fillRect(e.x+xsc, e.y+ysc, Tile.size-1, Tile.size-1);
			}
		}
		g.setColor(Color.RED);
		g.drawRect(xsc, ysc, l.grid_x, l.grid_y);
		g.drawImage(l.minimap, f.getWidth()-202, 2, null);
		g.drawRect(f.getWidth()-202, 2, 200, 200);
		g.setColor(Color.YELLOW);
		g.fillOval(f.getWidth()/2 - 10, f.getHeight()/2 - 10, 20, 20);
		double x = -xsc*(200.0/(double)l.grid_x)+f.getWidth()-127;
		double y = -ysc*(200.0/(double)l.grid_y)+57;
		if(x+10 > f.getWidth()-202 && x < f.getWidth()-2 && y+10 > 2 && y < 202){
			g.fillOval((int)x, (int)y, 10, 10);
		}
		g.setColor(Color.GREEN);
		g.drawString("Zoom: "+zoomlevel, 50, 80);
	}
	public Color randomColor() {
		return new Color((float)randomBiased(0.6), (float)randomBiased(0.6), (float)randomBiased(0.6));
	}
	public double randomBiased (double bias) {
	     double v = Math.pow(Math.random(), bias); 
	     return v;
	}
	public void tick() {	
		key.tick();
	}
	public static void main(String[] args) {
		new Main();
	}
}
