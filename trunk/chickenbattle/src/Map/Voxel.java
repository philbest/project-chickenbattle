package Map;

import Spelet.Weapon;

public class Voxel {
	public static final float maxTypes = 2;
	public static final int nothing = 0;
	public static final int grass = 1;
	public static final int rock = 2;

	public static final int DEFAULT_DURABILITY = 2;

	public int id;
	public int durability;
	public Voxel(int i) {
		id = i;
		if (id == grass)
			durability = 1;
		else if (id == rock)
			durability = 3;
	}
	public void hit(int bullet) {
		if (bullet == Weapon.bullet_ak) {
			durability--;
		} else if (bullet == Weapon.bullet_gun){
			durability--;
		} else if (bullet == Weapon.bullet_sniper) {
			durability-=2;
		} else if (bullet == Weapon.bullet_emp) {

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
		}
		return 0;
	}
	public boolean isDead() {
		if (durability <= 0)
			return true;
		return false;
	}
}
