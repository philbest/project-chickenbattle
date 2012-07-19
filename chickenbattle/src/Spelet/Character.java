package Spelet;
import Map.Chunk;
import Map.Map;
import Map.Voxel;
import Screens.Application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;


public class Character {
	public Vector3 position;
	public BoundingBox meshbox;
	public BoundingBox box;
	public Mesh model;
	public Matrix4 modelMatrix;
	public int weapon;
	public Array<Weapon> inventory;
	KeyframedModel charModel;
	public boolean jumping;
	public Vector3 movement, oldPos;
	float forceUp;
	boolean hookshotting;
	public Character() {
		hookshotting = false;
		forceUp = 0;
		movement = new Vector3();
		oldPos = new Vector3();
		inventory = new Array<Weapon>();
		inventory.add(new Weapon(Weapon.gun));
		inventory.add(new Weapon(Weapon.ak));
		inventory.add(new Weapon(Weapon.block));
		weapon = inventory.get(0).weaponID;
		position = new Vector3();
		box = new BoundingBox();
		meshbox = new BoundingBox();
		modelMatrix = new Matrix4();
	}
	public void setPos(Application app, float x, float y, float z) {
		model = app.renderer.charModel.subMeshes[0].mesh;
		position.set(x,y,z);
		modelMatrix = new Matrix4();
		modelMatrix.setToTranslation(position);
		model.calculateBoundingBox(meshbox);
		box.set(meshbox);
		box.mul(modelMatrix);
	}
	public void setPos(Vector3 pos) {
		position.set(pos);
		modelMatrix.setToTranslation(position);
		box.set(meshbox);
		box.mul(modelMatrix);
	}
	public void addMovement(Vector3 movement) {
		position.add(movement);
		modelMatrix.setToTranslation(position);
		box.set(meshbox);
		box.mul(modelMatrix);
	}
	public void ressurrect(Application app) {
		setPos(app,30,60,50);
		forceUp = 0;
		jumping = false;
		for (int i = 0; i < inventory.size; i++) {
			inventory.get(i).restart();
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
			if (Gdx.input.isKeyPressed(Input.Keys.R)) {
				inventory.get(weapon).reload();		
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
