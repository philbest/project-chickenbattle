import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.FloatArray;


public class AnimationPart {
	public Mesh partMesh;
	public Matrix4 modelMatrix;
	public Texture texture;
	BoundingBox box;
	public float x;
	public float y;
	public float z;
	public float w, h, d;
	public float rotationX;
	public float rotationZ;
	public AnimationPart(String str) {
		String[] strings = str.split(" ");
		// x,y,z,w,h,d,rotX,rotZ
		x = Float.parseFloat(strings[0]);
		y = Float.parseFloat(strings[1]);
		z = Float.parseFloat(strings[2]);
		w = Float.parseFloat(strings[3]);
		h = Float.parseFloat(strings[4]);
		d = Float.parseFloat(strings[5]);
		rotationX = Float.parseFloat(strings[6]);
		rotationZ = Float.parseFloat(strings[7]);
		FloatArray fa = new FloatArray();
		modelMatrix = new Matrix4();
		box = new BoundingBox();
		addTopFace(fa,0,0,0,w,h,d);
		addBotFace(fa,0,0,0,w,h,d);
		addLeftFace(fa,0,0,0,w,h,d);
		addRightFace(fa,0,0,0,w,h,d);
		addFrontFace(fa,0,0,0,w,h,d);
		addBackFace(fa,0,0,0,w,h,d);
		if (fa.size > 0) {
			partMesh = new Mesh(true, fa.size, 0,
					VertexAttributes.position, 
					VertexAttributes.normal,
					VertexAttributes.textureCoords);
			partMesh.setVertices(fa.items);
		}
		setTexture(new Texture(Gdx.files.internal("data/grassmap.png")));
		partMesh.calculateBoundingBox(box);
		updateModelMatrix();
	}
	public AnimationPart(float x, float y, float z, float w, float h, float d) {
		box = new BoundingBox();
		modelMatrix = new Matrix4();
		FloatArray fa = new FloatArray();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.h = h;
		this.d = d;
		addTopFace(fa,0,0,0,w,h,d);
		addBotFace(fa,0,0,0,w,h,d);
		addLeftFace(fa,0,0,0,w,h,d);
		addRightFace(fa,0,0,0,w,h,d);
		addFrontFace(fa,0,0,0,w,h,d);
		addBackFace(fa,0,0,0,w,h,d);
		if (fa.size > 0) {
			partMesh = new Mesh(true, fa.size, 0,
					VertexAttributes.position, 
					VertexAttributes.normal,
					VertexAttributes.textureCoords);
			partMesh.setVertices(fa.items);
		}
		setTexture(new Texture(Gdx.files.internal("data/grassmap.png")));
		partMesh.calculateBoundingBox(box);
		updateModelMatrix();
	}
	public String toString() {
		String ret = "";
		// x,y,z,w,h,d,rotX,rotZ
		ret = x + " " + y + " " + z + " " +  w + " " + h + " " + d  + " " + rotationX  + " " + rotationZ;		
		return ret;
	}
	public void updateModelMatrix() {
		modelMatrix.setToTranslation(x,y,z);
		modelMatrix.rotate(1,0,0,rotationX);
		modelMatrix.rotate(0,0,1,rotationZ);
		partMesh.calculateBoundingBox(box);
	}
	public void setTexture(Texture t) {
		texture = t;
	}
	public boolean contains(Vector3 vec) {
		Vector3 temp = new Vector3(vec);
		Matrix4 tempMat = new Matrix4(modelMatrix);
		tempMat.inv();
		temp.mul(tempMat);
		if (box.contains(temp)) {
			return true;
		}
		return false;
	}
	public void render(Application app, boolean hit) {
		app.modelViewProjectionMatrix.set(app.cam.combined);
		app.modelViewProjectionMatrix.mul(modelMatrix);
		if (hit)
			app.shader.setUniformi("s_hit", 1);
		else
			app.shader.setUniformi("s_hit", 0);
		texture.bind(0);
		app.shader.setUniformi("s_texture", 0);
		app.shader.setUniformMatrix("u_mvpMatrix", app.modelViewProjectionMatrix);
		partMesh.render(app.shader,GL20.GL_TRIANGLES);
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
