package network;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import network.Packet.AddPlayer;
import network.Packet.Added;
import network.Packet.BlockUpdate;
import network.Packet.Disconnected;
import network.Packet.Reject;
import network.Packet.Update;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class GameClient{
	Client client;
	Player[] players;
	Array<BlockUpdate> chunkstoupdate;
	Array<BlockUpdate> servedchunks;
	Semaphore listsafe;
	String name;
	Update update;
	BlockUpdate bupdate;
	public int id;

	public GameClient(){
		client = new Client();
		client.start();
		players = new Player[10];
		update = new Update();
		bupdate = new BlockUpdate();
		listsafe = new Semaphore(1);
		chunkstoupdate = new Array<BlockUpdate>();
		servedchunks = new Array<BlockUpdate>();

		Packet.register(client);
		name = "anon";
		try {
			client.connect(5000, "127.0.0.1", 54555, 54778);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AddPlayer player = new AddPlayer();
		player.name = name;
		client.sendTCP(player);

		client.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if(object instanceof Added){
					Added response = (Added)object;
					id = response.id;
					System.out.println("i got id " + id);
				}
				else if ( object instanceof AddPlayer){
					AddPlayer response = (AddPlayer)object;
					Player newPlayer = new Player(response.name);
					System.out.println("box " + response.id + "added");
					newPlayer.id = response.id;
					newPlayer.posX = response.startx;
					newPlayer.posY = response.starty;
					newPlayer.posZ = response.startz;
					players[response.id] = newPlayer;

				}
				else if (object instanceof Update) {
					Update response = (Update)object;
					if(players[response.id] != null){
						players[response.id].posX = response.x;
						players[response.id].posY = response.y;			
						players[response.id].posZ = response.z;	
					}
				}

				else if (object instanceof Disconnected){
					System.out.println("DCCC");
					players[((Disconnected) object).id] = null;
				}
				else if (object instanceof Reject){
					System.out.println("Rejected :(");
					System.exit(0);
				}
				else if(object instanceof BlockUpdate){
					BlockUpdate response = (BlockUpdate)object;
					listsafe.tryAcquire();
					chunkstoupdate.add(response);
					listsafe.release();
				}
			}
		});		
	}

	public Player[] getPlayers(){
		return players;
	}

	public Array<BlockUpdate> getChunks(){	
		servedchunks.clear();
		listsafe.tryAcquire();
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

	public void sendMessage(Player x){
		update.id = id;
		update.x = x.posX;
		update.y = x.posY;
		update.z = x.posZ;
		client.sendTCP(update);
	}
}
