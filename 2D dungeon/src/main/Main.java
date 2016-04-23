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
	public double zoomlevel = 0.5;
	
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
		floors.add(new Level(this));
	}
	private BufferedImage GetMinimap(Level l) {
		return l.minimap;
	}
	public void render(Graphics2D g) {
		for(Level l : new ArrayList<Level>(floors)){
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
