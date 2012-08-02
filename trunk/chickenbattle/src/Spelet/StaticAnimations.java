package Spelet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class StaticAnimations {
	public static Animation walk,sitdown,standup;
	public static BoundingBox walkBox,sitBox,standBox;


	public static void initiate() {
		String s1 = Gdx.files.internal("data/animations/steven6.cpart").readString();
		String s2 = Gdx.files.internal("data/animations/steven6.ckey").readString();
		String s3 = Gdx.files.internal("data/animations/down.cpart").readString();
		String s4 = Gdx.files.internal("data/animations/down.ckey").readString();
		String s5 = Gdx.files.internal("data/animations/up.cpart").readString();
		String s6 = Gdx.files.internal("data/animations/up.ckey").readString();
		walk = new Animation(s1,s2);
		for (AnimationPart ap : walk.parts) {
			if (walkBox == null) {
				walkBox = new BoundingBox(ap.box);
			} else {
				walkBox.ext(ap.box);
			}
		}
		sitdown = new Animation(s3,s4);
		for (AnimationPart ap : sitdown.parts) {
			if (sitBox == null) {
				sitBox = new BoundingBox(ap.box);
			} else {
				sitBox.ext(ap.box);
			}
		}
		standup = new Animation(s5,s6);
		for (AnimationPart ap : standup.parts) {
			if (standBox == null) {
				standBox = new BoundingBox(ap.box);
			} else {
				standBox.ext(ap.box);
			}
		}
	}
}
