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
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.math.Matrix;
import com.flyn.game_engine.math.Vector;
import com.flyn.game_engine.math.Vector2f;
import com.flyn.game_engine.math.Vector3f;

public class FileUtils {
	
	private static HashMap<String, RawModel> models = new HashMap<>(), normalMappingModels = new HashMap<>();

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
		HashMap<Vertex, Integer> verticesMap = new HashMap<>();
		ArrayList<Vector3f> verticesArray = new ArrayList<>(), normalsArray = new ArrayList<>();
		ArrayList<Vector2f> texturesArray = new ArrayList<>();
		ArrayList<Integer> indeicsArray = new ArrayList<>();
		int vertexCount = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String buffer;
			while((buffer = reader.readLine()) != null) {
				String[] data = buffer.replaceAll("  ", " ").split(" ");
				switch(data[0]) {
				case "f":
					for(int i = 0; i < 3; i++) {
						String[] faceData = data[i + 1].split("/");
						Vertex v = new Vertex(Integer.parseInt(faceData[0]) - 1, Integer.parseInt(faceData[1]) - 1, Integer.parseInt(faceData[2]) - 1);
						if(!verticesMap.containsKey(v)) verticesMap.put(v, vertexCount++);
						indeicsArray.add(verticesMap.get(v));
					}
					break;
				case "v":
					verticesArray.add(new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3])));
					break;
				case "vt":
					texturesArray.add(new Vector2f(Float.parseFloat(data[1]), Float.parseFloat(data[2])));
					break;
				case "vn":
					normalsArray.add(new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3])));
					break;
				}
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Couldn't find the file : " + filePath);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("Value format error." + filePath);
			e.printStackTrace();
		}
		int[] indices = indeicsArray.stream().mapToInt(i -> i).toArray();
		float[] vertices = new float[verticesMap.size() * 3], textures = new float[verticesMap.size() * 2], normals = new float[verticesMap.size() * 3];
		Iterator<Entry<Vertex, Integer>> vertexIter = verticesMap.entrySet().iterator();
		while(vertexIter.hasNext()) {
			Entry<Vertex, Integer> e = vertexIter.next();
			Vertex v = e.getKey();
			int index = e.getValue();
			vertices[index * 3] = verticesArray.get(v.vertexIndex).x();
			vertices[index * 3 + 1] = verticesArray.get(v.vertexIndex).y();
			vertices[index * 3 + 2] = verticesArray.get(v.vertexIndex).z();
			textures[index * 2] = texturesArray.get(v.textureIndex).x();
			textures[index * 2 + 1] = 1 - texturesArray.get(v.textureIndex).y();
			normals[index * 3] = normalsArray.get(v.normalIndex).x();
			normals[index * 3 + 1] = normalsArray.get(v.normalIndex).y();
			normals[index * 3 + 2] = normalsArray.get(v.normalIndex).z();
		}
		RawModel model = Loader.loadToVAO(indices, vertices, textures, normals);
		models.put(filePath, model);
		return model;
	}
	
	public static RawModel loadNormalMappingObjFile(String filePath) {
		if(normalMappingModels.containsKey(filePath)) return normalMappingModels.get(filePath);
		ArrayList<Vertex> verticesInfo = new ArrayList<>();
		ArrayList<Vector3f> verticesArray = new ArrayList<>(), normalsArray = new ArrayList<>();
		ArrayList<Vector2f> texturesArray = new ArrayList<>();
		ArrayList<Integer> indeicsArray = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String buffer;
			while((buffer = reader.readLine()) != null) {
				String[] data = buffer.replaceAll("  ", " ").split(" ");
				switch(data[0]) {
				case "f":
					Vertex[] vs = new Vertex[3];
					for(int i = 0; i < 3; i++) {
						String[] faceData = data[i + 1].split("/");
						Vertex v = new Vertex(Integer.parseInt(faceData[0]) - 1, Integer.parseInt(faceData[1]) - 1, Integer.parseInt(faceData[2]) - 1);
						int vertexNo = 0;
						if(verticesInfo.contains(v)) {
							vertexNo = verticesInfo.indexOf(v);
							vs[i] = verticesInfo.get(vertexNo);
						}
						else {
							vertexNo = verticesInfo.size();
							vs[i] = v;
							verticesInfo.add(v);
						}
						indeicsArray.add(vertexNo);
					}
					calculateTangents(verticesArray, texturesArray, vs);
					break;
				case "v":
					verticesArray.add(new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3])));
					break;
				case "vt":
					texturesArray.add(new Vector2f(Float.parseFloat(data[1]), Float.parseFloat(data[2])));
					break;
				case "vn":
					normalsArray.add(new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3])));
					break;
				}
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Couldn't find the file : " + filePath);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("Value format error." + filePath);
			e.printStackTrace();
		}
		int[] indicesData = indeicsArray.stream().mapToInt(i -> i).toArray();
		float[] verticesData = new float[verticesInfo.size() * 3];
		float[] texturesData = new float[verticesInfo.size() * 2];
		float[] normalsData = new float[verticesInfo.size() * 3];
		float[] tangentsData = new float[verticesInfo.size() * 3];
		for(int i = 0; i < verticesInfo.size(); i++) {
			Vertex v = verticesInfo.get(i);
			verticesData[i * 3] = verticesArray.get(v.vertexIndex).x();
			verticesData[i * 3 + 1] = verticesArray.get(v.vertexIndex).y();
			verticesData[i * 3 + 2] = verticesArray.get(v.vertexIndex).z();
			texturesData[i * 2] = texturesArray.get(v.textureIndex).x();
			texturesData[i * 2 + 1] = 1 - texturesArray.get(v.textureIndex).y();
			normalsData[i * 3] = normalsArray.get(v.normalIndex).x();
			normalsData[i * 3 + 1] = normalsArray.get(v.normalIndex).y();
			normalsData[i * 3 + 2] = normalsArray.get(v.normalIndex).z();
			Vector3f tangent = v.getTangent();
			tangentsData[i * 3] = tangent.x();
			tangentsData[i * 3 + 1] = tangent.y();
			tangentsData[i * 3 + 2] = tangent.z();
		}
		RawModel model = Loader.loadToVAO(indicesData, verticesData, texturesData, normalsData, tangentsData);
		normalMappingModels.put(filePath, model);
		return model;
	}
	
	private static boolean flag  = false;
	private static void calculateTangents(ArrayList<Vector3f> vertices, ArrayList<Vector2f> textures, Vertex[] index) {
		Vector3f[] vs = new Vector3f[3];
		for(int i = 0; i < 3; i++) vs[i] = vertices.get(index[i].vertexIndex);
		Vector3f deltaPos1 = (Vector3f) vs[1].minus(vs[0]);
		Vector3f deltaPos2 = (Vector3f) vs[2].minus(vs[0]);
		
		Vector2f[] uv = new Vector2f[3];
		for(int i = 0; i < 3; i++) uv[i] = textures.get(index[i].textureIndex);
		Vector2f deltaUv1 = (Vector2f) uv[1].minus(uv[0]);
		Vector2f deltaUv2 = (Vector2f) uv[2].minus(uv[0]);

		float r = 1 / (deltaUv1.x() * deltaUv2.y() - deltaUv1.y() * deltaUv2.x());
		deltaPos1.scale(deltaUv2.y());
		deltaPos2.scale(deltaUv1.y());
		Vector3f tangent = (Vector3f) deltaPos1.minus(deltaPos2);
		tangent.scale(r);
		
		for(Vertex v : index) v.addTangent(tangent);
		
		if(!flag) {
			flag = true;
			for(Vector3f v : vs) System.out.printf("v : %s%n", v.toString());
			System.out.printf("p1 : %s%n", deltaPos1.toString());
			System.out.printf("p2 : %s%n", deltaPos2.toString());
			for(Vector2f v : uv) System.out.printf("v : %s%n", v.toString());
			System.out.printf("uv1 : %s%n", deltaUv1.toString());
			System.out.printf("uv2 : %s%n", deltaUv2.toString());
			
		}
	}
	
	private static class Vertex {
		
		int vertexIndex, textureIndex, normalIndex;
		Vector3f tangent = new Vector3f();
		
		public Vertex(int vertexIndex, int textureIndex, int normalIndex) {
			this.vertexIndex = vertexIndex;
			this.textureIndex = textureIndex;
			this.normalIndex = normalIndex;
		}
		
		public void addTangent(Vector3f tangent) {
			this.tangent = (Vector3f) this.tangent.plus(tangent);
		}
		
		public Vector3f getTangent() {
			return (Vector3f) tangent.normalize();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Vertex) {
				Vertex o = (Vertex) obj;
				return vertexIndex == o.vertexIndex && textureIndex == o.textureIndex && normalIndex == o.normalIndex;
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			int hash = vertexIndex;
			hash = 31 * hash + textureIndex;
			hash = 31 * hash + normalIndex;
			return hash;
		}
		
	}

}
