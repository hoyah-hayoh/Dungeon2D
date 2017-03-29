package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import main.Main;

public class Projectile{

	private double dir = 0;
	int size = 5;
	double speed = 5;
	double distance = 0;
	public double x = 0;
	public double y = 0;
	boolean xy[] = new boolean[4];
	private Main m;
	private double xt = 0;
	private double yt = 0;
	
	public Projectile(double dir, double x, double y, Main m){
		this.dir  = dir;
		this.m = m;
		this.x = x;
		this.y = y;
	}
	public boolean[] move(double xmove, double ymove){
		xy[0] = false;//wall on the right
		xy[1] = false;//left
		xy[2] = false;//bottom
		xy[3] = false;//top
		
		double xvel = Math.cos(dir);
		double yvel = Math.sin(dir);
		
		for(Tile e : new ArrayList<Tile>(m.getCurrentLevel().grid)){
			if(e.type == "Wall"){
				Rectangle object = new Rectangle((int)e.x, (int)e.y, e.size, e.size);
				Rectangle aftermove = new Rectangle((int)(x+xmove), (int)(y), size, size);
				if(object.intersects(aftermove)){
					if(aftermove.x < object.x){
						xy[0] = true;
						/*xvel = -xvel;
						xmove = xvel;
						dir = Math.atan2(yvel, xvel);*/
					}else{
						xy[1] = true;
						/*xvel = -xvel;
						xmove = xvel;
						dir = Math.atan2(yvel, xvel);*/
					}
				}
				Rectangle object2 = new Rectangle((int)e.x, (int)e.y, e.size, e.size);
				Rectangle aftermove2 = new Rectangle((int)(x), (int)(y+ymove), size, size);
				if(object2.intersects(aftermove2)){
					if(aftermove.y < object.y){
						xy[2] = true;
					    /*yvel = -yvel;
						ymove = yvel;
						dir = Math.atan2(yvel, xvel);*/
					}else{
						xy[3] = true;
						/*yvel = -yvel;
						ymove = yvel;
						dir = Math.atan2(yvel, xvel);*/
					}
				}
			}
		}
		if(xy[0] || xy[1] || xy[2] || xy[3]){		
			m.projectiles.remove(this);
		}
		if(xmove > 0 && !xy[0]){
			x+=xmove;
			distance += xmove;
		}
		if(xmove < 0 && !xy[1]){
			x+=xmove;
			distance += xmove;
		}
		if(ymove > 0 && !xy[2]){
			y+=ymove;
			distance += ymove;
		}
		if(ymove < 0 && !xy[3]){
			y+=ymove;
			distance += ymove;
		}

		return xy;
	}
	public void render(Graphics2D g){
		g.setColor(Color.YELLOW);
		g.fillRect((int)x + m.xsc, (int)y + m.ysc, size, size);
		g.setColor(Color.GREEN);
		g.fillRect((int)xt + m.xsc, (int)yt + m.ysc, size, size);
	}
	public void tick(){
		move(speed * Math.cos(dir), speed * Math.sin(dir));
	}
}
