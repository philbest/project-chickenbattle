package Spelet;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	// Weapon sounds
	public static HashMap<Integer, Sound> weaponSounds;
	public static final int ak47 = 0;
	public static final int emp = 3;
	public static final int awp = 4;
	public static void initiate() {
		weaponSounds = new HashMap<Integer, Sound>();
		weaponSounds.put(Weapon.ak, Gdx.audio.newSound(Gdx.files.internal("data/Sound/Weapons/ak47.mp3")));
		weaponSounds.put(Weapon.gun, Gdx.audio.newSound(Gdx.files.internal("data/Sound/Weapons/deagle.mp3")));
		weaponSounds.put(Weapon.sniper, Gdx.audio.newSound(Gdx.files.internal("data/Sound/Weapons/awp.mp3")));
		weaponSounds.put(Weapon.emp, Gdx.audio.newSound(Gdx.files.internal("data/Sound/Weapons/emp.mp3")));
	}
	public static void playWeaponSound(int sid) {
		//if (weaponSounds.containsKey(sid))
			//weaponSounds.get(sid).play();
	}
}
