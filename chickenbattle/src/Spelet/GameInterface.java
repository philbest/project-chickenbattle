package Spelet;

import java.util.Random;

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
	public float[] bulletTimers;
	public static final float BULLET_ANIM_TIME = 0.04f;
	
	public Sprite[] bulletToDraw;
	public Sprite currentHealth;
	public Sprite currentShield;
	public Sprite[] healthsprites;
	public Sprite[] shieldsprites;
	public Sprite[] weaponsprites;
	public Sprite[][] bulletAnims;
	public Sprite currentWeapon;
	public Sprite items, item1, item2, item3;
	public Sprite currentItem, bloodSprite;
	public BitmapFont font;
	public boolean swapWeapon, blood, frag;
	public String killerName, killedName;
	public int currentCooldown, switchRender, bloodTimer, fragTimer;
	public int renderBullets;
	private Random rand = new Random();

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
		bulletAnims = new Sprite[5][];
		bulletAnims[0] = new Sprite[11];
		bulletAnims[0][0] = bulletanim0.createSprite("bullet0");
		bulletAnims[0][1] = bulletanim0.createSprite("bullet1");
		bulletAnims[0][2] = bulletanim0.createSprite("bullet2");
		bulletAnims[0][3] = bulletanim0.createSprite("bullet3");
		bulletAnims[0][4] = bulletanim0.createSprite("bullet4");
		bulletAnims[0][5] = bulletanim0.createSprite("bullet5");
		bulletAnims[0][6] = bulletanim0.createSprite("bullet6");
		bulletAnims[0][7] = bulletanim0.createSprite("bullet7");
		bulletAnims[0][8] = bulletanim0.createSprite("bullet8");
		bulletAnims[0][9] = bulletanim0.createSprite("bullet9");
		bulletAnims[0][10] = bulletanim0.createSprite("bullet10");
		bulletAnims[1] = new Sprite[8];
		bulletAnims[1][0] = bulletanim1.createSprite("bulleta0");
		bulletAnims[1][1] = bulletanim1.createSprite("bulleta1");
		bulletAnims[1][2] = bulletanim1.createSprite("bulleta2");
		bulletAnims[1][3] = bulletanim1.createSprite("bulleta3");
		bulletAnims[1][4] = bulletanim1.createSprite("bulleta4");
		bulletAnims[1][5] = bulletanim1.createSprite("bulleta5");
		bulletAnims[1][6] = bulletanim1.createSprite("bulleta6");
		bulletAnims[1][7] = bulletanim1.createSprite("bulleta7");
		bulletAnims[2] = new Sprite[8];
		bulletAnims[2][0] = bulletanim2.createSprite("bulletb0");
		bulletAnims[2][1] = bulletanim2.createSprite("bulletb1");
		bulletAnims[2][2] = bulletanim2.createSprite("bulletb2");
		bulletAnims[2][3] = bulletanim2.createSprite("bulletb3");
		bulletAnims[2][4] = bulletanim2.createSprite("bulletb4");
		bulletAnims[2][5] = bulletanim2.createSprite("bulletb5");
		bulletAnims[2][6] = bulletanim2.createSprite("bulletb6");
		bulletAnims[2][7] = bulletanim2.createSprite("bulletb7");
		bulletAnims[3] = new Sprite[6];
		bulletAnims[3][0] = bulletanim3.createSprite("bulletc0");
		bulletAnims[3][1] = bulletanim3.createSprite("bulletc1");
		bulletAnims[3][2] = bulletanim3.createSprite("bulletc2");
		bulletAnims[3][3] = bulletanim3.createSprite("bulletc3");
		bulletAnims[3][4] = bulletanim3.createSprite("bulletc4");
		bulletAnims[3][5] = bulletanim3.createSprite("bulletc5");
		bulletAnims[4] = new Sprite[7];
		bulletAnims[4][0] = bulletanim4.createSprite("bulletd0");
		bulletAnims[4][1] = bulletanim4.createSprite("bulletd1");
		bulletAnims[4][2] = bulletanim4.createSprite("bulletd2");
		bulletAnims[4][3] = bulletanim4.createSprite("bulletd3");
		bulletAnims[4][4] = bulletanim4.createSprite("bulletd4");
		bulletAnims[4][5] = bulletanim4.createSprite("bulletd5");
		bulletAnims[4][6] = bulletanim4.createSprite("bulletd6");
		currentItem = item1;
		killerName = "null";
		killedName = "null";

		bulletToDraw = new Sprite[30];
		currentBullet = new int[30];
		bulletTimers = new float[30];
		
		for(int i = 0; i < bulletToDraw.length; i++){
			bulletToDraw[i] = bulletAnims[0][0];
			currentBullet[i] = idleBullet;
			bulletTimers[i] = 0f;
		}
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

		for(int i = 0; i < renderBullets; i++){
			bulletToDraw[i].setPosition(Gdx.graphics.getWidth()-170-(i*10), -50);
			bulletToDraw[i].draw(sb, 0.75f);
		}

		for(int i=0;i<30;i++)
		{
			if(currentBullet[i] != idleBullet)
			{
				bulletToDraw[i].setPosition(Gdx.graphics.getWidth()-170-(i*10), -50);
				bulletToDraw[i].draw(sb, 1f);
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

	public void updateShells(int i){
		renderBullets = i;
	}
	
	public void animateShell(int index)
	{
		currentBullet[index] = rand.nextInt(5)+1;
	}

	public void update() {
		float delta = Gdx.graphics.getDeltaTime();
		
		currentCooldown -= delta*1000;
		switchRender -= delta*1000;
		bloodTimer -= delta*1000;
		fragTimer -= delta*1000;

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

		for(int i = 0; i < currentBullet.length; i++){
			if(currentBullet[i] == idleBullet){
				bulletToDraw[i] = bulletAnims[0][0];
			}
			else
			{
				bulletTimers[i] += delta;
				
				boolean done = false;
				
				if(bulletTimers[i]>=BULLET_ANIM_TIME*(float)(bulletAnims[currentBullet[i]-1].length-1))
				{
					done = true;
					currentBullet[i] = idleBullet;
					bulletTimers[i] = 0f;
				}
				
				for(int j=0;!done && j<bulletAnims[currentBullet[i]-1].length-1;j++)
				{
					if(bulletTimers[i]<BULLET_ANIM_TIME*((float)j))
					{
						done = true;
						bulletToDraw[i] = bulletAnims[currentBullet[i]-1][j];
					}
				}
			}
		}
	}
}