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

import java.awt.Color;
import java.awt.image.DataBufferInt;

import com.sbengine2d.engine.gfx.Font;
import com.sbengine2d.engine.gfx.GFXImage;
import com.sbengine2d.engine.gfx.GFXImageTile;
import com.sbengine2d.game.EnemyAI;

public class Renderer 
{
	public int AmbientColor = 0x82B9EA;
	private int pW, pH;
	private int[] p;
	private int[] l;
	private int[] zb;
	private int[] lm;
	private int[] lb;
	private int zdepth = 0;

	public int getPW()
	{
		return pW;
	}
	
	private int camx,camy;
	float skewX;
	float skewY;
	float zoom = 1f;
	
	public float getSkewX() {
		return skewX;
	}

	public void setSkewX(float skewX) {
		this.skewX = skewX;
	}

	public float getSkewY() {
		return skewY;
	}

	public void setSkewY(float sy) {
		this.skewY = sy;
	}

	public int getCamx() {
		return camx;
	}

	public void setCamx(int camx) {
		this.camx = camx;
	}

	public int getCamy() {
		return camy;
	}
	public void setCamy(int camy) {
		this.camy = camy;
	}

	public int getZdepth() {
		return zdepth;
	}

	public void setZdepth(int zdepth) {
		this.zdepth = zdepth;
	}
	public void addZoom(float z)
	{
		zoom -= z;
		System.out.println(zoom);
	}
	private Font font = Font.STANDARD;
	
	public Renderer(GameContainer gc) 
	{
		pW = gc.getWIDTH();
		pH = gc.getHEIGHT();
		p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
		zb = new int[p.length];
		lm = new int[p.length];
		lb = new int[p.length];
		l = new int[p.length];
	}
	
	public void setLightMap(int x, int y, int val)
	{
		if(x < 0 || x>= pW || y < 0|| y >= pH)
		{
			return;
		}
		
		
		int baseCol = lm[x+y*pW];
		
		int maxRed = Math.max(((baseCol >> 16) & 0xff), (val >> 16) & 0xff);
		int maxGreen = Math.max(((baseCol >> 8) & 0xff), (val >> 8) & 0xff);
		int maxBlue = Math.max(baseCol & 0xff, val & 0xff);
		
		
		
		lm[x+y*pW] = (maxRed << 16 | maxGreen << 8 | maxBlue);
	}
	public void process()
	{
		for(int i = 0; i < p.length; i ++)
		{
			float red = ((lm[i] >> 16) & 0xff)/255f;
			float green = ((lm[i] >> 8) & 0xff)/255f;
			float blue = (lm[i] & 0xff)/255f;
			
			p[i] = ((int)(((p[i] >> 16) & 0xff) * red) << 16 | (int)(((p[i] >> 8) & 0xff) * green) << 8 | (int)((p[i] & 0xff) * blue));
		}
	}
	
	
	public void clear() {
		for(int i = 0; i < p.length; i ++) {
			p[i] = AmbientColor;
			l[i] = -10;
			zb[i] = 0;
		}
	}
	
