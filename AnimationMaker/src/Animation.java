import com.badlogic.gdx.utils.Array;

public class Animation {
	public Array<AnimationPart> parts;
	public Array<KeyFrame> keyframes;
	public Animation() {
		parts = new Array<AnimationPart>();
		keyframes = new Array<KeyFrame>();
		parts.add(new AnimationPart());
	}
	public void render(Application app) {
		for (AnimationPart a : parts) {
			a.render(app);
		}
	}
}
