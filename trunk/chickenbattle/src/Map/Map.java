package Map;

import com.badlogic.gdx.utils.Array;


public class Map {
	public static final int chunkSize = 32;
	public static final int x = chunkSize*4; // Must be multiple of chunkSize;
	public static final int y = chunkSize*1; // Must be multiple of chunkSize;
	public static final int z = chunkSize*4; // Must be multiple of chunkSize;
	public Array<Chunk> chunks;
	public Array<Chunk> chunksToRebuild;

	public Map(Boolean x) {
		chunks = new Array<Chunk>();
		chunksToRebuild = new Array<Chunk>();
		buildChunks(x);
		System.out.println("LOL");
	}
	public void buildChunks(boolean rebuild) {
		for (int chunkX = 0; chunkX < x/chunkSize; chunkX++) {
			for (int chunkY = 0; chunkY < y/chunkSize; chunkY++) {
				for (int chunkZ = 0; chunkZ < z/chunkSize; chunkZ++) {
					Chunk newChunk = new Chunk(chunkX, chunkY, chunkZ);
					chunks.add(newChunk);
					chunksToRebuild.add(newChunk);
				}	
			}
		}

		if(rebuild){
			for (int i = 0; i < chunksToRebuild.size; i++) {
				chunksToRebuild.get(i).rebuildChunk();
			}
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
