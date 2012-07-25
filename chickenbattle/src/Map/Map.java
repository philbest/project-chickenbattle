package Map;


import Spelet.Heightmap;
import Spelet.HeightmapUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;


public class Map {
	public static final int chunkSize = 32;
	public static final int x = chunkSize*1; // Must be multiple of chunkSize;
	public static final int y = chunkSize*1; // Must be multiple of chunkSize;
	public static final int z = chunkSize*1; // Must be multiple of chunkSize;
	public static Heightmap heightmap;
	public Array<Chunk> chunks;
	public Array<Chunk> chunksToRebuild;
	public Map() {
		heightmap = HeightmapUtils.load(Gdx.files.internal("data/noise2.png"));
		chunks = new Array<Chunk>();
		chunksToRebuild = new Array<Chunk>();
		buildChunks();
	}
	public void buildChunks() {
		for (int chunkX = 0; chunkX < x/chunkSize; chunkX++) {
			for (int chunkY = 0; chunkY < y/chunkSize; chunkY++) {
				for (int chunkZ = 0; chunkZ < z/chunkSize; chunkZ++) {
					Chunk newChunk = new Chunk(chunkX, chunkY, chunkZ);
					chunks.add(newChunk);
					chunksToRebuild.add(newChunk);
				}	
			}
		}
		for (int i = 0; i < chunksToRebuild.size; i++) {
			chunksToRebuild.get(i).rebuildChunk();
		}
	}

	public void update() {
//		int startValue = Math.min(chunksToRebuild.size-1, 1);
//		for (int i = startValue; i >= 0; i--) {
//			chunksToRebuild.get(i).rebuildChunk();
//			chunksToRebuild.removeIndex(i);
//		}
	}
}
