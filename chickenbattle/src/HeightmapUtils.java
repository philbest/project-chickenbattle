
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class HeightmapUtils {

	public static Heightmap load(FileHandle file) {
		Pixmap pixmap = new Pixmap(file);
		return load(pixmap);
	}

	public static Heightmap load(Pixmap pixmap) {
		int width = pixmap.getWidth();
		int height = pixmap.getHeight();

		float[] data = new float[width * height];

		Color color = new Color();
		int i = 0;
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < height; x++) {
				int pixel = pixmap.getPixel(x, y);
				Color.rgba8888ToColor(color, pixel);
				data[i] = color.r;
				i++;
			}	
		}

		boolean flipY = true;
		return new Heightmap(width, height, data, flipY);
	}

}