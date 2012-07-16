import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.TimeUtils;


public class Map {
	public static final int chunkSize = 16;
	public static final int x = chunkSize*6; // Must be multiple of chunkSize;
	public static final int y = chunkSize; // Must be multiple of chunkSize;
	public static final int z = chunkSize*6; // Must be multiple of chunkSize;
	Array<Chunk> chunks;
	public Map() {
		chunks = new Array<Chunk>();

		buildChunks();
	}
	public void buildChunks() {
		Heightmap h = HeightmapUtils.load(Gdx.files.internal("data/noise2.png"));
		for (int chunkX = 0; chunkX < x/chunkSize; chunkX++) {
			for (int chunkY = 0; chunkY < y/chunkSize; chunkY++) {
				for (int chunkZ = 0; chunkZ < z/chunkSize; chunkZ++) {
					Chunk newChunk = new Chunk(chunkX, chunkY, chunkZ);
					for (int x2 = 0; x2 < chunkSize; x2++) {
						for (int z2 = 0; z2 < chunkSize; z2++) {
							for (int y2 = 0; y2 < h.elevation(x2%128, z2%128)*y; y2++) {
								newChunk.map[x2][y2][z2] = 1;
							}
						}
					}
					chunks.add(newChunk);
					newChunk.rebuildChunk(chunkX, chunkY, chunkZ);
				}	
			}
		}
	}
	
}
