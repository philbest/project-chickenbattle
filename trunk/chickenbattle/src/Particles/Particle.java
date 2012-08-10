package Particles;

import Map.Chunk;
import Map.Map;
import Map.Voxel;
import Screens.Application;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Particle {	
	private static Vector3 tmp = new Vector3();
	private static Vector3 tmp2 = new Vector3();
	protected Quaternion rotation = new Quaternion();

	public Vector3 position;
	public Vector3 velocity;
	public Vector3 size;
	public Vector3 sizeChange;
	public Vector3 dir;
	public Matrix4 modelMatrix;
	public float stepsAlive;
	public float steps;
	boolean emitter;
	boolean hasGravity;
	public int texVal;
	public float[] colorTint;
	boolean hasPhysics;
	public Particle(float px, float py, float pz, float vx, float vy, float vz, float w, float h, float d, float dw,float dh, float dd, float steps, boolean hasGravity, boolean emitter, boolean hasPhysics) {
		this.hasPhysics = hasPhysics;
		this.emitter = emitter;
		this.hasGravity = hasGravity;
		modelMatrix = new Matrix4();
		position = new Vector3(px,py,pz);
		velocity = new Vector3(vx,vy,vz);
		dir = new Vector3();
		size = new Vector3(w,h,d);
		sizeChange = new Vector3(dw,dh,dd);
		stepsAlive = steps;
		colorTint = new float[4];
		colorTint[0] = 0.7f;
		colorTint[1] = 0.3f;
		colorTint[2] = 0;
		colorTint[3] = 1f;
		texVal = MathUtils.random(0,15);
	}
	public void step(Application app, ParticleSystem sys) {
		dir.set(app.cam.position).sub(position).nor();
		setRotation(dir,app.cam.up);
		steps++;
		if (hasGravity)
			velocity.add(0,-0.01f,0);

		if (hasPhysics) {
			position.y += velocity.y;
			int cx = (int) position.x;
			int cy = (int) position.y;
			int cz = (int) position.z;
			if (cx >= 0 && cx < Map.x && cy >= 0 && cy < Map.y && cz >= 0 && cz < Map.z) {
				for (Chunk c : app.map.chunks) {
					if (c.x == (cx/Map.chunkSize) && c.y == (cy/Map.chunkSize) && c.z == (cz/Map.chunkSize)) {
						if (c.map[cx-c.x*Map.chunkSize][cy-c.y*Map.chunkSize][cz-c.z*Map.chunkSize].id != Voxel.nothing) {
							if (velocity.y > 0) {
								position.y -= velocity.y;
								velocity.y*=-0.5f;
								velocity.x*=0.6;
								velocity.z*=0.6;	
							} else {
								velocity.set(0,0,0);
							}
						}		
						break;
					}
				}
			}

			position.x += velocity.x;
			position.z += velocity.z;
			cx = (int) position.x;
			cy = (int) position.y;
			cz = (int) position.z;
			if (cx >= 0 && cx < Map.x && cy >= 0 && cy < Map.y && cz >= 0 && cz < Map.z) {
				for (Chunk c : app.map.chunks) {
					if (c.x == (cx/Map.chunkSize) && c.y == (cy/Map.chunkSize) && c.z == (cz/Map.chunkSize)) {
						if (c.map[cx-c.x*Map.chunkSize][cy-c.y*Map.chunkSize][cz-c.z*Map.chunkSize].id != Voxel.nothing) {
							position.x -= velocity.x;
							position.z -= velocity.z;
							velocity.x = velocity.x * -0.2f;
							velocity.z = velocity.z * -0.2f;
						}		
						break;
					}
				}
			}
		} else {
			position.add(velocity);
		}

		size.add(sizeChange);
		modelMatrix.setToTranslation(position);
		modelMatrix.scale(size.x,size.y,size.z);
		modelMatrix.rotate(rotation);
		if (emitter) {
			sys.addParticle(position.x,position.y,position.z,
					0,0.05f,0,
					0.8f, 0.8f, 0.8f, 0, 0, 0, 10, false, false,false);
		}
		colorTint[3] = 1-steps/stepsAlive;

	}
	public void setRotation (Vector3 dir, Vector3 up) {
		tmp.set(up).crs(dir).nor();
		tmp2.set(dir).crs(tmp).nor();
		rotation.setFromAxes(tmp.x, tmp.y, tmp.z, tmp2.x, tmp2.y, tmp2.z, dir.x, dir.y, dir.z);
	}

}
