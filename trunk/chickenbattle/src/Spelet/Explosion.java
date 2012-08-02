package Spelet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;

public class Explosion {
	float timeAlive;
	float ttl;
	float size;
	Mesh mesh;
	FloatArray fa;
	Vector3 dirCalc;
	Matrix4 rotMat;
	Matrix4 modelMatrix;
	public Explosion(float x, float y, float z, float radius, float ttl, int particles, float size) {
		modelMatrix = new Matrix4();
		modelMatrix.setToTranslation(x,y,z);
		dirCalc = new Vector3();
		rotMat = new Matrix4();
		this.ttl = ttl;
		this.size = size;
		fa = new FloatArray();
		for (int i = 0; i < particles; i++) {
			dirCalc.set(1,0,0);
			float ry = MathUtils.random(360);
			float rz = MathUtils.random(180);
			rotMat.setToRotation(0,1,0,ry);
			dirCalc.rot(rotMat);
			rotMat.setToRotation(0,0,1,rz);
			dirCalc.rot(rotMat);
			dirCalc.mul(MathUtils.random(3f));
			addVertices(dirCalc.x,dirCalc.y,dirCalc.z);
		}
		mesh = new Mesh(true, fa.size, 0,
				VertexAttributes.position, 
				VertexAttributes.particledirection);
		mesh.setVertices(fa.items);
	}
	public void addVertices(float dx, float dy, float dz) {
		fa.add(-size/2);
		fa.add(-size/2);
		fa.add(0);
		fa.add(dx);
		fa.add(dy);
		fa.add(dz);
		
		fa.add(size/2);
		fa.add(-size/2);
		fa.add(0);
		fa.add(dx);
		fa.add(dy);
		fa.add(dz);
		
		fa.add(size/2);
		fa.add(size/2);
		fa.add(0);
		fa.add(dx);
		fa.add(dy);
		fa.add(dz);
		
		fa.add(size/2);
		fa.add(size/2);
		fa.add(0);
		fa.add(dx);
		fa.add(dy);
		fa.add(dz);
		
		fa.add(-size/2);
		fa.add(size/2);
		fa.add(0);
		fa.add(dx);
		fa.add(dy);
		fa.add(dz);
		
		fa.add(-size/2);
		fa.add(-size/2);
		fa.add(0);
		fa.add(dx);
		fa.add(dy);
		fa.add(dz);
		
	}
	public void update() {
		timeAlive += Gdx.graphics.getDeltaTime()*1000;
	}
	public boolean isDead() {
		return (ttl < timeAlive);
	}
}
