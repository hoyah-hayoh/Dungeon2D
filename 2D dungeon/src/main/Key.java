package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Key implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener{

	Main m;
	public Key(Main m) {
		this.m = m;
	}
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_F1){
			System.exit(0);
		}
		if(key == KeyEvent.VK_UP){
			if(m.zoomlevel < 2){
				m.zoomlevel+=0.01;
			}
		}
		if(key == KeyEvent.VK_DOWN){
			if(m.zoomlevel > 0){
				m.zoomlevel-=0.01;
			}
		}
	}
	public void keyReleased(KeyEvent e) {

	}
	public void keyTyped(KeyEvent e) {

	}
	public void mouseDragged(MouseEvent e) {

	}
	public void mouseMoved(MouseEvent e) {
		m.mousex = e.getX();
		m.mousey = e.getY();
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}
	
	public void tick(){
		int x = m.mousex;
		int y = m.mousey;
		if(x > m.f.getWidth()/2+50) m.xsc -= 2;
		else if(x < m.f.getWidth()/2-50) m.xsc -= -2;
		
		if(y > m.f.getHeight()/2+50) m.ysc -= 2;
		else if(y < m.f.getHeight()/2-50) m.ysc -= -2;
	}
}
