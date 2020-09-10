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

import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;

import com.sbengine2d.engine.EngineMath;
import com.sbengine2d.engine.GameContainer;
import com.sbengine2d.engine.Renderer;
import com.sbengine2d.engine.gfx.GFXImage;



public class Camera {
	private float offX, offY, z, mcx,mcy;
	public float zoom = 0.2f;
	public PostProcessing post = new PostProcessing(4,5);
	private String targTag;
	private GameObject targ = null;
	float magnitude = 20;
	float sx,sy;
	GFXImage vignette;
	
	
	public Camera(String tag)
	{	
		this.targTag = tag;
		vignette = new GFXImage("/vignette.png");
	}
	public void render(Renderer r)
	{
		r.DrawIMG(vignette, (int)offX, (int)offY,false);
		offX += mcx;
		offY += mcy;
	}
	
	public void update(GameContainer gc,GameManager gm, float dt)
	{
		
		if(targ == null)
		{
			targ = gm.FindObject(targTag);
		}
		if(targ == null)
			return;
		
		offX = lerp(offX,((targ.getPosX() + targ.getWidth() / 2) - ((gc.getWIDTH()/2)/gc.getR().getZoom())),0.05f);
		offY = lerp(offY,((targ.getPosY() + targ.getHeight() / 2) - ((gc.getHEIGHT()/2)/gc.getR().getZoom())),0.05f);
		
		gc.getR().setCamx((int) offX);
		gc.getR().setCamy((int) offY);
		gc.getR().setSkewX(sx);
		gc.getR().setSkewY(sy);
		gc.getR().setCamZ(z);
		
		if(gc.getI().isKey(KeyEvent.VK_LEFT))
		{
			sx += dt/20;
		}
		if(gc.getI().isKey(KeyEvent.VK_RIGHT))
		{
			sx -= dt/20;
		}
		
		if(gc.getI().isKey(KeyEvent.VK_UP))
		{
			sy += dt/20;
		}
		if(gc.getI().isKey(KeyEvent.VK_DOWN))
		{
			sy -= dt/20;
		}
		if(gc.getI().isKey(KeyEvent.VK_E))
		{
			sy = 0;
			sx = 0;
		}

		gc.getR().addZoom(((gc.getI().getScroll()) * dt));
		//z += (gc.getI().getScroll() * dt);
		if(magnitude > 0.1f)
		{
			mcx = EngineMath.getRandomFloatInRange(-magnitude, magnitude);
			mcy = EngineMath.getRandomFloatInRange(-magnitude, magnitude);
			magnitude -= dt * (magnitude);
		}else
		{
			magnitude = 0;
		}
	}
	public float getOffX() {
		return offX;
	}
	public void setOffX(float offX) {
		this.offX = offX;
	}
	public float getOffY() {
		return offY;
	}
	public void setOffY(float offY) {
		this.offY = offY;
	}
	public String getTargTag() {
		return targTag;
	}
	public void setTargTag(String targTag) {
		this.targTag = targTag;
	}
	public GameObject getTarg() {
		return targ;
	}
	public void setTarg(GameObject targ) {
		this.targ = targ;
	}
	float lerp(float point1, float point2, float d)
	{
	    return point1 + d * (point2 - point1);
	}

	public void Shake(int amp)
	{
		magnitude += amp;
	}
}
