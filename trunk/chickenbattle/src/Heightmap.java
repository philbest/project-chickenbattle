public class Heightmap {

	public final int width;
	public final int height;

	public final float[] elevations;

	public final boolean flipY;

	public Heightmap(int width, int height, float[] elevations, boolean flipY) {
		this.width = width;
		this.height = height;
		this.elevations = elevations;
		this.flipY = flipY;

		if (width * height != elevations.length) {
			throw new IllegalArgumentException("Height map dimension mismatch, width * height (" + (width * height) + ") != data.length (" + elevations.length + ")");
		}
	}

	public int index(int x, int y) {
		if (flipY) {
			y = height - 1 - y;
		}
		return y * width + x;
	}

	// [0f, 1f]
	public float elevation(int x, int y) {
		return elevations[index(x, y)];
	}

	@Override
	public String toString() {
		return "[HeightMap: " + elevations.length + " data points, " + width + "x" + height + "]";
	}

}