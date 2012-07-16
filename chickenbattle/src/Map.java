import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;


public class Map {
	public static final int chunkSize = 32;
	public static final int x = chunkSize*6; // Must be multiple of chunkSize;
	public static final int y = chunkSize; // Must be multiple of chunkSize;
	public static final int z = chunkSize*6; // Must be multiple of chunkSize;
	public static Heightmap heightmap;
	public static int loadingMaps = 0;
	Array<Chunk> chunks;
	public Map() {
		heightmap = HeightmapUtils.load(Gdx.files.internal("data/noise2.png"));
		chunks = new Array<Chunk>();

		buildChunks();
	}
	public void buildChunks() {
		for (int chunkX = 0; chunkX < x/chunkSize; chunkX++) {
			for (int chunkY = 0; chunkY < y/chunkSize; chunkY++) {
				for (int chunkZ = 0; chunkZ < z/chunkSize; chunkZ++) {
					Chunk newChunk = new Chunk(chunkX, chunkY, chunkZ);
					chunks.add(newChunk);
					newChunk.rebuildChunk(newChunk.x,newChunk.y,newChunk.z);
				}	
			}
		}
	}
}
