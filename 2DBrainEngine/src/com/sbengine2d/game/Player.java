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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.sbengine2d.engine.EngineMath;
import com.sbengine2d.engine.GameContainer;
import com.sbengine2d.engine.Renderer;
import com.sbengine2d.engine.audio.SoundClip;
import com.sbengine2d.platformergame.Bullet;
public class Player extends GameObject{
	
	private boolean Ground = false;
	int hitDelay = 10;
	int healDelay = 500;
	int delay = 1000;
	int brightness = 10;
	private int tileX,tileY;
	private float offX,offY;
	private float speed = 850;
	private int defSpeed = 850;
	//private GFXImage image = new GFXImage("/Character.png");
	boolean aboveThresh = false;
	
	private float fallSpeed = 10;
	private float fallDistance = 0;
	Camera cam;
	private float jump = -8;
	float bdelay = 10;
	boolean doonce = false;
	
	SoundClip shoot_s = new SoundClip("/sound/shoot.wav");
	SoundClip jump_s = new SoundClip("/sound/jump.wav");
	SoundClip hit_s = new SoundClip("/sound/hit_player.wav");
	Particle part;
	float targX;
	@Override
	public void Start(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		gm.CreateObject(new Particle(tileX,tileY,0, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),Color.orange.hashCode(),Color.orange.hashCode(),7,0.45f, false));
	}
	
	
	public Player(int x, int y, int mHealth, int id) {
		this.tag = "player";
		this.tileX = x;
		this.tileY = y;
		this.offX = 0;
		this.offY = 0;
		this.posX = x * GameManager.tileSize;
		this.posY = y * GameManager.tileSize;
		this.width = 32;
		this.height = 32;
		this.maxHealth = mHealth;
		this.health = mHealth;
		this.id = id;
		this.live = true;
		com.sun.security.auth.module.NTSystem NTSystem = new com.sun.security.auth.module.NTSystem();
		this.name = NTSystem.getName();
	}
	
	@Override
	public void Update(GameContainer gc, GameManager gm, float dt) {
		cam = gm.getCamera();
		Color col = Color.orange;
		switch(EngineMath.getRandomIntegerInRange(0, 2))
		{
		case 0:
			col = Color.orange;
			break;
		case 1: 
			col = Color.yellow;
			break;
		case 2: 
			col = Color.red;
			break;
		}
		
		
		//if(EngineMath.getRandomIntegerInRange(0, 4) == 3)
			//gm.CreateObject(new Particle(tileX,tileY,2, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),col.hashCode(),col.hashCode(),14,0.25f, false));
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
		
		
		//L and R
		if(gc.getI().isKey(KeyEvent.VK_D))
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
			if((gm.getCollision(tileX+1, tileY)) && !gm.getCollision(tileX+1, tileY - 1))
			{
				offY -= GameManager.tileSize;
				speed += 30;
				fallDistance = 0;
			}else
				speed = lerp(speed,defSpeed,0.2f);
		}
		
		
		if(gc.getI().isKey(KeyEvent.VK_A))
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
			if((gm.getCollision(tileX-1, tileY)) && !gm.getCollision(tileX-1, tileY - 1))
			{
				offY -= GameManager.tileSize;
				speed += 30;
				fallDistance = 0;
			}else
				speed = lerp(speed,defSpeed,0.2f);
		}
		//end
		
		//Jump
		
		fallDistance += dt*fallSpeed;
		if(gc.getI().isKey(KeyEvent.VK_SPACE) && Ground)
		{
			fallDistance += jump;
			jump_s.play();
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
		
		
		float x = gc.getWIDTH()/2;
		float y = gc.getHEIGHT()/2;
		 
		double angle = Math.atan2(gc.getI().getMouseY()-y, gc.getI().getMouseX()-x);
		angle = 180+(Math.toDegrees(angle));
		angle = (int)(angle / 48);
		//Shooting
		if(gc.getI().isButton(MouseEvent.BUTTON1) && live && bdelay <=0)
		{
			bdelay = 5f;
			gm.CreateObject(new Bullet(tileX,tileY, (int)angle, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),id)); //2
			//shoot_s.setVolume(0.2f);
			shoot_s.play();
		}
		/*if(gc.getI().isButton(MouseEvent.BUTTON3) && live)
		{
			bdelay = 0;
			gm.CreateObject(new Bullet(tileX,tileY, 4, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),id)); //2
			gm.CreateObject(new Bullet(tileX,tileY, 5, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),id)); //1
			gm.CreateObject(new Bullet(tileX,tileY, 6, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),id)); //0
			gm.CreateObject(new Bullet(tileX, tileY, 7, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),id)); //3
		}*/
		particleSource = live;
	}
	@Override
	public void Render(GameContainer gc, Renderer r) {
		if(live)
		{
			//r.DrawText(name, (int)posX - 8, (int)posY + 16, 0xfffffff0);
			r.DrawFilledRect((int)posX, (int)posY, 16, (int)(16 + fallDistance), 0xffff9000,2);
		}
		else {
			r.DrawTextGUI("You Died! " + "(you can only respawn when out of enemy fire!)", 800, 450, 0xfffffff0);
		}
		r.DrawFilledRectGUI(gc.getWIDTH()/2-10 - (maxHealth),  gc.getHEIGHT()/2 + 200, (maxHealth*2)+10, 42, Color.black.hashCode());	
		r.DrawFilledRectGUI(gc.getWIDTH()/2-5 - (maxHealth),   gc.getHEIGHT()/2 + 205, (health*2), 32,       Color.red.hashCode());
	}
	@Override
	public void onDamage(int damageamm,int fromid,GameManager gm)
	{
		this.health -= damageamm;
		this.hit_s.play();
	}
	
	float lerp(float point1, float point2, float d)
	{
	    return point1 + d * (point2 - point1);
	}
}
