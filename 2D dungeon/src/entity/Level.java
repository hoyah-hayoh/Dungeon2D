package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import main.Main;

public class Level {
	
	public ArrayList<Tile> grid;
	public ArrayList<Room> rooms;
	public ArrayList<Tile> lighting;
	public int grid_x = 3200;
	public int grid_y = 2400;
	int number_rooms = 80;
	int room_max_size = 20;
	int room_min_size = 3;
	public Main m;
	public BufferedImage minimap;
	public ArrayList<Room> stairs = new ArrayList<Room>();
	
	
	public Level(Main m, int xsize, int ysize, int roomcount, int rmax, int rmin) {
		this.m = m;
		grid_x = xsize;
		grid_y = ysize;
		number_rooms = roomcount;
		room_max_size = rmax;
		room_min_size = rmin;
		createGrid();
	}
	private void SetMinimap() {
		BufferedImage mini = new BufferedImage((int)(grid_x/Tile.size), (int)(grid_y/Tile.size), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = mini.createGraphics();
		for(Tile e : new ArrayList<Tile>(grid)){
			g.setColor(e.getColor());
			g.fillRect(e.x/Tile.size, e.y/Tile.size, 1, 1);
		}
		g.dispose();

		minimap = mini;
	}
	public void newWall(int x, int y, Room r){
		for(Tile t : getTilesAtPoint(x, y)){
			if(t.type == "Floor"){
				return;
			}
		}
		Tile t = new Tile(x, y, "Wall", r);
		grid.add(t);
	}
	public void newFloor(int x, int y, Room r){
		grid.removeAll(getTilesAtPoint(x, y));
		Tile tile = new Tile(x, y, "Floor", r);
		grid.add(tile);
	}
	private void createGrid() {
		grid = new ArrayList<Tile>();
		rooms = new ArrayList<Room>();
		lighting = new ArrayList<Tile>();
		createRooms(number_rooms);
		shufflerooms();
		LinkRooms();
		addWalls();
		addFloor();
		//fillLevel();
		//Collections.reverse(grid);
		//LinkFloors();
		//SetMinimap();
	}
	public void addWall(int xx, int yy, Room r){
		
	}
	public ArrayList<Tile> getTilesAtPoint(int x, int y) {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
	    for (Tile o : grid) {
	    	
	    	double cx = o.x;
	    	double cy = o.y;
	        if(isPointInSquare(cx, cy, Tile.size, x, y)){
	            tiles.add(o);
	        }
	    }   
	    return tiles;
	}
	private boolean isPointInSquare(double cx, double cy, int size, int x, int y) {
		Rectangle rect = new Rectangle((int)cx, (int)cy, size, size);
	    return rect.contains(new Point(x,y));
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
		for(Room r : new ArrayList<Room>(rooms)){
			if(r != null){
				Room r2 = getNearestNeighbour(r);
				if(r2 != null){
					createPassage(r, r2);
				}
			}
		}
		m.floorscreated++;
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
		int centrex = m.roundm(r.x + r.size_x/2 - Tile.size/2);
		int centrey = m.roundm(r.y + r.size_y/2 - Tile.size/2);
		int centrex2 = m.roundm(r2.x + r2.size_x/2 - Tile.size/2);
		int centrey2 = m.roundm(r2.y + r2.size_y/2 - Tile.size/2);
		
		r.linked.add(r2);
		r2.linked.add(r);

		for(int xtile = 0; xtile < Math.abs(centrex2-centrex)+Tile.size*2; xtile+=Tile.size){
			if(centrex < centrex2){
				newWall(centrex+xtile, centrey-Tile.size, r);
				newWall(centrex+xtile, centrey+Tile.size+Tile.size, r);
				newFloor(centrex+xtile, centrey, r);
				newFloor(centrex+xtile, centrey+Tile.size, r);
			}else{
				newWall(centrex-xtile, centrey-Tile.size, r);
				newWall(centrex-xtile, centrey+Tile.size+Tile.size, r);
				newFloor(centrex-xtile, centrey, r);
				newFloor(centrex-xtile, centrey+Tile.size, r);
			}
		}
		for(int ytile = 0; ytile < Math.abs(centrey2-centrey)+Tile.size*2; ytile+=Tile.size){
			if(centrey < centrey2){
				newWall(centrex2-Tile.size, centrey+ytile, r2);
				newWall(centrex2+Tile.size+Tile.size, centrey+ytile, r2);
				newFloor(centrex2, centrey+ytile, r2);
				newFloor(centrex2+Tile.size, centrey+ytile, r2);
			}else{
				newWall(centrex2-Tile.size, centrey-ytile, r2);
				newWall(centrex2+Tile.size+Tile.size, centrey-ytile, r2);
				newFloor(centrex2, centrey-ytile, r2);
				newFloor(centrex2+Tile.size, centrey-ytile, r2);
			}
		}
	}
	private void addWalls() {
		for(Room r : new ArrayList<Room>(rooms)){
			r.addWalls();
		}
	}
	private void addFloor() {
		for(Room r : new ArrayList<Room>(rooms)){
			r.addFloor();
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
				boolean[] xy = r.move(m.roundm(xmove), m.roundm(ymove));
				for(int c = 0; c < xy.length; c++){
					if(xy[c]){ intersection = true;
						intersections++;
					}
				}
			}
			if(intersections < number_rooms/10){
				break;
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
			int xsize = m.roundm((int)(m.randomBiased(0.9)*((room_max_size-room_min_size)*Tile.size))+(room_min_size*Tile.size));
			int ysize = m.roundm((int)(m.randomBiased(0.9)*((room_max_size-room_min_size)*Tile.size))+(room_min_size*Tile.size));
			
			int[] pt = getRandomPointInCircle(radius, xsize, ysize);
			if(pt[0]-xsize/2 < 0) pt[0] = m.roundm(xsize/2)+Tile.size;
			if(pt[1]-ysize/2 < 0) pt[1] = m.roundm(ysize/2)+Tile.size;
			if(pt[0]+xsize/2 > grid_x) pt[0] = m.roundm(grid_x-xsize/2)-Tile.size;
			if(pt[1]+ysize/2 > grid_y) pt[1] = m.roundm(grid_y-ysize/2)-Tile.size;
			
			rooms.add(new Room(pt[0], pt[1], xsize, ysize, this, rooms.size()));
		}
	}
	public int[] getRandomPointInCircle(int radius, int xsize, int ysize){
		  double t = 2*Math.PI*Math.random();
		  double u = Math.random()+Math.random();
		  double r = 0;
		  if(u > 1) r = 2-u;
		  else r = u;
		  
		  int[] var = new int[2];
		  var[0] = (int) m.roundm(radius*r*Math.cos(t)+grid_x/2-xsize/2);
		  var[1] = (int) m.roundm(radius*r*Math.sin(t)+grid_y/2-ysize/2);
		  return var;
	}
}
