package Spelet;

import Map.Chunk;
import Map.Voxel;
import Map.Map;
import Particles.Particle;
import Screens.Application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Explosion {
	public Matrix4 mat;
	public Vector3 direction;
	public Vector3 position;
	public Vector3 velocity;
	public Vector3 acceleration;
	public Vector3 rotationVec;
	float timeAlive;
	float ttl;
	float gravity;
	float timer;
	float initialForceUp;
	float rotation;
	public float alpha;
	public Explosion(float x, float y, float z, float cx, float cy, float cz, float ttl) {
		position = new Vector3(x,y,z);
		velocity = new Vector3(0,0,0);
		acceleration = new Vector3(0,0,0);
		mat = new Matrix4();
		mat.setToTranslation(position);
		direction = new Vector3();		
		direction.set(x-cx,y-cy,z-cz);
		direction.nor();
		rotationVec = new Vector3(direction);
		this.ttl = ttl;
		gravity = -1f;
		timer = 0;
		initialForceUp = 8f;
		alpha = 1;
	}
	public void update(Application app) {
		timeAlive += Gdx.graphics.getDeltaTime()*1000;
		timer += Gdx.graphics.getDeltaTime()*1000;
		acceleration.set(direction);
		acceleration.add(0,gravity+initialForceUp,0);
		acceleration.mul(Gdx.graphics.getDeltaTime());
		if (initialForceUp > 0) {
			initialForceUp -= 60*Gdx.graphics.getDeltaTime();
			if (initialForceUp < 0) {
				initialForceUp = 0;
			}
		}
		velocity.add(acceleration);
		position.y += velocity.y;
		int cx = (int) position.x;
		int cy = (int) position.y;
		int cz = (int) position.z;
		if (cx >= 0 && cx < Map.x && cy >= 0 && cy < Map.y && cz >= 0 && cz < Map.z) {
			for (Chunk c : app.map.chunks) {
				if (c.x == (cx/Map.chunkSize) && c.y == (cy/Map.chunkSize) && c.z == (cz/Map.chunkSize)) {
					if (c.map[cx-c.x*Map.chunkSize][cy-c.y*Map.chunkSize][cz-c.z*Map.chunkSize].id != Voxel.nothing) {
						position.y -= velocity.y;
						velocity.y*=-0.75f;
						velocity.x*=0.6;
						velocity.z*=0.6;
						direction.z *=0.5f;
						direction.x *=0.5f;
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
						direction.z *=-0.5f;
						direction.x *=-0.5f;
					}		
					break;
				}
			}
		}

		direction.mul(0.9f);
		mat.setToTranslation(position);
		rotation ++;
		mat.rotate(rotationVec,rotation);
		if (timeAlive > ttl*0.8) {
			alpha = 1-(timeAlive-ttl*0.8f)/(ttl*0.2f);
		}
		if (timer > 16) {
			if (timeAlive < ttl*0.8) {
				Particle p = new Particle(position.x, position.y, position.z, 3, 3, 3, 10, false, false, true);
				p.setVelocity(0,0.1f,0);
				p.setColor(0.8f,0.8f,0.8f);
				app.explosionParticles.addParticle(p);
				p = new Particle(position.x, position.y, position.z, 3, 3, 3, 5, false, false, false);
				p.setVelocity(0,0.1f,0);
				p.setSizeChange(-0.1f,-0.1f,-0.1f);
				p.setColor(1f,0.75f,0.5f);
				app.explosionParticles.addParticle(p);
				timer -= 16;
			}
		}
	}
	public boolean isDead() {
		return timeAlive > ttl;
	}
}
