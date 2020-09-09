package com.sbengine2d.engine.gfx;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GFXImage {
	private int w;
	private int h;
	private int[] pix;
	Image base = null;
	
	public GFXImage(String pa) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(GFXImage.class.getResourceAsStream(pa));
			base = ImageIO.read(GFXImage.class.getResourceAsStream(pa));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		w = image.getWidth();
		h = image.getHeight();
		pix = image.getRGB(0,0,w,h,null,0,w);
		
		image.flush();
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int[] getPix() {
		return pix;
	}

	public void setPix(int[] pix) {
		this.pix = pix;
	}
	public Image getImage()
	{
		return base;
	}
}
