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
	Array<ServerInfo> servers;

	public MasterServer () throws IOException {
		servers = new Array<ServerInfo>();	
		wserver = new Server();
		wserver.start();
		Packet.register(wserver);
		wserver.bind(50000, 50002);   

		wserver.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof AddServer){
					AddServer rec = (AddServer) object;
					rec.ip = connection.getRemoteAddressTCP().getAddress().toString();	
					rec.ip = rec.ip.split("/")[1];
					System.out.println(rec.ip);
					if(rec.ip.equals("127.0.0.1") ||rec.ip.equals("localhost") ){
						rec.ip = "129.16.21.56";
					}
					ServerInfo toAdd = new ServerInfo();
					toAdd.con = connection;
					toAdd.ip = rec.ip;
					toAdd.motd = rec.motd;
					toAdd.online = rec.online;
					toAdd.playercap = rec.playercap;		
					servers.add(toAdd);
					System.out.println("MasterServer added a server!" + rec.ip );
				}
				else if(object instanceof UpdateServer){
					UpdateServer rec = (UpdateServer) object;
					for(int i =0; i < servers.size; i++){
						if(servers.get(i).con == connection){
							servers.get(i).Update(rec);
						}
					}
				}
				else if( object instanceof GetServers){
					AddServer toSend = new AddServer();
					for(int i = 0; i < servers.size; i++){
						ServerInfo server = servers.get(i);
						toSend.ip = server.ip;
						toSend.motd = server.motd;
						toSend.online = server.online;
						toSend.playercap = server.playercap;
						connection.sendTCP(toSend);
					}
				}
			}
			public void disconnected (Connection connection) {
				for(int i = servers.size-1; i >= 0; i--){
					if(servers.get(i).con == connection){
						servers.removeIndex(i);
					}
				}
			}
		});
	}

	public int getServers(){
		return servers.size;
	}

	public static class ServerInfo{
		Connection con;
		public String ip;
		public String motd;
		public int playercap;
		public int online;
		public ServerInfo(){		
		}

		public void Update(UpdateServer x){
			this.motd = x.motd;
			this.playercap = x.playercap;
			this.online = x.online;
		}
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

