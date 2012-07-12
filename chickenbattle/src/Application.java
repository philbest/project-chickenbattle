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
		if (cam.position.x > 0 && cam.position.x < Map.x*2 && cam.position.y > 0 && cam.position.y < Map.y*2 && cam.position.z > 0 && cam.position.z < Map.z*2) {
			
			
			Vector3 dirVec = cam.getPickRay(arg0, arg1).direction;
			//tile = 2x2x2
			Vector3 iterationPoint = new Vector3(cam.getPickRay(arg0,arg1).origin);
			int MAX_RANGE = 100;

			float range=0;
			boolean hit = false;

			do {
				int blockX = ((int) iterationPoint.x)/2;
				int blockY = ((int) iterationPoint.y)/2;
				int blockZ = ((int) iterationPoint.z)/2;
				if (map.map[blockX][blockY][blockZ] != null) {
					hit = true;
				}

				if(!hit)
				{
					float distX = dirVec.x==0?Float.MAX_VALUE:dirVec.x<0?(iterationPoint.x%2.0f)/-dirVec.y:(2.0f-iterationPoint.x%2.0f)/dirVec.x;
					float distY = dirVec.y==0?Float.MAX_VALUE:dirVec.y<0?(iterationPoint.y%2.0f)/-dirVec.y:(2.0f-iterationPoint.y%2.0f)/dirVec.y;
					float distZ = dirVec.z==0?Float.MAX_VALUE:dirVec.z<0?(iterationPoint.z%2.0f)/-dirVec.z:(2.0f-iterationPoint.z%2.0f)/dirVec.z;
					if (distX == 0)
						distX = Math.abs(2f/dirVec.x);
					if (distY == 0)
						distY = Math.abs(2f/dirVec.y);
					if (distZ == 0)
						distZ = Math.abs(2f/dirVec.z);
					float minDistance = Math.min(distX,Math.min(distY,distZ));
					System.out.println(minDistance + "MIN");
					iterationPoint.set(iterationPoint.x+dirVec.x*minDistance, 
							iterationPoint.y+dirVec.y*minDistance, 
							iterationPoint.z+dirVec.z*minDistance);
					Vector3 temp = new Vector3(iterationPoint);
					temp.sub(cam.getPickRay(arg0, arg1).origin );
					range= temp.len();
					System.out.println("RANGE " + range);
				}


			}
			while(!hit && range < MAX_RANGE);

			if(!hit)
				System.out.println("No blocks were hit by the ray.");
			else
				System.out.println("Hit a block at "+"("+iterationPoint.x+"),("+iterationPoint.y+"),("+iterationPoint.z+").");


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
