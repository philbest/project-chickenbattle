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
	int[][][] map;
	public static final int chunkSize = 32;
	public static final int x = chunkSize*2; // Must be multiple of chunkSize;
	public static final int y = chunkSize; // Must be multiple of chunkSize;
	public static final int z = chunkSize*2; // Must be multiple of chunkSize;
	//Chunk[][][] chunks;
	Array<Chunk> chunks;
	public long maxTime = 0;
	Vector3 above = new Vector3();
	Vector3 below = new Vector3();
	Vector3 behind = new Vector3();
	Vector3 front = new Vector3();
	Vector3 right = new Vector3();
	Vector3 left = new Vector3();
	Matrix4 calcMat;
	public Map() {
		calcMat = new Matrix4();
		Heightmap h = HeightmapUtils.load(Gdx.files.internal("data/noise2.png"));
		map = new int[x][y][z];
		//chunks = new Chunk[x/chunkSize][y/chunkSize][z/chunkSize];
		chunks = new Array<Chunk>();
		for (int x2 = 0; x2 < x; x2++) {
			for (int z2 = 0; z2 < z; z2++) {
//				for (int y2 = 0; y2 < 2; y2++) {
				for (int y2 = 0; y2 < h.elevation(x2, z2)*y; y2++) {
					map[x2][y2][z2] = 1;
				}
			}
		}
		buildChunks();
		System.out.println("max time = " + maxTime);
	}
	public void buildChunks() {
		for (int chunkX = 0; chunkX < x/chunkSize; chunkX++) {
			for (int chunkY = 0; chunkY < y/chunkSize; chunkY++) {
				for (int chunkZ = 0; chunkZ < z/chunkSize; chunkZ++) {
					System.out.println("Building chunk: " + chunkX + " " + chunkY + " " + chunkZ);
					chunks.add(new Chunk(chunkX,chunkY,chunkZ));
					rebuildChunk(chunks.get(chunks.size-1),chunkX, chunkY, chunkZ);
				}	
			}
		}
	}
	public void rebuildChunk(Chunk c, int chunkX, int chunkY, int chunkZ) {
		long time = TimeUtils.millis();
		if (c.chunkMesh != null) {
			c.chunkMesh.dispose();
		}
		FloatArray fa = new FloatArray();
		for (int x = 0; x < chunkSize; x++) {
			for (int y = 0; y < chunkSize; y++) {
				for (int z = 0; z < chunkSize; z++) { 
					if (map[x+chunkX*chunkSize][y+chunkY*chunkSize][z+chunkZ*chunkSize] == 1) {
						above.set(x+chunkX*chunkSize,y+chunkY*chunkSize+1,z+chunkZ*chunkSize);
						below.set(x+chunkX*chunkSize,y+chunkY*chunkSize-1,z+chunkZ*chunkSize);
						behind.set(x+chunkX*chunkSize,y+chunkY*chunkSize,z+chunkZ*chunkSize-1);
						front.set(x+chunkX*chunkSize,y+chunkY*chunkSize,z+chunkZ*chunkSize+1);
						right.set(x+chunkX*chunkSize+1,y+chunkY*chunkSize,z+chunkZ*chunkSize);
						left.set(x+chunkX*chunkSize-1,y+chunkY*chunkSize,z+chunkZ*chunkSize);
						if (above.y >= Map.y) {
							addTopFace(fa, x, y, z);
						} else if (map[(int) above.x][(int) above.y][(int) above.z] == 0) {
							addTopFace(fa, x, y, z);
						}
						if (below.y < 0) {
							addBotFace(fa, x, y, z);
						} else if (map[(int) below.x][(int) below.y][(int) below.z] == 0) {
							addBotFace(fa, x, y, z);
						}
						if (behind.z < 0) {
							addBackFace(fa, x, y, z);
						} else if (map[(int) behind.x][(int) behind.y][(int) behind.z] == 0) {
							addBackFace(fa, x, y, z);
						}
						if (front.z >= Map.z) {
							addFrontFace(fa, x, y, z);
						} else if (map[(int) front.x][(int) front.y][(int) front.z] == 0) {
							addFrontFace(fa, x, y, z);
						}
						if (right.x >= Map.x) {
							addRightFace(fa, x, y, z);
						} else if (map[(int) right.x][(int) right.y][(int) right.z] == 0) {
							addRightFace(fa, x, y, z);
						}
						if (left.x < 0) {
							addLeftFace(fa, x, y, z);
						} else if (map[(int) left.x][(int) left.y][(int) left.z] == 0) {
							System.out.println("ADDED");
							addLeftFace(fa, x, y, z);
						}
					}
				}
			}
		}


		if (fa.size > 0) {
			c.chunkMesh = new Mesh(true, fa.size, 0,
					new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ), 
					new VertexAttribute(Usage.Normal,3,ShaderProgram.NORMAL_ATTRIBUTE),
					new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ));
			c.chunkMesh.setVertices(fa.items);
			c.chunkMesh.calculateBoundingBox(c.bounds);
			calcMat.setToTranslation(chunkX*chunkSize, chunkY*chunkSize, chunkZ*chunkSize);
			c.bounds.mul(calcMat);
		}
		time = TimeUtils.millis()-time;
		maxTime = Math.max(time, maxTime);
		System.out.println("Time was: " + time + " and maxtime is " + maxTime);
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
