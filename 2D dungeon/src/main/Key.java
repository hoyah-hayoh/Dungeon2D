package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import entity.Tile;

public class Key implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener{

	Main m;
	boolean[] key = new boolean[68535];
	public Key(Main m) {
		this.m = m;
	}
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		key[keycode] = true;
	}
	public void keyReleased(KeyEvent e) {
		int keycode = e.getKeyCode();
		key[keycode] = false;
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
		m.player.shoot();
		
		
		m.mousepressed = true;
	}

	public void mouseReleased(MouseEvent e) {
		m.mousepressed = false;
	}
	
	public void tick(int tickcount){
		if(key[KeyEvent.VK_W]){
				m.player.move(0, -2);
		}
		if(key[KeyEvent.VK_S]){
			m.player.move(0, 2);
		}
		if(key[KeyEvent.VK_A]){
			m.player.move(-2, 0);
		}
		if(key[KeyEvent.VK_D]){
			m.player.move(2, 0);
		}
		if(key[KeyEvent.VK_UP]){
			m.ysc += Tile.size;
		}
		if(key[KeyEvent.VK_DOWN]){
			m.ysc -= Tile.size;
		}
		if(key[KeyEvent.VK_LEFT]){
			m.xsc += Tile.size;
		}
		if(key[KeyEvent.VK_RIGHT]){
			m.xsc -= Tile.size;
		}
		if(key[KeyEvent.VK_F1]){
			double time = ((System.currentTimeMillis()-m.starttime)/1000.0);
			System.out.println("Floors: "+m.floorscreated+" Time: "+time);
			double rate = time / ((double)m.floorscreated);
			System.out.println("Rate: "+rate);
			System.exit(0);
		}
		if(key[KeyEvent.VK_O]){
			if(m.currentfloor < m.floorscreated-1){
				m.currentfloor++;
			}
		}
		if(key[KeyEvent.VK_L]){
			if(m.currentfloor > 0){
				m.currentfloor--;
			}
		}
	}
}
