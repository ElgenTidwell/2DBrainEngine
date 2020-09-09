/*
MIT License

Copyright (c) 2020 Elgen Tidwell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.sbengine2d.engine;

import javax.swing.JFrame;

import com.sbengine2d.engine.Input;

public class GameContainer implements Runnable{
	
	private Thread thread;
	private Window window;
	private Renderer r;
	private Input i;
	private AbstractGame game;
	
	private boolean running = true;
	
	//logic rate cap and frames cap
	private final double UPDATE_CAP = 1.0 / 120;
	
	
	private int WIDTH = 1600;
	private int HEIGHT = 900;
	private float scale = 1f;
	

	private String engineVersion = "0.2.4";


	//SET YOUR OWN TITLE FOR EACH GAME
	private String title = "Scrambled Brain Engine 2D v" + engineVersion;

	
	public GameContainer(AbstractGame game) {
		this.game = game;
	}
	
	public void start() {
		window = new Window(this);
		r = new Renderer(this);
		i = new Input(this);
		thread = new Thread(this);
		thread.run();
	}
	
	public void stop() {
		
	}
	
	public void run() {
		running = true;
		
		boolean render = false;
		double ftime = 0;
		double ltime = System.nanoTime() / 1000000000.0;
		double ptime = 0;
		double utime = 0;
		
		double frameTime = 0;
		int frames = 0;
		int fps = 0;
		
		game.init(this);
		//main loop
		while(running)
		{
			render = false;
			
			ftime = System.nanoTime() / 1000000000.0;
			ptime = ftime - ltime;
			ltime = ftime;
			
			utime += ptime;
			frameTime += ptime;
			
			while(utime >= UPDATE_CAP) {
				utime -= UPDATE_CAP;
				render = true;
				game.update(this, (float)UPDATE_CAP);
				
				i.update();
				
				if(frameTime >= 1) {
					frameTime = 0;
					fps = frames;
					frames = 0;
				}
			}
			
			if(render) {
				r.clear();
				game.render(this, r);
				frames++;
				window.update();
			}else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		dispose();
	}
	
	private void dispose() {
		
	}
	
	public Input getI() {
		return i;
	}

	public Window getWindow() {
		return window;
	}
	
	public int getWIDTH() {
		return WIDTH;
	}

	public void setWIDTH(int wIDTH) {
		WIDTH = wIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public void setHEIGHT(int hEIGHT) {
		HEIGHT = hEIGHT;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Renderer getR() {
		return r;
	}
}
