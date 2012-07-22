package Screens;

import Spelet.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenu extends Screen {
	public SpriteBatch batch;
	Sprite background;
	Sprite crosshair;
	int oldX;
	int oldY;
	int xpos;
	int ypos;
	Sprite exit, sp, mp;
	Main main;
	public MainMenu(Main m) {
		main = m;
		batch = new SpriteBatch();
		background = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/bg.png")));
		crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
		exit = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/exit.png")));
		mp = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/multiplayer.png")));
		sp = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/singleplayer.png")));
	}
	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean mouseMoved(int arg0, int arg1) {
		xpos += (arg0-oldX);
		ypos += (arg1-oldY)*-1;
		oldX = arg0;
		oldY = arg1;
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		if (exit.getBoundingRectangle().contains(xpos,ypos)) {
			System.exit(0);
		}
		if (mp.getBoundingRectangle().contains(xpos,ypos)) {
			((Application)main.screens.get(Main.GAME)).multiplayer = true;
			//main.setScreen(Main.GAME);
			main.setScreen(Main.LOBBY);
		}
		if (sp.getBoundingRectangle().contains(xpos,ypos)) {
			((Application)main.screens.get(Main.GAME)).multiplayer = false;
			main.setScreen(Main.GAME);
		}

		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		mouseMoved(arg0,arg1);
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public void update() {
		Gdx.input.setCursorCatched(true);
		if (exit.getBoundingRectangle().contains(xpos,ypos)) {
			exit.setColor(1f,0f,0f,1);
		} else {
			exit.setColor(0f,0f,0f,1);
		}
		if (mp.getBoundingRectangle().contains(xpos,ypos)) {
			mp.setColor(1f,0f,0f,1);
		} else {
			mp.setColor(0f,0f,0f,1);
		}
		if (sp.getBoundingRectangle().contains(xpos,ypos)) {
			sp.setColor(1f,0f,0f,1);
		} else {
			sp.setColor(0f,0f,0f,1);
		}

	}

	public void render() {
		batch.begin();
		background.draw(batch);
		exit.setPosition(50, 50);
		exit.draw(batch);

		sp.setPosition(50, 400);
		sp.draw(batch);

		mp.setPosition(50, 300);
		mp.draw(batch);

		crosshair.setPosition(xpos-crosshair.getWidth()/2,ypos-crosshair.getHeight()/2);
		crosshair.draw(batch);
		batch.end();

	}


	public void enter() {
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		Gdx.input.setInputProcessor(this);
		oldX = Gdx.graphics.getWidth()/2;
		oldY = Gdx.graphics.getHeight()/2;
		ypos = oldY;
		xpos = oldX;
	}

}
