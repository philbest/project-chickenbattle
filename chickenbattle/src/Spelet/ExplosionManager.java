package Spelet;

import com.badlogic.gdx.utils.Array;

public class ExplosionManager {
	public Array<Explosion> explosions;
	public ExplosionManager() {
		explosions = new Array<Explosion>();
	}
	public void addExplotion(float x, float y, float z) {
		explosions.add(new Explosion(x,y,z,1,10000,100,0.2f));
	}
	public void update() {
		for (int i = explosions.size-1; i >= 0; i--) {
			explosions.get(i).update();
			if (explosions.get(i).isDead()) {
				explosions.removeIndex(i);
			}
		}
	}
}
