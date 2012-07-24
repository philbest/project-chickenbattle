import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.FloatArray;


public class AnimationPart {
	public Mesh partMesh;
	public Matrix4 modelMatrix;
	public AnimationPart() {
		
	}
	public AnimationPart(int x, int y, int z, int w, int h, int d) {
		FloatArray fa = new FloatArray();
		addTopFace(fa,0,0,0);
		addBotFace(fa,0,0,0);
		addLeftFace(fa,0,0,0);
		addRightFace(fa,0,0,0);
		addFrontFace(fa,0,0,0);
		addBackFace(fa,0,0,0);
		if (fa.size > 0) {
			partMesh = new Mesh(true, fa.size, 0,
					VertexAttributes.position, 
					VertexAttributes.normal,
					VertexAttributes.textureCoords,
					VertexAttributes.occlusion);
			partMesh.setVertices(fa.items);
		}
	}
	public void render(Application app) {
		app.modelViewProjectionMatrix.set(app.cam.combined);
		app.modelViewProjectionMatrix.mul(modelMatrix);	
		app.shader.setUniformMatrix("u_mvpMatrix", app.modelViewProjectionMatrix);
		partMesh.render(app.shader,GL20.GL_TRIANGLES);
	}
	
	public void addTopFace(FloatArray fa, int x, int y, int z) {
		fa.add(0+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0.5f);

		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0f);


		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0f);

		fa.add(0+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0f);

		fa.add(0+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0.5f);
	}
	public void addBotFace(FloatArray fa, int x, int y, int z) {
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0);
		fa.add(1);
		
		fa.add(1+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(1);

		fa.add(1+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(1+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(0+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0.5f);

		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0);
		fa.add(1);

	}

	public void addLeftFace(FloatArray fa, int x, int y, int z) {
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(0+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1);
		fa.add(0.5f);

		fa.add(0+x);
		fa.add(1+y);
		fa.add(1+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1);
		fa.add(0);

		fa.add(0+x);
		fa.add(1+y);
		fa.add(1+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1);
		fa.add(0);

		fa.add(0+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0);

		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
	}

	public void addRightFace(FloatArray fa, int x, int y, int z) {
		fa.add(1+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(1+x); // x1
		fa.add(0+y); // y1
		fa.add(0+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1f);
		fa.add(0.5f);

		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1f);
		fa.add(0f);

		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1f);
		fa.add(0f);

		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0f);

		fa.add(1+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
	}
	public void addFrontFace(FloatArray fa, int x, int y, int z) {
		fa.add(0+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(0.5f); // u1
		fa.add(0.5f); // v1

		fa.add(1+x); // x2
		fa.add(0+y); // y2
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(1f); // u2
		fa.add(0.5f); // v2

		fa.add(1f+x); // x3
		fa.add(1f+y); // y2
		fa.add(1f+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(1f); // u3
		fa.add(0f); // v3

		fa.add(1f+x); // x3
		fa.add(1f+y); // y2
		fa.add(1f+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(1f); // u3
		fa.add(0f); // v3

		fa.add(0+x); // x4
		fa.add(1+y); // y4
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(0.5f); // u4
		fa.add(0f); // v4

		fa.add(0+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(0.5f); // u1
		fa.add(0.5f); // v1

	}
	public void addBackFace(FloatArray fa, int x, int y, int z) {
		fa.add(1+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);

		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(1f);
		fa.add(0.5f);

		fa.add(0+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(1f);
		fa.add(0f);

		fa.add(0+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(1f);
		fa.add(0f);

		fa.add(1+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(0.5f);
		fa.add(0f);

		fa.add(1+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
	}
}
