package Spelet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class StaticAnimations {
	public static Animation walk;
	public static BoundingBox walkBox;
	public static void initiate() {
		String s1 = Gdx.files.internal("data/walk.cpart").readString();
		String s2 = Gdx.files.internal("data/walk.ckey").readString();
		walk = new Animation(s1,s2);
		walkBox = new BoundingBox(new Vector3(-1.6f,-0.7f,-1.1f),new Vector3(1.6f,4.6f,1.1f));
	}
}
