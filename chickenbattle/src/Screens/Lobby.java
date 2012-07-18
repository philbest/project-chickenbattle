package Screens;

import Spelet.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Lobby extends Screen{

	public Sprite mainbg;
	public Sprite background;
	public SpriteBatch sb;
	public Main main;
	public Lobby(Main m) {
		main = m;
		sb = new SpriteBatch();
		mainbg = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/bg.png")));
		background = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/lobbybg.png")));
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

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		sb.begin();
		mainbg.draw(sb);
		background.setPosition(300, 100);
		background.draw(sb,0.95f);
		sb.end();
	}
	public void refresh() {
		
	}

	@Override
	public void enter() {
		// TODO Auto-generated method stub
		
	}

}
