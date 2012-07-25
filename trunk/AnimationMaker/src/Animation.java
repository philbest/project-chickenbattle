import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
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
			
			keyframes.add(new KeyFrame(rows[i], 5));
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
	public void render(Application app) {
		if (playingAnimation) {
			app.clickedKeyFrame = currentKeyFrame;
			for (int i = 0; i < parts.size; i++) {
				if (animStep == maxSteps) {
					currentKeyFrame++;
					if (currentKeyFrame >= keyframes.size-1) {
						if (repeatingAnimation) {
							currentKeyFrame = 0;
							animStep = 0;
						} else{
							stopAnimation(app);
							animStep = 0;
						}
						render(app);
						return;
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
				a.updateModelMatrix();
				a.render(app, false);
				timer += Gdx.graphics.getDeltaTime()*1000;
				if (timer > 50) {
					animStep++;	
					timer = 0;
				}

			}
		} else {
			for (AnimationPart a : parts) {
				boolean hit = selectedPart == a;
				a.render(app, hit);
			}			
		}
	}
	public boolean clicked(float x, float y, float z) {
		temp.set(x,y,z);
		for (AnimationPart a : parts) {
			if (a.contains(temp)) {
				selectedPart = a;
				return true;
			}
		}
		return false;
	}
	public void addKeyFrame() {
		KeyFrame k = new KeyFrame(parts.size);
		for (int i = 0; i < parts.size; i++) {
			AnimationPart p = parts.get(i);
			k.positions[i] = new Vector3(p.x,p.y,p.z);
			k.rotationX[i] = p.rotationX;
			k.rotationZ[i] = p.rotationZ;
		}
		keyframes.add(k);
	}
	public void playAnimation(Application app, boolean repeat) {
		animStep = 0;
		for (int i = 0; i < parts.size; i++) {
			AnimationPart p = parts.get(i);
			p.x = keyframes.get(0).positions[i].x;
			p.y = keyframes.get(0).positions[i].y;
			p.z = keyframes.get(0).positions[i].z;
			p.rotationX = keyframes.get(0).rotationX[i];
			p.rotationZ = keyframes.get(0).rotationZ[i];
			p.updateModelMatrix();
		}		
		currentKeyFrame = 0;
		playingAnimation = true;
		repeatingAnimation = repeat;
	}
	public void stopAnimation(Application app) {
		for (int i = 0; i < parts.size; i++) {
			AnimationPart p = parts.get(i);
			p.x = keyframes.get(0).positions[i].x;
			p.y = keyframes.get(0).positions[i].y;
			p.z = keyframes.get(0).positions[i].z;
			p.rotationX = keyframes.get(0).rotationX[i];
			p.rotationZ = keyframes.get(0).rotationZ[i];
			p.updateModelMatrix();
		}
		playingAnimation = false;
		app.clickedKeyFrame = 0;
	}
	public void setKeyFrame(KeyFrame kf) {
		for (int i = 0; i < parts.size; i++) {
			AnimationPart p = parts.get(i);
			p.x = kf.positions[i].x;
			p.y = kf.positions[i].y;
			p.z = kf.positions[i].z;
			p.rotationX = kf.rotationX[i];
			p.rotationZ = kf.rotationZ[i];
			p.updateModelMatrix();
		}		
	}
	public String toString() {
		String ret = "";
		for (int i = 0; i < parts.size; i++) {
			ret += parts.get(i).toString(); 
			if (i < parts.size-1)
				ret+="\n";
		}

		return ret;
	}
	public String toString2() {
		String ret = "";
		for (int i = 0; i < keyframes.size; i++) {
			ret += "Frame:"+keyframes.get(i).toString();
			if (i < keyframes.size-1)
				ret+="\n";
		}
		return ret;
	}
	public void fromString(String str) {

	}
}
