package com.sbengine2d.engine.gfx;

public class Font {
	public static final Font STANDARD = new Font("/fonts/fontcms.png");
	
	private GFXImage fimage;
	private int[] offs;
	private int[] widths;
	
	public Font(String path) {
		fimage = new GFXImage(path);
		offs = new int[256];
		widths = new int [256];
		
		int unicode = 0;
		
		for(int i = 0; i < fimage.getW(); i++) {
			if(fimage.getPix()[i] == 0xff0000ff) {
				offs[unicode] = i;
			}
			
			if(fimage.getPix()[i] == 0xffffff00) {
				widths[unicode] = i - offs[unicode];
				unicode++;
			}
		}
	}

	public GFXImage getFimage() {
		return fimage;
	}

	public void setFimage(GFXImage fimage) {
		this.fimage = fimage;
	}

	public int[] getOffs() {
		return offs;
	}

	public void setOffs(int[] offs) {
		this.offs = offs;
	}

	public int[] getWidths() {
		return widths;
	}

	public void setWidths(int[] widths) {
		this.widths = widths;
	}
}
