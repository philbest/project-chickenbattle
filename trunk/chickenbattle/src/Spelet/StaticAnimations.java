package Spelet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class StaticAnimations {
	public static Animation walk;
	public static BoundingBox walkBox;
	public static void initiate() {
		String s1 = Gdx.files.internal("data/steven6.cpart").readString();
		String s2 = Gdx.files.internal("data/steven6.ckey").readString();
		walk = new Animation(s1,s2);
		for (AnimationPart ap : walk.parts) {
			if (walkBox == null) {
				walkBox = new BoundingBox(ap.box);
			} else {
				walkBox.ext(ap.box);
			}
		}
	}
}
