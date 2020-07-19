package com.flyn.game_engine;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.flyn.game_engine.math.SimplexNoise2D;
import com.flyn.game_engine.window.Window;

public class App {

	private static Window window = new Window();

	public static void main(String[] args) {
		new Thread(() -> {
			window.setWindow("Test", 800, 600);
			window.showWindow();
		}).start();
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
