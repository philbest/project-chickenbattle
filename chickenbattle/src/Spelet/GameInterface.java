package Spelet;

import network.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class GameInterface {
	public Sprite health;
	public Sprite ammo;
	public Sprite weapon;
	public TextureAtlas healthbar;
	public TextureAtlas weapons;
	public TextureAtlas shieldbar;
	public TextureAtlas bulletanim0, bulletanim1, bulletanim2, bulletanim3, bulletanim4;
	public static final int idleBullet = 0;
	public static final int bulletShell0 = 1;
	public static final int bulletShell1 = 2;
	public static final int bulletShell2 = 3;
	public static final int bulletShell3 = 4;
	public static final int bulletShell4 = 5;
	public int[] currentBullet;

	public Sprite[] bulletToDraw;
	public Sprite currentHealth;
	public Sprite currentShield;
	public Sprite[] healthsprites;
	public Sprite[] shieldsprites;
	public Sprite[] weaponsprites;
	public Sprite[] bullet0;
	public Sprite[] bullet1;
	public Sprite[] bullet2;
	public Sprite[] bullet3;
	public Sprite[] bullet4;
	public Sprite currentWeapon;
	public Sprite items, item1, item2, item3;
	public Sprite currentItem, bloodSprite;
	public BitmapFont font;
	public boolean swapWeapon, blood, frag;
	public String killerName, killedName;
	public int currentCooldown, switchRender, bloodTimer, fragTimer;
	public int shellTimer, anistep;
	public int renderBullets;

	public GameInterface() {
		font = new BitmapFont();
		healthbar = new TextureAtlas(Gdx.files.internal("data/gameinterface/health/pack"));
		shieldbar = new TextureAtlas(Gdx.files.internal("data/gameinterface/shield/pack"));
		bulletanim0 = new TextureAtlas(Gdx.files.internal("data/gameinterface/bullets/b1/pack"));
		bulletanim1 = new TextureAtlas(Gdx.files.internal("data/gameinterface/bullets/b2/pack"));
		bulletanim2 = new TextureAtlas(Gdx.files.internal("data/gameinterface/bullets/b3/pack"));
		bulletanim3 = new TextureAtlas(Gdx.files.internal("data/gameinterface/bullets/b4/pack"));
		bulletanim4 = new TextureAtlas(Gdx.files.internal("data/gameinterface/bullets/b5/pack"));
		weapons = new TextureAtlas(Gdx.files.internal("data/gameinterface/weapons/pack"));
		items = new Sprite(new Texture(Gdx.files.internal("data/gameinterface/items/items.png")));
		item1 = new Sprite(new Texture(Gdx.files.internal("data/gameinterface/items/1.png")));
		item2 = new Sprite(new Texture(Gdx.files.internal("data/gameinterface/items/2.png")));
		item3 = new Sprite(new Texture(Gdx.files.internal("data/gameinterface/items/3.png")));
		bloodSprite = new Sprite(new Texture(Gdx.files.internal("data/gameinterface/other/bloodsplatt.png")));
		bloodSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		healthsprites = new Sprite[10];
		healthsprites[0] = healthbar.createSprite("1");
		healthsprites[1] = healthbar.createSprite("2");
		healthsprites[2] = healthbar.createSprite("3");
		healthsprites[3] = healthbar.createSprite("4");
		healthsprites[4] = healthbar.createSprite("5");
		healthsprites[5] = healthbar.createSprite("6");
		healthsprites[6] = healthbar.createSprite("7");
		healthsprites[7] = healthbar.createSprite("8");
		healthsprites[8] = healthbar.createSprite("9");
		healthsprites[9] = healthbar.createSprite("10");
		currentHealth = healthsprites[9];
		weaponsprites = new Sprite[3];
		weaponsprites[0] = weapons.createSprite("usp");
		weaponsprites[1] = weapons.createSprite("ak");
		weaponsprites[2] = weapons.createSprite("block");
		shieldsprites = new Sprite[6];
		shieldsprites[0] = shieldbar.createSprite("0");
		shieldsprites[1] = shieldbar.createSprite("1");
		shieldsprites[2] = shieldbar.createSprite("2");
		shieldsprites[3] = shieldbar.createSprite("3");
		shieldsprites[4] = shieldbar.createSprite("4");
		shieldsprites[5] = shieldbar.createSprite("5");
		currentShield = shieldsprites[5];
		bullet0 = new Sprite[11];
		bullet0[0] = bulletanim0.createSprite("bullet0");
		bullet0[1] = bulletanim0.createSprite("bullet1");
		bullet0[2] = bulletanim0.createSprite("bullet2");
		bullet0[3] = bulletanim0.createSprite("bullet3");
		bullet0[4] = bulletanim0.createSprite("bullet4");
		bullet0[5] = bulletanim0.createSprite("bullet5");
		bullet0[6] = bulletanim0.createSprite("bullet6");
		bullet0[7] = bulletanim0.createSprite("bullet7");
		bullet0[8] = bulletanim0.createSprite("bullet8");
		bullet0[9] = bulletanim0.createSprite("bullet9");
		bullet0[10] = bulletanim0.createSprite("bullet10");
		bullet1 = new Sprite[8];
		bullet1[0] = bulletanim1.createSprite("bulleta0");
		bullet1[1] = bulletanim1.createSprite("bulleta1");
		bullet1[2] = bulletanim1.createSprite("bulleta2");
		bullet1[3] = bulletanim1.createSprite("bulleta3");
		bullet1[4] = bulletanim1.createSprite("bulleta4");
		bullet1[5] = bulletanim1.createSprite("bulleta5");
		bullet1[6] = bulletanim1.createSprite("bulleta6");
		bullet1[7] = bulletanim1.createSprite("bulleta7");
		bullet2 = new Sprite[8];
		bullet2[0] = bulletanim2.createSprite("bulletb0");
		bullet2[1] = bulletanim2.createSprite("bulletb1");
		bullet2[2] = bulletanim2.createSprite("bulletb2");
		bullet2[3] = bulletanim2.createSprite("bulletb3");
		bullet2[4] = bulletanim2.createSprite("bulletb4");
		bullet2[5] = bulletanim2.createSprite("bulletb5");
		bullet2[6] = bulletanim2.createSprite("bulletb6");
		bullet2[7] = bulletanim2.createSprite("bulletb7");
		bullet3 = new Sprite[6];
		bullet3[0] = bulletanim3.createSprite("bulletc0");
		bullet3[1] = bulletanim3.createSprite("bulletc1");
		bullet3[2] = bulletanim3.createSprite("bulletc2");
		bullet3[3] = bulletanim3.createSprite("bulletc3");
		bullet3[4] = bulletanim3.createSprite("bulletc4");
		bullet3[5] = bulletanim3.createSprite("bulletc5");
		bullet4 = new Sprite[7];
		bullet4[0] = bulletanim4.createSprite("bulletd0");
		bullet4[1] = bulletanim4.createSprite("bulletd1");
		bullet4[2] = bulletanim4.createSprite("bulletd2");
		bullet4[3] = bulletanim4.createSprite("bulletd3");
		bullet4[4] = bulletanim4.createSprite("bulletd4");
		bullet4[5] = bulletanim4.createSprite("bulletd5");
		bullet4[6] = bulletanim4.createSprite("bulletd6");
		currentItem = item1;
		killerName = "null";
		killedName = "null";


		bulletToDraw = new Sprite[30];
		currentBullet = new int[30];
		for(int i = 0; i < bulletToDraw.length; i++){
			bulletToDraw[i] = bullet0[0];
			currentBullet[i] = idleBullet;
		}
		renderBullets = 8;
	}
	public void restart() {

	}
	public void render(SpriteBatch sb) {
		currentHealth.setPosition(0+currentHealth.getWidth()/7, 0+currentHealth.getHeight()/7);
		currentHealth.draw(sb);
		currentShield.setPosition(3+currentShield.getWidth()/7, 5+currentHealth.getHeight());
		currentShield.draw(sb);
		currentWeapon.setPosition(Gdx.graphics.getWidth()-currentWeapon.getWidth(), 0+currentWeapon.getHeight()*2);
		bloodSprite.setPosition(0, 0);
		if(swapWeapon){
			currentWeapon.draw(sb);
		}
		if(blood){
			bloodSprite.draw(sb);
		}
		currentItem.setPosition(Gdx.graphics.getWidth()/2-items.getWidth()/2, 0);
		currentItem.draw(sb);

		if(renderBullets > 0){
			for(int i = 0; i <= renderBullets; i++){
				bulletToDraw[i].setPosition(Gdx.graphics.getWidth()-170-(i*10), -50);
				bulletToDraw[i].draw(sb, 0.75f);
			}
		}
		if(frag){
			font.draw(sb, killerName + " has fragged " + killedName, Gdx.graphics.getWidth()/50, Gdx.graphics.getHeight()-font.getXHeight());
		}
	}

	public void updateKiller(Player killer){
		killerName = killer.name;
		fragTimer = 2000;
	}

	public void updateKilled(Player killed){
		killedName = killed.name;
		fragTimer = 2000;
	}

	public void updateHealth(int hp){
		currentHealth = healthsprites[hp-1];
	}

	public void updateShields(int shield){
		currentShield = shieldsprites[shield];
	}

	public void updateBlood(boolean bool){
		if(bool){
			bloodTimer = 200;
		}
	}



	public void updateWeapon(int weapon){
		currentWeapon = weaponsprites[weapon];
		if(weapon == 0){
			currentItem = item1;
		}
		else if(weapon == 1){
			currentItem = item2;
		}
		else if(weapon == 2){
			currentItem = item3;
		}
	}

	public void swapWeapon(){
		switchRender = 1700;
	}

	public void updateShells(int i, int random){
		renderBullets = i;
		//currentBullet[i] = random;
	}

	public void update() {
		currentCooldown -= Gdx.graphics.getDeltaTime()*1000;
		switchRender -= Gdx.graphics.getDeltaTime()*1000;
		bloodTimer -= Gdx.graphics.getDeltaTime()*1000;
		fragTimer -= Gdx.graphics.getDeltaTime()*1000;
		shellTimer += Gdx.graphics.getDeltaTime()*1000;


		if(switchRender > 0){
			swapWeapon = true;
		}
		else{
			swapWeapon = false;
		}
		if(bloodTimer > 0){
			blood = true;
		}
		else{
			blood = false;
		}
		if(fragTimer > 0){
			frag = true;
		}
		else{
			frag = false;
		}	

		/*
		for(int i = 0; i < currentBullet.length; i++){
			int step = 80;
			if(currentBullet[i] == idleBullet){
				//nothing
			}
			else if(currentBullet[i] == bulletShell0){
				if(shellTimer < step){
					bulletToDraw[i] = bullet0[0];
				}
				else if(shellTimer < step*2){
					bulletToDraw[i] = bullet0[1];
				}
				else if(shellTimer < step*3){
					bulletToDraw[i] = bullet0[2];
				}
				else if(shellTimer < step*4){
					bulletToDraw[i] = bullet0[3];
				}
				else if(shellTimer < step*5){
					bulletToDraw[i] = bullet0[4];
				}
				else if(shellTimer < step*6){
					bulletToDraw[i] = bullet0[5];
				}
				else if(shellTimer < step*7){
					bulletToDraw[i] = bullet0[6];
				}
				else if(shellTimer < step*8){
					bulletToDraw[i] = bullet0[7];
				}
				else if(shellTimer < step*9){
					bulletToDraw[i] = bullet0[8];
				}
				else if(shellTimer < step*10){
					bulletToDraw[i] = bullet0[9];
				}
				else if(shellTimer < step*11){
					bulletToDraw[i] = bullet0[10];
				}
				else{

				}

			}
		}
		 */
	}

}
