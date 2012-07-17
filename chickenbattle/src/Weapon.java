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
	public Weapon(int w) {
		weaponID = w;
		if (weaponID == gun) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
			wpn = new Sprite(new Texture(Gdx.files.internal("data/gun.png")));
		} else if (weaponID == ak) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
			wpn = new Sprite(new Texture(Gdx.files.internal("data/ak.png")));
			wpn.setSize(256,256);
			offset = -120;
		} else if (weaponID == block) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
			wpn = new Sprite(new Texture(Gdx.files.internal("data/block.png")));
		}
	}
	public void render(SpriteBatch sb) {
		wpn.setPosition(Gdx.graphics.getWidth()-wpn.getWidth(), 0+offset);
		wpn.draw(sb);
		crosshair.setPosition(Gdx.graphics.getWidth()/2-crosshair.getWidth()/2,Gdx.graphics.getHeight()/2-crosshair.getHeight()/2);
		crosshair.draw(sb);
	}
}
