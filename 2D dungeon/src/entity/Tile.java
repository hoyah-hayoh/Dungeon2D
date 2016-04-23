package entity;

import java.awt.Color;

public class Tile {
	public static int size = 10;
	public int x;
	public int y;
	private String type;
	private Room owner;
	
	public Tile(int x, int y, String type, Room owner){
		this.x = x;
		this.y = y;
		this.type = type;
		this.owner = owner;
	}
	public Color getColor(){
		if(type == "Floor"){
			return Color.BLUE;
		}else if(type == "Wall"){
			return Color.CYAN;
		}
		return Color.RED;
		//return owner.colour;
	}
}
