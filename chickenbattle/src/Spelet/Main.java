package Spelet;
import java.util.HashMap;

import network.GameClient;

import Screens.Application;
import Screens.Lobby;
import Screens.MainMenu;
import Screens.Screen;
import Screens.Skills;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;


public class Main implements ApplicationListener{
	public Screen activeScreen;
	public HashMap<Integer, Screen> screens;
	public static final int MAINMENU = 0;
	public static final int GAME = 1;
	public static final int LOBBY = 2;
	public static final int SKILLS = 3;
	public String name;
	public Application app;
	public GameClient client;
	public Main (){
	
	}
	public void create() {
		/*FYI, regarding the full screen windowed mode,  you can change System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");  then call Gdx.graphics.setDisplayMode  and it will update.
			18<Brice> So it's very easy to implement an option for full screen windowed mode, without having to restart
		 * 
		 */
		SoundManager.initiate();
		VertexAttributes.initiate();
		StaticAnimations.initiate();
		StaticVariables.initiate();
		screens = new HashMap<Integer, Screen>();
		screens.put(MAINMENU, new MainMenu(this));
		screens.put(LOBBY, new Lobby(this));
		screens.put(GAME, new Application(this));
		screens.put(SKILLS, new Skills(this));
		activeScreen = screens.get(MAINMENU);
		activeScreen.enter();
		Gdx.graphics.setVSync(true);
	}


	public void setScreen(int id) {
		activeScreen.leave();
		activeScreen = screens.get(id);
		activeScreen.enter();
	}

	public void dispose() {
	}

	public void pause() {
	}

	public void render() {
//		System.out.println(Gdx.graphics.getFramesPerSecond());
		GL20 gl = Gdx.graphics.getGL20();
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		activeScreen.update();
		activeScreen.render();
	}

	public void resize(int arg0, int arg1) {

	}

	public void resume() {

	}
}
