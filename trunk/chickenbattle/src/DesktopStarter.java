

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopStarter {
	public static void main(String[] args) {
		new LwjglApplication(new Main(), "Game",1000,700,true);
	}
}