package com.flyn.game_engine.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.render.RawModel;
import com.flyn.game_engine.utils.Loader;

public class SkyboxRenderer {
	
	private static final float SIZE = 500f;
	
	private static final String[] TEXTURE_FILES = {
			"src/main/java/texture/right.png",
			"src/main/java/texture/left.png",
			"src/main/java/texture/top.png",
			"src/main/java/texture/bottom.png",
			"src/main/java/texture/back.png",
			"src/main/java/texture/front.png",
	};
	
	private static final String[] NIGHT_TEXTURE_FILES = {
			"src/main/java/texture/nightRight.png",
			"src/main/java/texture/nightLeft.png",
			"src/main/java/texture/nightTop.png",
			"src/main/java/texture/nightBottom.png",
			"src/main/java/texture/nightBack.png",
			"src/main/java/texture/nightFront.png",
	};
	
	private RawModel cube;
	private int dayTexture, nightTexture;
	
	public SkyboxRenderer(SkyboxShader shader, Matrix4f projectionMatrix) {
		float[] vertices = {        
			    -SIZE,  SIZE, -SIZE,
			    -SIZE, -SIZE, -SIZE,
			     SIZE, -SIZE, -SIZE,
			     SIZE, -SIZE, -SIZE,
			     SIZE,  SIZE, -SIZE,
			    -SIZE,  SIZE, -SIZE,

			    -SIZE, -SIZE,  SIZE,
			    -SIZE, -SIZE, -SIZE,
			    -SIZE,  SIZE, -SIZE,
			    -SIZE,  SIZE, -SIZE,
			    -SIZE,  SIZE,  SIZE,
			    -SIZE, -SIZE,  SIZE,

			     SIZE, -SIZE, -SIZE,
			     SIZE, -SIZE,  SIZE,
			     SIZE,  SIZE,  SIZE,
			     SIZE,  SIZE,  SIZE,
			     SIZE,  SIZE, -SIZE,
			     SIZE, -SIZE, -SIZE,

			    -SIZE, -SIZE,  SIZE,
			    -SIZE,  SIZE,  SIZE,
			     SIZE,  SIZE,  SIZE,
			     SIZE,  SIZE,  SIZE,
			     SIZE, -SIZE,  SIZE,
			    -SIZE, -SIZE,  SIZE,

			    -SIZE,  SIZE, -SIZE,
			     SIZE,  SIZE, -SIZE,
			     SIZE,  SIZE,  SIZE,
			     SIZE,  SIZE,  SIZE,
			    -SIZE,  SIZE,  SIZE,
			    -SIZE,  SIZE, -SIZE,

			    -SIZE, -SIZE, -SIZE,
			    -SIZE, -SIZE,  SIZE,
			     SIZE, -SIZE, -SIZE,
			     SIZE, -SIZE, -SIZE,
			    -SIZE, -SIZE,  SIZE,
			     SIZE, -SIZE,  SIZE
		};
		cube = Loader.loadToVAO(vertices, 3);
		dayTexture = Loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = Loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader.enable();
		shader.connectTextureUnit();
		shader.setProjection(projectionMatrix);
		shader.disable();
	}
	
	public void render() {
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	private void bindTextures() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, dayTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexture);
	}

}
