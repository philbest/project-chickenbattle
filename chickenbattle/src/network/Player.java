package network;


import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Player {
	public String name;
	public int id,hp,kills,deaths, shields;
	public long lasthit;
	public float posX, posY,posZ,dirX,dirY, dirZ;  
	public BoundingBox box;
	public float animTimer;
	public long lastRegged;
	public boolean falldeath, initShield,dead;
	public Player(String xs){
		this.name = xs;
		hp = 10;
		shields = 5;
		lastRegged = 0l;
		dead = false;
		falldeath = false;
		initShield = false;
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

	public void setName(String xs){
		this.name = xs;
	}
	
}
