

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;




public class Main implements ApplicationListener{

	public Application app;
	public Main (){
	
	}
	public void create() {
		Cube.initiate();
		StaticVariables.initiate();
		app = new Application();
		Gdx.graphics.setVSync(true);
	}


	public void setScreen(int id) {
	}

	public void dispose() {
	}

	public void pause() {
	}

	public void render() {
//		System.out.println(Gdx.graphics.getFramesPerSecond());
		GL20 gl = Gdx.graphics.getGL20();
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		app.update();
		app.render();
	}

	public void resize(int arg0, int arg1) {

	}

	public void resume() {

	}
}
