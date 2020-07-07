package com.flyn.game_engine.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

}
