package network;

import com.badlogic.gdx.graphics.Mesh;

public class Player {
	public String name;
	public int id, posX, posY,posZ;  
	public Mesh cubeMesh;
	
	public Player(String xs){
		this.name = xs;
		posX = 0;
		posY = 0;
		posZ =0;
	}
	
	
	
}
