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

import com.sbengine2d.engine.Renderer;

public class GUIRenderer {
	private List<GUIComponent> guis = new ArrayList<GUIComponent>();

	public GUIRenderer(List<GUIComponent> guis) {
		this.guis = guis;
	}
	
	public void AddGUI(GUIComponent gui)
	{
		guis.add(gui);
	}
	public void RemoveGUI(GUIComponent gui)
	{
		guis.remove(gui);
	}
	
	public void drawGUIs(Renderer r)
	{
		for(GUIComponent gui : guis)
		{
			r.DrawFilledRectGUI(gui.getPosX(), gui.getPosY(), gui.getSizeX(), gui.getSizeY(), gui.getColor());
			
			for(GUIText text : gui.getTexts())
			{
				r.DrawTextGUI(text.text, text.getOffx() + gui.getPosX(),text.getOffy() + gui.getPosY(), Color.white.hashCode());
			}
		}
	}
}
