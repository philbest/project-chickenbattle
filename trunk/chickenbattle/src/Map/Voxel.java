package Map;

import com.badlogic.gdx.math.MathUtils;

import Spelet.Weapon;

public class Voxel {
	public static final float maxTypes = 2;
	public static final int nothing = 0;
	public static final int grass = 1;
	public static final int rock = 2;

	public float defaultDurability = 2;

	public int id;
	public float durability;
	public short random;
	public Voxel(int i) {
		id = i;
		if (id == grass) {
			durability = 1;
		} else if (id == rock) {
			durability = 3;
		}
		random = (short) MathUtils.random(1000);
		defaultDurability = durability;
	}
	public void hit(int bullet) {
		if (bullet == Weapon.bullet_ak) {
			durability--;
		} else if (bullet == Weapon.bullet_gun){
			durability--;
		} else if (bullet == Weapon.bullet_sniper) {
			durability-=2;
		} else if (bullet == Weapon.bullet_emp) {

		} else if (bullet == Weapon.bullet_rocket) {
			durability = 0;
		}
	}
	public int damageDone(int bullet) {
		if (bullet == Weapon.bullet_ak) {
			return 1;
		} else if (bullet == Weapon.bullet_gun){
			return 1;
		} else if (bullet == Weapon.bullet_sniper) {
			return 2;
		} else if (bullet == Weapon.bullet_emp) {
			return 0;
		} else if (bullet == Weapon.bullet_rocket) {
			return (int) durability;
		}
		return 0;
	}
	public boolean isDead() {
		if (durability <= 0)
			return true;
		return false;
	}
}
