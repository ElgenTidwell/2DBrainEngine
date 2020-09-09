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
package com.sbengine2d.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.sbengine2d.engine.GameContainer;

public class GUIComponent {
	private int sizeX,sizeY,posX,posY;
	
	public int textoffsetx,textoffsety;
	private int color;
	
	private List<GUIText> texts = new ArrayList<GUIText>();
	private GUIButton button;
	public GUIComponent(int sizeX, int sizeY, int posX, int posY, int color) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.posX = posX;
		this.posY = posY;
		this.color = color;
		this.button = new GUIButton(color,Color.GRAY.hashCode());
	}
	
	public void Update(float deltatime,GameContainer gm)
	{
		button.update(gm,this);
		color = button.currentColor;
	}
	
	public List<GUIText> getTexts() {
		return texts;
	}
	public void AddText(GUIText text)
	{
		texts.add(text);
	}
	public int getSizeX() {
		return sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	public int getColor() {
		return color;
	}

	public GUIButton getButton() {
		return button;
	}

	public void setButton(GUIButton button) {
		this.button = button;
	}
}
