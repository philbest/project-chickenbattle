package Spelet;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Weapon {
	public int weaponID = 1;

	public static final int gun = 0;
	public static final int ak = 1;
	public static final int block = 2;
	public static final int emp = 3;
	public static final int sniper = 4;
	public static final int rocketlauncher = 5;
	// Bullet types
	public static final int bullet_gun = 1000;
	public static final int bullet_ak = 1001;
	public static final int bullet_block = 1002;
	public static final int bullet_emp = 1003;
	public static final int bullet_sniper = 1004;
	public static final int bullet_rocket = 1005;
	
	
	
	public Sprite wpn;
	public Sprite[] gunSpr;
	public Sprite[] akSpr;
	public Sprite[] blockSpr;
	public Sprite[] gunReload;
	public Sprite[] akReload;
	public Sprite[] empSpr;
	public Sprite[] empRecharge;
	public Sprite[] sniperSpr;
	public Sprite[] rocketSpr;
	public Sprite[] sniperReload;
	public Sprite[] akCH, empCH;
	public Sprite crosshair;
	public Sprite sniperZoom;
	int offset;
	public boolean shootbool = false, reloading = false, empCooldown = false;
	public boolean zoomS = false, zoomR = false;
	public int maxBullets;
	public int currentBullets;
	public int magBullets;
	public int magSize;
	public int cooldown;
	public int reloadTimes;
	public int currentCooldown, shootAnim, reloadTimer, empTimer, empAnim, rocketAnim;
	public long lastShot;
	public BitmapFont font;
	public int bulletType;
	public Weapon(int w) {
		weaponID = w;
		sniperZoom = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperzoom.png")));
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
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
		empSpr = new Sprite[10];
		empSpr[0] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp.png")));
		empSpr[1] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp1.png")));
		empSpr[2] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp2.png")));
		empSpr[3] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp3.png")));
		empSpr[4] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp4.png")));
		empSpr[5] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp5.png")));
		empSpr[6] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp6.png")));
		empSpr[7] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp7.png")));
		empSpr[8] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp8.png")));
		empSpr[9] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp9.png")));
		empRecharge = new Sprite[6];
		empRecharge[0] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emp.png")));
		empRecharge[1] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emprecharge.png")));
		empRecharge[2] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emprecharge1.png")));
		empRecharge[3] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emprecharge2.png")));
		empRecharge[4] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emprecharge3.png")));
		empRecharge[5] = new Sprite(new Texture(Gdx.files.internal("data/weapons/emprecharge4.png")));
		sniperReload = new Sprite[10];
		sniperReload[0] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperreload.png")));
		sniperReload[1] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperreload1.png")));
		sniperReload[2] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperreload2.png")));
		sniperReload[3] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperreload3.png")));
		sniperReload[4] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperreload4.png")));
		sniperReload[5] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperreload5.png")));
		sniperReload[6] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperreload6.png")));
		sniperReload[7] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperreload7.png")));
		sniperReload[8] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperreload8.png")));
		sniperReload[9] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniperreload9.png")));
		sniperSpr = new Sprite[4];
		sniperSpr[0] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniper.png")));
		sniperSpr[1] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniper1.png")));
		sniperSpr[2] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniper2.png")));
		sniperSpr[3] = new Sprite(new Texture(Gdx.files.internal("data/weapons/sniper3.png")));
		akCH = new Sprite[3];
		akCH[0] = new Sprite(new Texture(Gdx.files.internal("data/crosshairs/akcrosshair.png")));
		akCH[1] = new Sprite(new Texture(Gdx.files.internal("data/crosshairs/akcrosshair1.png")));
		akCH[2] = new Sprite(new Texture(Gdx.files.internal("data/crosshairs/akcrosshair2.png")));
		empCH = new Sprite[2];
		empCH[0] = new Sprite(new Texture(Gdx.files.internal("data/crosshairs/empcrosshair.png")));
		empCH[1] = new Sprite(new Texture(Gdx.files.internal("data/crosshairs/empcrosshair1.png")));
		rocketSpr = new Sprite[5];
		rocketSpr[0] = new Sprite(new Texture(Gdx.files.internal("data/weapons/rocket.png")));
		rocketSpr[1] = new Sprite(new Texture(Gdx.files.internal("data/weapons/rocket1.png")));
		rocketSpr[2] = new Sprite(new Texture(Gdx.files.internal("data/weapons/rocket2.png")));
		rocketSpr[3] = new Sprite(new Texture(Gdx.files.internal("data/weapons/rocket3.png")));
		rocketSpr[4] = new Sprite(new Texture(Gdx.files.internal("data/weapons/rocket4.png")));
		rocketSpr[0].setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		rocketSpr[1].setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		rocketSpr[2].setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		rocketSpr[3].setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		rocketSpr[4].setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		reloadTimes = 0;
		if (weaponID == gun) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairs/guncrosshair.png")));
			wpn = gunSpr[0];
			maxBullets = 28;
			magSize = 8;
			currentBullets = 28;
			magBullets = 8;
			cooldown = 500;
			bulletType = Weapon.bullet_gun;
		} else if (weaponID == ak) {
			crosshair = akCH[0];
			wpn = akSpr[0];
			maxBullets = 90;
			currentBullets = 90;
			magBullets = 30;
			magSize = 30;
			cooldown = 100;
			bulletType = Weapon.bullet_ak;
		} else if (weaponID == block) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairs/blockcrosshair.png")));
			wpn = new Sprite(new Texture(Gdx.files.internal("data/weapons/block.png")));
			maxBullets = 30;
			currentBullets = 30;
			magBullets = 30;
			magSize = 30;
			cooldown = 500;
			bulletType = Weapon.bullet_block;
		}
		else if (weaponID == emp) {
			crosshair = empCH[0];
			wpn = empSpr[0];
			maxBullets = 1;
			currentBullets = 1;
			magBullets = 1;
			magSize = 1;
			cooldown = 1000;
			bulletType = Weapon.bullet_emp;
		} else if (weaponID == sniper) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairs/snipercrosshair.png")));
			wpn = sniperSpr[0];
			maxBullets = 16;
			currentBullets = 12;
			magBullets = 4;
			magSize = 4;
			cooldown = 1500;
			bulletType = Weapon.bullet_sniper;
		} else if (weaponID == rocketlauncher) {
			crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairs/rocketcrosshair.png")));
			wpn = rocketSpr[0];
			maxBullets = 6;
			currentBullets = 5;
			magBullets = 1;
			magSize = 1;
			cooldown = 1500;
			bulletType = Weapon.bullet_rocket;
			rocketAnim = 2100;
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
		} else if (weaponID == emp) {
			maxBullets = 1;
			currentBullets = 2;
			magBullets = 1;
			magSize = 1;
			cooldown = 500;
			empTimer = 0;
		}
		else if (weaponID == sniper) {
			maxBullets = 16;
			currentBullets = 12;
			magBullets = 4;
			magSize = 4;
			cooldown = 1500;
		}
		else if (weaponID == rocketlauncher) {
			maxBullets = 6;
			currentBullets = 5;
			magBullets = 1;
			magSize = 1;
			rocketAnim = 2100;
			cooldown = 1500;
		}
	}
	public void render(SpriteBatch sb) {
		wpn.setPosition(Gdx.graphics.getWidth()-wpn.getWidth(), 0+offset);
		wpn.draw(sb);
		crosshair.setPosition(Gdx.graphics.getWidth()/2-crosshair.getWidth()/2,Gdx.graphics.getHeight()/2-crosshair.getHeight()/2);
		if(weaponID != sniper){
			crosshair.draw(sb);
		}
		if(zoomS){
			sniperZoom.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			sniperZoom.setPosition(0, 0);
			sniperZoom.draw(sb);
		}
		/*
		if(empCooldown && weaponID == 3){
			font.draw(sb, Integer.toString((empTimer/1000)), Gdx.graphics.getWidth()-100, 20);
		}
		 */
	}
	public void update() {
		float delta = Gdx.graphics.getDeltaTime()*1000;

		currentCooldown -= delta;
		shootAnim -= delta;
		reloadTimer -= delta;
		empTimer -= delta;
		empAnim -= delta;
		rocketAnim -= delta;

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
		if(empTimer > 0){
			empCooldown = true;
		}
		else{
			empCooldown = false;
		}
		if(shootbool && weaponID == gun){
			for(int i = 0; i < gunSpr.length; i++){
				if(shootAnim > i*100){
					wpn = gunSpr[i];
				}
			}
		}
		if(reloadTimes > 0 && weaponID == gun){
			for(int i = 0; i < gunReload.length; i++){
				if(reloadTimer > i*100){
					wpn = gunReload[i];
				}
			}
			if(!reloading){
				reloadTimes--;
				reloadTimer = 500;
			}
		}
		if(reloading && weaponID == ak){
			for(int i = 0; i < akReload.length; i++){
				if(reloadTimer > i*60){
					wpn = akReload[i];
				}

			}
		}
		if(reloadTimes > 0 && weaponID == sniper){
			for(int i = 0; i < sniperReload.length; i++){
				if(reloadTimer > i*60){
					wpn = sniperReload[i];
				}
			}
			if(!reloading){
				reloadTimes--;
				reloadTimer = 500;
			}
		}
		if(reloadTimes > 0 && weaponID == ak){
			for(int i = 0; i < akSpr.length; i++){
				if(shootAnim > i*60){
					wpn = akSpr[i];
				}
			}
			if(!reloading){
				reloadTimes--; 
				reloadTimer = 500;
			}
		}
		if(reloadTimes > 0 && weaponID == emp){
			crosshair = empCH[1];
			for(int i = 0; i < empRecharge.length; i++){
				if(reloadTimer > i*80){
					wpn = empRecharge[i];
				}
			}
			if(!reloading){
				reloadTimes--; 
				reloadTimer = 500;
			}
		}
		else if(reloadTimes == 0 && weaponID == emp){
			crosshair = empCH[0];
		}

		if(shootbool && weaponID == block){
			for(int i = 0; i < blockSpr.length; i++){
				if(shootAnim > i*100){
					wpn = blockSpr[i];
				}
			}
		}
		if(shootbool && weaponID == ak){
			for(int i = 0; i < akSpr.length; i++){
				if(shootAnim > i*100){
					wpn = akSpr[i];
				}
			}
		}
		if(shootbool && weaponID == sniper){
			for(int i = 0; i < blockSpr.length; i++){
				if(shootAnim > i*75){
					wpn = sniperSpr[i];
				}
			}
		}
		if(shootbool && weaponID == rocketlauncher){
			for(int i = 0; i < rocketSpr.length; i++){
				if(shootAnim > i*60){
					wpn = rocketSpr[i];
				}
			}
		}

		if(empAnim > 0){
			for(int i = 0; i < empSpr.length;i++){
				if(empAnim > i*100){
					wpn = empSpr[i];
				}
			}
		}

	}
	public boolean shoot() {
		if(!reloading){
			if (magBullets > 0 && currentCooldown <= 0) {
				magBullets--;
				currentCooldown = cooldown;
				shootAnim = 300;
				SoundManager.playWeaponSound(weaponID);
				return true;
			}
		}
		return false;
	}

	public boolean shootEMP(){
		if(!reloading && !empCooldown){
			if (magBullets > 0) {
				empTimer = 8000;
				magBullets--;
				empAnim = 1000;
				SoundManager.playWeaponSound(weaponID);
				return true;
			}
		}
		return false;
	}


	public void reload() {
		if (magBullets < magSize) {

			if (magBullets+currentBullets>=magSize) {
				reloadTimer = 500;
				if(weaponID == sniper){
					reloadTimes = magSize-magBullets;
				}
				if(weaponID == gun){
					reloadTimes = (magSize-magBullets)/3+1;
				}
				if(weaponID == ak){
					reloadTimes = (magSize-magBullets)/10;
				}
				if(weaponID == emp){
					reloadTimes = 16;
				}
				currentBullets-=magSize-magBullets;
				magBullets = magSize;

			} else {
				magBullets += currentBullets;
				currentBullets = 0;
			}
		}
	}
}
