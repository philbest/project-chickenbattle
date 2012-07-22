package network;


import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Player {
	public String name;
	public int id,hp,kills,deaths, shields;
	public long lasthit;
	public float posX, posY,posZ;  
	public BoundingBox box;
	public float animTimer, shieldTimer, shieldTimer2;
	public boolean killer, killed;
	public Player(String xs){
		this.name = xs;
		hp = 10;
		shields = 5;
		shieldTimer = 0;
		shieldTimer2 = 0;
		killer = false;
		killed = false;
		posX = 0;
		posY = 0;
		posZ =0;
		kills =0;
		deaths=0;
		box = new BoundingBox();
	}
	public void setBox(Vector3[] x){
		box.set(x);
	}	

}
