import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;


public class Application implements InputProcessor{
	public Renderer renderer;
	public Cube cube;
	PerspectiveCamera cam;
	int draggedX;
	int draggedY;
	float angleUP, angleLeft;
	public Map map;
	public LightSource light;
	public Vector3 from;
	public Vector3 to;
	public boolean adding;
	public Application() {
		from = new Vector3(0,0,0);
		to = new Vector3(0,0,0);
		light = new LightSource(5,6,5);
		map = new Map();
		map.maxTime = 0;
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0,0,40);
		cam.update();
		renderer = new Renderer();
		Gdx.input.setInputProcessor(this);
	}
	public void render() {
		renderer.render(this);
	}
	public void update() {
		Gdx.input.setCursorCatched(true);
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			Vector3 movement = new Vector3();
			movement.set(cam.direction);
			movement.nor();
			movement.mul(Gdx.graphics.getDeltaTime()*10);
			cam.position.add(movement);
			cam.update();
		}
		if (Gdx.input.isKeyPressed((Input.Keys.S))) {
			Vector3 movement = new Vector3();
			movement.set(cam.direction);
			movement.nor();
			movement.mul(Gdx.graphics.getDeltaTime()*10*-1);
			cam.position.add(movement);
			cam.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			Vector3 movement = new Vector3();
			movement.set(cam.direction);
			movement.crs(cam.up);
			movement.mul(Gdx.graphics.getDeltaTime()*10);
			cam.position.add(movement);
			cam.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			Vector3 movement = new Vector3();
			movement.set(cam.direction);
			movement.crs(cam.up);
			movement.mul(Gdx.graphics.getDeltaTime()*-10);

			cam.position.add(movement);
			cam.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			cam.position.y+=Gdx.graphics.getDeltaTime()*10;
			cam.update();
		}
	}
	@Override
	public boolean keyDown(int arg0) {
		if (Input.Keys.NUM_1 == arg0) {
			adding = true;
		} else if (Input.Keys.NUM_2 == arg0) {
			adding = false;
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
	@Override
	public boolean scrolled(int arg0) {

		return false;
	}
	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		float range = 0;
		Vector3 point = new Vector3(cam.getPickRay(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2).origin);
		Vector3 direction = new Vector3(cam.getPickRay(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2).direction);
		direction.nor();
		direction.mul(0.5f);
		from.set(point);
		to.set(direction).mul(100);
		boolean hit = false;
		int pointX = (int) point.x;
		int pointY = (int) point.y;
		int pointZ = (int) point.z;
		if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
			if (map.map[pointX][pointY][pointZ] == 1) {
				hit = true;
				System.out.println("inside" + pointX + " " + pointY + " " + pointZ);
			}
		}
		while (!hit && range < 200) {
			range += direction.len();
			point.add(direction);
			pointX = (int) point.x;
			pointY = (int) point.y;
			pointZ = (int) point.z;
			if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
				if (map.map[pointX][pointY][pointZ] == 1) {
					hit = true;
					System.out.println("hit" + pointX + " " + pointY + " " + pointZ);
				}
			}
			if (hit) {
				if (adding) {
					point.sub(direction);
					pointX = (int) point.x;
					pointY = (int) point.y;
					pointZ = (int) point.z;
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						map.map[pointX][pointY][pointZ] = 1;
						map.rebuildChunk((int)(pointX/Map.chunkSize),(int)(pointY/Map.chunkSize),(int)(pointZ/Map.chunkSize));
					}
				} else {
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						map.map[pointX][pointY][pointZ] = 0;
						map.rebuildChunk((int)(pointX/Map.chunkSize),(int)(pointY/Map.chunkSize),(int)(pointZ/Map.chunkSize));
					}
				}
			}
		}
		return false;
	}
	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {

		return false;
	}
	@Override
	public boolean touchMoved(int arg0, int arg1) {
		if (draggedX == 0)
			draggedX = arg0;
		if (draggedY == 0)
			draggedY = arg1;
		float deltaX = (arg0-draggedX)*2;
		float deltaY = (arg1-draggedY)*2;
		deltaY = deltaY*-1;
		draggedX = arg0;
		draggedY = arg1;
		angleLeft -= deltaX/5;
		angleUP += deltaY/5;
		if (angleUP < -90)
			angleUP = -90;
		if (angleUP > 90)
			angleUP = 90;
		cam.direction.set(0,0,-1);
		cam.up.set(0,1,0);
		cam.rotate(angleUP, 1, 0, 0);
		cam.rotate(angleLeft, 0, 1, 0);
		cam.update();
		return false;
	}
	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
