package Particles;

import Screens.Application;
import Spelet.VertexAttributes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class ParticleSystem {
	public Array<Particle> particles;
	public float timer;
	public static Mesh quad;
	public static Texture midExplo;
	public ParticleSystem() {
		midExplo = new Texture(Gdx.files.internal("data/Particles/midexplosion.bmp"));
		particles = new Array<Particle>();
		initiateMesh();

	}
	public void addParticle(float px, float py, float pz, float vx, float vy, float vz, float w, float h, float d, float dw,float dh, float dd, float steps, boolean grav, boolean emitts, boolean phys) {
		particles.add(new Particle(px, py,pz, vx, vy, vz, w, h, d, dw,dh, dd, steps, grav, emitts, phys));
	}
	public void update(Application app) {
		timer += Gdx.graphics.getDeltaTime()*1000;
		float stepSpeed = 32;
		if (timer >= stepSpeed) {
			for (int i = particles.size-1; i >= 0; i--) {
				particles.get(i).step(app,this);
				if (particles.get(i).steps > particles.get(i).stepsAlive) {
					particles.removeIndex(i);
				}
			}
			timer -= stepSpeed;
		}
	}
	public void addExplosion(float cx, float cy, float cz) {
		addParticle(cx,cy,cz,
				0,0,0,
				5,5,5,
				-0.01f,-0.01f,-0.01f,
				100,false,false,false);
		addParticle(cx,cy,cz,
				0,0,0,
				5,5,5,
				3,3,3,
				20,false,false,false);
		Vector3 temp = new Vector3();
		Matrix4 rot = new Matrix4();
		for (int i = 0; i < 30; i++) {
			temp.set(1,0,0);
			rot.setToRotation(0,0,1,MathUtils.random(50f,90f));
			temp.rot(rot);
			rot.setToRotation(0,1,0, MathUtils.random(360f));
			temp.rot(rot);
			temp.nor();
			temp.mul(MathUtils.random(0.3f,0.4f));
			addParticle(cx,cy,cz,
					temp.x,temp.y,temp.z,
					1,1,1,
					0,0,0,
					100,true,true,true);	
		}
		for (int i = 0; i < 40; i++) {
			temp.set(1,0,0);
			rot.setToRotation(0,0,1,MathUtils.random(10f,40f));
			temp.rot(rot);
			rot.setToRotation(0,1,0, MathUtils.random(360f));
			temp.rot(rot);
			temp.nor();
			temp.mul(0.7f);
			addParticle(cx,cy,cz,
					temp.x,temp.y,temp.z,
					1,1,1,
					0,0,0,
					20,true,true,true);	
		}
	}



	public static void initiateMesh() {
		FloatArray fa = new FloatArray();
		fa.add(-0.5f); // x1
		fa.add(-0.5f); // y1
		fa.add(0);
		fa.add(5);
		fa.add(5);
		fa.add(5);
		fa.add(0); // u1
		fa.add(0.25f); // v1

		fa.add(0.5f); // x2
		fa.add(-0.5f); // y2
		fa.add(0f);
		fa.add(5);
		fa.add(5);
		fa.add(5);
		fa.add(0.25f); // u2
		fa.add(0.25f); // v2

		fa.add(0.5f); // x3
		fa.add(0.5f); // y2
		fa.add(0f);
		fa.add(5);
		fa.add(5);
		fa.add(5);
		fa.add(0.25f); // u3
		fa.add(0); // v3

		fa.add(0.5f); // x3
		fa.add(0.5f); // y2
		fa.add(0f);
		fa.add(5);
		fa.add(5);
		fa.add(5);
		fa.add(0.25f); // u3
		fa.add(0); // v3

		fa.add(-0.5f); // x4
		fa.add(0.5f); // y4
		fa.add(0f);
		fa.add(5);
		fa.add(5);
		fa.add(5);
		fa.add(0); // u4
		fa.add(0); // v4

		fa.add(-0.5f); // x1
		fa.add(-0.5f); // y1
		fa.add(0f);
		fa.add(5);
		fa.add(5);
		fa.add(5);
		fa.add(0); // u1
		fa.add(0.25f); // v1
		fa.shrink();
		quad = new Mesh(true, 6, 0,
				VertexAttributes.position,
				VertexAttributes.normal,
				VertexAttributes.textureCoords);
		quad.setVertices(fa.items);
	}
}
