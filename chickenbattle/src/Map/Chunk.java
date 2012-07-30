package Map;

import Spelet.StaticVariables;
import Spelet.VertexAttributes;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.FloatArray;


public class Chunk {
	public Voxel[][][] map;
	public Mesh chunkMesh;
	public BoundingBox bounds;
	public int x, y, z;
	public int hieght;
	float distance;	
	float xf, yf, zf;

	public Chunk(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		hieght=32;
		map = new Voxel[Map.chunkSize][Map.chunkSize][Map.chunkSize];
		distance = 0;
		bounds = new BoundingBox();

		float caves, center_falloff, plateau_falloff, density;

		for (int x2 = 0; x2 < Map.chunkSize; x2++) {
			for (int z2 = 0; z2 < Map.chunkSize; z2++) {
				for (int y2 = 0; y2 < Map.chunkSize; y2++) {
					xf=(float)(x2+ (x*Map.chunkSize))/((float)Map.chunkSize*4);
					yf=(float)(y2+ (y*Map.chunkSize))/((float)Map.chunkSize*1.5f);
					zf=(float)(z2+ (z*Map.chunkSize))/((float)Map.chunkSize*4);

					if(yf <= 0.70){
						plateau_falloff = 1.0f;
					} else if(0.70f < yf && yf < 0.9) {
						plateau_falloff = 1.0f-(yf-0.7f)*10.0f;
					} else {
						plateau_falloff = 0.0f;
					}
					center_falloff = 0.1f/(
							(float)Math.pow((xf-0.5f)*1.5f,2)+
							(float)Math.pow((yf)*0.8f, 2) +
							(float)Math.pow((zf-0.5)*1.5f, 2)
					);
					caves = (float)Math.pow(simplex_noise(1, xf*5, yf*5, zf*5), 3);
					density = (simplex_noise(5, xf, yf*0.5f, zf) * center_falloff *plateau_falloff);
					density *= (float)Math.pow(noise((xf+1)*3.0f, (yf+1)*3.0f, (zf+1)*3.0f)+0.4f, 1.8f);
					if(caves<1.5f){
						density = 0;
					}
					if(density > 3.1f)
						map[x2][y2][z2] = new Voxel(Voxel.grass);
					else
						map[x2][y2][z2] = new Voxel(Voxel.nothing);
				}
			}
		}
		for (int x2 = 0; x2 < Map.chunkSize; x2++) {
			for (int z2 = 0; z2 < Map.chunkSize; z2++) {
				for (int y2 = 0; y2 < Map.chunkSize-1; y2++) {
					if (map[x2][y2+1][z2].id == Voxel.grass) {
						map[x2][y2][z2] = new Voxel(Voxel.rock);
					}
				}
			}
		}
	}

	public void addAcircle(int xCenter, int yCenter, int radius) {
		int x, y, r2;
		for(int z = 10; z >= 0; z-- ){
			r2 = radius * radius;
			for (x = -radius; x <= radius; x++) {
				y = (int) (Math.sqrt(r2 - x*x) + 0.5);
				map[xCenter+x][z][yCenter+ y] = new Voxel(Voxel.grass);
				map[xCenter+x][z][yCenter-y] = new Voxel(Voxel.grass);
			}
			radius--;
		}
	}

	public void addAsquare(int xstart, int ystart, int length) {
		int x, y, r2;
		for(int z = 1; z >= 0; z-- ){
			for (x = xstart; x <= length; x++) {
				for (y = ystart; y <= length; y++) {
					map[x][z][y] = new Voxel(Voxel.grass);
				}
			}
		}
	}