	public void setPixelAndLayer(int x, int y, int value, int Layer) {
		setPixelAndLayer(x,y,value,255,1, 1,0, 0,Layer);
	}
	public void setPixel(int x, int y, int value) {
		setPixel(x,y,value,255,1, 1,0, 0);
	}
	public void setPixel(int x, int y, int value,int alphamod) {
		setPixel(x,y,value,alphamod,1, 1,0, 0);
	}
	public void setPixel(int x, int y, int value,int alphamod,float xScale, float yScale,float xSkew, float ySkew) {
		int alpha = ((value >> 24) & alphamod);
		int newX,newY;
		
		newX = (int)((xScale*x + ySkew*y));
		newY = (int)((yScale*y + xSkew*x));
		
		
		if((newX < 0 || newX>= pW || newY < 0|| newY >= pH) || alpha == 0){
			return;
		}
		if(zb[newX + newY * pW] > zdepth) {
			return;
		}
		if(alpha == 255)
		{
			p[newX + newY * pW] = value;
		}
		else {
			int pcolor = p[newX+newY*pW];
			int newR = ((pcolor >> 16) & 0xff) - (int)((((pcolor >> 16) & 0xff) - ((value >> 16) & 0xff)) * alpha / 255);
			int newG = ((pcolor >> 8) & 0xff) - (int)((((pcolor >> 8) & 0xff) - ((value >> 8) & 0xff)) * alpha / 255);
			int newB = (pcolor & 0xff) - (int)(((pcolor & 0xff) - (value & 0xff)) * alpha / 255);
			
			p[newX + newY * pW] = (newR << 16 | newG << 8 | newB);
	
		}
	}
	public void setPixelAndLayer(int x, int y, int value,int alphamod,float xScale, float yScale,float xSkew, float ySkew, int Layer) {
		int alpha = ((value >> 24) & alphamod);
		int newX,newY;
		
		newX = (int)((xScale*x + ySkew*y));
		newY = (int)((yScale*y + xSkew*x));
		
		
		if((newX < 0 || newX>= pW || newY < 0|| newY >= pH) || alpha == 0){
			return;
		}
		if(zb[newX + newY * pW] > zdepth) {
			return;
		}
		if(l[newX + newY * pW] > Layer)
		{
			return;
		}
		if(alpha == 255)
		{
			p[newX + newY * pW] = value;
			l[newX + newY * pW] = Layer;
		}
		else {
			int pcolor = p[newX+newY*pW];
			int newR = ((pcolor >> 16) & 0xff) - (int)((((pcolor >> 16) & 0xff) - ((value >> 16) & 0xff)) * alpha / 255);
			int newG = ((pcolor >> 8) & 0xff) - (int)((((pcolor >> 8) & 0xff) - ((value >> 8) & 0xff)) * alpha / 255);
			int newB = (pcolor & 0xff) - (int)(((pcolor & 0xff) - (value & 0xff)) * alpha / 255);
			
			p[newX + newY * pW] = (newR << 16 | newG << 8 | newB);
			l[newX + newY * pW] = Layer;
		}
	}
	float camZ;
	public void setCamZ(float z)
	{
		camZ = z;
	}
	public void setPixel(int x, int y,int z, int value,int alphamod,float xScale, float yScale,float xSkew, float ySkew) {
		int alpha = ((value >> 24) & alphamod);
		int newX,newY;
		
		newX = (int)((xScale*x + ySkew*y));
		newY = (int)((yScale*y + xSkew*x));
		
		
		if((newX < 0 || newX>= pW || newY < 0|| newY >= pH) || alpha <= 0){
			return;
		}
		if(zb[newX + newY * pW] > zdepth) {
			return;
		}
		if(alpha == 255)
		{
			p[newX + newY * pW] = value+z;
		}
		else {
			int pcolor = p[newX+newY*pW];
			int newR = ((pcolor >> 16) & 0xff) - (int)((((pcolor >> 16) & 0xff) - ((value >> 16) & 0xff)) * alpha / 255);
			int newG = ((pcolor >> 8) & 0xff) - (int)((((pcolor >> 8) & 0xff) - ((value >> 8) & 0xff)) * alpha / 255);
			int newB = (pcolor & 0xff) - (int)(((pcolor & 0xff) - (value & 0xff)) * alpha / 255);
			
			p[newX + newY * pW] = (newR << 16 | newG << 8 | newB)+z;
		}
	}
	
