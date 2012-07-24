import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;


public class VertexAttributes {
	public static VertexAttribute position;
	public static VertexAttribute normal;
	public static VertexAttribute textureCoords;
	public static VertexAttribute occlusion;
	public static void initiate() {
		position = new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE );
		normal = new VertexAttribute(Usage.Normal,3,ShaderProgram.NORMAL_ATTRIBUTE);
		textureCoords = new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0");
		occlusion = new VertexAttribute(Usage.Generic,1,"a_occlusion");
	}

}
