import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Weapon {
	public int weaponID = 0;
	public static final int gun = 0;
	public static final int ak = 1;
	public static final int block = 2;
	public Sprite wpn;
	public Sprite crosshair;
	int offset;
	public int maxBullets;
	public int currentBullets;
	public int magBullets;
	public int magSize;
	public int cooldown;
	public int currentCooldown;
	public long lastShot;
	public Weapon(int w) {
		weaponID = w;
		if (weaponID == gun) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
			wpn = new Sprite(new Texture(Gdx.files.internal("data/gun.png")));
			maxBullets = 28;
			magSize = 8;
			currentBullets = 28;
			magBullets = 8;
			cooldown = 500;
		} else if (weaponID == ak) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
			wpn = new Sprite(new Texture(Gdx.files.internal("data/ak.png")));
			wpn.setSize(256,256);
			offset = -120;
			maxBullets = 90;
			currentBullets = 90;
			magBullets = 30;
			magSize = 30;
			cooldown = 200;
		} else if (weaponID == block) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
			wpn = new Sprite(new Texture(Gdx.files.internal("data/block.png")));
			maxBullets = 30;
			currentBullets = 30;
			magBullets = 30;
			magSize = 30;
			cooldown = 500;
		}
	}
	public void render(SpriteBatch sb) {
		wpn.setPosition(Gdx.graphics.getWidth()-wpn.getWidth(), 0+offset);
		wpn.draw(sb);
		crosshair.setPosition(Gdx.graphics.getWidth()/2-crosshair.getWidth()/2,Gdx.graphics.getHeight()/2-crosshair.getHeight()/2);
		crosshair.draw(sb);
	}
	public void update() {
		currentCooldown -= Gdx.graphics.getDeltaTime()*1000;
	}
	public boolean shoot() {
		if (magBullets > 0 && currentCooldown <= 0) {
			magBullets--;
			currentCooldown = cooldown;
			return true;
		}
		return false;
	}
	public void reload() {
		if (magBullets < magSize) {
			if (magBullets+currentBullets>=magSize) {
				currentBullets-=magSize-magBullets;
				magBullets = magSize;
			} else {
				magBullets += currentBullets;
				currentBullets = 0;
			}
		}
	}
}
