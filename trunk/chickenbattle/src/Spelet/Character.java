package Spelet;
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
	public Character() {
		//charModel = ModelLoaderRegistry.loadKeyframedModel(Gdx.files.internal("data/TankTracks.md5anim"));
//		MD5Model charModel = MD5Loader.loadModel(Gdx.files.internal("data/md5/cg_mesh.md5mesh").read(),false);
//		this.charModel.
		//texture = new Texture(Gdx.files.internal("data/Test.png"), Format.RGB565, true);
		inventory = new Array<Weapon>();
		inventory.add(new Weapon(Weapon.gun));
		inventory.add(new Weapon(Weapon.ak));
		inventory.add(new Weapon(Weapon.block));
		weapon = inventory.get(0).weaponID;
	}
	public void setPos(float x, float y, float z) {

		position = new Vector3(x,y,z);
		model = Cube.cubeMesh;
		box = new BoundingBox();
		meshbox = new BoundingBox();
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

}
