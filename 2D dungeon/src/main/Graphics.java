package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Graphics extends Thread{

	private Main m;
	public Graphics(Main m) {
		this.m = m;
	}
	public void run() {
		while(true){
			try {
				Graphics2D g = null;
				try {
					g = (Graphics2D) m.bs.getDrawGraphics();
					g.scale(m.zoomlevel, m.zoomlevel);
					g.setColor(Color.BLACK);
					g.fillRect(0,0,(int)(m.f.getWidth()/m.zoomlevel),(int)(m.f.getHeight()/m.zoomlevel));
					m.render(g);
				} finally {
					if (g != null)
						g.dispose();
				}
				m.bs.show();
			} catch (Exception err) {}
		}
	}
}
