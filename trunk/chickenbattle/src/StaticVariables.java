import java.util.concurrent.Semaphore;

import com.badlogic.gdx.math.Matrix4;


public class StaticVariables {
	private static Matrix4 tempMat;
	private static Semaphore sema;
	public static void initiate() {
		tempMat = new Matrix4();
		sema = new Semaphore(1);
	}
	public static Matrix4 acquireCalcMat() {
		sema.tryAcquire();
		return tempMat;
	}
	public static void releaseSema() {
		sema.release();
	}
}
