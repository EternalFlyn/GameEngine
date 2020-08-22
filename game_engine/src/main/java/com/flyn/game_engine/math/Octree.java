package com.flyn.game_engine.math;

import java.util.ArrayList;
import java.util.HashMap;

import com.flyn.game_engine.entity.object.Box;

public class Octree {

	private boolean isParent;
	public Vector3f p1, p2;
	private Vector3f[] points;
	private Octree[] child;
	private ArrayList<Box> boxes;

	public Octree(int vertexCount, float[] data) {
		checkSize(vertexCount, data);
		if(vertexCount <= 8) {
			isParent = false;
			addPoint(vertexCount, data);
		}
		else {
			isParent = true;
			child = new Octree[8];
			split(vertexCount, data);
		}
	}

	private Octree(ArrayList<Vector3f> data, Vector3f p1, Vector3f p2) {
		this.p1 = p1;
		this.p2 = p2;
		if(data.size() <= 8) {
			isParent = false;
			addPoint(data.size(), data);
		}
		else {
			isParent = true;
			child = new Octree[8];
			split(data.size(), data);
		}
	}

	private void checkSize(int vertexCount, float[] data) {
		p1 = new Vector3f(data[0], data[1], data[2]);
		p2 = new Vector3f(data[0], data[1], data[2]);
		int ptr = 3;
		for(int i = 1; i < vertexCount; i++) {
			if(data[ptr] < p1.x()) p1.set("x", data[ptr]);
			else if(data[ptr] > p2.x()) p2.set("x", data[ptr]);
			ptr++;
			if(data[ptr] < p1.y()) p1.set("y", data[ptr]);
			else if(data[ptr] > p2.y()) p2.set("y", data[ptr]);
			ptr++;
			if(data[ptr] < p1.z()) p1.set("z", data[ptr]);
			else if(data[ptr] > p2.z()) p2.set("z", data[ptr]);
			ptr++;
		}
	}

	private void addPoint(int vertexCount, float[] data) {
		points = new Vector3f[vertexCount];
		int ptr = 0;
		for(int i = 0; i < vertexCount; i++) points[i] = new Vector3f(data[ptr++], data[ptr++], data[ptr++]);
	}
	
	private void addPoint(int vertexCount, ArrayList<Vector3f> data) {
		points = new Vector3f[vertexCount];
		for(int i = 0; i < vertexCount; i++) points[i] = data.get(i);
	}
	
	private void split(int vertexCount, float[] data) {
		Vector3f m = new Vector3f((p1.x() + p2.x()) / 2, (p1.y() + p2.y()) / 2, (p1.z() + p2.z()) / 2);
		HashMap<Integer, ArrayList<Vector3f>> splitData = new HashMap<>();
		for(int i = 0; i < 8; i++) splitData.put(i, new ArrayList<>());
		int ptr = 0;
		for(int i = 0; i < vertexCount; i++) {
			Vector3f p = new Vector3f(data[ptr++], data[ptr++], data[ptr++]);
			int n = 0;
			if (p.x() < m.x()) n += 4;
			if (p.y() < m.y()) n += 2;
			if (p.z() < m.z()) n += 1;
			splitData.get(n).add(p);
		}
		createChild(splitData, m);
	}
	
	private void split(int vertexCount, ArrayList<Vector3f> data) {
		Vector3f m = new Vector3f((p1.x() + p2.x()) / 2, (p1.y() + p2.y()) / 2, (p1.z() + p2.z()) / 2);
		HashMap<Integer, ArrayList<Vector3f>> splitData = new HashMap<>();
		for(int i = 0; i < 8; i++) splitData.put(i, new ArrayList<>());
		for(int i = 0; i < vertexCount; i++) {
			Vector3f p = data.get(i);
			int n = 0;
			if (p.x() < m.x()) n += 4;
			if (p.y() < m.y()) n += 2;
			if (p.z() < m.z()) n += 1;
			splitData.get(n).add(p);
		}
		createChild(splitData, m);
	}
	
	private void createChild(HashMap<Integer, ArrayList<Vector3f>> data, Vector3f m) {
		child[0] = new Octree(data.get(0), m, p2);
		child[1] = new Octree(data.get(1), new Vector3f(m.x(), m.y(), p1.z()), new Vector3f(p2.x(), p2.y(), m.z()));
		child[2] = new Octree(data.get(2), new Vector3f(m.x(), p1.y(), m.z()), new Vector3f(p2.x(), m.y(), p2.z()));
		child[3] = new Octree(data.get(3), new Vector3f(m.x(), p1.y(), p1.z()), new Vector3f(p2.x(), m.y(), m.z()));
		child[4] = new Octree(data.get(4), new Vector3f(p1.x(), m.y(), m.z()), new Vector3f(m.x(), p2.y(), p2.z()));
		child[5] = new Octree(data.get(5), new Vector3f(p1.x(), m.y(), p1.z()), new Vector3f(m.x(), p2.y(), m.z()));
		child[6] = new Octree(data.get(6), new Vector3f(p1.x(), p1.y(), m.z()), new Vector3f(m.x(), m.y(), p2.z()));
		child[7] = new Octree(data.get(7), p1, m);
	}
	
	public ArrayList<Box> getModel() {
		if(boxes == null) {
			boxes = new ArrayList<>();
			boxes.add(new Box(p1, p2));
			if(isParent) {
				for(Octree o : child) boxes.addAll(o.getModel());
			}
		}
		return boxes;
	}

}
