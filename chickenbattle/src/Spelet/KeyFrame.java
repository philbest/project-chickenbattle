package Spelet;

import com.badlogic.gdx.math.Vector3;

public class KeyFrame {
	public Vector3[] positions;
	public float[] rotationX;
	public float[] rotationZ;
	public KeyFrame(int objects) {
		positions = new Vector3[objects];
		rotationX = new float[objects];
		rotationZ = new float[objects];
	}
	public KeyFrame(String str, int objects) {
		String[] rows = str.split("\n");
		positions = new Vector3[objects];
		rotationX = new float[objects];
		rotationZ = new float[objects];
		for (int i = 0; i < rows.length; i++) {
			String[] data = rows[i].split(" ");
			positions[i] = new Vector3();
			positions[i].x = Float.parseFloat(data[0]);
			positions[i].y = Float.parseFloat(data[1]);
			positions[i].z = Float.parseFloat(data[2]);
			rotationX[i] = Float.parseFloat(data[3]);
			rotationZ[i] = Float.parseFloat(data[4]);
		}
	}
	public String toString() {
		String ret="";
		for (int i = 0; i < positions.length; i++) {
			ret += positions[i].x + " " + positions[i].y + " " + positions[i].z + " " + rotationX[i] + " " + rotationZ[i];
			if (i < positions.length-1)
				ret+="\n";
		}
		return ret;
	}
}
