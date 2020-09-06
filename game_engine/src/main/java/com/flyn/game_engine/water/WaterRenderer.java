package com.flyn.game_engine.water;

import java.awt.Color;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.basic.Camera;
import com.flyn.game_engine.basic.Light;
import com.flyn.game_engine.basic.MasterRenderer;
import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.basic.Window;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector2f;
import com.flyn.game_engine.utils.Loader;

public class WaterRenderer {
	
	private static float[] vertices = {-1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
	private static Color skyColor = new Color(94, 134, 193); //Pale Denim
	
	private int dudvTexture, normalTexture;
	private float moveFactor = 0;
	
	private RawModel quad;
	private WaterShader shader = new WaterShader();
	private WaterFrameBuffers fbos;
	
	public WaterRenderer(Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
		this.fbos = fbos;
		shader.enable();
		shader.setProjection(projectionMatrix);
		shader.connectTextureUnit();
		shader.disable();
		dudvTexture = Loader.loadTexture("src/main/java/texture/waterDUDV.png");
		normalTexture = Loader.loadTexture("src/main/java/texture/normalMap.png");
		quad = Loader.loadToVAO(vertices, 2);
	}
	
	public void render(ArrayList<WaterTile> water, Camera camera, Light light) {
		shader.enable();
		shader.setViewPosition(camera);
		shader.setViewPlaneDistance(new Vector2f(MasterRenderer.NEAR_PLANE, MasterRenderer.FAR_PLANE));
		shader.setSkyColor(skyColor);
		shader.setLight(light);
		
		moveFactor += 0.03 * Window.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.setMoveFactor(moveFactor);
		
		bindTextures();
		
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		MasterRenderer.disableCulling();
		for(WaterTile tile : water) {
			shader.setTransformation(tile.getTransformationMatirx());
			shader.setWaveStrength(tile.getWaveStrength());
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		MasterRenderer.enableCulling();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.disable();
	}
	
	public void setProjectionMatrix(Matrix4f projection) {
		shader.enable();
		shader.setProjection(projection);
		shader.disable();
	}
	
	public void remove() {
		shader.remove();
	}
	
	private void bindTextures() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
	}

}
