package Spelet;
import Map.Chunk;
import Map.Map;
import Map.Voxel;
import Screens.Application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;


public class Character {
	public Vector3 position;
	public BoundingBox box;
	public Matrix4 modelMatrix;
	public int weapon;
	public Array<Weapon> inventory;
	public boolean jumping;
	public Vector3 movement, oldPos;
	public String charName;
	float forceUp;
	boolean hookshotting;
	public Animation currentAnim;
	public String state;
	public Texture modelTexture = null;
	public int health;
	public int shields;
	public boolean bloodsplatt, killer, killed;
	Mesh model;
	public Character(String name) {

		health = 10;
		shields = 5;
		bloodsplatt = false;
		killer = false;
		killed = false;
		this.charName = name;
		hookshotting = false;
		state = "chill";
		forceUp = 0;
		movement = new Vector3();
		oldPos = new Vector3();
		inventory = new Array<Weapon>();
		inventory.add(new Weapon(Weapon.gun));
		inventory.add(new Weapon(Weapon.ak));
		inventory.add(new Weapon(Weapon.block));
		inventory.add(new Weapon(Weapon.emp));
		inventory.add(new Weapon(Weapon.sniper));
		weapon = inventory.get(0).weaponID;
		position = new Vector3();
		box = new BoundingBox();
		modelMatrix = new Matrix4();
		model = StaticAnimations.walk.parts.get(4).partMesh;		
	}
	
	public void updateKill(boolean k1, boolean k2){
		this.killer = k1;
		this.killed = k2;
	}
	
	public void updateName(String xs){
		charName = xs;
	}

	public void setPos(float x, float y, float z) {

		position.set(x,y,z);
		modelMatrix = new Matrix4();
		modelMatrix.setToTranslation(position);
		box.set(StaticAnimations.walkBox);
		box.mul(modelMatrix);
	}
	public void setPos(Vector3 pos) {
		position.set(pos);
		modelMatrix.setToTranslation(position);
		box.set(StaticAnimations.walkBox);
		box.mul(modelMatrix);
	}
	public void addMovement(Vector3 movement) {
		position.add(movement);
		modelMatrix.setToTranslation(position);
		box.set(StaticAnimations.walkBox);
		box.mul(modelMatrix);
	}
	
	public void resurrect() {
		setPos(30,60,50);
		forceUp = 0;
		killed = false;
		killer = false;
		jumping = false;
		for (int i = 0; i < inventory.size; i++) {
			inventory.get(i).restart();
		}
	}
	
	public void updateHealth(int hp){
		if(hp < health && health <= 4){
			bloodsplatt = true;
			health = hp;
		}
		else{
			health = hp;
			bloodsplatt = false;
		}
	}
	
	public void updateShield(int s){
		shields = s;
	}
	
	public void regenShield(){
		if(shields < 5){
			shields++;
		}
	}
	
	public void update(Application app) {

		if (hookshotting) {

		} else {
			if (Gdx.input.isKeyPressed(Input.Keys.W)) {
				oldPos.set(position);
				movement.set(app.cam.direction.x,0,app.cam.direction.z);
				movement.nor();
				movement.mul(Gdx.graphics.getDeltaTime()*10);
				addMovement(movement);

				for (Vector3 vec : box.getCorners()) {
					int pointX = (int) vec.x;
					int pointY = (int) vec.y;
					int pointZ = (int) vec.z;
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						for (Chunk c : app.map.chunks) {
							if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
								if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize].id == Voxel.grass) {
									setPos(oldPos);
								}		
								break;
							}
						}
					}
				}
			}
			if (Gdx.input.isKeyPressed((Input.Keys.S))) {
				oldPos.set(position);
				movement.set(app.cam.direction.x,0,app.cam.direction.z);
				movement.nor();
				movement.mul(Gdx.graphics.getDeltaTime()*10*-1);
				addMovement(movement);
				for (Vector3 vec : box.getCorners()) {
					int pointX = (int) vec.x;
					int pointY = (int) vec.y;
					int pointZ = (int) vec.z;
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						for (Chunk c : app.map.chunks) {
							if (c.x == (pointX/app.map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
								if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize].id == Voxel.grass) {
									setPos(oldPos);
								}		
								break;
							}
						}
					}
				}
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				oldPos.set(position);
				movement.set(app.cam.direction.x,0,app.cam.direction.z);
				movement.crs(app.cam.up);
				movement.nor();
				movement.mul(Gdx.graphics.getDeltaTime()*10);
				addMovement(movement);
				for (Vector3 vec : box.getCorners()) {
					int pointX = (int) vec.x;
					int pointY = (int) vec.y;
					int pointZ = (int) vec.z;
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						for (Chunk c : app.map.chunks) {
							if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
								if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] .id == Voxel.grass) {
									setPos(oldPos);
								}		
								break;
							}
						}
					}
				}

			}
			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				oldPos.set(position);
				movement.set(app.cam.direction.x,0,app.cam.direction.z);
				movement.crs(app.cam.up);
				movement.nor();
				movement.mul(Gdx.graphics.getDeltaTime()*-10);
				addMovement(movement);
				for (Vector3 vec : box.getCorners()) {
					int pointX = (int) vec.x;
					int pointY = (int) vec.y;
					int pointZ = (int) vec.z;
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						for (Chunk c : app.map.chunks) {
							if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
								if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] .id == Voxel.grass) {
									setPos(oldPos);
								}		
								break;
							}
						}
					}
				}

			}
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				oldPos.set(position);
				movement.set(0,-1*Gdx.graphics.getDeltaTime()*10,0);
				addMovement(movement);
				for (Vector3 vec : box.getCorners()) {
					int pointX = (int) vec.x;
					int pointY = (int) vec.y;
					int pointZ = (int) vec.z;
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						for (Chunk c : app.map.chunks) {
							if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
								if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] .id == Voxel.grass) {
									jumping = true;
									forceUp = 5;
								}		
								break;
							}
						}
					}
				}
				position.set(oldPos);
			}			
			if (jumping) {
				oldPos.set(position);
				movement.set(0,Gdx.graphics.getDeltaTime()*10*forceUp,0);
				addMovement(movement);
				for (Vector3 vec : box.getCorners()) {
					int pointX = (int) vec.x;
					int pointY = (int) vec.y;
					int pointZ = (int) vec.z;
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						for (Chunk c : app.map.chunks) {
							if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
								if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] .id == Voxel.grass) {
									setPos(oldPos);
									jumping = false;
								}		
								break;
							}
						}
					}
					forceUp -= 2.5f*Gdx.graphics.getDeltaTime();
					if (forceUp < 0) {
						jumping = false;
						forceUp = 0;
					}
				}

			} else {
				oldPos.set(position);
				movement.set(0,Gdx.graphics.getDeltaTime()*10*forceUp,0);
				forceUp -= 2.5f*Gdx.graphics.getDeltaTime();
				addMovement(movement);
				for (Vector3 vec : box.getCorners()) {
					int pointX = (int) vec.x;
					int pointY = (int) vec.y;
					int pointZ = (int) vec.z;
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						for (Chunk c : app.map.chunks) {
							if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
								if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] .id == Voxel.grass) {
									setPos(oldPos);
									forceUp = 0;
								}		
								break;
							}
						}
					}
				}
			}
		}
	}
}
