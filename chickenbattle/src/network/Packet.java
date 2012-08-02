package network;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Packet {
	static public final int port = 54556;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(Update.class);
		kryo.register(AddPlayer.class);
		kryo.register(Added.class);
		kryo.register(Disconnected.class);
		kryo.register(Reject.class);
		kryo.register(BlockUpdate.class);
		kryo.register(BlockDamage.class);
		kryo.register(Bullet.class);
		kryo.register(Hit.class);
		kryo.register(AddServer.class);
		kryo.register(UpdateServer.class);
		kryo.register(GetServers.class);
		kryo.register(Message.class);
		kryo.register(ExplosionUpd.class);
	}

	static public class Update {
		public int id,hp, shields, kills,deaths;
		
		//Direction
		public float px,py,pz,dx,dy,dz;
	
		public long lasthit, lastRegged;
		// bounding box
		public float x1,y1,z1,x2,y2,z2,x3,y3,z3,x4,y4,z4,x5,y5,z5,x6,y6,z6,x7,y7,z7,x8,y8,z8;
		public int currentbox, currentTeam;
		public boolean killer, killed, falldeath, initShield,dead, hit;
		public String name;
	}

	static public class AddPlayer {
		public String name;
		public int id, team;
		public float startx,starty,startz;
	}

	static public class Added {
		public int id;
		public float x,y,z;
	}
	static public class ExplosionUpd{
		public float x,y,z;
	}

	static public class Disconnected{
		public int id;
	}
	static public class Reject{

	}

	static public class BlockUpdate{
		public int chunk,x,y,z,size,modi;
	}
	
	static public class BlockDamage{
		public int chunk,x,y,z,damage;
	}

	static public class Bullet{
		public int id;
		public float ox,oy,oz,dx,dy,dz;
		public int type;
	}

	static public class Hit{
		public int id;

	}
	static public class AddServer{
		public String ip;
		public String motd;
		public int mode;
		public int playercap;
		public int online;
	}
	static public class UpdateServer{
		public String motd;
		public int playercap;
		public int online;
	}
	static public class Message{
		public int type;
		public long created;
		public String message;
	}

	static public class GetServers{
	}
}
