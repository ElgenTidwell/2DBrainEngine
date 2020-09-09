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

import java.awt.Color;
import java.util.Random;

import com.sbengine2d.engine.EngineMath;
import com.sbengine2d.engine.GameContainer;
import com.sbengine2d.engine.Renderer;
import com.sbengine2d.engine.audio.SoundClip;
import com.sbengine2d.platformergame.Bullet;

public class EnemyAI extends GameObject{
	
	private boolean Ground = false;
	int delay = 1000;
	private int tileX,tileY;
	private float offX,offY;
	private int speed = 650;
	//private GFXImage image = new GFXImage("/Character.png");
	
	private float fallSpeed = 10;
	private float fallDistance = 0;
	private float jump = -8;
	private int mDir;
	private AI myai = new AI();
	private boolean jumping, shooting;
	float targX;
	int bdelay;
	int healDelay = 500;
	int hitDelay = 10;
	boolean agrovated;
	SoundClip onhit = new SoundClip("/sound/hit_enemy.wav");
	@Override
	public void Start(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		
	}
	
	public EnemyAI(int x, int y, int mHealth, int id) {
		this.tag = "enemy";
		this.tileX = x;
		this.tileY = y;
		this.offX = 0;
		this.offY = 0;
		this.posX = x * GameManager.tileSize;
		this.posY = y * GameManager.tileSize;
		this.width = 32;
		this.height = 32;
		this.maxHealth = mHealth;
		this.health = EngineMath.getRandomIntegerInRange(-(mHealth), mHealth/2);
		this.id = id;
	}
	
