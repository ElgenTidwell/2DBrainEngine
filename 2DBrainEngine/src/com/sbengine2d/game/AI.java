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

import com.sbengine2d.engine.EngineMath;
import com.sbengine2d.engine.GameContainer;
import com.sbengine2d.engine.Renderer;

public class AI extends GameObject{
	public int xdest,ydest;
	public int xpath,ypath;
	boolean switched;
	@Override
	public void Start(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		
	}
	public void FindRandomDestination(GameManager gm,float posX, float posY) {
		xdest = ((int)EngineMath.getRandomFloatInRange(posX - 3200, posX + 3200) / 16)*16;
		ydest = ((int)EngineMath.getRandomFloatInRange(posY - 3200, posY + 3200) / 16)*16;
		
		
		CalculatePath(gm,(int)posX/16,(int)posY/16);
	}
	
	//EVERY MAN FOR HIMSELF!!!!
	public void Attack(GameManager gm,float posX, float posY, float fromx, float fromy)
	{
		xdest = (int)fromx;
		ydest = (int)fromy;
		
		CalculatePath(gm,(int)posX/16,(int)posY/16);
	}
	
	
	public void CalculatePath(GameManager gm,int tx,int ty)
	{
		int mDir = EnemyAI.sin(xdest - xpath*16);
		
		if(mDir == 1)
		{
			for(int x = tx; x < xdest/16; x++)
			{
				for(int y = ty; y < ydest/16; y++)
				{
					if(gm.getCollision((x*GameManager.tileSize)+1,(y*GameManager.tileSize)) && !gm.getCollision((x*GameManager.tileSize)+1, (y*GameManager.tileSize) - 1))
					{
							y -= GameManager.tileSize;
					}
					if(gm.getCollision(x, y))
					{
						xpath = x;
						ypath = y;
						return;
					}else
					{
						xpath = x;
						ypath = y;
					}
						
					if(mDir != 1)
					{
						return;
					}
				}
			}
		}
		else
		{
			for(int x = tx; x > xdest/16; x--)
			{
				for(int y = ty; y > ydest/16; y--)
				{
					if(gm.getCollision((x*GameManager.tileSize)-1,(y*GameManager.tileSize)) && !gm.getCollision((x*GameManager.tileSize)-1, (y*GameManager.tileSize) - 1))
					{
							y -= GameManager.tileSize;
					}
					if(gm.getCollision(x, y))
					{
						xpath = x;
						ypath = y;
						return;
					}else
					{
						xpath = x;
						ypath = y;
					}
					
					if(mDir == 1)
					{
						CalculatePath(gm,tx,ty);
						return;
					}
				}
			}
		}
	}


	@Override
	public void Update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void Render(GameContainer gc, Renderer r) {
		r.DrawFilledRect(xdest, ydest, 30, 30, Color.cyan.hashCode(), false);
		r.DrawFilledRect(xpath*16, ypath*16, 15, 15, Color.red.hashCode(), false);
	}
}
