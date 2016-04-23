package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Main;

public class Level {
	
	public ArrayList<Tile> grid;
	public ArrayList<Room> rooms;
	public int grid_x = 3200;
	public int grid_y = 2400;
	int number_rooms = 80;
	int room_max_size = 20;
	int room_min_size = 3;
	public Main m;
	public BufferedImage minimap;
	
	public Level(Main m) {
		this.m = m;
		createGrid();
	}
	private void GetMinimap(Level l) {
		BufferedImage mini = new BufferedImage(l.grid_x, l.grid_y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = mini.createGraphics();
		
		for(Tile e : new ArrayList<Tile>(l.grid)){
			g.setColor(e.getColor());
			g.fillRect(e.x, e.y, Tile.size-1, Tile.size-1);
		}
		
		g.dispose();
		//scale it etc
	}
	private void createGrid() {
		grid = new ArrayList<Tile>();
		rooms = new ArrayList<Room>();
		createRooms(number_rooms);
		shufflerooms();
		addTiles();
		LinkRooms();
	}
	private void LinkRooms() {
		for(Room r : new ArrayList<Room>(rooms)){
			if(r != null){
				Room r2 = getNearestNeighbour(r);
				if(r2 != null){
					createPassage(r, r2);
				}
			}
		}
	}
	private Room getNearestNeighbour(Room r) {
		double dist = 10000000;
		Room room = null;
		for(Room r2 : new ArrayList<Room>(rooms)){
			if(!r.linked.contains(r2) && r2 != r){
				int cx = r.x + r.size_x/2;
				int cy = r.y + r.size_y/2;
				int cx2 = r2.x + r2.size_x/2;
				int cy2 = r2.y + r2.size_y/2;
				double distance = Math.sqrt((cx - cx2) * (cx - cx2) + (cy - cy2) * (cy - cy2));
				if(distance <= dist){ 
					dist = distance; 
					room = r2;
				}
			}
		}
		return room;
	}
	private void createPassage(Room r, Room r2) {
		int centrex = roundm(r.x + r.size_x/2 - Tile.size/2);
		int centrey = roundm(r.y + r.size_y/2 - Tile.size/2);
		int centrex2 = roundm(r2.x + r2.size_x/2 - Tile.size/2);
		int centrey2 = roundm(r2.y + r2.size_y/2 - Tile.size/2);
		
		r.linked.add(r2);
		r2.linked.add(r);
		
		if(r.id < r2.id){
			r2.id = r.id;
			r2.colour = r.colour;
		}else{
			r.id = r2.id;
			r.colour = r2.colour;
		}
		for(int xtile = 0; xtile < Math.abs(centrex2-centrex)+Tile.size; xtile+=Tile.size){
			if(centrex < centrex2){
				Tile tile = new Tile(centrex+xtile, centrey, "", r);
				grid.add(tile);
				Tile tile2 = new Tile(centrex+xtile, centrey+Tile.size, "", r);
				grid.add(tile2);
			}else{
				Tile tile = new Tile(centrex-xtile, centrey, "", r);
				grid.add(tile);
				Tile tile2 = new Tile(centrex-xtile, centrey+Tile.size, "", r);
				grid.add(tile2);
			}
		}
		for(int ytile = 0; ytile < Math.abs(centrey2-centrey)+Tile.size*2; ytile+=Tile.size){
			if(centrey < centrey2){
				Tile tile = new Tile(centrex2, centrey+ytile, "", r2);
				grid.add(tile);
				Tile tile2 = new Tile(centrex2+Tile.size, centrey+ytile, "", r2);
				grid.add(tile2);
			}else{
				Tile tile = new Tile(centrex2, centrey-ytile, "", r2);
				grid.add(tile);
				Tile tile2 = new Tile(centrex2+Tile.size, centrey-ytile, "", r2);
				grid.add(tile2);
			}
		}
	}
	private void addTiles() {
		for(Room r : new ArrayList<Room>(rooms)){
			r.addTiles();
		}
	}
	private void shufflerooms(){
		boolean intersection = false;
		int iterations = 0;
		do{
			int intersections = 0;
			intersection = false;
			for(Room r : new ArrayList<Room>(rooms)){
				double ranx = Math.random();
				double rany = Math.random();
				int xmove = 0;
				int ymove = 0;
				
				double c1 = 0.5, c2 = 0.5;
				
				if(r.xy[0]){
					c1 = 0.75;
				}else if(r.xy[1]){
					c1 = 0.25;
				}
				if(r.xy[2]){
					c2 = 0.75;
				}else if(r.xy[3]){
					c2 = 0.25;
				}
				
				if(ranx < c1){
					xmove = -((10/r.size_x)+Tile.size);
				}else{
					xmove = ((10/r.size_y)+Tile.size);
				}
				if(rany < c2){
					ymove = -((10/r.size_y)+Tile.size);
				}else{
					ymove = ((10/r.size_y)+Tile.size);
				}
				boolean[] xy = r.move(roundm(xmove), roundm(ymove));
				for(int c = 0; c < xy.length; c++){
					if(xy[c]){ intersection = true;
						intersections++;
					}
				}
			}
		}while(intersection);
	}
	private void createRooms(int i) {
		int radius;
		if(grid_x > grid_y){
			radius = grid_y/2;
		}else{
			radius = grid_x/2;	
		}
		for(int r = 0; r < i; r++){
			int xsize = roundm((int)(m.randomBiased(0.8)*((room_max_size-room_min_size)*Tile.size))+(room_min_size*Tile.size));
			int ysize = roundm((int)(m.randomBiased(0.8)*((room_max_size-room_min_size)*Tile.size))+(room_min_size*Tile.size));
			
			int[] pt = getRandomPointInCircle(radius, xsize, ysize);
			if(pt[0]-xsize/2 < 0) pt[0] = roundm(xsize/2)+Tile.size;
			if(pt[1]-ysize/2 < 0) pt[1] = roundm(ysize/2)+Tile.size;
			if(pt[0]+xsize/2 > grid_x) pt[0] = roundm(grid_x-xsize/2)-Tile.size;
			if(pt[1]+ysize/2 > grid_y) pt[1] = roundm(grid_y-ysize/2)-Tile.size;
			
			rooms.add(new Room(pt[0], pt[1], xsize, ysize, this, rooms.size()));
		}
	}
	public int roundm(double n) {
		int m = Tile.size;
		return (int) (Math.floor(((n + m - 1)/m))*m);
	}
	public int[] getRandomPointInCircle(int radius, int xsize, int ysize){
		  double t = 2*Math.PI*Math.random();
		  double u = Math.random()+Math.random();
		  double r = 0;
		  if(u > 1) r = 2-u;
		  else r = u;
		  
		  int[] var = new int[2];
		  var[0] = (int) roundm(radius*r*Math.cos(t)+grid_x/2-xsize/2);
		  var[1] = (int) roundm(radius*r*Math.sin(t)+grid_y/2-ysize/2);
		  return var;
	}
}
