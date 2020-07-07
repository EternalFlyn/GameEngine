package com.flyn.game_engine.render;

public class RawModel {
	
	private int vaoID;
	private int indicesID;
	private int vertexCount;
	
	public RawModel(int vaoID, int indicesID, int vertexCount) {
		this.vaoID = vaoID;
		this.indicesID = indicesID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getIndicesID() {
		return indicesID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

}
