package com.flyn.game_engine.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileUtils {
	
	private FileUtils() {}
	
	public static String loadAsString(String filePath) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String buffer;
			while((buffer = reader.readLine()) != null) result.append(buffer).append("\n");
			reader.close();
		} catch(IOException e) {
			System.err.println("Couldn't find the file : " + filePath);
			e.printStackTrace();
		}
		return result.toString();
	}
	
	public static int[][] loadImage(String filePath) {
		int width = 0, height = 0;
		int[] pixel = null;
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(filePath));
			width = image.getWidth();
			height = image.getHeight();
			pixel = new int[width * height];
			image.getRGB(0, 0, width, height, pixel, 0, width);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		int[] data = new int[width * height];
		for(int i = 0; i < width * height; i++) {
			int a = (pixel[i] & 0xFF000000) >> 24;
			int r = (pixel[i] & 0xFF0000) >> 16;
			int g = (pixel[i] & 0xFF00) >> 8;
			int b = (pixel[i] & 0xFF);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		return new int[][] {{width, height}, data};
	}

}
