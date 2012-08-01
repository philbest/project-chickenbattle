package Screens;
import network.GameClient;
import network.Packet.BlockDamage;
import network.Packet.BlockUpdate;
import network.Packet.Message;
import network.Player;
import Map.Chunk;
import Map.Map;
import Map.Voxel;
import Spelet.Character;
import Spelet.GameInterface;
import Spelet.LightSource;
import Spelet.Main;
import Spelet.Renderer;
import Spelet.StaticAnimations;
import Spelet.StaticVariables;
import Spelet.Weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;


public class Application extends Screen implements InputProcessor{
	public Renderer renderer;
	public Boolean send;
	public PerspectiveCamera cam;
	int draggedX;
	int draggedY;
	float angleUP, angleLeft;
	public Map map;
	public LightSource light;
	public Vector3 from;
	public Vector3 comparevec;
	public Vector3 to;
	public Vector3 oldPos;
	public Character ch;
	public Array<BlockUpdate> chunkstoupdate;
	public Array<Chunk> chunkstorebuild;
	public Array<Message> servermessages;
	public Array<BlockDamage> chunkdamage;
	public boolean zoom;
	Vector3 movement;
	GameClient client;
	public int clientid;
	public Player[] players;
	public GameInterface gi;
	public boolean scoreboard;
	int timer;
	public boolean multiplayer;
	int mptimer;
	public int bloodTimer;
	public Main main;
	public int ping;
	public long recoilTime, recoilAK;
	public ParticleEffect particle;
	public Application(Main m){
		main = m;
		movement = new Vector3();
		scoreboard = false;
		ch = new Character(m.name);
		ch.setPos(7,15,7);
		players = new Player[10];
		oldPos = new Vector3();
		comparevec = new Vector3();
		zoom=false;
		from = new Vector3(0,0,0);
		to = new Vector3(0,0,0);
		light = new LightSource(200,500,16);
		chunkstoupdate = new Array<BlockUpdate>();
		chunkstorebuild = new Array<Chunk>();
		servermessages = new Array<Message>();
		chunkdamage = new Array<BlockDamage>();
		map = new Map();
		send = false;
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.near = 0.1f;
		cam.position.set(0,50,40);
		cam.near = 0.1f;
		cam.update();
		renderer = new Renderer(this);
		gi = new GameInterface(this);
		gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
		multiplayer = true;
		particle = new ParticleEffect();
		particle.setPosition(500, 500);
		particle.start();
		particle.load(Gdx.files.internal("data/particle/fire"), Gdx.files.internal("data/particle/"));
		bloodTimer = 0;
		if(multiplayer)
			client = m.client;
	}



