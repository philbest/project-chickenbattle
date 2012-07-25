package Screens;

import Spelet.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Skills extends Screen {
	public SpriteBatch batch;
	public Sprite skillScreen;


	Main main;
	public Skills(Main m) {
		main = m;
		batch = new SpriteBatch();
		skillScreen = new Sprite(new Texture(Gdx.files.internal("data/skills/leveltemplate.png")));
	}
	public boolean keyDown(int arg0) {
		if (Input.Keys.I == arg0) {

			main.setScreen(1);	
		} 
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

		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {

		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public void update() {
		((Application)main.screens.get(Main.GAME)).update();

	}

	public void render() {
		((Application)main.screens.get(Main.GAME)).render();
		batch.begin();
		skillScreen.setPosition(0, 0);
		skillScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		skillScreen.draw(batch);
		batch.end();

	}


	public void enter() {
		Gdx.input.setInputProcessor(this);


	}

}
