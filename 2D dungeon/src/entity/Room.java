package entity;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import main.Main;

public class Room {
	
	public int size_y;
	public int size_x;
	public int y;
	public int x;
	private Level l;
	public boolean[] xy = new boolean[4];
	public ArrayList<Room> linked = new ArrayList<Room>();
	public int id = -1;
	public Color colour;
	
	public Room(int x, int y, int size_x, int size_y, Level l, int id) {
		this.x = x;
		this.y = y;
		this.size_x = size_x;
		this.size_y = size_y;
		this.l = l;
		this.colour = l.m.randomColor();
		this.id = id;
	}
	public void addWalls(){
		for(int xx = x-Tile.size; xx < x+size_x+Tile.size; xx+=Tile.size){
			for(int yy = y-Tile.size; yy < y+size_y+Tile.size; yy+=Tile.size){
				if(xx == x-Tile.size || xx == x+size_x || yy == y-Tile.size || yy == y+size_y){
					l.newWall(xx, yy, this);
				}
			}
		}
	}
	public void addFloor(){
		for(int xx = x; xx < x+size_x; xx+=Tile.size){
			for(int yy = y; yy < y+size_y; yy+=Tile.size){
				l.newFloor(xx, yy, this);
			}
		}
	}
	public boolean[] move(double xmove, double ymove){
		xy[0] = false;//object on the right
		xy[1] = false;//left
		xy[2] = false;//bottom
		xy[3] = false;//top
		if(x+xmove <= 0){
			xy[1] = true;
		}else if(x+xmove+size_x >= l.grid_x){
			xy[0] = true;
		}else if(y+ymove <= 0){
			xy[3] = true;
		}
		else if(y+ymove+size_y >= l.grid_y){
			xy[2] = true;
		}
		for(Room e : new ArrayList<Room>(l.rooms)){
			if(e != this){
				Rectangle object = new Rectangle((int)e.x, (int)e.y, e.size_x, e.size_y);
				Rectangle aftermove = new Rectangle((int)(x+xmove), (int)(y), size_x, size_y);
				if(object.intersects(aftermove)){
					if(aftermove.x < object.x){
						xy[0] = true;
					}else{
						xy[1] = true;
					}
				}
				Rectangle object2 = new Rectangle((int)e.x, (int)e.y, e.size_x, e.size_y);
				Rectangle aftermove2 = new Rectangle((int)(x), (int)(y+ymove), size_x, size_y);
				if(object2.intersects(aftermove2)){
					if(aftermove.y < object.y){
						xy[2] = true;
					}else{
						xy[3] = true;
					}
				}
			}
		}
		if(xmove > 0 && !xy[0]){
			x+=xmove;
		}
		if(xmove < 0 && !xy[1]){
			x+=xmove;
		}
		if(ymove > 0 && !xy[2]){
			y+=ymove;
		}
		if(ymove < 0 && !xy[3]){
			y+=ymove;
		}

		return xy;
	}
	public int[] getDirectionOf(Room r) {
		int[] dir = new int[2];
		dir[0] = r.x-x;
		dir[1] = r.y-y;
		return dir;
	}
}
