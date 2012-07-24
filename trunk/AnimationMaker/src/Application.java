import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;


public class Application {
	PerspectiveCamera cam;
	public float camRadius;
	public int camOrientation;
	public Application() {
		// Insert starting details:
		cam = new PerspectiveCamera(); 
		camRadius = 5;
	}
	public void changeRadius(float dr) {
		camRadius += dr;
		
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
		// Insert code;
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
}
