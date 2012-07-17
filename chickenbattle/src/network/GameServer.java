package network;

import java.io.IOException;
import java.util.HashMap;

import network.Packet.AddPlayer;
import network.Packet.Added;
import network.Packet.BlockUpdate;
import network.Packet.Disconnected;
import network.Packet.Reject;
import network.Packet.Update;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer {
	Server server;
	Player[] gameState;
	HashMap<Connection,Integer> connectionIDs;
	Connection[] connections;
	Update toSend;
	BlockUpdate btoSend;
	int startx,starty,startz;
	public int ids;

	public GameServer () throws IOException {
		server = new Server();
		gameState = new Player[10];
		connections = new Connection[10];
		toSend = new Update();
		connectionIDs = new HashMap<Connection,Integer>();
		server.start();
		Packet.register(server);

		server.bind(54555, 54778);   
		startx = 0;
		starty = 0;
		startz = 0;

		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof AddPlayer){
					AddPlayer received = (AddPlayer)object;
					System.out.println("AddPlayer From : " + received.name);
					if(fixId()){
						Added reply = new Added();					
						reply.id = ids;
						System.out.println("giving" + received.name + "id: " + ids);
						connection.sendTCP(reply);
						AddPlayer oldPlayers = new AddPlayer();
						for(int i=0; i <gameState.length; i++){
							if(gameState[i] != null){
								System.out.println("mywife");
								oldPlayers.id = i;
								oldPlayers.startx = gameState[i].posX;
								oldPlayers.starty = gameState[i].posY;
								oldPlayers.startz = gameState[i].posZ;
								connection.sendTCP(oldPlayers);
							}
						}
						gameState[ids] = new Player(received.name);
						connections[ids] = connection; 
						connectionIDs.put(connection, ids);

						AddPlayer newPlayer = new AddPlayer();
						newPlayer.id = ids;
						newPlayer.startx = startx;
						newPlayer.starty = starty;
						newPlayer.startz = startz;
						server.sendToAllTCP(newPlayer);
					}
					else{
						connection.sendTCP(new Reject());
					}
				}

				else if (object instanceof Update) {
					Update received = (Update)object;
					toSend.id = received.id;
					toSend.x = received.x;
					toSend.y = received.y;	
					toSend.z = received.z;	
					gameState[received.id].posX = received.x;
					gameState[received.id].posY = received.y;
					gameState[received.id].posZ = received.z;
					server.sendToAllTCP(toSend);
				}
				else if (object instanceof BlockUpdate){
					BlockUpdate received = (BlockUpdate)object;
					btoSend = received;
					server.sendToAllTCP(btoSend);		
				}
			}

			public void disconnected (Connection c) {
				if(connectionIDs.get(c)!= null){
					Disconnected dc = new Disconnected();	
					dc.id = connectionIDs.get(c);
					connectionIDs.remove(c);
					gameState[dc.id] = null;
					connections[dc.id] = null;
					server.sendToAllTCP(dc);
				}
			}
		});
	}

	public boolean fixId(){
		for(int i=0; i < gameState.length; i++){
			if(gameState[i] == null){
				ids = i;
				return true;
			}		 
		}
		return false;
	}
	public static void main (String[] args) throws IOException {
		new GameServer();
		System.out.println("rnning");
	}

}

