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
		kryo.register(Bullet.class);
		kryo.register(Hit.class);
	}

	static public class Update {
		public int id,hp;
		public float x,y,z;
		public float x1,y1,z1,x2,y2,z2,x3,y3,z3,x4,y4,z4,x5,y5,z5,x6,y6,z6,x7,y7,z7,x8,y8,z8;
		public int currentbox;
	}

	static public class AddPlayer {
		public String name;
		public int id;
		public float startx,starty,startz;
	}

	static public class Added {
		public int id;
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

	static public class Bullet{
		public int id;
		public float ox,oy,oz,dx,dy,dz;
	}

	static public class Hit{
		public int id;

	}


}