	public int compareTo(Object arg0) {
		return (int) (distance-((Chunk) arg0).distance);
	}
	public void rebuildChunk() {
		int chunkX = x;
		int chunkY = y;
		int chunkZ = z;
		if (chunkMesh != null) {
			chunkMesh.dispose();
		}
		FloatArray fa = new FloatArray();
		for (int x = 0; x < Map.chunkSize; x++) {
			for (int y = 0; y < Map.chunkSize; y++) {
				for (int z = 0; z < Map.chunkSize; z++) { 
					if (map[x][y][z].id != Voxel.nothing) {
						if (y+1 >= Map.chunkSize) {
							addTopFace(fa, x, y, z,map[x][y][z].id);
						} else if (map[x][y+1][z].id == Voxel.nothing) {
							addTopFace(fa, x, y, z,map[x][y][z].id);
						}
						if (y-1 < 0) {
							addBotFace(fa, x, y, z,map[x][y][z].id);
						} else if (map[x][y-1][z].id == Voxel.nothing) {
							addBotFace(fa, x, y, z,map[x][y][z].id);
						}
						if (z-1 < 0) {
							addBackFace(fa, x, y, z,map[x][y][z].id);
						} else if (map[x][y][z-1].id == Voxel.nothing) {
							addBackFace(fa, x, y, z,map[x][y][z].id);
						}
						if (z+1 >= Map.chunkSize) {
							addFrontFace(fa, x, y, z,map[x][y][z].id);
						} else if (map[x][y][z+1].id == Voxel.nothing) {
							addFrontFace(fa, x, y, z,map[x][y][z].id);
						}
						if (x+1 >= Map.chunkSize) {
							addRightFace(fa, x, y, z,map[x][y][z].id);
						} else if (map[x+1][y][z].id == Voxel.nothing) {
							addRightFace(fa, x, y, z,map[x][y][z].id);
						}
						if (x-1 < 0) {
							addLeftFace(fa, x, y, z,map[x][y][z].id);
						} else if (map[x-1][y][z].id == Voxel.nothing) {
							addLeftFace(fa, x, y, z,map[x][y][z].id);
						}
					}
				}
			}
		}


		if (fa.size > 0) {
			chunkMesh = new Mesh(true, fa.size, 0,
					VertexAttributes.position, 
					VertexAttributes.normal,
					VertexAttributes.textureCoords,
					VertexAttributes.occlusion);
			chunkMesh.setVertices(fa.items);
			chunkMesh.calculateBoundingBox(bounds);
			Matrix4 calcMat = StaticVariables.acquireCalcMat();
			calcMat.setToTranslation(chunkX*Map.chunkSize, chunkY*Map.chunkSize, chunkZ*Map.chunkSize);
			bounds.mul(calcMat);
			StaticVariables.releaseSema();
		}
	}
	public void addTopFace(FloatArray fa, int x, int y, int z, int type) {
		float occlusion = 0;
		if (x-1 < 0 || y+1 >= Map.chunkSize || map[x-1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (y+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x][y+1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(2f/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (x+1 >= Map.chunkSize || y+1 >= Map.chunkSize || map[x+1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (y+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x][y+1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(2f/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (x+1 >= Map.chunkSize || y+1 >= Map.chunkSize || map[x+1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (y+1 >= Map.chunkSize || z-1 < 0 || map[x][y+1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(1f/3f);
		fa.add(occlusion); // Occlusionvalue


		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(1f/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y+1 >= Map.chunkSize || z-1 < 0 || map[x][y+1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (y+1 >= Map.chunkSize || x-1 <0 || map[x-1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1f/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (x-1 < 0 || y+1 >= Map.chunkSize || map[x-1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (y+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x][y+1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(2f/3f);
		fa.add(occlusion); // Occlusionvalue
	}
	public void addBotFace(FloatArray fa, int x, int y, int z, int type) {
		float occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || y-1 < 0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || y-1 < 0 || map[x+1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(1);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y-1 < 0 || map[x][y-1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || y-1 < 0 || map[x+1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(2f/3f);
		fa.add(occlusion); // Occlusionvalue

		fa.add(1+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(2f/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y-1 < 0 || map[x][y-1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || y-1 < 0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(2f/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || y-1 < 0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1);
		fa.add(occlusion); // Occlusionvalue

	}

	public void addLeftFace(FloatArray fa, int x, int y, int z, int type) {
		float occlusion = 0;
		if (y-1 < 0 || x-1 <0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z-1 < 0 || map[x-1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1f/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y-1 < 0 || x-1 <0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z+1 >= Map.chunkSize || map[x-1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(1f/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y+1 >= Map.chunkSize || x-1 <0 || map[x-1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z+1 >= Map.chunkSize || map[x-1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(1+y);
		fa.add(1+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(0);
		fa.add(occlusion); // Occlusionvalue

		fa.add(0+x);
		fa.add(1+y);
		fa.add(1+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(0);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y+1 >= Map.chunkSize || x-1 <0 || map[x-1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z-1 < 0 || map[x-1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(0);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y-1 < 0 || x-1 <0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z-1 < 0 || map[x-1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1f/3f);
		fa.add(occlusion); // Occlusionvalue

	}

	public void addRightFace(FloatArray fa, int x, int y, int z, int type) {
		float occlusion = 0;
		if (y-1 < 0 || x+1 >= Map.chunkSize || map[x+1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x+1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y-1 < 0 || x+1 >= Map.chunkSize || map[x+1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z-1 <0 || map[x+1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(0+y); // y1
		fa.add(0+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(1/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y+1 >= Map.chunkSize || x+1 >= Map.chunkSize || map[x+1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z-1 <0 || map[x+1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y+1 >= Map.chunkSize || x+1 >= Map.chunkSize || map[x+1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x+1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y-1 < 0 || x+1 >= Map.chunkSize || map[x+1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x+1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1/3f);
		fa.add(occlusion); // Occlusionvalue

	}
	public void addFrontFace(FloatArray fa, int x, int y, int z, int type) {
		float occlusion = 0;
		if (z+1 >= Map.chunkSize || y-1 < 0 || map[x][y-1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 <0 || z+1 >= Map.chunkSize || map[x-1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1/3f); // v1
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y-1 < 0 || map[x][y-1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x+1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x2
		fa.add(0+y); // y2
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(1/3f); // v2
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y+1 >= Map.chunkSize || map[x][y+1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x+1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1f+x); // x3
		fa.add(1f+y); // y2
		fa.add(1f+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(0f); // v3
		fa.add(occlusion); // Occlusionvalue

		fa.add(1f+x); // x3
		fa.add(1f+y); // y2
		fa.add(1f+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(0f); // v3
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y+1 >= Map.chunkSize || map[x][y+1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 <0 || z+1 >= Map.chunkSize || map[x-1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x4
		fa.add(1+y); // y4
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(0f); // v4
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y-1 < 0 || map[x][y-1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 <0 || z+1 >= Map.chunkSize || map[x-1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1/3f); // v1
		fa.add(occlusion); // Occlusionvalue

	}
	public void addBackFace(FloatArray fa, int x, int y, int z, int type) {
		float occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z-1 < 0 || map[x+1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z-1 < 0 || map[x-1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(1/3f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z-1 < 0 || y+1 >= Map.chunkSize || map[x][y+1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z-1 < 0 || map[x-1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		fa.add(0+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(type/Voxel.maxTypes);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue


		occlusion = 0;
		if (z-1 < 0 || y+1 >= Map.chunkSize || map[x][y+1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z-1 < 0 || map[x+1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z-1 < 0 || map[x+1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add((type-1)/Voxel.maxTypes);
		fa.add(1/3f);
		fa.add(occlusion); // Occlusionvalue

	}

	public static float grad[][] = {
		{1.0f,1.0f,0.0f},{-1.0f,1.0f,0.0f},{1.0f,-1.0f,0.0f},{-1.0f,-1.0f,0.0f},
		{1.0f,0.0f,1.0f},{-1.0f,0.0f,1.0f},{1.0f,0.0f,-1.0f},{-1.0f,0.0f,-1.0f},
		{0.0f,1.0f,1.0f},{0.0f,-1.0f,1.0f},{0.0f,1.0f,-1.0f},{0.0f,-1.0f,-1.0f}
	};

	public static int perm[] = {151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180, 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};

	float dot(float x, float y, float z, float[] g){
		return x*g[0] + y*g[1] + z*g[2];
	}

	float noise(float xin, float yin, float zin){
		float F3, G3, t, X0, Y0, Z0, x0, y0, z0, s, x1, y1, z1, x2, y2, z2, x3, y3, z3, t0, t1, t2, t3, n0, n1, n2, n3;
		int i, j, k, ii, jj, kk, i1, j1, k1, i2, j2, k2, gi0, gi1, gi2, gi3;

		F3 = 1.0f/3.0f;
		s = (xin+yin+zin)*F3;
		i = (int)(xin+s);
		j = (int)(yin+s);
		k = (int)(zin+s);
		G3 = 1.0f/6.0f;
		t = (i+j+k)*G3;
		X0 = i-t;
		Y0 = j-t;
		Z0 = k-t;
		x0 = xin-X0;
		y0 = yin-Y0;
		z0 = zin-Z0;

		if(x0 >= y0){
			if(y0 >= z0){
				i1=1; j1=0; k1=0; i2=1; j2=1; k2=0;
			}
			else if(x0 >= z0){
				i1=1; j1=0; k1=0; i2=1; j2=0; k2=1;
			}
			else{
				i1=0; j1=0; k1=1; i2=1; j2=0; k2=1;
			}
		}
		else{
			if(y0 < z0){
				i1=0; j1=0; k1=1; i2=0; j2=1; k2=1;
			}
			else if(x0 < z0){ 
				i1=0; j1=1; k1=0; i2=0; j2=1; k2=1;
			}
			else{
				i1=0; j1=1; k1=0; i2=1; j2=1; k2=0;
			}
		}

		x1 = x0 - i1 + G3;
		y1 = y0 - j1 + G3;
		z1 = z0 - k1 + G3;
		x2 = x0 - i2 + 2.0f*G3;
		y2 = y0 - j2 + 2.0f*G3;
		z2 = z0 - k2 + 2.0f*G3;
		x3 = x0 - 1.0f + 3.0f*G3;
		y3 = y0 - 1.0f + 3.0f*G3;
		z3 = z0 - 1.0f + 3.0f*G3;

		ii = i & 255;
		jj = j & 255;
		kk = k & 255;

		gi0 = perm[ii+perm[jj+perm[kk]]] % 12;
		gi1 = perm[ii+i1+perm[jj+j1+perm[kk+k1]]] % 12;
		gi2 = perm[ii+i2+perm[jj+j2+perm[kk+k2]]] % 12;
		gi3 = perm[ii+1+perm[jj+1+perm[kk+1]]] % 12;

		t0 = 0.6f - x0*x0 - y0*y0 - z0*z0;
		if(t0<0){
			n0 = 0.0f;
		}
		else{
			t0 *= t0;
			n0 = t0 * t0 * dot(x0, y0, z0, grad[gi0]);
		}

		t1 = 0.6f - x1*x1 - y1*y1 - z1*z1;
		if(t1<0){
			n1 = 0.0f;
		}
		else{
			t1 *= t1;
			n1 = t1 * t1 * dot(x1, y1, z1, grad[gi1]);
		}

		t2 = 0.6f - x2*x2 - y2*y2 - z2*z2;
		if(t2<0){
			n2 = 0.0f;
		}
		else{
			t2 *= t2;
			n2 = t2 * t2 * dot(x2, y2, z2, grad[gi2]);
		}

		t3 = 0.6f - x3*x3 - y3*y3 - z3*z3;
		if(t3<0){
			n3 = 0.0f;
		}
		else{
			t3 *= t3;
			n3 = t3 * t3 * dot(x3, y3, z3, grad[gi3]);
		}

		return 16.0f*(n0 + n1 + n2 + n3)+1.0f;
	}

	private float simplex_noise(int octaves, float x, float y, float z){
		float value = 0.0f;
		int i;
		for(i=0; i<octaves; i++){
			value += noise(
					(float)(x*Math.pow(2, i)),
					(float)(y*Math.pow(2, i)),
					(float)(z*Math.pow(2, i))
			);
		}
		return value;
	}


}
