package Spelet;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	// Weapon sounds
	public static HashMap<Integer, Sound> weaponSounds;
	public static final int ak47 = 0;
	public static void initiate() {
		weaponSounds = new HashMap<Integer, Sound>();
		weaponSounds.put(Weapon.ak, Gdx.audio.newSound(Gdx.files.internal("data/Sound/Weapons/ak47.mp3")));
		weaponSounds.put(Weapon.gun, Gdx.audio.newSound(Gdx.files.internal("data/Sound/Weapons/deagle.mp3")));
	}
	public static void playWeaponSound(int sid) {
		if (weaponSounds.containsKey(sid))
		weaponSounds.get(sid).play();
	}
}
