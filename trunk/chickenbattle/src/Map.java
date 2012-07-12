
public class Map {
	Cube[][][] map;
	public static final int x = 10;
	public static final int y = 3;
	public static final int z = 10;
	public Map() {
		map = new Cube[x][y][z];
		
		for (int x2 = 0; x2 < x; x2++) {
			for (int z2 = 0; z2 < z; z2++) {
				map[x2][0][z2] = new Cube();
			}
		}
		map[0][1][0] = new Cube();
		map[0][1][1] = new Cube();
		map[0][1][2] = new Cube();
		
		map[1][1][1] = new Cube();
		
		map[2][1][0] = new Cube();
		map[2][1][1] = new Cube();
		map[2][1][2] = new Cube();
		
		map[4][1][0] = new Cube();
		map[4][1][1] = new Cube();
		map[4][1][2] = new Cube();
	}
}
