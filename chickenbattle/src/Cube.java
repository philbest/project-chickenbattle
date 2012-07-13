import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;


public class Cube {
	public static Mesh cubeMesh;
	public static void initiate() {
		float[] verts = new float[288];
		int i = 0;
		// Front
		verts[i++] = 0; // x1
		verts[i++] = 0; // y1
		verts[i++] = 1;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 1; // Normal Z
		verts[i++] = 0.5f; // u1
		verts[i++] = 0.5f; // v1

		verts[i++] = 1f; // x2
		verts[i++] = 0; // y2
		verts[i++] = 1;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 1; // Normal Z
		verts[i++] = 1f; // u2
		verts[i++] = 0.5f; // v2

		verts[i++] = 1f; // x3
		verts[i++] = 1f; // y2
		verts[i++] = 1f;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 1; // Normal Z
		verts[i++] = 1f; // u3
		verts[i++] = 0f; // v3

		verts[i++] = 1f; // x3
		verts[i++] = 1f; // y2
		verts[i++] = 1f;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 1; // Normal Z
		verts[i++] = 1f; // u3
		verts[i++] = 0f; // v3

		verts[i++] = 0; // x4
		verts[i++] = 1f; // y4
		verts[i++] = 1;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 1; // Normal Z
		verts[i++] = 0.5f; // u4
		verts[i++] = 0f; // v4

		verts[i++] = 0; // x1
		verts[i++] = 0; // y1
		verts[i++] = 1;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 1; // Normal Z
		verts[i++] = 0.5f; // u1
		verts[i++] = 0.5f; // v1

		// top
		verts[i++] = 0; // x1
		verts[i++] = 1; // y1
		verts[i++] = 1; // z1
		verts[i++] = 0; // Normal X
		verts[i++] = 1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0f;
		verts[i++] = 0.5f;

		verts[i++] = 1; // x1
		verts[i++] = 1; // y1
		verts[i++] = 1; // z1
		verts[i++] = 0; // Normal X
		verts[i++] = 1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0.5f;

		verts[i++] = 1; // x1
		verts[i++] = 1; // y1
		verts[i++] = 0; // z1
		verts[i++] = 0; // Normal X
		verts[i++] = 1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0f;

		verts[i++] = 1; // x1
		verts[i++] = 1; // y1
		verts[i++] = 0; // z1
		verts[i++] = 0; // Normal X
		verts[i++] = 1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0f;

		verts[i++] = 0; // x1
		verts[i++] = 1; // y1
		verts[i++] = 0; // z1
		verts[i++] = 0; // Normal X
		verts[i++] = 1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0f;
		verts[i++] = 0f;

		verts[i++] = 0; // x1
		verts[i++] = 1; // y1
		verts[i++] = 1; // z1
		verts[i++] = 0; // Normal X
		verts[i++] = 1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0f;
		verts[i++] = 0.5f;

		// Right
		verts[i++] = 1; // x1
		verts[i++] = 0; // y1
		verts[i++] = 1; // z1
		verts[i++] = 1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0.5f;

		verts[i++] = 1; // x1
		verts[i++] = 0; // y1
		verts[i++] = 0; // z1
		verts[i++] = 1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 1f;
		verts[i++] = 0.5f;

		verts[i++] = 1; // x1
		verts[i++] = 1; // y1
		verts[i++] = 0; // z1
		verts[i++] = 1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 1f;
		verts[i++] = 0f;

		verts[i++] = 1; // x1
		verts[i++] = 1; // y1
		verts[i++] = 0; // z1
		verts[i++] = 1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 1f;
		verts[i++] = 0f;

		verts[i++] = 1; // x1
		verts[i++] = 1; // y1
		verts[i++] = 1; // z1
		verts[i++] = 1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0f;

		verts[i++] = 1; // x1
		verts[i++] = 0; // y1
		verts[i++] = 1; // z1
		verts[i++] = 1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0.5f;

		// bot
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0; // Normal X
		verts[i++] = -1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0;
		verts[i++] = 1;

		verts[i++] = 1;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0; // Normal X
		verts[i++] = -1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 1;

		verts[i++] = 1;
		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = 0; // Normal X
		verts[i++] = -1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0.5f;

		verts[i++] = 1;
		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = 0; // Normal X
		verts[i++] = -1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0.5f;

		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = 0; // Normal X
		verts[i++] = -1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0f;
		verts[i++] = 0.5f;

		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0; // Normal X
		verts[i++] = -1; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0;
		verts[i++] = 1;

		// Back
		verts[i++] = 1;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = -1; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0.5f;

		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = -1; // Normal Z
		verts[i++] = 1f;
		verts[i++] = 0.5f;

		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = 0;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = -1; // Normal Z
		verts[i++] = 1f;
		verts[i++] = 0f;

		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = 0;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = -1; // Normal Z
		verts[i++] = 1f;
		verts[i++] = 0f;


		verts[i++] = 1;
		verts[i++] = 1;
		verts[i++] = 0;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = -1; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0f;

		verts[i++] = 1;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = -1; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0.5f;

		// Left
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = -1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0.5f;

		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = -1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 1;
		verts[i++] = 0.5f;

		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = 1;
		verts[i++] = -1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 1;
		verts[i++] = 0;

		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = 1;
		verts[i++] = -1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 1;
		verts[i++] = 0;

		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = 0;
		verts[i++] = -1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0;

		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = -1; // Normal X
		verts[i++] = 0; // Normal Y
		verts[i++] = 0; // Normal Z
		verts[i++] = 0.5f;
		verts[i++] = 0.5f;

		cubeMesh = new Mesh(true, 36, 0,
				new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ), 
				new VertexAttribute(Usage.Normal,3,ShaderProgram.NORMAL_ATTRIBUTE),
				new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );

		cubeMesh.setVertices( verts );
	}
}