	public void DrawText(String text, int ox, int oy, int color) {
		ox -= camx;
		oy -= camy;
		ox *= zoom;
		oy *= zoom;
		int offs = 0;
		
		for(int i = 0; i < text.length(); i ++) {
			int unicode = text.codePointAt(i);
			
			
			
			for(int y = 0; y < font.getFimage().getH(); y++) {
				for(int x = 0; x < font.getWidths()[unicode]; x++) {
					if(font.getFimage().getPix()[(x + font.getOffs()[unicode]) + y * font.getFimage().getW()] == 0xffffffff) {
						setPixel(x + ox + offs,y + oy,color);
					}
				}
			}
			
			offs += font.getWidths()[unicode];
		}
	}
	public void DrawTextWithNewFont(String text, int ox, int oy, int color, Font _font) {
		ox -= camx;
		oy -= camy;
		ox *= zoom;
		oy *= zoom;
		int offs = 0;
		
		for(int i = 0; i < text.length(); i ++) {
			int unicode = text.codePointAt(i);
			
			
			
			for(int y = 0; y < _font.getFimage().getH(); y++) {
				for(int x = 0; x < _font.getWidths()[unicode]; x++) {
					if(_font.getFimage().getPix()[(x + _font.getOffs()[unicode]) + y * _font.getFimage().getW()] == 0xffffffff) {
						setPixel(x + ox + offs,y + oy,color);
					}
				}
			}
			
			offs += font.getWidths()[unicode];
		}
	}
	public void DrawTextGUI(String text, int ox, int oy, int color) {
		int offs = 0;
		for(int i = 0; i < text.length(); i ++) {
			int unicode = text.codePointAt(i);
			
			
			
			for(int y = 0; y < font.getFimage().getH(); y++) {
				for(int x = 0; x < font.getWidths()[unicode]; x++) {
					if(font.getFimage().getPix()[(x + font.getOffs()[unicode]) + y * font.getFimage().getW()] == 0xffffffff) {
						setPixel(x + ox + offs,y + oy,color);
					}
				}
			}
			
			offs += font.getWidths()[unicode];
		}
	}
	
	
	public void DrawIMG(GFXImage image, int offx, int offy,boolean scalewz) {
		offx -= camx;
		offy -= camy;
		if(scalewz)
		{
			offx *= zoom;
			offy *= zoom;
			int w = (int)(image.getW()*zoom+0.1);
			int h = (int)(image.getH()*zoom+0.1);
			image.setW(w);
			image.setH(h);
		}
		//Image Render Optimizing
		if(offx < -image.getW()) return;
		if(offy < -image.getH()) return;
		if(offx >= pW) return;
		if(offy >= pH) return;
		
		int newX = 0;
		int newY = 0;
		int newW = image.getW();
		int newH = image.getH();
		
		
		//image clipping code
		if(offx < 0) {newX -= offx;}
		if(offy < 0) {newY -= offy;}	
		if(newW + offx > pW) {newW -= (newW + offx - pW);}
		if(newH + offy > pH) {newH -= (newH + offy - pH);}
		
		for(int y = newY; y < newH; y++) {
			for(int x = newX; x < newW; x++) {
				setPixel(x + offx,y + offy, image.getPix()[x + y * image.getW()]);
			}
		}		
	}
	public void DrawIMG(GFXImage image, int offx, int offy,boolean scalewz,int hi, int wi) {
		offx -= camx;
		offy -= camy;
		if(scalewz)
		{
			offx *= zoom;
			offy *= zoom;
			int w = (int)(image.getW()*zoom+0.1);
			int h = (int)(image.getH()*zoom+0.1);
			image.setW(w);
			image.setH(h);
		}
		//Image Render Optimizing
		if(offx < -image.getW()) return;
		if(offy < -image.getH()) return;
		if(offx >= pW) return;
		if(offy >= pH) return;
		
		int newX = 0;
		int newY = 0;
		int newW = image.getW()*wi;
		int newH = image.getH()*hi;
		
		
		//image clipping code
		if(offx < 0) {newX -= offx;}
		if(offy < 0) {newY -= offy;}	
		if(newW + offx > pW) {newW -= (newW + offx - pW);}
		if(newH + offy > pH) {newH -= (newH + offy - pH);}
		
		for(int y = newY; y < newH; y++) {
			for(int x = newX; x < newW; x++) {
				setPixel(x + offx,y + offy, image.getPix()[x + y * image.getW()]);
			}
		}		
	}
	
	public void DrawIMGTile(GFXImageTile image,int offx,int offy,int tilex,int tiley) {
		offx -= camx;
		offy -= camy;
		offx *= zoom;
		offy *= zoom;
		int w = (int)(image.getW()*zoom+0.1);
		int h = (int)(image.getH()*zoom+0.1);
		image.setW(w);
		image.setH(h);
		//Image Render Optimizing
		if(offx < -image.getTileW()) return;
		if(offy < -image.getTileH()) return;
		if(offx >= pW) return;
		if(offy >= pH) return;
		
		int newX = 0;
		int newY = 0;
		int newW = image.getTileW();
		int newH = image.getTileH();
		
		
		//image clipping code
		if(offx < 0) {newX -= offx;}
		if(offy < 0) {newY -= offy;}	
		if(newW + offx > pW) {newW -= (newW + offx - pW);}
		if(newH + offy > pH) {newH -= (newH + offy - pH);}
		
		for(int y = newY; y < newH; y++) {
			for(int x = newX; x < newW; x++) {
				setPixel(x + offx,y + offy, image.getPix()[(x + tilex * image.getTileW()) + (y + tiley * image.getTileH()) * image.getW()]);
			}
		}	
	}
	public float getZoom()
	{
		return zoom;
	}
	public void DrawRect(int ox,int oy,int w,int h, int color) {
		ox -= camx;
		oy -= camy;
		for(int y = 0; y <= h; y++) {
			setPixel(ox,y+oy,color,255,zoom,zoom,skewX,skewY);
			setPixel(ox+w,y+oy,color,255,zoom,zoom,skewX,skewY);
		}
		for(int x = 0; x <= w; x++) {
			setPixel(x+ox,oy,color,255,zoom,zoom,skewX,skewY);
			setPixel(x+ox,oy+h,color,255,zoom,zoom,skewX,skewY);
		}
	}
	public void DrawFilledRect(int offx,int offy,int w,int h, int color, boolean bloom) {
		offx -= camx;
		offy -= camy;
		offx *= zoom;
		offy *= zoom;
		w*=zoom+0.1;
		h*=zoom+0.1;

		
		if(offx < -w - skewX) return;
		if(offy < -h - skewY) return;
		if(offx >= pW + skewX) return;
		if(offy >= pH + skewY) return;
		
		int newX = 0;
		int newY = 0;
		int newW = w;
		int newH = h;

		int off = 2;
		for(int y = newY; y < newH; y++) {
			for(int x = newX; x < newW; x++) {
				if(!((offx < -x) || (offy < -y) || (x+offx >= pW) || (y+offy >= pH)))
					setPixelAndLayer(x+offx,y+offy,color,255,1,1,skewX,skewY,2);
			}
		}
	}
	
