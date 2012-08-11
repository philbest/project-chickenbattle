package Particles;

import java.util.Comparator;

public class ParticleComparator implements Comparator<Particle>{
	static ParticleComparator compa;
	public static ParticleComparator getInstance() {
		if (compa == null)
			compa = new ParticleComparator();
		return compa;		
	}
	@Override
	public int compare(Particle arg0, Particle arg1) {
		if (arg0.distance < arg1.distance)
			return -1;
		if (arg0.distance > arg1.distance)
			return 1;
		return 0;
	}

}
