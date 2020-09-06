package com.flyn.game_engine.misc;

public class SimplexNoise2D {

	private static class Vector2d {

		double x, y;

		Vector2d(double x, double y) {
			this.x = x;
			this.y = y;
		}

		static double dot(Vector2d v1, Vector2d v2) {
			return v1.x * v2.x + v1.y * v2.y;
		}

	}

	private static final int PERM[] = {151,160,137,91,90,15,
			131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
			190,6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
			88,237,149,56,87,174,20,125,136,171,168,68,175,74,165,71,134,139,48,27,166,
			77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
			102,143,54,65,25,63,161,1,216,80,73,209,76,132,187,208,89,18,169,200,196,
			135,130,116,188,159,86,164,100,109,198,173,186,3,64,52,217,226,250,124,123,
			5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
			223,183,170,213,119,248,152,2,44,154,163,70,221,153,101,155,167,43,172,9,
			129,22,39,253,19,98,108,110,79,113,224,232,178,185,112,104,218,246,97,228,
			251,34,242,193,238,210,144,12,191,179,162,241,81,51,145,235,249,14,239,107,
			49,192,214,31,181,199,106,157,184,84,204,176,115,121,50,45,127,4,150,254,
			138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
	};

	private static final double F = 0.5f * (Math.sqrt(3) - 1), G = (3 - Math.sqrt(3.0)) / 6;

//	private static final Vector2d[] GRADIENTS = {
//			new Vector2d(1, 0), new Vector2d(0, 1),
//			new Vector2d(-1, 0), new Vector2d(0, -1),
//			new Vector2d(0.707, 0.707), new Vector2d(-0.707, 0.707),
//			new Vector2d(0.707, -0.707), new Vector2d(-0.707, -0.707)
//	};
	
	private static final Vector2d[] GRADIENTS = {
			new Vector2d(0.4472135955, 0.894427191), new Vector2d(0.894427191, 0.4472135955),
			new Vector2d(-0.4472135955, 0.894427191), new Vector2d(-0.894427191, 0.4472135955),
			new Vector2d(-0.4472135955, -0.894427191), new Vector2d(-0.894427191, -0.4472135955),
			new Vector2d(0.4472135955, -0.894427191), new Vector2d(0.894427191, -0.4472135955)
	};

	public static float getSimplexNoiseValue(float x, float y) {
		Vector2d skewPoint = ToSquare(new Vector2d(x, y));
		Vector2d p0 = ToTriangle(new Vector2d(Math.floor(skewPoint.x), Math.floor(skewPoint.y)));
		Vector2d d0 = new Vector2d(x - p0.x, y - p0.y);
		double c0 = getComponent(d0, getGradient(skewPoint));
		Vector2d d1;
		double c1;
		if(d0.x > d0.y) {
			d1 = new Vector2d(d0.x - 1 + G, d0.y + G);
			c1 = getComponent(d1, new Vector2d(skewPoint.x + 1, skewPoint.y));
		}
		else {
			d1 = new Vector2d(d0.x + G, d0.y - 1 + G);
			c1 = getComponent(d1, new Vector2d(skewPoint.x, skewPoint.y + 1));
		}
		Vector2d d2 = new Vector2d(d0.x - 1 + 2 * G, d0.y - 1 + 2 * G);
		double c2 = getComponent(d2, getGradient(new Vector2d(skewPoint.x + 1, skewPoint.y + 1)));
//		System.out.printf("dx0 : %f, dx1 : %f, dx2 : %f%n", d0.x, d1.x, d2.x);
//		System.out.printf("dy0 : %f, dy1 : %f, dy2 : %f%n", d0.y, d1.y, d2.y);
		return (float) (70 * (c0 + c1 + c2));
	}

	private static Vector2d ToSquare(Vector2d p) {
		double sr = (p.x + p.y) * F;
		return new Vector2d(p.x + sr, p.y + sr);
	}

	private static Vector2d ToTriangle(Vector2d p) {
		double sr = (p.x + p.y) * G;
		return new Vector2d(p.x - sr, p.y - sr);
	}

	private static Vector2d getGradient(Vector2d seed) {
		int i = (int) Math.floor(seed.x), j = (int) Math.floor(seed.y);
		int index = PERM[(i + PERM[(j & 255)] & 255)] % 8;
		return GRADIENTS[index];
	}

	private static double getComponent(Vector2d distance, Vector2d gradients) {
		double result = 0, t = 0.5 - distance.x * distance.x - distance.y * distance.y;
		if (t >= 0) result = Math.pow(t, 4) * Vector2d.dot(distance, gradients);
//		if(Vector2d.dot(distance, gradients) > 1) System.out.println(Vector2d.dot(distance, gradients));
		return result;
	}

}
