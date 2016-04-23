package main;

import java.awt.Color;
import java.awt.event.KeyEvent;


public class TimeHandler extends Thread{

	private Main m;
	private int tickcount = 0;
	int tickrate = 0;
	boolean run = true;
	public TimeHandler(Main m, int tickrate) {
		this.m = m;
		this.tickrate = tickrate;
	}
	public void run(){
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1.0 / (tickrate);
		
		while (run) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			while (unprocessedSeconds > secondsPerTick) {
				m.tick();
				tickcount++;
				unprocessedSeconds -= secondsPerTick;
				if (tickcount % tickrate == 0) {	
					previousTime += 1000;
				}
			}
		}
	}
}
