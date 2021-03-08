package com.sbengine2d.ui;
import com.sbengine2d.engine.GameContainer;
import com.sbengine2d.engine.Renderer;
import com.sbengine2d.game.GameManager;
import com.sbengine2d.game.GameObject;

public class UIBox extends GameObject{
	
	private int color;
	private int x,y,width,height;
	
	public UIBox(int color, int x, int y, int width, int height) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void Start(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Render(GameContainer gc, Renderer r) {
		r.DrawFilledRectGUI(x, y, width, height, color);
	}
	
}
