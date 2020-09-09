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

import java.awt.event.MouseEvent;

import com.sbengine2d.engine.GameContainer;

public class GUIButton {
	private int unselectedColor;
	private int selectedColor;
	private boolean clicked;
	private boolean selected;
	public int currentColor;
	public GUIButton(int unselectedColor, int selectedColor) {
		this.unselectedColor = unselectedColor;
		this.selectedColor = selectedColor;
	}
	
	public int getUnselectedColor() {
		return unselectedColor;
	}
	public int getSelectedColor() {
		return selectedColor;
	}
	public boolean isClicked() {
		return clicked;
	}
	
	public void update(GameContainer gc,GUIComponent comp)
	{
		int x = gc.getI().getMouseX();
		int y = gc.getI().getMouseY();
		
		if(x > comp.getPosX() && x < comp.getSizeX() + comp.getPosX())
		{
			if(y > comp.getPosY() && y < comp.getSizeY() + comp.getPosY())
			{
				selected = true;
			}else
				selected = false;
		}else
			selected = false;
			
		if(selected)
		{
			currentColor = selectedColor;
			clicked = gc.getI().isButton(MouseEvent.BUTTON1);
		}else
		{
			currentColor = unselectedColor;
			clicked = false;
		}
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
