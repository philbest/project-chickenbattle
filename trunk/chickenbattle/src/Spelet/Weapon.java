package Spelet;
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
	public Sprite[] gunSpr;
	public Sprite[] akSpr;
	public Sprite[] blockSpr;
	public Sprite[] gunReload;
	public Sprite[] akReload;
	public Sprite crosshair;
	int offset;
	public boolean shootbool = false, reloading = false;
	public int maxBullets;
	public int currentBullets;
	public int magBullets;
	public int magSize;
	public int cooldown;
	public int currentCooldown, shootAnim, reloadTimer;
	public long lastShot;
	public Weapon(int w) {
		weaponID = w;
		gunSpr = new Sprite[3];
		gunSpr[0] = new Sprite(new Texture(Gdx.files.internal("data/weapons/gun.png")));
		gunSpr[1] = new Sprite(new Texture(Gdx.files.internal("data/weapons/gun1.png")));
		gunSpr[2] = new Sprite(new Texture(Gdx.files.internal("data/weapons/gun2.png")));
		akSpr = new Sprite[5];
		akSpr[0] = new Sprite(new Texture(Gdx.files.internal("data/weapons/ak.png")));
		akSpr[1] = new Sprite(new Texture(Gdx.files.internal("data/weapons/ak1.png")));
		akSpr[2] = new Sprite(new Texture(Gdx.files.internal("data/weapons/ak2.png")));
		akSpr[3] = new Sprite(new Texture(Gdx.files.internal("data/weapons/ak3.png")));
		akSpr[4] = new Sprite(new Texture(Gdx.files.internal("data/weapons/ak4.png")));
		blockSpr = new Sprite[3];
		blockSpr[0] = new Sprite(new Texture(Gdx.files.internal("data/weapons/block.png")));
		blockSpr[1] = new Sprite(new Texture(Gdx.files.internal("data/weapons/block1.png")));
		blockSpr[2] = new Sprite(new Texture(Gdx.files.internal("data/weapons/block2.png")));
		gunReload = new Sprite[5];
		gunReload[0] = gunSpr[0];
		gunReload[1] = new Sprite(new Texture(Gdx.files.internal("data/weapons/gunreload.png")));
		gunReload[2] = new Sprite(new Texture(Gdx.files.internal("data/weapons/gunreload1.png")));
		gunReload[3] = new Sprite(new Texture(Gdx.files.internal("data/weapons/gunreload2.png")));
		gunReload[4] = new Sprite(new Texture(Gdx.files.internal("data/weapons/gunreload3.png")));
		akReload = new Sprite[8];
		akReload[0] = akSpr[0];
		akReload[1] = new Sprite(new Texture(Gdx.files.internal("data/weapons/akreload.png")));
		akReload[2] = new Sprite(new Texture(Gdx.files.internal("data/weapons/akreload1.png")));
		akReload[3] = new Sprite(new Texture(Gdx.files.internal("data/weapons/akreload2.png")));
		akReload[4] = new Sprite(new Texture(Gdx.files.internal("data/weapons/akreload3.png")));
		akReload[5] = new Sprite(new Texture(Gdx.files.internal("data/weapons/akreload4.png")));
		akReload[6] = new Sprite(new Texture(Gdx.files.internal("data/weapons/akreload5.png")));
		akReload[7] = new Sprite(new Texture(Gdx.files.internal("data/weapons/akreload6.png")));

		if (weaponID == gun) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/weapons/crosshairsmaller.png")));
			wpn = gunSpr[0];
			maxBullets = 28;
			magSize = 8;
			currentBullets = 28;
			magBullets = 8;
			cooldown = 500;
		} else if (weaponID == ak) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/weapons/crosshairsmaller.png")));
			wpn = akSpr[0];
			maxBullets = 90;
			currentBullets = 90;
			magBullets = 30;
			magSize = 30;
			cooldown = 100;
		} else if (weaponID == block) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/weapons/crosshairsmaller.png")));
			wpn = new Sprite(new Texture(Gdx.files.internal("data/weapons/block.png")));
			maxBullets = 30;
			currentBullets = 30;
			magBullets = 30;
			magSize = 30;
			cooldown = 500;
		}
	}
	public void restart() {
		if (weaponID == gun) {
			maxBullets = 28;
			magSize = 8;
			currentBullets = 28;
			magBullets = 8;
			cooldown = 500;
		} else if (weaponID == ak) {
			maxBullets = 90;
			currentBullets = 90;
			magBullets = 30;
			magSize = 30;
			cooldown = 100;
		} else if (weaponID == block) {
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
		float delta = Gdx.graphics.getDeltaTime()*1000;

		currentCooldown -= delta;
		shootAnim -= delta;
		reloadTimer -= delta;

		if(currentCooldown<0)
			currentCooldown = 0;

		if(shootAnim > 0){
			shootbool = true;
		}
		else{
			shootbool = false;
		}

		if(reloadTimer > 0){
			reloading = true;
		}
		else{
			reloading = false;
		}

		if(shootbool && weaponID == gun){
			for(int i = 0; i < gunSpr.length; i++){
				if(shootAnim > i*100){
					wpn = gunSpr[i];
				}
			}
		}
		if(reloading && weaponID == gun){
			for(int i = 0; i < gunReload.length; i++){
				if(reloadTimer > i*100){
					wpn = gunReload[i];
				}
				
			}
		}
		if(reloading && weaponID == ak){
			for(int i = 0; i < akReload.length; i++){
				if(reloadTimer > i*60){
					wpn = akReload[i];
				}
				
			}
		}
		if(shootbool && weaponID == ak){
			for(int i = 0; i < akSpr.length; i++){
				if(shootAnim > i*60){
					wpn = akSpr[i];
				}
			}
		}
		if(shootbool && weaponID == block){
			for(int i = 0; i < blockSpr.length; i++){
				if(shootAnim > i*100){
					wpn = blockSpr[i];
				}
			}
		}

	}
	public boolean shoot(boolean mute) {
		if(!reloading){
			if (magBullets > 0 && currentCooldown <= 0) {
				magBullets--;
				currentCooldown = cooldown;
				shootAnim = 300;
				if(!mute){
					SoundManager.playWeaponSound(weaponID);
				}
				return true;
			}
		}
		return false;
	}
	public void reload() {
		if (magBullets < magSize) {
			if (magBullets+currentBullets>=magSize) {
				reloadTimer = 500;
				currentBullets-=magSize-magBullets;
				magBullets = magSize;
			} else {
				magBullets += currentBullets;
				currentBullets = 0;
			}
		}
	}
}
