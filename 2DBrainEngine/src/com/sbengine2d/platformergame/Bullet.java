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
package com.sbengine2d.platformergame;


import java.awt.Color;
import java.util.Random;

import com.sbengine2d.engine.EngineMath;
import com.sbengine2d.engine.GameContainer;
import com.sbengine2d.engine.Renderer;
import com.sbengine2d.game.GameManager;
import com.sbengine2d.game.GameObject;
import com.sbengine2d.game.Particle;
import com.sbengine2d.game.ParticleEmmiter;

public class Bullet extends GameObject{
	int pid;
	private float speed = 1500;
	private int dir;
	private int tileX,tileY;
	private float offX,offY;
	private int col = 0xffff0000;
	private float damage = 4;
	float bulletSpread = 0;
	boolean doonce = true;
	int startx,starty;
	
	public Bullet(int tX, int tY, int dir, float ox, float oy, int pid)
	{
		this.dir = dir;
		this.tileX = tX;
		this.tileY = tY;
		this.startx = tX;
		this.starty = tY;
		this.offX = ox;
		this.offY = oy;
		posX = tileX * GameManager.tileSize + offX;
		posY = tileY * GameManager.tileSize + offY;
		this.pid = pid;
		this.bulletSpread = getRandomNumberInRange(-0.5f,0.5f);
	}
	
	@Override
	public void Update(GameContainer gc, GameManager gm, float dt) {
		switch(dir)
		{
		case 5: offY += speed * dt; break;
		case 3: offX += speed * dt; break;
		case 1: offY -= speed * dt; break;
		case 7: offX -= speed * dt; break;
		case 4: offX += speed * dt; offY += (speed)* dt + bulletSpread; break;
		case 0: offX -= speed * dt; offY -= (speed)* dt - bulletSpread; break;
		case 2: offX += speed * dt; offY -= (speed)* dt - bulletSpread; break;
		case 6: offX -= speed * dt; offY += (speed)* dt + bulletSpread; break;
		}
		
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
		offX -= bulletSpread;
		offY += bulletSpread;
		if(gm.getCollision(tileX, tileY))
		{
			this.dead = true;
		}
		posX = tileX * GameManager.tileSize + offX;
		posY = tileY * GameManager.tileSize + offY;
		if(dir == 3)
		{
			
		}
		
		if(gm.getObjectFromCoordinates(tileX, tileY) != null)
		{
			GameObject p = gm.getObjectFromCoordinates(tileX, tileY);
			if(p != null)
			{
				if(pid != p.id)
				{
					p.onDamage((int)damage,pid,gm);
					p.hit = true;
					if(doonce && pid != p.id && p.particleSource)
					{
						if(!gc.getR().WasRendered((int)posX - 5, (int)posY - 5, 5,5)) {return;}
						
						ParticleEmmiter pe = new ParticleEmmiter(Color.RED.hashCode(),tileX,tileY,offX,offY, gm);
						doonce = false;
						if(p.getTag() == "player")
						gm.getCamera().Shake(4);
					}
				}
			}
		}
	}

	@Override
	public void Render(GameContainer gc, Renderer r) {
		r.DrawFilledRect((int)posX - 5, (int)posY - 5, 5, 5, col,3);
	}
	
	private static float getRandomNumberInRange(float min, float max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextFloat() * (max - min) + min;
	}
	int min(int r)
	{
		if(r <= 0)
		{
			return 0;
		}else
		return r;
	}

	@Override
	public void Start(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		
	}
}
