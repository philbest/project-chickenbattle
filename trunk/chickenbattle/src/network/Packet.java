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
	}

	static public class Update {
		public int id,x,y,z;
	}

	static public class AddPlayer {
		public String name;
		public int id,startx,starty,startz;
	}

	static public class Added {
		public int id,x,y,z;
	}
	
	static public class Disconnected{
		public int id;
	}
	static public class Reject{
		
	}
	
	static public class BlockUpdate{
		public int chunk,x,y,z,size,modi;
	}


}
