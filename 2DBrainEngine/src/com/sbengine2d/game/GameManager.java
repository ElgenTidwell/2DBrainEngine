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


package com.sbengine2d.game;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sbengine2d.engine.AbstractGame;
import com.sbengine2d.engine.ColorParser;
import com.sbengine2d.engine.GameContainer;
import com.sbengine2d.engine.Renderer;
import com.sbengine2d.engine.audio.SoundClip;
import com.sbengine2d.engine.gfx.GFXImage;
import com.sbengine2d.gui.GUIComponent;
import com.sbengine2d.gui.GUIRenderer;
import com.sbengine2d.gui.GUIText;

public class GameManager extends AbstractGame {
	private GFXImage levelImage;
	public static final int tileSize = 16;
	private boolean[] collision;
	private boolean[] tile;
	private int levelW, levelH;
	Color color = Color.black;
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private Camera cam;
	//private SoundClip music = new SoundClip("/sound/Track02.wav");
	String level;
	String colorSTR;
	String ArenaFill;
	String waveSize;
	ColorParser cp = new ColorParser();
	int[] tilecolor;
	float waveDelay = 20;
	
	//GUI stuff
	List<GUIComponent> guicomp = new ArrayList<GUIComponent>();
	GUIRenderer guiRenderer = new GUIRenderer(guicomp);
	boolean loaded = false;
	SoundClip music;
	
	
	void readConfig()
	{
		try {
		String configPath = "res/config/config.txt";
		File cfg = new File(configPath);
		FileReader fr = new FileReader(cfg);
		BufferedReader br = new BufferedReader(fr);
		String line;
		int time = 0;
		while((line = br.readLine()) != null)
		{
			if(time == 0)
			{
				level = line;
			}else if(time == 1)
			{
				ArenaFill = line;
			}else if(time == 2)
			{
				waveSize = line;
			}
			else if(time == 3)
			{
				colorSTR = line;
			}
			time ++;
		}
		br.close();
		}catch(FileNotFoundException e)
		{
			System.out.println("File not Found");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public GameManager() {	
		readConfig();
		music = new SoundClip(colorSTR);
		music.loop();
		//GUI on bootup
		objects.add(new StaticEntity(0,0,"MenuTarget"));
		cam = new Camera("MenuTarget");
		guicomp.add(new GUIComponent(800,100,700,350,Color.DARK_GRAY.hashCode()));
		guicomp.get(0).AddText(new GUIText("Play",400,35));
	}
	
	public void LoadMainGame()
	{
		loaded = true;
		objects.add(new Player(8, 3 , 180,0));
		cam = new Camera("player");
		loadLevel(level);
		//Integer.parseInt(waveSize)
		spawnWavesWithChunkCheck(Integer.parseInt(waveSize));
	}

	@Override
	public void init(GameContainer gc) {
		//music.loop();
		gc.getR().setAmbientColor(0x82B9EA);
		for (GameObject obj : objects) {
			obj.Start(gc, this);
		}
		
	}

	@Override
	// this is the main update loop
	public void update(GameContainer gc, float dt) {
		for (int i = 0; i < objects.size(); i++) {
			GameObject obj = objects.get(i);
			if (objects.get(i).isDead()) {
				objects.remove(i);
				i--;
			}
			obj.Update(gc, this, dt);
		}
		cam.update(gc, this, dt);
		System.out.println(gc.getR().getPW());
		updateUI(dt,gc);
	}
	
	void updateUI(float dt, GameContainer gc)
	{
		if(!loaded)
		{
			for(int i = 0; i < guicomp.size(); i++)
			{
				guicomp.get(i).Update(dt, gc);
			}
		}
		if(!loaded && guicomp.get(0).getButton().isClicked())
		{
			LoadMainGame();
			for(int i = 0; i < guicomp.size(); i++)
			{
				guicomp.remove(i);
			}
			System.out.println("Load!");
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		for (int y = 0; y < levelH; y++) {
			for (int x = 0; x < levelW; x++) {
				if (tile[x + y * levelW]) {
					if(ArenaFill.equalsIgnoreCase("fill"))
					{
						r.DrawFilledRect(x * tileSize, y * tileSize, tileSize, tileSize, tilecolor[x + y * levelW],-1);
					}
					else
					if(ArenaFill.equalsIgnoreCase("wire"))
					{
						r.DrawRect(x * tileSize, y * tileSize, tileSize, tileSize, tilecolor[x + y * levelW]);
					}
				}
			}
		}
		
		for (GameObject obj : objects) {
			obj.Render(gc, r);
		}
		cam.render(r);
		guiRenderer.drawGUIs(r);
	}
	public void CreateObject(GameObject obj)
	{
		objects.add(obj);
	}
	public void loadLevel(String path) {
		levelImage = new GFXImage(path);
		levelW = levelImage.getW();
		levelH = levelImage.getH();
		collision = new boolean[levelW * levelH];
		tilecolor = new int[levelW * levelH];
		tile = new boolean[levelW * levelH];
		for (int y = 0; y < levelImage.getH(); y++) {
			for (int x = 0; x < levelImage.getW(); x++) {
				int alpha = ((levelImage.getPix()[x + y * levelImage.getW()] >> 24) & 255);
				if (alpha != 0) {
					tilecolor[x+y*levelImage.getW()] = levelImage.getPix()[x + y * levelImage.getW()];
					tile[x+y*levelImage.getW()] = true;
					if(alpha == 255) {
						collision[x + y * levelImage.getW()] = true;	
					} else {
						collision[x + y * levelImage.getW()] = false;
					}
				}else
				{
					tile[x+y*levelImage.getW()] = false;
				}
			}
		}
	}

	public boolean getCollision(int x, int y) {
		if (x < 0 || x >= levelW || y < 0 || y >= levelH)
			return true;
		else
			return collision[x + y * levelW];
	}

	public static void main(String args[]) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.start();
	}
	public GameObject FindObject(String tag)
	{
		for(int i = 0; i < objects.size(); i ++)
		{
			if(objects.get(i).getTag().equals(tag))
			{
				return objects.get(i);
			}
		}
		return null;
	}
	
	void spawnEnemyWave(int size)
	{
		int x = size;
		while(x > 0)
		{
			x--;
			objects.add(new EnemyAI(12, 3, 180,9999999 - x));
		}
	}
	public GameObject FindOBJFromID(int id)
	{
		for (int i = 0; i < objects.size() - 1; i++) {
			if (objects.get(i).tag == "enemy") {
				GameObject eai = objects.get(i);
				if(eai.id == id)
				{
					return eai;
				}
			}
		}
		return null;
	}
	public GameObject getObjectFromCoordinates(float minx, float miny)
	{
		for(int i = 0; i < objects.size(); i++)
		{
			if((int)objects.get(i).getPosX() / tileSize == minx && (int)objects.get(i).getPosY() / tileSize == miny)
			{
				return objects.get(i);
			}
		}
		
		return null;
	}
	
	void spawnWavesWithChunkCheck(int size) {
		if(size > 64)
		{
			int ammountOfChunks = size / 32;
			int chunk = ammountOfChunks;
			while(chunk > 0)
			{
				spawnEnemyWave(32);
				chunk --;
			}
		}else
			spawnEnemyWave(size);
	}
	public Camera getCamera()
	{
		return cam;
	}
	public int getLevelWidth()
	{
		return levelW;
	}
	public int getLevelHeight()
	{
		return levelH;
	}
}
