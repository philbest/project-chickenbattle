import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.TimeUtils;


public class Map {
	int[][][] map;
	public static final int chunkSize = 32;
	public static final int x = chunkSize*20; // Must be multiple of chunkSize;
	public static final int y = chunkSize; // Must be multiple of chunkSize;
	public static final int z = chunkSize*20; // Must be multiple of chunkSize;
	Mesh[][][] chunks;
	long maxTime = 0;
	public Map() {
		map = new int[x][y][z];
		chunks = new Mesh[x/16][y/16][z/16];
		for (int x2 = 0; x2 < x; x2++) {
			for (int z2 = 0; z2 < z; z2++) {
				map[x2][0][z2] = 1;
			}
		}
		map[0][1][0] = 1;
		map[0][1][1] = 1;
		map[0][1][2] = 1;

		map[1][1][1] = 1;

		map[2][1][0] = 1;
		map[2][1][1] = 1;
		map[2][1][2] = 1;

		map[4][1][0] = 1;
		map[4][1][1] = 1;
		map[4][1][2] = 1;
		buildChunks();
		System.out.println("max time = " + maxTime);
	}
	public void buildChunks() {
		for (int chunkX = 0; chunkX < x/chunkSize; chunkX++) {
			for (int chunkY = 0; chunkY < y/chunkSize; chunkY++) {
				for (int chunkZ = 0; chunkZ < z/chunkSize; chunkZ++) {
					System.out.println("Building chunk: " + chunkX + " " + chunkY + " " + chunkZ);
					rebuildChunk(chunkX, chunkY, chunkZ);
				}	
			}
		}
	}
	public void rebuildChunk(int chunkX, int chunkY, int chunkZ) {
		long time = TimeUtils.millis();
		if (chunks[chunkX][chunkY][chunkZ] != null) {
			chunks[chunkX][chunkY][chunkZ].dispose();
			System.out.println("Disposed");
		}
		FloatArray fa = new FloatArray();
		for (int x = 0; x < chunkSize; x++) {
			for (int y = 0; y < chunkSize; y++) {
				for (int z = 0; z < chunkSize; z++) {
					if (map[x+chunkX*chunkSize][y+chunkY*chunkSize][z+chunkZ*chunkSize] == 1) {
						Vector3 above = new Vector3(x+chunkX*chunkSize,y+chunkY*chunkSize+1,z+chunkZ*chunkSize);
						Vector3 below = new Vector3(x+chunkX*chunkSize,y+chunkY*chunkSize-1,z+chunkZ*chunkSize);
						Vector3 behind = new Vector3(x+chunkX*chunkSize,y+chunkY*chunkSize,z+chunkZ*chunkSize-1);
						Vector3 front = new Vector3(x+chunkX*chunkSize,y+chunkY*chunkSize,z+chunkZ*chunkSize+1);
						Vector3 right = new Vector3(x+chunkX*chunkSize+1,y+chunkY*chunkSize,z+chunkZ*chunkSize);
						Vector3 left = new Vector3(x+chunkX*chunkSize-1,y+chunkY*chunkSize,z+chunkZ*chunkSize);
						if (above.y >= y) {
							addTopFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						} else if (map[(int) above.x][(int) above.y][(int) above.z] == 0) {
							addTopFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						}
						if (below.y < 0) {
							addBotFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						} else if (map[(int) below.x][(int) below.y][(int) below.z] == 0) {
							addBotFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						}
						if (behind.z < 0) {
							addBackFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						} else if (map[(int) behind.x][(int) behind.y][(int) behind.z] == 0) {
							addBackFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						}
						if (front.z >= z) {
							addFrontFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						} else if (map[(int) front.x][(int) front.y][(int) front.z] == 0) {
							addFrontFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						}
						if (right.x >= x) {
							addRightFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						} else if (map[(int) right.x][(int) right.y][(int) right.z] == 0) {
							addRightFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						}
						if (left.x < 0) {
							addLeftFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						} else if (map[(int) left.x][(int) left.y][(int) left.z] == 0) {
							addLeftFace(fa, x+chunkX*chunkSize, y+chunkY*chunkSize, z+chunkZ*chunkSize);
						}
					}
				}
			}
		}
		
		
		chunks[chunkX][chunkY][chunkZ] = new Mesh(true, fa.size, 0,
				new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ), 
				new VertexAttribute(Usage.Normal,3,ShaderProgram.NORMAL_ATTRIBUTE),
				new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ));
		chunks[chunkX][chunkY][chunkZ].setVertices(fa.items);
		time = TimeUtils.millis()-time;
		maxTime = Math.max(time, maxTime);
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

		fa.add(1f+x); // x2
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
		fa.add(1f+y); // y4
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
