package Spelet;

import java.util.HashMap;

import Screens.Application;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.TimeUtils;

public class ExplosionManager {
	public Array<Explosion> explosions;
	public HashMap<Integer, Mesh> parts;
	public ExplosionManager() {
		explosions = new Array<Explosion>();
		parts = new HashMap<Integer,Mesh>();
		FloatArray fa = new FloatArray();
		float blockSize = 0.5f;
		addTopFace(fa,0,0,0,blockSize,blockSize,blockSize);
		addBotFace(fa,0,0,0,blockSize,blockSize,blockSize);
		addRightFace(fa,0,0,0,blockSize,blockSize,blockSize);
		addLeftFace(fa,0,0,0,blockSize,blockSize,blockSize);
		addFrontFace(fa,0,0,0,blockSize,blockSize,blockSize);
		addBackFace(fa,0,0,0,blockSize,blockSize,blockSize);
		if (fa.size > 0) {
			Mesh mesh = new Mesh(true, fa.size, 0,
					VertexAttributes.position, 
					VertexAttributes.normal,
					VertexAttributes.textureCoords);
			mesh.setVertices(fa.items);
			parts.put(1, mesh);
		}
	}
	public void addExplotion(float x, float y, float z, float cx, float cy, float cz) {
		float ttl = 5000;
		explosions.add(new Explosion(x,y,z,cx,cy,cz,ttl));
//		explosions.add(new Explosion(x+0.5f,y,z,cx,cy,cz,ttl));
//		explosions.add(new Explosion(x,y,z+0.5f,cx,cy,cz,ttl));
//		explosions.add(new Explosion(x+0.5f,y,z+0.5f,cx,cy,cz,ttl));
//		
//		explosions.add(new Explosion(x,y+0.5f,z,cx,cy,cz,ttl));
//		explosions.add(new Explosion(x+0.5f,y+0.5f,z,cx,cy,cz,ttl));
//		explosions.add(new Explosion(x,y+0.5f,z+0.5f,cx,cy,cz,ttl));
//		explosions.add(new Explosion(x+0.5f,y+0.5f,z+0.5f,cx,cy,cz,ttl));
	}
	public void update(Application app) {
		for (int i = explosions.size-1; i >= 0; i--) {
			explosions.get(i).update(app);
			if (explosions.get(i).isDead()) {
				explosions.removeIndex(i);
			}
		}
	}

	public void addTopFace(FloatArray fa, int x, int y, int z, float w, float h, float d) {
		fa.add(-w/2+x); // x1
		fa.add(h/2+y); // y1
		fa.add(d/2+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0.5f);

		fa.add(w/2+x); // x1
		fa.add(h/2+y); // y1
		fa.add(d/2+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(w/2+x); // x1
		fa.add(h/2+y); // y1
		fa.add(-d/2+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0f);


		fa.add(w/2+x); // x1
		fa.add(h/2+y); // y1
		fa.add(-d/2+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0f);

		fa.add(-w/2+x); // x1
		fa.add(h/2+y); // y1
		fa.add(-d/2+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0f);

		fa.add(-w/2+x); // x1
		fa.add(h/2+y); // y1
		fa.add(d/2+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0.5f);
	}
	public void addBotFace(FloatArray fa, int x, int y, int z, float w, float h, float d) {
		fa.add(-w/2+x);
		fa.add(-h/2+y);
		fa.add(-d/2+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0);
		fa.add(1);

		fa.add(w/2+x);
		fa.add(-h/2+y);
		fa.add(-d/2+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(1);

		fa.add(w/2+x);
		fa.add(-h/2+y);
		fa.add(d/2+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(w/2+x);
		fa.add(-h/2+y);
		fa.add(d/2+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(-w/2+x);
		fa.add(-h/2+y);
		fa.add(d/2+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0.5f);

		fa.add(-w/2+x);
		fa.add(-h/2+y);
		fa.add(-d/2+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0);
		fa.add(1);

	}

	public void addLeftFace(FloatArray fa, int x, int y, int z, float w, float h, float d) {
		fa.add(-w/2+x);
		fa.add(-h/2+y);
		fa.add(-d/2+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(-w/2+x);
		fa.add(-h/2+y);
		fa.add(d/2+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1);
		fa.add(0.5f);

		fa.add(-w/2+x);
		fa.add(h/2+y);
		fa.add(d/2+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1);
		fa.add(0);

		fa.add(-w/2+x);
		fa.add(h/2+y);
		fa.add(d/2+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1);
		fa.add(0);

		fa.add(-w/2+x);
		fa.add(h/2+y);
		fa.add(-d/2+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0);

		fa.add(-w/2+x);
		fa.add(-h/2+y);
		fa.add(-d/2+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
	}

	public void addRightFace(FloatArray fa, int x, int y, int z, float w, float h, float d) {
		fa.add(w/2+x); // x1
		fa.add(-h/2+y); // y1
		fa.add(d/2+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(w/2+x); // x1
		fa.add(-h/2+y); // y1
		fa.add(-d/2+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1f);
		fa.add(0.5f);

		fa.add(w/2+x); // x1
		fa.add(h/2+y); // y1
		fa.add(-d/2+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1f);
		fa.add(0f);

		fa.add(w/2+x); // x1
		fa.add(h/2+y); // y1
		fa.add(-d/2+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1f);
		fa.add(0f);

		fa.add(w/2+x); // x1
		fa.add(h/2+y); // y1
		fa.add(d/2+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0f);

		fa.add(w/2+x); // x1
		fa.add(-h/2+y); // y1
		fa.add(d/2+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
	}
	public void addFrontFace(FloatArray fa, int x, int y, int z, float w, float h, float d) {
		fa.add(-w/2+x); // x1
		fa.add(-h/2+y); // y1
		fa.add(d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(0.5f); // u1
		fa.add(0.5f); // v1

		fa.add(w/2+x); // x2
		fa.add(-h/2+y); // y2
		fa.add(d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(1f); // u2
		fa.add(0.5f); // v2

		fa.add(w/2+x); // x3
		fa.add(h/2+y); // y2
		fa.add(d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(1f); // u3
		fa.add(0f); // v3

		fa.add(w/2+x); // x3
		fa.add(h/2+y); // y2
		fa.add(d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(1f); // u3
		fa.add(0f); // v3

		fa.add(-w/2+x); // x4
		fa.add(h/2+y); // y4
		fa.add(d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(0.5f); // u4
		fa.add(0f); // v4

		fa.add(-w/2+x); // x1
		fa.add(-h/2+y); // y1
		fa.add(d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(0.5f); // u1
		fa.add(0.5f); // v1

	}
	public void addBackFace(FloatArray fa, int x, int y, int z, float w, float h, float d) {
		fa.add(w/2+x);
		fa.add(-h/2+y);
		fa.add(-d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(-w/2+x);
		fa.add(-h/2+y);
		fa.add(-d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(1f);
		fa.add(0.5f);

		fa.add(-w/2+x);
		fa.add(h/2+y);
		fa.add(-d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(1f);
		fa.add(0f);

		fa.add(-w/2+x);
		fa.add(h/2+y);
		fa.add(-d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(1f);
		fa.add(0f);

		fa.add(w/2+x);
		fa.add(h/2+y);
		fa.add(-d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(0.5f);
		fa.add(0f);

		fa.add(w/2+x);
		fa.add(-h/2+y);
		fa.add(-d/2+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
	}
}
