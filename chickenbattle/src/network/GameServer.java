package network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import network.Packet.AddPlayer;
import network.Packet.AddServer;
import network.Packet.Added;
import network.Packet.BlockDamage;
import network.Packet.BlockUpdate;
import network.Packet.Bullet;
import network.Packet.Disconnected;
import network.Packet.ExplosionUpd;
import network.Packet.Hit;
import network.Packet.Message;
import network.Packet.Reject;
import network.Packet.Update;
import network.Packet.UpdateServer;
import Map.Chunk;
import Map.Map;
import Map.Voxel;
import Spelet.StaticVariables;
import Spelet.Weapon;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer {
	Server server;
	Client lobbyconnection;
	public Player[] player;
	Vector3[] bbCorners;
	HashMap<Connection,Integer> connectionIDs;
	Connection[] connections;
	private Semaphore toSendSema;
	private Semaphore btoSendSema;
	private Semaphore bdamageSema;
	private Semaphore updServerSema;
	private Semaphore hittoSendSema;
	private Semaphore broadcastSema;
	private Semaphore exploSema;
	Array<Update> toSend;
	Array<BlockUpdate> btoSend;
	Array<BlockDamage> bdamage;
	Array<UpdateServer> updServer;
	Array<Hit> hittoSend;
	Array<Message> broadcast;
	Array<ExplosionUpd> explo;
	Vector3 point;
	Vector3 direction;
	int startx,starty,startz;
	Map map;
	InetAddress ownIP;
	int pointX,pointY,pointZ;
	Json json;

	String motd;
	int online;
	int playercap;
	int serverMode;
	int ids;
	boolean hit;
	public static final float FALL_DEATH_LIMIT = -50f;

	Thread thread;
	boolean running;
	long timer;
	long lastUpdate;
	long timerOffset;
	public GameServer (String m, int gamemode) throws IOException {
		running = true;
		map = new Map(false);
		server = new Server();

		player = new Player[10];
		connections = new Connection[10];
		ownIP = InetAddress.getLocalHost();
		bbCorners = new Vector3[8];
		json = new Json();

		for(int i=0; i < 8; i++)
			bbCorners[i] = new Vector3(0,0,0);
		point = new Vector3(0,0,0);
		direction = new Vector3(0,0,0);
		toSendSema = new Semaphore(1);
		btoSendSema = new Semaphore(1);
		bdamageSema = new Semaphore(1);
		updServerSema = new Semaphore(1);
		hittoSendSema = new Semaphore(1);
		broadcastSema = new Semaphore(1);
		exploSema = new Semaphore(1);
		toSend = new Array<Update>();
		bdamage = new Array<BlockDamage>();
		btoSend = new Array<BlockUpdate>();
		hittoSend = new Array<Hit>();
		connectionIDs = new HashMap<Connection,Integer>();
		updServer = new Array<UpdateServer>();
		broadcast = new Array<Message>();
		explo = new Array<ExplosionUpd>();
		server.start();
		Packet.register(server);
		server.bind(54555, 54778);   
		startx = 0;
		starty = 0;
		startz = 0;
		lobbyconnection = new Client();
		lobbyconnection.start();
		Packet.register(lobbyconnection);
		//lobbyconnection.connect(5000, "192.168.0.101", 50000, 50002);
		//lobbyconnection.connect(5000, "129.16.21.56", 50000, 50002);
		lobbyconnection.connect(5000, "46.239.100.249", 50000, 50002);

		this.motd =m;
		this.online =0;
		this.playercap = player.length;
		serverMode = gamemode;

		AddServer addS = new AddServer();
		addS.mode = gamemode;
		addS.motd =motd;
		addS.online =online;
		addS.playercap =playercap;
		lobbyconnection.sendTCP(addS);
		//		String addservea = json.toJson(addS);
		//		System.out.println(addservea);

		//		AddServer passing = json.fromJson(AddServer.class, addservea);
		//
		//		System.out.println(passing.motd);

		lobbyconnection.addListener(new Listener() {	
			public void received (Connection connection, Object object) {

				if (object instanceof UpdateServer){
				}
			}
		});

		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof AddPlayer){
					AddPlayer received = (AddPlayer)object;
					System.out.println("Adding player: " + received.name);
					if(fixId()){
						Added reply = new Added();					
						reply.id = ids;
						System.out.println(received.name + "gets id: " + ids);
						connection.sendTCP(reply);
						AddPlayer oldPlayers = new AddPlayer();
						for(int i=0; i <player.length; i++){
							if(player[i] != null){
								oldPlayers.id = i;
								oldPlayers.name = player[i].name;
								oldPlayers.startx = player[i].posX;
								oldPlayers.starty = player[i].posY;
								oldPlayers.startz = player[i].posZ;
								connection.sendTCP(oldPlayers);
							}
						}
						player[ids] = new Player(received.name);
						player[ids].name = received.name;
						player[ids].currentTeam = received.team;
						connections[ids] = connection; 
						connectionIDs.put(connection, ids);

						AddPlayer newPlayer = new AddPlayer();
						newPlayer.id = ids;
						newPlayer.name = received.name;	
						newPlayer.team = received.team;
						newPlayer.startx = startx;
						newPlayer.starty = starty;
						newPlayer.startz = startz;
						server.sendToAllTCP(newPlayer);

						UpdateServer msg = new UpdateServer();
						msg.motd = motd;
						msg.playercap =playercap;
						msg.online = numPlayers();
						try {
							updServerSema.acquire();
							updServer.add(msg);
							updServerSema.release();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					else{
						connection.sendTCP(new Reject());
					}
				}

				else if (object instanceof Update) {
					final Update received = (Update)object;
					Update msg = new Update();
					msg.id = received.id;
					msg.px = received.px;
					msg.py = received.py;	
					msg.pz = received.pz;

					msg.dx = received.dx;
					msg.dy = received.dy;	
					msg.dz = received.dz;

					msg.kills = player[received.id].kills;
					msg.deaths = player[received.id].deaths;
					msg.hp = player[received.id].hp;
					msg.shields = player[received.id].shields;
					msg.name = player[received.id].name;
					msg.lasthit = player[received.id].lasthit;
					msg.lastRegged = player[received.id].lastRegged;
					msg.falldeath = player[received.id].falldeath;
					msg.dead = player[received.id].dead;
					msg.initShield = player[received.id].initShield;
					msg.currentTeam = player[received.id].currentTeam;
					msg.hit = player[received.id].hit;

					player[received.id].posX = received.px;
					player[received.id].posY = received.py;
					player[received.id].posZ = received.pz;

					player[received.id].dirX = received.dx;
					player[received.id].dirY = received.dy;
					player[received.id].dirZ = received.dz;


					bbCorners[0].set(received.x1, received.y1, received.z1);
					bbCorners[1].set(received.x2, received.y2, received.z2);
					bbCorners[2].set(received.x3, received.y3, received.z3);
					bbCorners[3].set(received.x4, received.y4, received.z4);
					bbCorners[4].set(received.x5, received.y5, received.z5);
					bbCorners[5].set(received.x6, received.y6, received.z6);
					bbCorners[6].set(received.x7, received.y7, received.z7);
					bbCorners[7].set(received.x8, received.y8, received.z8);		

					msg.x1 = received.x1;
					msg.y1 = received.y1;
					msg.z1 = received.z1;

					msg.x2 = received.x2;
					msg.y2 = received.y2;
					msg.z2 = received.z2;

					msg.x3 = received.x3;
					msg.y3 = received.y3;
					msg.z3 = received.z3;

					msg.x4 = received.x4;
					msg.y4 = received.y4;
					msg.z4 = received.z4;

					msg.x5 = received.x5;
					msg.y5 = received.y5;
					msg.z5 = received.z5;

					msg.x6 = received.x6;
					msg.y6 = received.y6;
					msg.z6 = received.z6;

					msg.x7 = received.x7;
					msg.y7 = received.y7;
					msg.z7 = received.z7;

					msg.x8 = received.x8;
					msg.y8 = received.y8;
					msg.z8 = received.z8;

					player[received.id].setBox(bbCorners);
					player[received.id].falldeath = false;
					player[received.id].initShield = false;
					player[received.id].dead = false;
					player[received.id].hit = false;

					if(player[received.id].shields < 5){
						long currTime = System.currentTimeMillis();
						if((currTime-player[received.id].lasthit > 6000l && currTime-player[received.id].lastRegged > 2000l)){
							if(player[received.id].shields == 0){
								player[received.id].initShield = true;
							}
							player[received.id].shields++;
							player[received.id].lastRegged = currTime;
						}
					}

					if(player[received.id].posY < FALL_DEATH_LIMIT)
					{

						if(!player[received.id].falldeath){
							player[received.id].deaths += 1;
							player[received.id].hp = 10;
							player[received.id].shields = 5;
							player[received.id].falldeath = true;
							player[received.id].dead = true;
							player[received.id].posY = 50;
							Message bc = new Message();
							bc.type = StaticVariables.falldeath;
							bc.message = player[received.id].name;
							try {
								broadcastSema.acquire();
								broadcast.add(bc);
								broadcastSema.release();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					try {
						toSendSema.acquire();
						toSend.add(msg);
						toSendSema.release();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//server.sendToAllTCP(toSend); // FIX
				}
				else if (object instanceof BlockUpdate){
					BlockUpdate received = (BlockUpdate)object;
					BlockUpdate msg = new BlockUpdate();
					msg.chunk = received.chunk;
					msg.x = received.x;
					msg.y = received.y;
					msg.z = received.z;
					msg.size = received.size;
					msg.modi = received.modi;
					try {
						btoSendSema.acquire();
						btoSend.add(msg);
						btoSendSema.release();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//server.sendToAllTCP(btoSend);//FIX		
				} else if(object instanceof Bullet){
					hit = false;
					Hit msg = new Hit();
					float range = 0;
					Bullet b = (Bullet)object;
					direction.set(b.dx, b.dy, b.dz);
					point.set(b.ox,b.oy,b.oz);
					while (!hit && range < 200) {
						range += direction.len();
						point.add(direction);
						for(int i=0; i < player.length; i++){
							Player compare = player[i];
							if(compare != null && i != b.id){
								if(compare.box.contains(point) && (compare.currentTeam != player[b.id].currentTeam || serverMode == 6)){
									msg.id = i;
									if(b.type == Weapon.bullet_emp){
										compare.shields = 0;
										compare.initShield = true;
										compare.lasthit = System.currentTimeMillis();
									}else{
										if(compare.shields == 0 && b.type == Weapon.bullet_sniper){
											compare.hp = compare.hp-10;
											compare.hit = true;
											compare.lasthit = System.currentTimeMillis();
										}else if(compare.shields > 0 && b.type == Weapon.bullet_sniper){
											compare.shields =compare.shields-1;
											compare.hp = compare.hp-5;
											if(compare.shields == 0){
												compare.initShield = true;
											}
											compare.lasthit = System.currentTimeMillis();
										}else if(compare.shields > 0){
											compare.shields =compare.shields-1;
											if(compare.shields == 0){
												compare.initShield = true;
											}
											compare.lasthit = System.currentTimeMillis();
										}else if(compare.shields == 0 && b.type == Weapon.bullet_gun){
											compare.hp = compare.hp-5;
											compare.hit = true;
											compare.lasthit = System.currentTimeMillis();
										}else{
											compare.hp =compare.hp-1;
											compare.hit = true;
											compare.lasthit = System.currentTimeMillis();
										}

										if(compare.hp <= 0){
											player[b.id].kills += 1;

											compare.dead = true;
											compare.deaths += 1;
											compare.hp = 10;
											compare.shields = 5;
											Message bc = new Message();
											bc.type = StaticVariables.frag;
											bc.created = TimeUtils.millis();
											bc.message = player[b.id].name + "," + compare.name;
											try {
												broadcastSema.acquire();
												broadcast.add(bc);
												broadcastSema.release();
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
										player[i] = compare;
										hit = true;
										try {
											hittoSendSema.acquire();
											hittoSend.add(msg);
											hittoSendSema.release();
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										//server.sendToAllTCP(hittoSend); // FIX
									}
								}
							}
						}
						if(!hit){
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
							if (b.type == Weapon.bullet_block) {
								point.sub(direction);
								pointX = (int) point.x;
								pointY = (int) point.y;
								pointZ = (int) point.z;
								if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
									for (int i = 0; i < map.chunks.size; i++){
										Chunk c = map.chunks.get(i);
										if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {											
											c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize].id = Voxel.grass;
											BlockUpdate bo = new BlockUpdate();
											bo.chunk = i;
											bo.x = pointX;
											bo.y = pointY;
											bo.z = pointZ;
											bo.size = Map.chunkSize;
											bo.modi = Voxel.grass;
											try {
												btoSendSema.acquire();
												btoSend.add(bo);
												btoSendSema.release();
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											//server.sendToAllTCP(btoSend); // FIX
										}
									}		
								}
							} else {
								if (b.type == Weapon.bullet_rocket) {
									int centerX = pointX;
									int centerY = pointY;
									int centerZ = pointZ;
									int radius = 5;
									Vector3 vec = new Vector3(pointX, pointY, pointZ);
									Vector3 vec2 = new Vector3();
									for (int y = centerY-radius; y < centerY+radius; y++) {
										for (int x = centerX-radius; x < centerX+radius; x++) {
											for (int z = centerZ-radius; z < centerZ+radius; z++) {
												vec2.set(x,y,z);
												float distance = vec2.dst(vec);
												if (distance < radius) {
													pointX = x;
													pointY = y;
													pointZ = z;
													if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
														for (int i = 0; i < map.chunks.size; i++){
															Chunk c = map.chunks.get(i);
															if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
																int structuralDamage = c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize].damageDone(b.type);

																Voxel vox = c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize];
																if (vox.id != Voxel.nothing) { 
																	vox.durability -= structuralDamage;

																	if(vox.durability <= 0) {
																		vox.id = Voxel.nothing;
																	}
																	BlockDamage bd = new BlockDamage();
																	bd.chunk = i;
																	bd.x = pointX;
																	bd.y = pointY;
																	bd.z = pointZ;
																	bd.damage = structuralDamage;
																	try {
																		bdamageSema.acquire();
																		bdamage.add(bd);
																		bdamageSema.release();
																	} catch (InterruptedException e) {
																		// TODO Auto-generated catch block
																		e.printStackTrace();
																	}
																	
																	//server.sendToAllTCP(bdamage); // FIX
																	if (distance > radius/2 && MathUtils.random(1,10) >= 8) {
																		ExplosionUpd eu = new ExplosionUpd();
																		eu.x = pointX;
																		eu.y = pointY;
																		eu.z = pointZ;
																		eu.cx = centerX;
																		eu.cy = centerY;
																		eu.cz = centerZ;
																		try {
																			exploSema.acquire();
																			explo.add(eu);
																			exploSema.release();
																		} catch (InterruptedException e) {
																			// TODO Auto-generated catch block
																			e.printStackTrace();
																		}
																		
																		//sdderver.sendToAllTCP(explo);
																	}
																}
															} 
														}
													}
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
												int structuralDamage = c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize].damageDone(b.type);

												Voxel vox = c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize];
												vox.durability -= structuralDamage;

												if(vox.durability <= 0) {
													vox.id = Voxel.nothing;
												}
												BlockDamage bd = new BlockDamage();
												bd.chunk = i;
												bd.x = pointX;
												bd.y = pointY;
												bd.z = pointZ;
												bd.damage = structuralDamage;
												try {
													bdamageSema.acquire();
													bdamage.add(bd);
													bdamageSema.release();
												} catch (InterruptedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
												//server.sendToAllTCP(bdamage); // FIX
											}
										}

									}
								}
							}
						}
					}
				}
			}
			public void disconnected (Connection c) {
				if(connectionIDs.get(c)!= null){
					UpdateServer msg = new UpdateServer();
					Disconnected dc = new Disconnected();	
					dc.id = connectionIDs.get(c);
					connectionIDs.remove(c);
					player[dc.id] = null;
					connections[dc.id] = null;
					server.sendToAllTCP(dc);
					msg.motd = motd;
					msg.playercap =playercap;
					msg.online = numPlayers();
					try {
						updServerSema.acquire();
						updServer.add(msg);
						updServerSema.release();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//lobbyconnection.sendTCP(updServer); // FIX
				}
			}
		});
		thread = new Thread(new ServerThread());
		thread.start();
	}

	public int numPlayers(){
		int c =0;
		for(int i = 0; i < player.length; i++){
			if(player[i] != null)
				c++;
		}
		return c;
	}

	public boolean fixId(){
		for(int i=0; i < player.length; i++){
			if(player[i] == null){
				ids = i;
				return true;
			}		 
		}
		return false;
	}
	public static void main (String[] args) throws IOException {
		try
		{
			new GameServer("Server hosted as standalone", 5);
			System.out.println("Game server is online!");
		}
		catch(Exception e)
		{
			System.out.println(e);
			System.out.println("Server fucked up.");
		}	
	}
	public void kill() {
		running = false;
	}

	private class ServerThread implements Runnable {
		public void run() {
			while (running) {
				if (timer >= 16) {
					int size = 0;
					try {
						bdamageSema.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					size = bdamage.size;
					for (int i = size-1; i >= 0; i--) {
						server.sendToAllTCP(bdamage.get(i));
						bdamage.removeIndex(i);
					}
					bdamageSema.release();

					try {
						toSendSema.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					size = toSend.size;
					for (int i = size-1; i >= 0; i--) {
						server.sendToAllTCP(toSend.get(i));
						toSend.removeIndex(i);
					}
					toSendSema.release();
					
					try {
						btoSendSema.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					size = btoSend.size;
					for (int i = size-1; i >= 0; i--) {
						server.sendToAllTCP(btoSend.get(i));
						btoSend.removeIndex(i);
					}
					btoSendSema.release();
					
					try {
						updServerSema.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					size = updServer.size;
					for (int i = size-1; i >= 0; i--) {
						server.sendToAllTCP(updServer.get(i));
						updServer.removeIndex(i);
					}
					updServerSema.release();
					
					try {
						hittoSendSema.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					size = hittoSend.size;
					for (int i = size-1; i >= 0; i--) {
						server.sendToAllTCP(hittoSend.get(i));
						hittoSend.removeIndex(i);
					}
					hittoSendSema.release();
					
					try {
						exploSema.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					size = explo.size;
					for (int i = size-1; i >= 0; i--) {
						server.sendToAllTCP(explo.get(i));
						explo.removeIndex(i);
					}
					exploSema.release();
					
					try {
						broadcastSema.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					size = broadcast.size;
					for (int i = size-1; i >= 0; i--) {
						server.sendToAllTCP(broadcast.get(i));
						broadcast.removeIndex(i);
					}
					broadcastSema.release();
					
					lastUpdate = System.currentTimeMillis();
					timerOffset = timer-16;
					timer -= 16;
				} else {
					timer = System.currentTimeMillis()-lastUpdate+timerOffset;
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}


}

