package com.flyn.game_engine.math;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.entity.object.Box;

public class OctreeRenderer {
	
	public void render(ArrayList<Octree> trees) {
		for(Octree o : trees) {
			for(Box b : o.getModel()) {
				GL30.glBindVertexArray(b.getModel().getVaoID());
				GL20.glEnableVertexAttribArray(0);
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, b.getModel().getVertexCount());
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				GL20.glDisableVertexAttribArray(0);
				GL30.glBindVertexArray(0);
			}
		}
	}

}
