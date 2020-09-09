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

public class ColorParser {

	/**
	 * A class to get the Color value from a string color name
	 */
	 private Color color;

	 public ColorParser(){
	    color = Color.WHITE;
	 }
	/**
	 * Get the color from a string name
	 * 
	 * @param col name of the color
	 * @return White if no color is given, otherwise the Color object
	 */
	public Color getColor(String col) {
	    switch (col.toLowerCase()) {
	    case "black":
	        color = Color.BLACK;
	        break;
	    case "blue":
	        color = Color.BLUE;
	        break;
	    case "cyan":
	        color = Color.CYAN;
	        break;
	    case "darkgray":
	        color = Color.DARK_GRAY;
	        break;
	    case "gray":
	        color = Color.GRAY;
	        break;
	    case "green":
	        color = Color.GREEN;
	        break;

	    case "yellow":
	        color = Color.YELLOW;
	        break;
	    case "lightgray":
	        color = Color.LIGHT_GRAY;
	        break;
	    case "magneta":
	        color = Color.MAGENTA;
	        break;
	    case "orange":
	        color = Color.ORANGE;
	        break;
	    case "pink":
	        color = Color.PINK;
	        break;
	    case "red":
	        color = Color.RED;
	        break;
	    case "white":
	        color = Color.WHITE;
	        break;
	        }
	    return color;
	    }
}
