package Spelet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

	public Sprite currentHealth;
	public Sprite currentShield;
	public Sprite[] healthsprites;
	public Sprite[] shieldsprites;
	public Sprite[] weaponsprites;
	public Sprite currentWeapon;
	public Sprite items, item1, item2, item3;
	public Sprite currentItem;
	public boolean swapWeapon;
	public int currentCooldown, switchRender;

	public GameInterface() {
		healthbar = new TextureAtlas(Gdx.files.internal("data/gameinterface/health/pack"));
		shieldbar = new TextureAtlas(Gdx.files.internal("data/gameinterface/shield/pack"));
		weapons = new TextureAtlas(Gdx.files.internal("data/gameinterface/weapons/pack"));
		items = new Sprite(new Texture(Gdx.files.internal("data/gameinterface/items/items.png")));
		item1 = new Sprite(new Texture(Gdx.files.internal("data/gameinterface/items/1.png")));
		item2 = new Sprite(new Texture(Gdx.files.internal("data/gameinterface/items/2.png")));
		item3 = new Sprite(new Texture(Gdx.files.internal("data/gameinterface/items/3.png")));
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
		currentItem = item1;
	}
	public void restart() {

	}
	public void render(SpriteBatch sb) {
		currentHealth.setPosition(0+currentHealth.getWidth()/7, 0+currentHealth.getHeight()/7);
		currentHealth.draw(sb);
		currentShield.setPosition(3+currentShield.getWidth()/7, 5+currentHealth.getHeight());
		currentShield.draw(sb);
		currentWeapon.setPosition(Gdx.graphics.getWidth()-currentWeapon.getWidth(), 0+currentWeapon.getHeight()*2);
		if(swapWeapon){
			currentWeapon.draw(sb);
		}
		currentItem.setPosition(Gdx.graphics.getWidth()/2-items.getWidth()/2, 0);
		currentItem.draw(sb);
	}

	public void updateHealth(int hp){
		currentHealth = healthsprites[hp-1];
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

	public void update() {
		currentCooldown -= Gdx.graphics.getDeltaTime()*1000;
		switchRender -= Gdx.graphics.getDeltaTime()*1000;
		if(switchRender > 0){
			swapWeapon = true;
		}
		else{
			swapWeapon = false;
		}
	}

}
