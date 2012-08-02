package Spelet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class ExplosionManager {
	public Array<Explosion> explosions;
	public ExplosionManager() {
		explosions = new Array<Explosion>();
	}
	public void addExplotion() {
		//Explosion(float radius, float startTime, float ttl, int particles, float size) {
		explosions.add(new Explosion(1,TimeUtils.millis(),1000,100,0.2f));
	}
	public void update() {
		for (int i = explosions.size; i >= 0; i--) {
			if (explosions.get(i).isDead()) {
				explosions.removeIndex(i);
			}
		}
	}
	public void render() {
		
	}
}
