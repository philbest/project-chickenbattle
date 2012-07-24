import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class Application implements InputProcessor{
	PerspectiveCamera cam;
	public float camRadius;
	public int camOrientation;
	public Animation animation;
	public ShaderProgram shader;
	public Matrix4 modelViewProjectionMatrix;	
	public Application() {
		// Insert starting details:
		modelViewProjectionMatrix = new Matrix4();
		cam = new PerspectiveCamera(); 
		camRadius = 5;
		animation = new Animation();
		shader = new ShaderProgram(Gdx.files.internal("data/simple.vert").readString(),
				Gdx.files.internal("data/simple.frag").readString());
		if (!shader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile simple shader: "
					+ shader.getLog());
	}
	public void changeRadius(float dr) {
		camRadius += dr;

	}
	public void update(){

	}
	public void render() {
		shader.begin();
		animation.render(this);
		shader.end();
	}

	public void clickedLeft() {
		camOrientation = camOrientation-1;
		if (camOrientation==-1)
			camOrientation = 3;
		// 0 = Front, 1 == Right, 2 == Back, 3 = Left
		if (camOrientation == 0) {
			cam.position.set(0,0,camRadius);
		} else if (camOrientation == 1) {
			cam.position.set(camRadius,0,0);
		} else if (camOrientation == 2) {
			cam.position.set(0,0,-camRadius);
		} else {
			cam.position.set(-camRadius,0,0);
		}
		cam.lookAt(0,0,0);

	}
	public void clickedRight() {
		camOrientation = (camOrientation+1) % 4;
		// 0 = Front, 1 == Right, 2 == Back, 3 = Left
		if (camOrientation == 0) {
			cam.position.set(0,0,camRadius);
		} else if (camOrientation == 1) {
			cam.position.set(camRadius,0,0);
		} else if (camOrientation == 2) {
			cam.position.set(0,0,-camRadius);
		} else {
			cam.position.set(-camRadius,0,0);
		}
		cam.lookAt(0,0,0);
	}
	public void clickedUp() {
		// Insert code;
		cam.lookAt(0,0,0);
	}
	public void clickedDown() {
		// Insert code;
		cam.lookAt(0,0,0);
	}
	@Override
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
}
