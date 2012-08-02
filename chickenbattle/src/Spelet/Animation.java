package Spelet;

import network.Player;
import Screens.Application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
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
	public Matrix4 startPosMatrix;
	public Animation(String str, String str2) {
		startPosMatrix = new Matrix4();
		playingAnimation = false;
		temp = new Vector3();
		parts = new Array<AnimationPart>();
		keyframes = new Array<KeyFrame>();
		String[] rows = str.split("\n");
		for(int i = 0; i < rows.length; i++) {
			parts.add(new AnimationPart(rows[i]));
		}
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
			a.x = keyframes.get(currentKeyFrame).positions[i].x+(keyframes.get(currentKeyFrame+1).positions[i].x-keyframes.get(currentKeyFrame).positions[i].x)*animStep/maxSteps;
			a.y = keyframes.get(currentKeyFrame).positions[i].y+(keyframes.get(currentKeyFrame+1).positions[i].y-keyframes.get(currentKeyFrame).positions[i].y)*animStep/maxSteps;
			a.z = keyframes.get(currentKeyFrame).positions[i].z+(keyframes.get(currentKeyFrame+1).positions[i].z-keyframes.get(currentKeyFrame).positions[i].z)*animStep/maxSteps;
			a.rotationX = keyframes.get(currentKeyFrame).rotationX[i] + (keyframes.get(currentKeyFrame+1).rotationX[i]-keyframes.get(currentKeyFrame).rotationX[i])*animStep/maxSteps;
			a.rotationZ = keyframes.get(currentKeyFrame).rotationZ[i] + (keyframes.get(currentKeyFrame+1).rotationZ[i]-keyframes.get(currentKeyFrame).rotationZ[i])*animStep/maxSteps;
			startPosMatrix.setToTranslation(player.posX,player.posY,player.posZ);
			float angle;
			// x = dirz;
			// y = dirx;
			//Vector3 temp = new Vector3(player.dirZ,player.dirX,0);
			Vector3 temp = new Vector3(player.dirX,player.dirZ,0);
			temp.nor();
			double radians = MathUtils.atan2(temp.x,temp.y);
			startPosMatrix.rotate(0,1,0,(float)(MathUtils.radiansToDegrees*radians)+180);
			a.updateModelMatrix(startPosMatrix);
			a.render(app, player);
			timer += Gdx.graphics.getDeltaTime()*1000;
			if (timer > 50) {
				animStep++;	
				timer = 0;
			}

		}
	}
}
