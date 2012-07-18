package Screens;

import com.badlogic.gdx.InputProcessor;

public abstract class Screen implements InputProcessor{

	public abstract void update();
	
	public abstract void render();
	
	public abstract void enter();
	
	public void leave(){
	}
}
