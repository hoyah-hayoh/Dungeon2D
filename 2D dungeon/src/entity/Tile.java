package entity;

import java.awt.Color;

import main.Main;

public class Tile {
	public static int size = 40;
	public int x;
	public int y;
	public String type;
	private Room owner;
	public float brightness = 0f;
	Color color;
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
	public Color getLighting() {
		return new Color(0f, 0f, 0f, 1f-brightness);
	}
}
