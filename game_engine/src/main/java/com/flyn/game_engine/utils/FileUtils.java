package com.flyn.game_engine.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.imageio.ImageIO;

import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.RawModel;

public class FileUtils {
	
	private static HashMap<String, RawModel> models = new HashMap<>();

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

	public static RawModel loadObjFile(String filePath) {
		if(models.containsKey(filePath)) return models.get(filePath);
		HashSet<Integer> NumberList = new HashSet<>();
		ArrayList<Float> verticesArray = new ArrayList<>();
		ArrayList<Vector3f> texturesArray = new ArrayList<>(), normalsArray = new ArrayList<>();
		ArrayList<Integer> indeicsArray = new ArrayList<>();
		float[] vertices = null, textures = null, normals = null;
		int[] indices = null;
		boolean inFaceData = false;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String buffer;
			while((buffer = reader.readLine()) != null) {
				String[] data = buffer.replaceAll("  ", " ").split(" ");
				switch(data[0]) {
				case "v":
					verticesArray.add(Float.parseFloat(data[1]));
					verticesArray.add(Float.parseFloat(data[2]));
					verticesArray.add(Float.parseFloat(data[3]));
					break;
				case "vt":
					texturesArray.add(new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), 0));
					break;
				case "vn":
					normalsArray.add(new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3])));
					break;
				case "f":
					if(!inFaceData) {
						inFaceData = true;
						int length = verticesArray.size();
						vertices = new float[length];
						textures = new float[(int) (2.0f / 3.0f * length)];
						normals = new float[length];
					}
					for(int i = 0; i < 3; i++) {
						String[] faceData = data[i + 1].split("/");
						int vertexNumber = Integer.parseInt(faceData[0]) - 1;
						indeicsArray.add(vertexNumber);
						if(!NumberList.contains(vertexNumber)) {
							NumberList.add(vertexNumber);
							Vector3f textureVector = texturesArray.get(Integer.parseInt(faceData[1]) - 1);
							textures[vertexNumber * 2] = textureVector.x;
							textures[vertexNumber * 2 + 1] = 1 - textureVector.y;
							Vector3f normalVector = normalsArray.get(Integer.parseInt(faceData[2]) - 1);
							normals[vertexNumber * 3] = normalVector.x;
							normals[vertexNumber * 3 + 1] = normalVector.y;
							normals[vertexNumber * 3 + 2] = normalVector.z;
						}
					}
				}
			}
			reader.close();
			Iterator<Float> iter = verticesArray.iterator();
			for(int i = 0; i < vertices.length; i++) vertices[i] = iter.next();
			indices = indeicsArray.stream().mapToInt(i -> i).toArray();
		} catch (NumberFormatException | IOException e) {
			System.err.println("Couldn't find the file : " + filePath);
			e.printStackTrace();
		}
		RawModel model = Loader.loadToVAO(indices, vertices, textures, normals);
		models.put(filePath, model);
		return model;
	}

}