	@Override
	public void Update(GameContainer gc, GameManager gm, float dt) {
		if(health >= maxHealth)
		{
			live = true;
		}
		if(!hit)
		{
			healDelay -= 1;
		}else
		{
			healDelay = 500;
			hitDelay --;
		}
		
		if(hitDelay <= 0)
		{
			hit = false;
			hitDelay = 10;
		}
		
		if(healDelay <= 0 && health <= maxHealth)
		{
			health++;
		}
		bdelay --;
		if(health <= 0)
		{
			live = false;
		}
		
		//TODO: Actual ai
		//myai.xpath*16, myai.ypath*16
		calculateAIPaths(dt,gc,gm);
		myai.CalculatePath(gm, tileX, tileY);
		
		
		//L and R
		if(mDir == 1)
		{
			if(gm.getCollision(tileX+1, tileY) || gm.getCollision(tileX+1, tileY + (int) Math.signum((int)offY)))
			{
				if(offX < 0)
				{
					offX += dt*speed;
					if(offX > 0)
					{
						offX = 0;
					}
				}else
					offX = 0;
			}else
			{
				offX += dt*speed;
			}
			if(gm.getCollision(tileX+1, tileY) && !gm.getCollision(tileX+1, tileY - 1))
			{
				offY -= GameManager.tileSize;
				fallDistance = 0;
			}
		}
		if(mDir == 0)
		{
			if(gm.getCollision(tileX-1, tileY) || gm.getCollision(tileX-1, tileY + (int) Math.signum((int)offY)))
			{
				if(offX > 0)
				{
					offX -= dt*speed;
					if(offX < 0)
					{
						offX = 0;
					}
				}else
					offX = 0;
			}else
			{
				offX -= dt*speed;
			}
			if(gm.getCollision(tileX-1, tileY) && !gm.getCollision(tileX-1, tileY - 1))
			{
				offY -= GameManager.tileSize;
				fallDistance = 0;
			}
		}
		//end
		
		//Jump
		
		fallDistance += dt*fallSpeed;
		if(jumping && Ground)
		{
			fallDistance += jump;
			Ground = false;
		}
		
		offY += fallDistance;
		if(fallDistance < 0)
		{
			if((gm.getCollision(tileX, tileY - 1) || gm.getCollision(tileX + (int)Math.signum(offX), tileY - 1)) && offY < 0)
			{
				fallDistance = 0;
				offY = 0;
			}
		}
		if(fallDistance > 0)
		{
			if((gm.getCollision(tileX, tileY + 1) || gm.getCollision(tileX + (int)Math.signum(offX), tileY + 1)) && offY >= 0)
			{
				fallDistance = 0;
				offY = 0;
				Ground = true;
			}
		}
		//end of Jump
		
		//final pos
		if(offY > GameManager.tileSize / 2)
		{
			tileY ++;
			offY -= GameManager.tileSize;
		}
		if(offY < -GameManager.tileSize / 2)
		{
			tileY --;
			offY += GameManager.tileSize;
		}
		if(offX > GameManager.tileSize / 2)
		{
			tileX ++;
			offX -= GameManager.tileSize;
		}
		if(offX < -GameManager.tileSize / 2)
		{
			tileX --;
			offX += GameManager.tileSize;
		}
		posX = tileX * GameManager.tileSize + offX;
		posY = tileY * GameManager.tileSize + offY;
		
		double angle = Math.atan2(posX-myai.xdest, posY-myai.xdest);
		angle = 180+(Math.toDegrees(angle));
		angle = (int)(angle / 48);
		myai.CalculatePath(gm, tileX, tileY);
		//Shooting
		if(shooting && live && bdelay <= 0)
		{
			bdelay = 2;
			
			gm.CreateObject(new Bullet(tileX,tileY, (int)angle, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),id)); //2
		}
		particleSource = live;
	}
	
	void calculateAIPaths(float dt, GameContainer gc, GameManager gm)
	{
		if(agrovated) {return;}
		if(getRandomNumberInRange(0,90) == 2)
			myai.FindRandomDestination(gm,posX,posY);
		
		mDir = sin(myai.xpath*16 - posX);
		
		if(gm.getCollision(myai.xdest/16, myai.ydest/16))
		{
			myai.FindRandomDestination(gm,posX,posY);
		}
		jumping = false;
		
		if(posX > myai.xpath && posX < 18f + myai.xpath)
		{
			if(posY > myai.ypath && posY < 18f + myai.ypath)
			{
				myai.FindRandomDestination(gm,posX,posY);
				myai.CalculatePath(gm, tileX, tileY);
			}
		}
		
		
		if((int)posX/16 == (int)myai.xpath)
		{
			if((int)posY/16 > (int)myai.ypath/16)
			{
				jumping = true;
				myai.FindRandomDestination(gm,posX,posY);
				myai.CalculatePath(gm, tileX, tileY);
				shooting = false;
			}
			if((int)posY/16 == (int)myai.ypath)
			{
				myai.FindRandomDestination(gm,posX,posY);
				myai.CalculatePath(gm, tileX, tileY);
			}else if((int)posY/16 == (int)myai.ypath + 1)
			{
				myai.FindRandomDestination(gm,posX,posY);
				myai.CalculatePath(gm, tileX, tileY);
			}
			else if((int)posY/16 == (int)myai.ypath - 1)
			{
				myai.FindRandomDestination(gm,posX,posY);
				myai.CalculatePath(gm, tileX, tileY);
			}
		}
		if((int)posX/16 == (int)myai.xdest/16)
		{
			if((int)posY/16 == (int)myai.ydest/16)
			{
				myai.FindRandomDestination(gm,posX,posY);
				myai.CalculatePath(gm, tileX, tileY);
			}
			
		}else
		//old "ai"
		/*
		if(getRandomNumberInRange(0,12) == 6)
			mDir = getRandomNumberInRange(0,1);
		*/
		if(getRandomNumberInRange(0,15) < 4)
			shooting = true;
		else
			shooting = false;
			
	}

	@Override
	public void Render(GameContainer gc, Renderer r) {
		if(live)
		{
			r.DrawText(name, (int)posX - 8, (int)posY + 16, 0xfffffff0);
			r.DrawFilledRect((int)posX, (int)posY, 16, (int)(16 + fallDistance), 0xffff6000,true);
			r.DrawFilledRect((int)posX - (maxHealth/4) - 2, (int)posY - 18, maxHealth/2 + 4, 12, 0xff0f0f0f,false);

		}
		//myai.render(gc, r);
		//myai.Render(gc, r);
		r.DrawFilledRect((int)posX - (maxHealth/4), (int)posY - 16, health/2, 8, 0xffff0000,false);
	}
	@Override
	public void onDamage(int damageamm,int fromid,GameManager gm)
	{
		health -= damageamm;
		
		onhit.setVolume(0.2f);
		
		if(fromid == 0 && live)
		{
			onhit.stop();
			onhit.play();
		}
	}
	
	float lerp(float point1, float point2, float d)
	{
	    return point1 + d * (point2 - point1);
	}
	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	public static int sin(float f)
	{
		int r;
		if(f > 0)
			r = 1;
		else
			r = 0;
		return r;
	}
}
