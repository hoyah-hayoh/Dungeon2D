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
			long start = System.nanoTime();
			try {
				Graphics2D g = null;
				try {
					g = (Graphics2D) m.bs.getDrawGraphics();
					g.setColor(Color.BLACK);
					g.fillRect(0,0,m.f.getWidth(),m.f.getHeight());
					m.render(g);
				} finally {
					if (g != null)
						g.dispose();
				}
				m.bs.show();
			} catch (Exception err) {}
			m.fps = 1e9f/(System.nanoTime() - start);
		}
	}
}