	public void render() {
		renderer.render(this);
	}
	public void update() {
		Gdx.input.setCursorCatched(true);
		ch.update(this);
		map.update();
		gi.update();
		if(bloodTimer > -1000){
			bloodTimer -= Gdx.graphics.getDeltaTime()*1000;
		}
		ch.updateName(((Lobby)main.screens.get(Main.LOBBY)).playerName);
		ch.inventory.get(ch.weapon).update();
		gi.updateWeapon(ch.weapon);
		gi.updateMags(ch.inventory.get(ch.weapon).currentBullets+ch.inventory.get(ch.weapon).magBullets);
		recoilTime = System.currentTimeMillis();
		if(recoilTime - recoilAK > 1000f && ch.inventory.get(ch.weapon).weaponID == 1){
			ch.inventory.get(ch.weapon).crosshair = ch.inventory.get(ch.weapon).akCH[0];
		}
		else if(recoilTime - recoilAK > 500f  && ch.inventory.get(ch.weapon).weaponID == 1){
			ch.inventory.get(ch.weapon).crosshair = ch.inventory.get(ch.weapon).akCH[1];
		}
		else if(recoilTime - recoilAK > 200f && ch.inventory.get(ch.weapon).weaponID == 1){
			ch.inventory.get(ch.weapon).crosshair = ch.inventory.get(ch.weapon).akCH[2];
		}
		//		if(multiplayer && client.dead){
		//			client.dead = false;
		//			ch.resurrect();
		//			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
		//		}
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
			touchDown(draggedX, draggedY, 0, 0);
		movement.set(ch.position);
		movement.add(0,2,0);
		cam.position.set(movement);
		if(zoom){
			cam.fieldOfView = 10;
		} else {
			cam.fieldOfView = 67;
		}
		cam.update();
		mptimer+= Gdx.graphics.getDeltaTime()*1000;
		if(mptimer > 50){
			mptimer = 0;
			send = true;
		}

		if(multiplayer){
			clientid = client.id;
			players = client.getPlayers();
			if(players[client.id] != null){	
				if(players[client.id].dead){
					ch.resurrect();			
					players[client.id].dead = false;
					gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
				}
				players[client.id].posX = ch.position.x; 
				players[client.id].posY = ch.position.y; 
				players[client.id].posZ = ch.position.z;
				players[client.id].dirX = cam.direction.x; 
				players[client.id].dirY = cam.direction.y; 
				players[client.id].dirZ = cam.direction.z;
				players[client.id].box = ch.box;
				ch.updateHealth(players[client.id].hp);
				ch.updateShield(players[client.id].shields);
				
				if(send){
					client.sendMessage(players[client.id],ch.box.getCorners());
					send = false;
				}
				for(int i = servermessages.size-1; i >=0; i--){		
					if(TimeUtils.millis() - servermessages.get(i).created >= 3000){
						servermessages.removeIndex(i);
					}
				}
				servermessages.addAll(client.getMessages());
				gi.updateInitShield(players[client.id].initShield);
			}
			chunkstoupdate.clear();
			chunkstorebuild.clear();
			chunkstoupdate = client.getChunks();

			for(int i=0; i < chunkstoupdate.size; i++ ){
				Chunk c = map.chunks.get(chunkstoupdate.get(i).chunk);
				c.map[chunkstoupdate.get(i).x-c.x*chunkstoupdate.get(i).size][chunkstoupdate.get(i).y-c.y*chunkstoupdate.get(i).size][chunkstoupdate.get(i).z-c.z*chunkstoupdate.get(i).size].id = chunkstoupdate.get(i).modi;
				chunkstorebuild.add(c);
			}
		
			chunkdamage = client.getStructuralDamage();

			for(int i=0;i<chunkdamage.size;i++){
				BlockDamage bd = chunkdamage.get(i);
				Chunk c = map.chunks.get(bd.chunk);

				Voxel vox = c.map[bd.x-c.x*Map.chunkSize][bd.y-c.y*Map.chunkSize][bd.z-c.z*Map.chunkSize];
				vox.durability -= bd.damage;

				Gdx.app.log("block dura iz ", Float.toString(vox.durability));

				if(vox.durability <= 0)
				{
					vox.id = Voxel.nothing;
				}

				if(!chunkstorebuild.contains(c, true))
					chunkstorebuild.add(c);
			}

			chunkdamage.clear();

			for(int i=0; i<chunkstorebuild.size; i++){
				chunkstorebuild.get(i).rebuildChunk();
			}
		}
		gi.updateHealth(ch.health);
		gi.updateBlood(ch.bloodsplatt);
		gi.updateShields(ch.shields);
	}
	@Override
	public boolean keyDown(int arg0) {
		if (Input.Keys.NUM_1 == arg0) {
			ch.weapon = ch.inventory.get(0).weaponID;
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_2 == arg0) {
			ch.weapon = ch.inventory.get(1).weaponID;
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_3 == arg0) {
			ch.weapon = ch.inventory.get(2).weaponID;
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_4 == arg0) {
			ch.weapon = ch.inventory.get(3).weaponID;
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_5 == arg0) {
			ch.weapon = ch.inventory.get(4).weaponID;
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_9 == arg0){
			ch.setPos(30,60,50);
		}
		else if (Input.Keys.TAB == arg0){
			scoreboard = true;
		}
		else if (Input.Keys.R == arg0){
			ch.inventory.get(ch.weapon).reload();
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
		}
		else if(Input.Keys.I == arg0){
			main.setScreen(3);
		}

		return false;
	}
	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		if (Input.Keys.TAB == arg0){
			scoreboard = false;
		}
		return false;
	}
	@Override
	public boolean scrolled(int arg0) {
		int currentweapon = ch.weapon;
		if(arg0 == -1){
			currentweapon+=1;
			if(currentweapon == ch.inventory.size)
				currentweapon=0;
			ch.weapon = ch.inventory.get(currentweapon).weaponID;
		}
		else if(arg0 == 1){
			currentweapon-=1;
			if(currentweapon == -1)
				currentweapon=ch.inventory.size-1;
			ch.weapon = ch.inventory.get(currentweapon).weaponID;
		}
		gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
		gi.swapWeapon();
		return false;
	}
	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		if(arg3 == Input.Buttons.RIGHT ){
			if(ch.inventory.get(ch.weapon).weaponID == 4){
				zoom = true;
				ch.inventory.get(ch.weapon).zoomS = true;
			}
		} else if(ch.inventory.get(ch.weapon).weaponID == 3){		
			if(ch.inventory.get(ch.weapon).shootEMP()){
				ch.inventory.get(ch.weapon).reload();
				gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
				gi.animateShell(ch.inventory.get(ch.weapon).magBullets);
				Vector3 point = new Vector3(cam.getPickRay(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2).origin);
				Vector3 direction = new Vector3(cam.getPickRay(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2).direction);
				from.set(point);
				to.set(from);
				direction.mul(100);
				to.add(direction);
				direction.nor();
				direction.mul(0.5f);
				if(multiplayer){		
					client.sendBullet(point,direction, clientid, Weapon.bullet_emp);
				}
			}
		} else {
			if (ch.inventory.get(ch.weapon).shoot()) {
				gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
				gi.animateShell(ch.inventory.get(ch.weapon).magBullets);
				float range = 0;
				Vector3 point = new Vector3(cam.getPickRay(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2).origin);
				Vector3 direction = new Vector3(cam.getPickRay(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2).direction);
				from.set(point);
				to.set(from);
				direction.mul(100);
				to.add(direction);
				direction.nor();
				direction.mul(0.5f);
				boolean hit = false;
				int pointX = (int) point.x;
				int pointY = (int) point.y;
				int pointZ = (int) point.z;

				recoil();

				if(multiplayer){		
					if(ch.inventory.get(ch.weapon).weaponID == 4){
						client.sendBullet(point,direction, clientid, Weapon.bullet_sniper);
					}
					else{
						client.sendBullet(point,direction, clientid, Weapon.bullet_gun);
					}
				}
				while (!hit && range < 200) {
					range += direction.len();
					point.add(direction);
					pointX = (int) point.x;
					pointY = (int) point.y;
					pointZ = (int) point.z;
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {		
						for (Chunk c : map.chunks) {
							if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
								if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] .id != Voxel.nothing) {
									hit = true;
								}
								break;
							}
						}
					}
				}
				if (hit) {
					if (ch.weapon == Weapon.block) {
						point.sub(direction);
						pointX = (int) point.x;
						pointY = (int) point.y;
						pointZ = (int) point.z;
						if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
							for (int i = 0; i < map.chunks.size; i++){
								Chunk c = map.chunks.get(i);
								if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
									if(multiplayer){
										client.sendChunkUpdate(i, pointX, pointY, pointZ, Map.chunkSize, Voxel.grass);
									} else{
										c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize].id = Voxel.grass;
										c.rebuildChunk();
										break;
									}
								}		
							}
						}
					} else {
						if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
							for (int i = 0; i < map.chunks.size; i++){
								Chunk c = map.chunks.get(i);
								if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
									//TODO Different bullets - different damage?

									int structuralDamage = c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize].damageDone(ch.inventory.get(ch.weapon).bulletType);

									if(multiplayer){
										client.damageChunk(i, pointX, pointY, pointZ, structuralDamage);
									} else {
										c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize].hit(ch.inventory.get(ch.weapon).bulletType);
										if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize].isDead())
											c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize].id = Voxel.nothing;
										c.rebuildChunk();
										break;
									}
								}

							}
						}
					}
				}

			}
		}
		return false;
	}
	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		mouseMoved(arg0,arg1);
		return false;
	}
	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		if(arg3 == Input.Buttons.RIGHT ){
			zoom = false;
			ch.inventory.get(ch.weapon).zoomS = false;
		}
		return false;
	}
	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		if (draggedX == 0)
			draggedX = arg0;
		if (draggedY == 0)
			draggedY = arg1;
		float deltaX = (arg0-draggedX)*2;
		float deltaY = (arg1-draggedY)*2;
		deltaY = deltaY*-1;
		draggedX = arg0;
		draggedY = arg1;
		angleLeft -= deltaX/5;
		angleUP += deltaY/5;
		if (angleUP < -90)
			angleUP = -90;
		if (angleUP > 90)
			angleUP = 90;
		cam.direction.set(0,0,-1);
		cam.up.set(0,1,0);
		cam.rotate(angleUP, 1, 0, 0);
		cam.rotate(angleLeft, 0, 1, 0);
		cam.update();
		return false;
	}
	private void recoil(){
		if(ch.weapon == Weapon.ak){
			cam.direction.set(0,0,-6);
			cam.up.set(0,6,0);
			int temp = MathUtils.random(0, 10);
			angleUP += temp;
			angleLeft += MathUtils.random(-5, 5);
			cam.direction.set(0,0,-1);
			cam.up.set(0,1,0);
			angleUP += MathUtils.random(0, 4);
			cam.rotate(angleUP, 1, 0, 0);
			cam.rotate(angleLeft, 0, 1, 0);
			cam.update();
			recoilAK = System.currentTimeMillis();
		}
		else if(ch.weapon == Weapon.gun ){
			cam.direction.set(0,0,-1);
			cam.up.set(0,1,0);
			angleUP += 2;
			cam.rotate(angleUP, 1, 0, 0);
			cam.rotate(angleLeft, 0, 1, 0);
			cam.update();
		}
		else if(ch.weapon == Weapon.sniper){
			cam.direction.set(0,0,-1);
			cam.up.set(0,1,0);
			angleUP += MathUtils.random(15, 35);
			cam.rotate(angleUP, 1, 0, 0);
			cam.rotate(angleLeft, 0, 1, 0);
			cam.update();
		}
	}
	@Override
	public void enter() {

		ch.inventory.get(ch.weapon).currentCooldown = 200;

		if(multiplayer){
			client = main.client;
			players = new Player[10];
		}
		Gdx.input.setInputProcessor(this);
	}
}
