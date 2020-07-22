package com.flyn.game_engine;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.flyn.game_engine.math.SimplexNoise2D;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.window.Window;

public class App {

	private static Window window = new Window();

	public static void main(String[] args) {
		new Thread(() -> {
			window.setWindow("Test", 800, 600);
			window.showWindow();
		}).start();
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
	

	
	public static float test(Vector3f p1, Vector3f p2, Vector3f p3, float x, float y) {
		float xy = x + y;
		float y1 = xy * (p2.y - p1.y) / p2.x + p1.y;
		float y2 = xy * (p3.y - p1.y) / p3.z + p1.y;
		float r = x / xy;
		return r * y1 + (1 - r) * y2;
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
