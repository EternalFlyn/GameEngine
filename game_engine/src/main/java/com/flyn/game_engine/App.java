package com.flyn.game_engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.flyn.game_engine.basic.Window;
import com.flyn.game_engine.font.FontGenerator;
import com.flyn.game_engine.font.Glyph;
import com.flyn.game_engine.math.Matrix;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector2f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.math.Vector4f;
import com.flyn.game_engine.misc.PerlinNoise;
import com.flyn.game_engine.misc.SimplexNoise2D;
import com.flyn.game_engine.utils.FileUtils;

public class App {

	private static Window window = new Window();

	public static void main(String[] args) {
		new Thread(() -> {
			window.setWindow("Test", 1280, 720);
			window.showWindow();
		}).start();
		
//		new TestFrame();
		
//		PerlinNoise pn = new PerlinNoise();
//		pn.getPerlinNoise(0, 0);
//		long t1 , t2;
//		
//		t1 = System.nanoTime();
//		pn.getPerlinNoise(100, 300);
//		t1 -= System.nanoTime();
//
//		t2 = System.nanoTime();
//		pn.getPerlinNoise(100, 300);
//		t2 -= System.nanoTime();
//		
//		System.out.printf("t1 : %,d t2 : %,d%n", -t1, -t2);
//
//		t1 = System.nanoTime();
//		for(int i = 0; i < 10000; i++) pn.getPerlinNoise(100, 300);
//		t1 -= System.nanoTime();
//		
//		System.out.printf("t1 : %,4f", -t1 / 10000.0f);
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
		private int height = 650, width = 610, noiseSize = 450;
		private JFrame window = new JFrame();
		private BufferedImage simplexNoise = new BufferedImage(noiseSize, noiseSize, BufferedImage.TYPE_INT_ARGB);
		private BufferedImage whiteNoise = new BufferedImage(noiseSize, noiseSize, BufferedImage.TYPE_INT_ARGB);
		private BufferedImage perlinNoise = new BufferedImage(noiseSize, noiseSize, BufferedImage.TYPE_INT_ARGB);
		private BufferedImage fractalNoise = new BufferedImage(noiseSize, noiseSize, BufferedImage.TYPE_INT_ARGB);
		private Font f;
		
		public TestFrame() {
			try {
				f = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/java/font/NaikaiFont-Light.ttf"));
			} catch (FontFormatException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace();
			}
			f = f.deriveFont(Font.PLAIN, 30);
			FontGenerator.addFontFile("Naikai", "src/main/java/font/NaikaiFont-Light.ttf");
			window.setSize(width, height);
			window.setContentPane(this);
			window.setVisible(true);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			for(int i = 0; i < noiseSize; i++) {
				for(int j = 0; j < noiseSize; j++) {
					simplexNoise.setRGB(i, j, getColor(SimplexNoise2D.getSimplexNoiseValue((float) i / 10.0f, (float) j / 10.0f)));
				}
			}
			Random r = new Random();
			for(int i = 0; i < noiseSize; i++) {
				for(int j = 0; j < noiseSize; j++) {
					whiteNoise.setRGB(i, j, getColor(r.nextFloat() * 2 - 1));
				}
			}
			PerlinNoise pn = new PerlinNoise();
			for(int i = 0; i < noiseSize; i++) {
				for(int j = 0; j < noiseSize; j++) {
					perlinNoise.setRGB(i, j, getColor(pn.getPerlinNoise((float) i / 100.0f, (float) j / 100.0f)));
				}
			}
			for(int i = 0; i < noiseSize; i++) {
				for(int j = 0; j < noiseSize; j++) {
					float base = 100, mul = 1;
					float pixel = 0;
					for(int n = 0; n < 5; n++) {
						pixel += pn.getPerlinNoise((float) i / base * mul, (float) j / base * mul) / mul;
						mul *= 2;
					}
					if(pixel > 1) pixel = 1;
					else if(pixel < -1) pixel = -1;
					fractalNoise.setRGB(i, j, getColor(pixel));
				}
			}
		}
		
		private int getColor(float v) {
			int gray = (int) ((v + 1) * 127.5f);
			if(gray < 0 || gray > 255) return Color.RED.getRGB();
			return new Color(gray, gray, gray).getRGB();
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.clearRect(0, 0, window.getWidth(), window.getHeight());
			g.setFont(f);
			long t1, t2;
			String text = "志剛87";
			t1 = System.nanoTime();
			g.drawString(text, 0, noiseSize + 100);
			t1 -= System.nanoTime();
			int offset = 0;
			t2 = System.nanoTime();
			for(Glyph gl : FontGenerator.getGlyphs("Naikai", Font.PLAIN, text)) {
				BufferedImage i = gl.getGlyphImage();
				g.drawImage(i, offset, noiseSize + 150, null);
				offset += i.getWidth();
			}
			t2 -= System.nanoTime();
			System.out.printf("t1 : %,d, t2 : %,d, %f%n", -t1, -t2, (float) (t1) / (float) t2);
			g.drawImage(simplexNoise, 0, 0, null);
			g.drawImage(whiteNoise, 0 + noiseSize * 1, 0, null);
			g.drawImage(perlinNoise, 0 + noiseSize * 2, 0, null);
			g.drawImage(fractalNoise, 0 + noiseSize * 3, 0, null);
		}
	}
	
}
