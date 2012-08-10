package Screens;
import network.GameClient;
import network.Packet.BlockDamage;
import network.Packet.BlockUpdate;
import network.Packet.ExplosionUpd;
import network.Packet.Message;
import network.Player;
import Map.Chunk;
import Map.Map;
import Map.Voxel;
import Particles.ParticleSystem;
import Spelet.Character;
import Spelet.ExplosionManager;
import Spelet.GameInterface;
import Spelet.LightSource;
import Spelet.Main;
import Spelet.Renderer;
import Spelet.Weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
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
	public Array<ExplosionUpd> newExplosions;
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
	public ExplosionManager explosions;
	public ParticleSystem explosionParticles;
	//public ParticleEffect particle;
	public Application(Main m){
		explosions = new ExplosionManager();
		explosionParticles = new ParticleSystem();
		main = m;
		movement = new Vector3();
		scoreboard = false;
		ch = new Character(m.name);
		ch.setPos(Map.chunkSize*6/2,Map.chunkSize*2/2,Map.chunkSize*6/2);
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
		newExplosions = new Array<ExplosionUpd>();
		map = new Map(true);
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
		//		particle = new ParticleEffect();
		//		particle.setPosition(500, 500);
		//		particle.start();
		//		particle.load(Gdx.files.internal("data/particle/fire"), Gdx.files.internal("data/particle/"));
		bloodTimer = 0;
		if(multiplayer)
			client = m.client;
	}



	public void render() {
		renderer.render(this);
	}
	public void update() {
		explosions.update(this);
		explosionParticles.update(this);
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
					ch.resurrect(Map.chunkSize*6/2,Map.chunkSize*2/2,Map.chunkSize*6/2);			
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

				if(vox.durability <= 0) {
					vox.id = Voxel.nothing;
				}

				if(!chunkstorebuild.contains(c, true))

					chunkstorebuild.add(c);
			}
			chunkdamage.clear();

			newExplosions = client.explosions;
			ExplosionUpd e = null;
			for (int i = 0; i < newExplosions.size; i++) {
				e=newExplosions.get(i);
				explosions.addExplotion(e.x, e.y, e.z, e.cx, e.cy,e.cz);
			}
			if (e != null) {
				explosionParticles.addExplosion(e.cx,e.cy,e.cz);
			}
			newExplosions.clear();

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
			zoom=false;
			ch.weapon = ch.inventory.get(0).weaponID;		
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_2 == arg0) {
			zoom=false;
			ch.weapon = ch.inventory.get(1).weaponID;
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_3 == arg0) {
			zoom=false;
			ch.weapon = ch.inventory.get(2).weaponID;
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_4 == arg0) {
			zoom=false;
			ch.weapon = ch.inventory.get(3).weaponID;
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_5 == arg0) {
			ch.weapon = ch.inventory.get(4).weaponID;
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_6 == arg0) {
			ch.weapon = ch.inventory.get(5).weaponID;
			gi.updateShells(ch.inventory.get(ch.weapon).magBullets);
			gi.swapWeapon();
		} else if (Input.Keys.NUM_9 == arg0){
			ch.setPos(Map.chunkSize*6/2,Map.chunkSize*2/2,Map.chunkSize*6/2);
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
			if(ch.inventory.get(ch.weapon).weaponID == 5){
				ch.inventory.get(ch.weapon).zoomR = true;
			}
		}
		else if(arg3 == Input.Buttons.LEFT ){

			if (ch.inventory.get(ch.weapon).shoot()) {

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

				recoil();

				if(multiplayer){		
					client.sendBullet(point,direction, clientid, ch.inventory.get(ch.weapon).bulletType);
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
			ch.inventory.get(ch.weapon).zoomR = false;
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

		if(zoom){
			angleLeft -= deltaX/100;
			angleUP += deltaY/100;
		}
		else{
			angleLeft -= deltaX/10;
			angleUP += deltaY/10;
		}
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
			cam.direction.set(0,0,-1);
			cam.up.set(0,1,0);
			angleLeft += MathUtils.random(-1, 1);
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
			angleLeft += MathUtils.random(-1, 1);
			angleUP += MathUtils.random(0, 6);
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
