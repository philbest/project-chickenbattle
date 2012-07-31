package network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

import network.Packet.AddPlayer;
import network.Packet.AddServer;
import network.Packet.Added;
import network.Packet.BlockDamage;
import network.Packet.BlockUpdate;
import network.Packet.Bullet;
import network.Packet.Disconnected;
import network.Packet.Hit;
import network.Packet.Message;
import network.Packet.Reject;
import network.Packet.Update;
import network.Packet.UpdateServer;
import Spelet.StaticVariables;
import Spelet.Weapon;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer {
	Server server;
	Client lobbyconnection;
	Message broadcast;
	public Player[] player;
	Vector3[] bbCorners;
	HashMap<Connection,Integer> connectionIDs;
	Connection[] connections;
	Update toSend;
	Update newN;
	BlockUpdate btoSend;
	BlockDamage bdtoSend;
	UpdateServer updServer;
	Hit hittoSend;
	Vector3 point;
	Vector3 direction;
	int startx,starty,startz;
	InetAddress ownIP;

	String motd;
	int online;
	int playercap;

	int ids;
	boolean hit;
	public static final float FALL_DEATH_LIMIT = -50f;

	public GameServer (String m) throws IOException {
		server = new Server();
		broadcast = new Message();
		player = new Player[10];
		connections = new Connection[10];
		ownIP = InetAddress.getLocalHost();
		bbCorners = new Vector3[8];
		for(int i=0; i < 8; i++)
			bbCorners[i] = new Vector3(0,0,0);
		point = new Vector3(0,0,0);
		direction = new Vector3(0,0,0);
		toSend = new Update();
		hittoSend = new Hit();
		connectionIDs = new HashMap<Connection,Integer>();
		updServer = new UpdateServer();
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
		lobbyconnection.connect(5000, "129.16.21.56", 50000, 50002);

		this.motd =m;
		this.online =0;
		this.playercap = player.length;

		AddServer addS = new AddServer();
		addS.motd =motd;
		addS.online =online;
		addS.playercap =playercap;

		lobbyconnection.sendTCP(addS);

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
						connections[ids] = connection; 
						connectionIDs.put(connection, ids);

						AddPlayer newPlayer = new AddPlayer();
						newPlayer.id = ids;
						newPlayer.name = received.name;	
						newPlayer.startx = startx;
						newPlayer.starty = starty;
						newPlayer.startz = startz;

						updServer.motd = motd;
						updServer.playercap =playercap;
						updServer.online = numPlayers();
						lobbyconnection.sendTCP(updServer);

						server.sendToAllTCP(newPlayer);
					}
					else{
						connection.sendTCP(new Reject());
					}
				}

				else if (object instanceof Update) {
					final Update received = (Update)object;
					toSend.id = received.id;
					toSend.px = received.px;
					toSend.py = received.py;	
					toSend.pz = received.pz;

					toSend.dx = received.dx;
					toSend.dy = received.dy;	
					toSend.dz = received.dz;

					toSend.kills = player[received.id].kills;
					toSend.deaths = player[received.id].deaths;
					toSend.hp = player[received.id].hp;
					toSend.shields = player[received.id].shields;
					toSend.name = player[received.id].name;
					toSend.lasthit = player[received.id].lasthit;
					toSend.lastRegged = player[received.id].lastRegged;
					toSend.falldeath = player[received.id].falldeath;
					toSend.dead = player[received.id].dead;
					toSend.initShield = player[received.id].initShield;

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

					toSend.x1 = received.x1;
					toSend.y1 = received.y1;
					toSend.z1 = received.z1;

					toSend.x2 = received.x2;
					toSend.y2 = received.y2;
					toSend.z2 = received.z2;

					toSend.x3 = received.x3;
					toSend.y3 = received.y3;
					toSend.z3 = received.z3;

					toSend.x4 = received.x4;
					toSend.y4 = received.y4;
					toSend.z4 = received.z4;

					toSend.x5 = received.x5;
					toSend.y5 = received.y5;
					toSend.z5 = received.z5;

					toSend.x6 = received.x6;
					toSend.y6 = received.y6;
					toSend.z6 = received.z6;

					toSend.x7 = received.x7;
					toSend.y7 = received.y7;
					toSend.z7 = received.z7;

					toSend.x8 = received.x8;
					toSend.y8 = received.y8;
					toSend.z8 = received.z8;

					player[received.id].setBox(bbCorners);
					player[received.id].falldeath = false;
					player[received.id].initShield = false;
					player[received.id].dead = false;

					if(player[received.id].shields < 5){
						long currTime = System.currentTimeMillis();
						if((currTime-player[received.id].lasthit > 6000l && currTime-player[received.id].lastRegged > 2000l)){
							System.out.println("Added shield to: " + player[received.id].name);
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
							broadcast.type = StaticVariables.falldeath;
							broadcast.message = player[received.id].name;
							broadCast(broadcast);
						}
					}

					server.sendToAllTCP(toSend);
				}
				else if (object instanceof BlockUpdate){
					BlockUpdate received = (BlockUpdate)object;
					btoSend = received;
					server.sendToAllTCP(btoSend);		
				}
				else if(object instanceof BlockDamage){
					BlockDamage received = (BlockDamage) object;
					bdtoSend = received;
					server.sendToAllTCP(bdtoSend);
				}
				else if(object instanceof Bullet){
					hit = false;

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
								if(compare.box.contains(point)){
									hittoSend.id = i;
									if(b.type == Weapon.bullet_emp){
										compare.shields = 0;
										compare.initShield = true;
										compare.lasthit = System.currentTimeMillis();
									}
									else{
										if(compare.shields == 0 && b.type == Weapon.bullet_sniper){
											compare.hp = compare.hp-5;
											compare.lasthit = System.currentTimeMillis();
										}
										else if(compare.shields > 0){
											compare.shields =compare.shields-1;
											if(compare.shields == 0){
												compare.initShield = true;
											}
											compare.lasthit = System.currentTimeMillis();
										}
										else{
											compare.hp =compare.hp-1;
											compare.lasthit = System.currentTimeMillis();
										}

										if(compare.hp <= 0){
											player[b.id].kills += 1;
											
											compare.dead = true;
											compare.deaths += 1;
											compare.hp = 10;
											compare.shields = 5;

											broadcast.type = StaticVariables.frag;
											broadcast.created = TimeUtils.millis();
											broadcast.message = player[b.id].name + "," + compare.name;
											broadCast(broadcast);
										}
										player[i] = compare;
										hit = true;
										server.sendToAllTCP(hittoSend);
									}
								}
							}
						}
					}
				}
			}

			public void disconnected (Connection c) {
				if(connectionIDs.get(c)!= null){
					Disconnected dc = new Disconnected();	
					dc.id = connectionIDs.get(c);
					connectionIDs.remove(c);
					player[dc.id] = null;
					connections[dc.id] = null;
					server.sendToAllTCP(dc);
					updServer.motd = motd;
					updServer.playercap =playercap;
					updServer.online = numPlayers();
					lobbyconnection.sendTCP(updServer);
				}
			}
		});
	}

	public int numPlayers(){
		int c =0;
		for(int i = 0; i < player.length; i++){
			if(player[i] != null)
				c++;
		}
		return c;
	}

	public void broadCast(Message msg){
		server.sendToAllTCP(msg);
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
			new GameServer("Server hosted as standalone");
			System.out.println("Game server is online!");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Server fucked up.");
		}	
	}

}

