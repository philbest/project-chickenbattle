package Spelet;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.TimeUtils;

public class Explosion {
	float startTime;
	float timeAlive;
	float ttl;
	float size;
	Mesh mesh;
	FloatArray fa;
	Vector3 dirCalc;
	Matrix4 rotMat;
	public Explosion(float radius, float startTime, float ttl, int particles, float size) {
		dirCalc = new Vector3();
		rotMat = new Matrix4();
		this.startTime = startTime;
		this.ttl = ttl;
		this.size = size;
		fa = new FloatArray();
		for (int i = 0; i < particles; i++) {
			dirCalc.set(1,0,0);
			float ry = MathUtils.random(360);
			float rz = MathUtils.random(180);
			rotMat.setToRotation(0,1,0,ry);
			rotMat.rotate(0,0,1,rz);
			dirCalc.rot(rotMat);
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
	public boolean isDead() {
		return (System.currentTimeMillis() > startTime+ttl);
	}
	public void render(ShaderProgram shader) {
		shader.setUniformf("u_ptime", (TimeUtils.millis()-startTime));
		mesh.render(shader, GL20.GL_TRIANGLES);
	}
}
