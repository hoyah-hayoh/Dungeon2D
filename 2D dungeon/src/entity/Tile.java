package entity;

import java.awt.Color;

import main.Main;

public class Tile {
	public static int size = 10;
	public int x;
	public int y;
	public String type;
	private Room owner;
	Color color;
	public boolean render = true;
	
	public Tile(int x, int y, String type, Room owner){
		this.x = x;
		this.y = y;
		this.type = type;
		this.owner = owner;
		color = Color.RED;
	}
	public Color getColor(){
		if(type == "Floor"){
			return color;
		}else if(type == "Wall"){
			return Color.DARK_GRAY;
		}
		if(owner == null){
			return Color.RED;
		}else{
			return owner.colour;
		}
	}
}
