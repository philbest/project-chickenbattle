package Spelet;

import network.Player;
import Screens.Application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class Animation {
	public Array<AnimationPart> parts;
	public Array<KeyFrame> keyframes;
	public AnimationPart selectedPart;
	public Vector3 temp;
	public int currentKeyFrame;
	boolean playingAnimation;
	public float animStep;
	public float maxSteps;
	public float timer;
	public boolean repeatingAnimation;
	public Animation(String str, String str2) {
		playingAnimation = false;
		temp = new Vector3();
		parts = new Array<AnimationPart>();
		keyframes = new Array<KeyFrame>();
		String[] rows = str.split("\n");
		for(int i = 0; i < rows.length; i++) {
			parts.add(new AnimationPart(rows[i]));
		}
		System.out.println(str2);
		rows = str2.split("Frame:");
		for(int i = 1; i < rows.length; i++) {

			keyframes.add(new KeyFrame(rows[i], parts.size));
		}
		maxSteps = 30;
	}
	public Animation() {
		playingAnimation = false;
		temp = new Vector3();
		parts = new Array<AnimationPart>();
		keyframes = new Array<KeyFrame>();
		// Leggs
		parts.add(new AnimationPart(-1,0,0,0.5f,2,0.5f));
		parts.add(new AnimationPart(1,0,0,0.5f,2,0.5f));
		// Arms
		parts.add(new AnimationPart(-1.5f,2,0,0.5f,1.5f,0.5f));
		parts.add(new AnimationPart(1.5f,2,0,0.5f,1.5f,0.5f));
		// Body
		parts.add(new AnimationPart(0,2,0,2,2,1));
		// Neck
		parts.add(new AnimationPart(0,2.7f,0,0.5f,0.5f,0.5f));
		// Head
		parts.add(new AnimationPart(0,3,0,2,1,2));

		maxSteps = 30;
	}
	public void render(Application app, Player player) {
		for (int i = 0; i < parts.size; i++) {
			if (animStep == maxSteps) {
				currentKeyFrame++;
				if (currentKeyFrame >= keyframes.size-1) {
					currentKeyFrame = 0;
					animStep = 0;
				} else {
					animStep = 0;
				}
			}
			AnimationPart a = parts.get(i);
			a.x = keyframes.get(currentKeyFrame).positions[i].x+(keyframes.get(currentKeyFrame+1).positions[i].x-keyframes.get(currentKeyFrame).positions[i].x)*animStep/maxSteps+player.posX;
			a.y = keyframes.get(currentKeyFrame).positions[i].y+(keyframes.get(currentKeyFrame+1).positions[i].y-keyframes.get(currentKeyFrame).positions[i].y)*animStep/maxSteps+player.posY;
			a.z = keyframes.get(currentKeyFrame).positions[i].z+(keyframes.get(currentKeyFrame+1).positions[i].z-keyframes.get(currentKeyFrame).positions[i].z)*animStep/maxSteps+player.posZ;
			a.rotationX = keyframes.get(currentKeyFrame).rotationX[i] + (keyframes.get(currentKeyFrame+1).rotationX[i]-keyframes.get(currentKeyFrame).rotationX[i])*animStep/maxSteps;
			a.rotationZ = keyframes.get(currentKeyFrame).rotationZ[i] + (keyframes.get(currentKeyFrame+1).rotationZ[i]-keyframes.get(currentKeyFrame).rotationZ[i])*animStep/maxSteps;
			a.updateModelMatrix();
			a.render(app, player);
			timer += Gdx.graphics.getDeltaTime()*1000;
			if (timer > 50) {
				animStep++;	
				timer = 0;
			}

		}
	}
}
