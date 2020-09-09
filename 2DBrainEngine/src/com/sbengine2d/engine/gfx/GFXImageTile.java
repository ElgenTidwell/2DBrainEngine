package com.sbengine2d.engine.gfx;

public class GFXImageTile extends GFXImage{
	private int tileW, tileH;
	
	public GFXImageTile(String pa, int tilew, int tileh) {
		super(pa);
		this.tileH = tileh;
		this.tileW = tilew;
		
	}

	public int getTileW() {
		return tileW;
	}

	public void setTileW(int tileW) {
		this.tileW = tileW;
	}

	public int getTileH() {
		return tileH;
	}

	public void setTileH(int tileH) {
		this.tileH = tileH;
	}
}
