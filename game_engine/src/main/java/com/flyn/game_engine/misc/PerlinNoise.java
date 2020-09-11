package com.flyn.game_engine.misc;

import java.util.HashMap;

import com.flyn.game_engine.math.Vector2f;

public class PerlinNoise {
	
	private static final int[] PERM = {151,160,137,91,90,15,
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
	private final static Vector2f[] GRADIENTS = new Vector2f[256];
	
	static {
		float d = (float) (360.0 / 256.0);
		for(int i = 0; i < 256; i++) {
			GRADIENTS[i] = generateGradient(i * d);
		}
	}
	
	private long seed = 0;
	private HashMap<Vector2f, Float> values = new HashMap<>();
	
	private static int fastFloor(float f) {
		return (f >= 0 ? (int) f : (int) f - 1);
	}
	
	private static float lerp(float a, float b, float t) {
		return a + t * (b - a);
	}
	
	private static float easeCurve(float a, float b) {
		float x = a - b;
		return 3 * x * x - 2 * x * x * x;
	}
	
	private static Vector2f generateGradient(float angle) {
		double a = Math.toRadians(angle);
		Vector2f result = new Vector2f((float) Math.cos(a), (float) Math.sin(a));
		return result;
	}
	
	public PerlinNoise() {
		this.seed = System.nanoTime();
	}
	
	public PerlinNoise(long seed) {
		this.seed = seed;
	}
	
	public float getPerlinNoise(float x, float y) {
		Vector2f key = new Vector2f(x, y);
		if(values.containsKey(key)) return values.get(key);		
		int x0 = fastFloor(x), y0 = fastFloor(y), x1 = x0 + 1, y1 = y0 + 1;
		Vector2f o = new Vector2f(x, y), d[] = new Vector2f[4];
		float[] g = new float[4];
		for(int i = 0; i < 4; i++) d[i] = (Vector2f) o.minus(new Vector2f(i % 2 == 0 ? x0 : x1, i / 2 == 0 ? y0 : y1));
		for(int i = 0; i < 4; i++) g[i] = getGradient(i % 2 == 0 ? x0 : x1, i / 2 == 0 ? y0 : y1).dot(d[i]);
		float sx = easeCurve(x, x0), sy = easeCurve(y, y0);
		float a = lerp(g[0], g[1], sx), b = lerp(g[2], g[3], sx), result = lerp(a, b, sy);
		values.put(key, result);
		return result;
	}
	
	private Vector2f getGradient(int x, int y) {
		x += seed;
		y += seed;
		int index = PERM[(x + PERM[(y & 255)] & 255)];
		return GRADIENTS[index];
	}
	
}
