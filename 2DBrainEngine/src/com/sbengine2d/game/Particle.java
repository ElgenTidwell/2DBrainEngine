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
package com.sbengine2d.game;


import java.util.Random;

import com.sbengine2d.engine.GameContainer;
import com.sbengine2d.engine.Renderer;

public class Particle extends GameObject{
	int pid;
	private float speed = 1500;
	private int dir;
	private int tileX,tileY;
	private float offX,offY;
	private int col = 0xffff0000;
	int endcolor;
	float spread = 0;
	float grav = -1.5f;
	float gspeed = 0.5f;
	boolean loops;
	float duration = 0.55f;
	@Override
	public void Start(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		
	}
	
	public Particle(int tX, int tY, int dir, float ox, float oy, int color,int endColor, float spread, float gravity, boolean loop)
	{
		this.dir = dir;
		this.tileX = tX;
		this.tileY = tY;
		this.offX = ox;
		this.offY = oy;
		posX = tileX * GameManager.tileSize + offX;
		posY = tileY * GameManager.tileSize + offY;
		this.endcolor = endColor;
		this.col = color;
		this.spread = spread;
		this.gspeed = gravity;
		this.loops = loop;
	}
	
	@Override
	public void Update(GameContainer gc, GameManager gm, float dt) {
		
		if(!gc.getR().WasRendered((int)posX - 5, (int)posY - 5, 12, 12)) {dead = true;}
		
		grav += gspeed;
		switch(dir)
		{
			case 0: offY += speed * dt; break;
			case 1: offX += speed * dt; break;
			case 2: offY -= speed * dt; break;
			case 3: offX -= speed * dt; break;
			case 4: offX += speed * dt; offY += speed* dt + spread; break;
			case 5: offX -= speed * dt; offY -= speed* dt - spread; break;
			case 6: offX += speed * dt; offY -= speed* dt - spread; break;
			case 7: offX -= speed * dt; offY += speed* dt + spread; break;
		}
		
		offY += (grav + getRandomNumberInRange(-0.45f,0.45f));
		
		offX += getRandomNumberInRange(-spread,spread);
		
		if(offY > GameManager.tileSize)
		{
			tileY ++;
			offY -= GameManager.tileSize;
		}
		if(offY < 0)
		{
			tileY --;
			offY += GameManager.tileSize;
		}
		if(offX > GameManager.tileSize)
		{
			tileX ++;
			offX -= GameManager.tileSize;
		}
		if(offX < 0)
		{
			tileX --;
			offX += GameManager.tileSize;
		}
		posX = tileX * GameManager.tileSize + offX;
		posY = tileY * GameManager.tileSize + offY;
		if(!loops)
		{
			duration -= dt;
		}
		if(duration <= 0)
		{
			dead = true;
		}
	}

	@Override
	public void Render(GameContainer gc, Renderer r) {
		r.DrawFilledRect((int)posX - 5, (int)posY - 5, 14 - (int)getRandomNumberInRange(-2,6), 14 - (int)getRandomNumberInRange(-2,7), col,true);
	}
	
	private static float getRandomNumberInRange(float min, float max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextFloat() * (max - min) + min;
	}
}
