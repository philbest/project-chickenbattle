import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.collision.BoundingBox;


public class Chunk implements Comparable{
	Mesh chunkMesh;
	BoundingBox bounds;
	int x, y, z;
	float distance;
	public Chunk(int x, int y, int z) {
		distance = 0;
		this.x = x;
		this.y = y;
		this.z = z;
		bounds = new BoundingBox();
	}
	public int compareTo(Object arg0) {
		return (int) (distance-((Chunk) arg0).distance);
	}

}
