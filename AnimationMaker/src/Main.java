import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;


public class Main implements ApplicationListener{
	Application app;
	
	public Main() {
		app = new Application();
	}
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		GL20 gl = Gdx.graphics.getGL20();
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		app.update();
		app.render();
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
