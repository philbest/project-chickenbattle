package Map;

public class Voxel {
	public static final int nothing = 0;
	public static final int grass = 1;
	public static final int rock = 2;
	
	public static final int DEFAULT_DURABILITY = 2;
	
	public int id;
	public int durability;
	
	public Voxel(int i) {
		id = i;
		durability = DEFAULT_DURABILITY;
	}
}
