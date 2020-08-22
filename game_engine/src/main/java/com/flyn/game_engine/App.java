package com.flyn.game_engine;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.flyn.game_engine.basic.Window;
import com.flyn.game_engine.math.Matrix;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.SimplexNoise2D;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.math.Vector4f;

public class App {

	private static Window window = new Window();

	public static void main(String[] args) {
		new Thread(() -> {
			window.setWindow("Test", 1280, 720);
			window.showWindow();
		}).start();
		
//		Matrix4f m = new Matrix4f();
//		m.elements[0][0] = 0.8033333f;
//		m.elements[1][1] = 1.428148f;
//		m.elements[2][2] = -1.0001999f;
//		m.elements[3][2] = -1.0f;
//		m.elements[2][3] = -0.20002f;
//		System.out.println(m);
//		System.out.println(m.getDet());	
		
//		long t1 = 0;
//		Random r = new Random();
//		int times = 1000000;
//		for(int i = 0; i < times; i++) {
//			Matrix4f m = new Matrix4f();
//			for(int j = 0; j < m.elements.length; j++) m.elements[j] = r.nextFloat();
//			long t = System.nanoTime();
//			m.inverse();
//			t1 += System.nanoTime() - t;
//		}
//		System.out.println((float) (t1) / 1000000000);
//		System.out.println(t1 / times);
		
//		float x = 0.001f, y = 1.000f;
//		Vector3f p1 = new Vector3f(0, 100000, 0);
//		Vector3f p2 = new Vector3f(1, -0, 0);
//		Vector3f p3 = new Vector3f(0, 0, 1);
//		long ta = System.nanoTime();
//		float bc = baryCentric(p2, p3, p1, x, y);
//		long tb = System.nanoTime();
//		float my = test(p1, p2, p3, x, y);
//		long tc = System.nanoTime();
//		System.out.printf("bc : %f, my : %f, d : %f%n", bc, my, bc - my);
//		System.out.printf("t1 : %d, t2 : %d", tb - ta, tc - tb);
//		new Window();
	}
	
	private static class TestFrame extends JPanel {

		private static final long serialVersionUID = 1L;
		private int height = 650, width = 610;
		private JFrame window = new JFrame();
		private float[][] terrains = new float[50][50];
		
		public TestFrame() {
			window.setSize(width, height);
			window.setContentPane(this);
			window.setVisible(true);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			for(int i = 0; i < 50; i++) {
				for(int j = 0; j < 50; j++) {
					terrains[i][j] = SimplexNoise2D.getSimplexNoiseValue((float) i / (float) 10, (float) j / (float) 10);
				}
			}
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.clearRect(0, 0, window.getWidth(), window.getHeight());
			for(int a = 0; a < terrains.length; a++) {
				for(int b = 0; b < terrains[0].length; b++) {
					int x = (int) (terrains[a][b] * 128 + 128);
					Color c;
					try {
						c = new Color(x, x, x);
					} catch(Exception e) {
						c = Color.RED;
//						System.out.printf("x : %d, y : %d, %f%n", a, b, terrains[a][b]);
					}
					g.setColor(c);
					g.fillRoundRect(a * 5, b * 5, 5, 5, 5, 5);
				}
			}
		}
	}
	
}
