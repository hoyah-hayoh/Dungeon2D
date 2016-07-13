package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import main.Main;

public class Player {
	
	private Main m;
	boolean[] xy = new boolean[4];
	public int x = 0;
	public int y = 0;
	int size = Tile.size;
	public Player(Main m, int startx, int starty){
		this.m = m;
		this.x = startx;
		this.y = starty;
		m.xsc = -(x-m.f.getWidth()/2);
		m.ysc = -(y-m.f.getHeight()/2);
	}
	public boolean[] move(double xmove, double ymove){
		xy[0] = false;//wall on the right
		xy[1] = false;//left
		xy[2] = false;//bottom
		xy[3] = false;//top
/*		
		if(x+xmove <= 0){
			xy[1] = false;
		}else if(x+xmove+size >= m.getCurrentLevel().grid_x){
			xy[0] = false;
		}else if(y+ymove <= 0){
			xy[3] = false;
		}else if(y+ymove+size >= m.getCurrentLevel().grid_y){
			xy[2] = false;
		}*/
		
		for(Tile e : new ArrayList<Tile>(m.getCurrentLevel().grid)){
			if(e.type == "Wall"){
				Rectangle object = new Rectangle((int)e.x, (int)e.y, e.size, e.size);
				Rectangle aftermove = new Rectangle((int)(x+xmove), (int)(y), size, size);
				if(object.intersects(aftermove)){
					if(aftermove.x < object.x){
						xy[0] = true;
					}else{
						xy[1] = true;
					}
				}
				Rectangle object2 = new Rectangle((int)e.x, (int)e.y, e.size, e.size);
				Rectangle aftermove2 = new Rectangle((int)(x), (int)(y+ymove), size, size);
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
			m.xsc -= xmove;
		}
		if(xmove < 0 && !xy[1]){
			x+=xmove;
			m.xsc -= xmove;
		}
		if(ymove > 0 && !xy[2]){
			y+=ymove;
			m.ysc -= ymove;
		}
		if(ymove < 0 && !xy[3]){
			y+=ymove;
			m.ysc -= ymove;
		}

		return xy;
	}
	public void render(Graphics2D g){
		g.setColor(Color.YELLOW);
		g.fillOval(x + m.xsc, y + m.ysc, size, size);
	}
	public boolean contains(double xpoint, double ypoint, double xcir, double ycir, double radius) {
	    double ellw = radius*2;
	    if (ellw <= 0.0) {
	        return false;
	    }
	    double normx = (xpoint - xcir) / ellw - radius;
	    double ellh = radius*2;
	    if (ellh <= 0.0) {
	        return false;
	    }
	    double normy = (ypoint - ycir) / ellh - radius;
	    return (normx * normx + normy * normy) < radius*radius;
	}
	public void shoot() {
		double targetx = (x)+m.mousex-(m.f.getWidth()/2);
		double targety = (y)+m.mousey-(m.f.getHeight()/2);
		double deltaX = targetx - this.x;
		double deltaY = targety - this.y;	
		double dir = Math.atan2(deltaY, deltaX);
		m.projectiles.add(new Projectile(dir, x, y, m));
	}
}
