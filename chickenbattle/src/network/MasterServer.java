package network;

import java.io.IOException;
import java.util.HashMap;

import network.Packet.AddServer;
import network.Packet.GetServers;

import network.Packet.UpdateServer;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class MasterServer {
	Server wserver;
	Array<AddServer> servers;
	HashMap<Connection,Integer> connectionIDs;
	public MasterServer () throws IOException {
		servers = new Array<AddServer>();	
		connectionIDs = new HashMap<Connection,Integer>();
		wserver = new Server();
		wserver.start();
		Packet.register(wserver);
		wserver.bind(50000, 50002);   

		wserver.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof AddServer){
					AddServer rec = (AddServer) object;
					rec.ip = connection.getRemoteAddressTCP().getHostName();
					if(rec.ip.equals("127.0.0.1") ||rec.ip.equals("localhost") ){
						rec.ip = "192.168.0.101";
					}
					
					servers.add(rec);
					connectionIDs.put(connection, connectionIDs.size());
					System.out.println("MasterServer added a server!" + rec.ip );
				}
				else if(object instanceof UpdateServer){

				}
				else if( object instanceof GetServers){
					
					for(int i = 0; i < servers.size; i++){
						System.out.println("server requests");
						connection.sendTCP(servers.get(i));
					}
				}
				
				
			}
			public void disconnected (Connection c) {
				if(connectionIDs.get(c)!= null){				
					servers.removeIndex(connectionIDs.get(c));
					connectionIDs.remove(c);
				}
			}
		});
	}

	public int getServers(){
		return servers.size;
	}

	public static void main (String[] args) throws IOException {
		try
		{
			MasterServer serv = new MasterServer();
			System.out.println("MasterServer is online!" );
		}
		catch(Exception e)
		{
			System.out.println("Server fucked up.");
		}	
	}


}

