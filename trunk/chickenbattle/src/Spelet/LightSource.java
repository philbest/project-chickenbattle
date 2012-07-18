package Spelet;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;


public class LightSource {
	public int posX;
	public int posY;
	public int posZ;
	public static Mesh lightMesh;
	public float[] color;
	public float[] getViewSpacePositions(Matrix4 view){
		int i = 0;
		float position[] = new float[3];
		Vector3 tempVec = new Vector3(posX,posY,posZ);
		tempVec.mul(view);
		position[i++] = tempVec.x;
		position[i++] = tempVec.y;
		position[i++] = tempVec.z;
		return position;
	}
	public LightSource(int x, int y, int z) {
		posX = x;
		posY = y;
		posZ = z;
		color = new float[4];
		color[0] = 1f;
		color[1] = 1f;
		color[2] = 0f;
		color[3] = 1f;
		if (lightMesh == null) {
			float[] verts = new float[180];
			int i = 0;
			// Front
			verts[i++] = -1; // x1
			verts[i++] = -1; // y1
			verts[i++] = 1;
			verts[i++] = 0.5f; // u1
			verts[i++] = 0.5f; // v1

			verts[i++] = 1f; // x2
			verts[i++] = -1; // y2
			verts[i++] = 1;
			verts[i++] = 1f; // u2
			verts[i++] = 0.5f; // v2

			verts[i++] = 1f; // x3
			verts[i++] = 1f; // y2
			verts[i++] = 1;
			verts[i++] = 1f; // u3
			verts[i++] = 0f; // v3

			verts[i++] = 1f; // x3
			verts[i++] = 1f; // y2
			verts[i++] = 1;
			verts[i++] = 1f; // u3
			verts[i++] = 0f; // v3

			verts[i++] = -1; // x4
			verts[i++] = 1f; // y4
			verts[i++] = 1;
			verts[i++] = 0.5f; // u4
			verts[i++] = 0f; // v4

			verts[i++] = -1; // x1
			verts[i++] = -1; // y1
			verts[i++] = 1;
			verts[i++] = 0.5f; // u1
			verts[i++] = 0.5f; // v1

			// top
			verts[i++] = -1; // x1
			verts[i++] = 1; // y1
			verts[i++] = 1; // z1
			verts[i++] = 0f;
			verts[i++] = 0.5f;

			verts[i++] = 1; // x1
			verts[i++] = 1; // y1
			verts[i++] = 1; // z1
			verts[i++] = 0.5f;
			verts[i++] = 0.5f;

			verts[i++] = 1; // x1
			verts[i++] = 1; // y1
			verts[i++] = -1; // z1
			verts[i++] = 0.5f;
			verts[i++] = 0f;

			verts[i++] = 1; // x1
			verts[i++] = 1; // y1
			verts[i++] = -1; // z1
			verts[i++] = 0.5f;
			verts[i++] = 0f;

			verts[i++] = -1; // x1
			verts[i++] = 1; // y1
			verts[i++] = -1; // z1
			verts[i++] = 0f;
			verts[i++] = 0f;

			verts[i++] = -1; // x1
			verts[i++] = 1; // y1
			verts[i++] = 1; // z1
			verts[i++] = 0f;
			verts[i++] = 0.5f;

			// Right
			verts[i++] = 1; // x1
			verts[i++] = -1; // y1
			verts[i++] = 1; // z1
			verts[i++] = 0.5f;
			verts[i++] = 0.5f;

			verts[i++] = 1; // x1
			verts[i++] = -1; // y1
			verts[i++] = -1; // z1
			verts[i++] = 1f;
			verts[i++] = 0.5f;

			verts[i++] = 1; // x1
			verts[i++] = 1; // y1
			verts[i++] = -1; // z1
			verts[i++] = 1f;
			verts[i++] = 0f;

			verts[i++] = 1; // x1
			verts[i++] = 1; // y1
			verts[i++] = -1; // z1
			verts[i++] = 1f;
			verts[i++] = 0f;

			verts[i++] = 1; // x1
			verts[i++] = 1; // y1
			verts[i++] = 1; // z1
			verts[i++] = 0.5f;
			verts[i++] = 0f;

			verts[i++] = 1; // x1
			verts[i++] = -1; // y1
			verts[i++] = 1; // z1
			verts[i++] = 0.5f;
			verts[i++] = 0.5f;

			// bot
			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = 0;
			verts[i++] = 1;

			verts[i++] = 1;
			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = 0.5f;
			verts[i++] = 1;

			verts[i++] = 1;
			verts[i++] = -1;
			verts[i++] = 1;
			verts[i++] = 0.5f;
			verts[i++] = 0.5f;

			verts[i++] = 1;
			verts[i++] = -1;
			verts[i++] = 1;
			verts[i++] = 0.5f;
			verts[i++] = 0.5f;

			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = 1;
			verts[i++] = 0f;
			verts[i++] = 0.5f;

			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = 0;
			verts[i++] = 1;

			// Back
			verts[i++] = 1;
			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = 0.5f;
			verts[i++] = 0.5f;

			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = 1f;
			verts[i++] = 0.5f;

			verts[i++] = -1;
			verts[i++] = 1;
			verts[i++] = -1;
			verts[i++] = 1f;
			verts[i++] = 0f;

			verts[i++] = -1;
			verts[i++] = 1;
			verts[i++] = -1;
			verts[i++] = 1f;
			verts[i++] = 0f;


			verts[i++] = 1;
			verts[i++] = 1;
			verts[i++] = -1;
			verts[i++] = 0.5f;
			verts[i++] = 0f;

			verts[i++] = 1;
			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = 0.5f;
			verts[i++] = 0.5f;

			// Left
			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = 0.5f;
			verts[i++] = 0.5f;

			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = 1;
			verts[i++] = 1;
			verts[i++] = 0.5f;

			verts[i++] = -1;
			verts[i++] = 1;
			verts[i++] = 1;
			verts[i++] = 1;
			verts[i++] = 0;

			verts[i++] = -1;
			verts[i++] = 1;
			verts[i++] = 1;
			verts[i++] = 1;
			verts[i++] = 0;

			verts[i++] = -1;
			verts[i++] = 1;
			verts[i++] = -1;
			verts[i++] = 0.5f;
			verts[i++] = 0;

			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = -1;
			verts[i++] = 0.5f;
			verts[i++] = 0.5f;

			lightMesh = new Mesh(true, 36, 0,
					new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
					new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );

			lightMesh.setVertices( verts );
		}
	}
}
