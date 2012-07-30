package network;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import network.Packet.AddPlayer;
import network.Packet.AddServer;
import network.Packet.Added;
import network.Packet.BlockDamage;
import network.Packet.BlockUpdate;
import network.Packet.Bullet;
import network.Packet.Disconnected;
import network.Packet.GetServers;
import network.Packet.Hit;
import network.Packet.Message;
import network.Packet.Reject;
import network.Packet.Update;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class GameClient{
	Client client;
	Player[] players;
	Array<BlockUpdate> chunkstoupdate;
	Array<BlockDamage> blockdamage;
	Array<BlockUpdate> servedchunks;

	public Array<AddServer> serverlist;
	Semaphore listsafe;
	Vector3[] bbCorners;
	Array<Message> servermessages;
	Array<Message> servermessagestemp;
	Update update;
	Bullet bullet;
	BlockUpdate bupdate;
	BlockDamage bdamage;
	public int id;
	public boolean dead;
	public String name;

	public GameClient(){
		client = new Client();
		client.start();
		players = new Player[10];
		update = new Update();
		bupdate = new BlockUpdate();
		bbCorners = new Vector3[8];
		for(int i=0; i < 8; i++)
			bbCorners[i] = new Vector3(0,0,0);
		bullet = new Bullet();
		bdamage = new BlockDamage();
		listsafe = new Semaphore(1);
		chunkstoupdate = new Array<BlockUpdate>();
		servedchunks = new Array<BlockUpdate>();
		blockdamage = new Array<BlockDamage>();
		serverlist = new Array<AddServer>();
		servermessages = new Array<Message>();
		servermessagestemp = new Array<Message>();
		Packet.register(client);

		client.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if(object instanceof Added){
					Added response = (Added)object;
					id = response.id;
				}
				else if ( object instanceof AddPlayer){
					AddPlayer response = (AddPlayer)object;
					Player newPlayer = new Player(response.name);
					newPlayer.id = response.id;
					newPlayer.name = response.name;
					newPlayer.posX = response.startx;
					newPlayer.posY = response.starty;
					newPlayer.posZ = response.startz;
					players[response.id] = newPlayer;

				}
				else if (object instanceof Update) {
					Update response = (Update)object;
					if(players[response.id] != null){
						players[response.id].posX = response.px;
						players[response.id].posY = response.py;			
						players[response.id].posZ = response.pz;

						players[response.id].dirX = response.dx;
						players[response.id].dirY = response.dy;			
						players[response.id].dirZ = response.dz;

						players[response.id].hp = response.hp;
						players[response.id].shields = response.shields;
						players[response.id].kills = response.kills;	
						players[response.id].deaths = response.deaths;	
						players[response.id].lasthit = response.lasthit;
						players[response.id].lastRegged = response.lastRegged;
						players[response.id].initShield = response.initShield;
						players[response.id].falldeath = response.falldeath;
						if(players[id].falldeath)
							dead = true;

						bbCorners[0].set(response.x1, response.y1, response.z1);
						bbCorners[1].set(response.x2, response.y2, response.z2);
						bbCorners[2].set(response.x3, response.y3, response.z3);
						bbCorners[3].set(response.x4, response.y4, response.z4);
						bbCorners[4].set(response.x5, response.y5, response.z5);
						bbCorners[5].set(response.x6, response.y6, response.z6);
						bbCorners[6].set(response.x7, response.y7, response.z7);
						bbCorners[7].set(response.x8, response.y8, response.z8);					

						players[response.id].setBox(bbCorners);				
					}
				}

				else if (object instanceof Disconnected){
					players[((Disconnected) object).id] = null;
				}
				else if (object instanceof Reject){
					System.exit(0);
				}
				else if(object instanceof BlockUpdate){
					BlockUpdate response = (BlockUpdate)object;
					try {
						listsafe.acquire();
					} catch (InterruptedException ignored){}
					chunkstoupdate.add(response);
					listsafe.release();
				}

				else if(object instanceof Hit){
					Hit response = (Hit)object;
					players[response.id].lasthit = TimeUtils.millis();
					if(response.id == id){
						if(players[response.id].hp == 1){
							dead = true;
						}
					}
				}
				else if(object instanceof AddServer){
					AddServer response = (AddServer)object;
					serverlist.add(response);
					System.out.println(response.ip);
					System.out.println("filling serverlist");
				}
				else if(object instanceof Message){
					Message response = (Message)object;
					try {
						listsafe.acquire();
					} catch (InterruptedException ignored){}
					response.created = TimeUtils.millis();
					servermessages.add(response);
					listsafe.release();
				}
				else if(object instanceof BlockDamage){
					BlockDamage damage = (BlockDamage) object;
					try {
						listsafe.acquire();
					} catch (InterruptedException ignored){}
					blockdamage.add(damage);
					listsafe.release();
				}
			}
		});		
	}

	public Player[] getPlayers(){
		return players;
	}

	public Array<Message> getMessages(){	
		try {
			listsafe.acquire();
		} catch (InterruptedException ignored){}
		servermessagestemp.clear();
		servermessagestemp.addAll(servermessages);
		servermessages.clear();
		listsafe.release();
		return servermessagestemp;
	}

	public Array<BlockDamage> getStructuralDamage(){
		return blockdamage;
	}
	
	public void getServers(){
		serverlist.clear();
		GetServers request = new GetServers();
		client.sendTCP(request);
	}

	public void Connect(String ip, int tcpport, int udpport){
		try {
			client.connect(5000, ip, tcpport, udpport);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	public void AddPlayer(String name){
		AddPlayer player = new AddPlayer();
		player.name = name;
		client.sendTCP(player);
	}
	public void Disconnect(){
		if(client.isConnected())
			client.close();
	}

	public Array<BlockUpdate> getChunks(){	
		servedchunks.clear();
		try {
			listsafe.acquire();
		} catch (InterruptedException ignored){}
		servedchunks.addAll(chunkstoupdate);
		chunkstoupdate.clear();
		listsafe.release();
		return servedchunks;
	}

	public void sendChunkUpdate(int chunk, int x, int y, int z, int size, int modi){
		bupdate.chunk = chunk;
		bupdate.x = x;
		bupdate.y = y;
		bupdate.z = z;
		bupdate.size = size;
		bupdate.modi = modi;
		client.sendTCP(bupdate);
	}
	
	public void damageChunk(int chunk, int x, int y, int z, int damage)
	{
		bdamage.chunk = chunk;
		bdamage.x = x;
		bdamage.y = y;
		bdamage.z = z;
		bdamage.damage = damage;
		client.sendTCP(bdamage);
	}

	public void sendBullet(Vector3 origin, Vector3 dir, int id, int type){
		bullet.id = id;
		bullet.type = type;

		bullet.ox =origin.x;
		bullet.oy =origin.y;
		bullet.oz =origin.z;

		bullet.dx =dir.x;
		bullet.dy =dir.y;
		bullet.dz =dir.z;

		client.sendTCP(bullet);
	}

	public void Terminate(){
		if(client.isConnected())
			client.close();
		client.stop();
	}


	public void sendMessage(Player x, Vector3[] ch){
		update.id = id;
//		Pos
		update.px = x.posX;
		update.py = x.posY;
		update.pz = x.posZ;
//		Dir
		update.dx = x.dirX;
		update.dy = x.dirY;
		update.dz = x.dirZ;
//		Boundingbox
		update.x1 = ch[0].x;
		update.y1 = ch[0].y;
		update.z1 = ch[0].z;

		update.x2 = ch[1].x;
		update.y2 = ch[1].y;
		update.z2 = ch[1].z;

		update.x3 = ch[2].x;
		update.y3 = ch[2].y;
		update.z3 = ch[2].z;

		update.x4 = ch[3].x;
		update.y4 = ch[3].y;
		update.z4 = ch[3].z;

		update.x5 = ch[4].x;
		update.y5 = ch[4].y;
		update.z5 = ch[4].z;

		update.x6 = ch[5].x;
		update.y6 = ch[5].y;
		update.z6 = ch[5].z;

		update.x7 = ch[6].x;
		update.y7 = ch[6].y;
		update.z7 = ch[6].z;

		update.x8 = ch[7].x;
		update.y8 = ch[7].y;
		update.z8 = ch[7].z;

		client.sendTCP(update);
	}
}