	public boolean WasRendered(int offx,int offy,int w,int h)
	{
		offx -= camx;
		offy -= camy;
		offx *= zoom;
		offy *= zoom;
		w*=zoom+0.1;
		h*=zoom+0.1;

		
		if(offx < -w - skewX) return false;
		if(offy < -h - skewY) return false;
		if(offx >= pW + skewX) return false;
		if(offy >= pH + skewY) return false;
		
		
		return true;
	}
	
	public void DrawFilledRect(int offx,int offy,int w,int h, int color, int layer) {
		offx -= camx;
		offy -= camy;
		offx *= zoom;
		offy *= zoom;
		w*=zoom+0.1;
		h*=zoom+0.1;

		
		if(offx < -w - skewX) return;
		if(offy < -h - skewY) return;
		if(offx >= pW + skewX) return;
		if(offy >= pH + skewY) return;
		
		int newX = 0;
		int newY = 0;
		int newW = w;
		int newH = h;

		int off = 2;
		for(int y = newY; y < newH; y++) {
			for(int x = newX; x < newW; x++) {
				setPixelAndLayer(x+offx,y+offy,color,layer);
			}
		}
	}
	public void DrawFilledRectGUI(int offx,int offy,int w,int h, int color) {	
		if(offx < -w - skewX) return;
		if(offy < -h - skewY) return;
		if(offx >= pW + skewX) return;
		if(offy >= pH + skewY) return;
		
		int newX = 0;
		int newY = 0;
		int newW = w;
		int newH = h;

		int off = 2;
		for(int y = newY; y < newH; y++) {
			for(int x = newX; x < newW; x++) {
				if(!((offx < -x) || (offy < -y) || (x+offx >= pW) || (y+offy >= pH)))
					setPixelAndLayer(x+offx,y+offy,color,9999);
			}
		}
	}
	public void DrawLine(int ax,int ay,int bx,int by, int color) {
		ax -= camx;
		ay -= camy;
		bx -= camx;
		by -= camy;
		ax *= zoom;
		ay *= zoom;
		bx *= zoom;
		by *= zoom;
		/*
		if(ax < -bx - skewX) return;
		if(ay < -by - skewY) return;
		if(ax >= pW + skewX) return;
		if(ay >= pH + skewY) return;
		
		if(bx < -ax - skewX) return;
		if(by < -ay - skewY) return;
		if(bx >= pW + skewX) return;
		if(by >= pH + skewY) return;
		*/
		
		double DT = Math.sqrt(Math.pow((bx-ax), 2)+Math.pow((by-ay), 2));
		double x;
		double y;
		for(int i = 0; i < DT; i++)
		{
			double D = i;
			double T = D/DT;
			
			x = (1-T)*ax+T*bx;
			y = (1-T)*ay+T*by;
			setPixel((int)x,(int)y,0,color,255,zoom,zoom,skewX,skewY);
		}
	}
	public void DrawLine(int ax,int ay,int bx,int by, int color,int thickness) {
		ax -= camx;
		ay -= camy;
		bx -= camx;
		by -= camy;
		ax *= zoom;
		ay *= zoom;
		bx *= zoom;
		by *= zoom;
		/*
		if(ax < -bx - skewX) return;
		if(ay < -by - skewY) return;
		if(ax >= pW + skewX) return;
		if(ay >= pH + skewY) return;
		
		if(bx < -ax - skewX) return;
		if(by < -ay - skewY) return;
		if(bx >= pW + skewX) return;
		if(by >= pH + skewY) return;
		*/
		
		double DT = Math.sqrt(Math.pow((bx-ax), 2)+Math.pow((by-ay), 2));
		double x;
		double y;
		for(int D = 0; D < DT; D++)
		{
				double T = D/DT;
				
				x = (1-T)*ax+T*bx;
				y = (1-T)*ay+T*by;
				for(int i = -thickness; i < thickness; i++)
					setPixel((int)(x+i),(int)(y+i),0,color,255,zoom,zoom,skewX,skewY);
		}
	}
	public int getAmbientColor() {
		return AmbientColor;
	}

	public void setAmbientColor(int ambientColor) {
		AmbientColor = ambientColor;
	}
}
