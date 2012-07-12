import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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
	public Application() {
		light = new LightSource(5,6,5);
		map = new Map();
		cube = new Cube();
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
	public boolean scrolled(int arg0) {

		return false;
	}
	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		float range = 0;
		Vector3 point = new Vector3(cam.getPickRay(arg0, arg1).origin);
		Vector3 direction = new Vector3(cam.getPickRay(arg0,arg1).direction);
		direction.nor();
		boolean hit = false;
		int pointX = (int) point.x;
		int pointY = (int) point.y;
		int pointZ = (int) point.z;
		if (pointX >= 0 && pointX < map.x && pointY >= 0 && pointY < map.y && pointZ >= 0 && pointZ < map.z) {
			if (map.map[pointX][pointY][pointZ] != null) {
				hit = true;
				System.out.println("inside" + pointX + " " + pointY + " " + pointZ);
			}
		}
		int dx=0;
		int dy=0;
		int dz=0;
		while (!hit && range <200) {
			range+=1;
			float distX, distY, distZ;
			if (direction.x == 0) {
				distX = Float.MAX_VALUE;
			} else if (direction.x > 0) {
				distX = (pointX+dx)+1-point.x;
			} else {
				distX = point.x-(pointX+dx);
			}
			if (direction.y == 0) {
				distY = Float.MAX_VALUE;
			} else if (direction.y > 0) {
				distY = (pointY+dy)+1-point.y;
			} else {
				distY = point.y-(pointY+dy);
			}
			if (direction.z == 0) {
				distZ = Float.MAX_VALUE;
			} else if (direction.z > 0) {
				distZ = (pointZ+dz)+1-point.z;
			} else {
				distZ = point.z-(pointZ+dz);				
			}
			if (distX <= distY && distX <= distZ) {
				if (direction.x > 0) {
					dx++;
				} else {
					dx--;
				}
			} else if (distY <= distX && distY <= distZ) {
				if (direction.y > 0) {
					dy++;
				} else {
					dy--;
				}
			} else if (distZ <= distX && distZ <= distY) {
				if (direction.z > 0) {
					dz++;
				} else {
					dz--;
				}
			}
			if ((pointX+dx) >= 0 && (pointX+dx) < map.x && (pointY+dy) >= 0 && (pointY+dy) < map.y && (pointZ+dz) >= 0 && (pointZ+dz) < map.z) {
				if (map.map[pointX+dx][pointY+dy][pointZ+dz] != null) {
					hit = true;
					System.out.println("HIT" + (pointX+dx) + " " + (pointY+dy) + " " + (pointZ+dz));
				}
			}
		}
		draggedX = arg0;
		draggedY = arg1;
		return false;
	}
	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		float deltaX = arg0-draggedX;
		float deltaY = arg1-draggedY;
		deltaY = deltaY*-1;
		draggedX = arg0;
		draggedY = arg1;
		angleLeft -= deltaX/5;
		angleUP += deltaY/5;
		cam.direction.set(0,0,-1);
		cam.up.set(0,1,0);
		cam.rotate(angleUP, 1, 0, 0);
		cam.rotate(angleLeft, 0, 1, 0);
		cam.update();
		return false;
	}
	@Override
	public boolean touchMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
