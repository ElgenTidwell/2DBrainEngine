package com.sbengine2d.ui;
import com.sbengine2d.engine.GameContainer;
import com.sbengine2d.engine.Renderer;
import com.sbengine2d.game.GameManager;
import com.sbengine2d.game.GameObject;

public class UISlider extends GameObject{

	private UIBox boxbg,boxover;
	
	public UISlider(UIBox boxbg, UIBox boxover) {
		this.boxbg = boxbg;
		this.boxover = boxover;
	}

	@Override
	public void Start(GameContainer gc, GameManager gm) {
		boxbg.Start(gc, gm);
		boxover.Start(gc, gm);
	}

	@Override
	public void Update(GameContainer gc, GameManager gm, float dt) {
		boxbg.Update(gc, gm, dt);
		boxover.Update(gc, gm, dt);
	}

	@Override
	public void Render(GameContainer gc, Renderer r) {
		boxbg.Render(gc, r);
		boxover.Render(gc, r);
	}

}
