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

import com.sbengine2d.engine.GameContainer;
import com.sbengine2d.engine.Renderer;

public class ParticleEmmiter extends GameObject{
	
	boolean doonce = true;
	int tileX, tileY;
	float offX, offY;
	int col;
	
	public ParticleEmmiter(int color, int tilex, int tiley, float ox, float oy, GameManager gm)
	{
		this.tileX = tilex;
		this.tileY = tiley;
		this.offX = ox;
		this.offY = oy;
		this.doonce = true;
		this.col = color;
		emit(gm);
	}
	float delay = 10;
	@Override
	public void Update(GameContainer gc, GameManager gm, float dt) {
	}

	@Override
	public void Render(GameContainer gc, Renderer r) {
		
	}
	
	void emit(GameManager gm)
	{
		gm.CreateObject(new Particle(tileX,tileY,2, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),col,col,7,0.45f, false));
		gm.CreateObject(new Particle(tileX,tileY,1, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),col,col,7,0.45f, false));
		gm.CreateObject(new Particle(tileX,tileY,0, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),col,col,7,0.45f, false));
		gm.CreateObject(new Particle(tileX,tileY,3, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),col,col,7,0.45f, false));
		gm.CreateObject(new Particle(tileX,tileY,4, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),col,col,7,0.45f, false));
		gm.CreateObject(new Particle(tileX,tileY,5, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),col,col,7,0.45f, false));
		gm.CreateObject(new Particle(tileX,tileY,6, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),col,col,7,0.45f, false));
		gm.CreateObject(new Particle(tileX,tileY,7, offX + (GameManager.tileSize / 2), offY + (GameManager.tileSize / 2),col,col,7,0.45f, false));
	}

	@Override
	public void Start(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		
	}

}
