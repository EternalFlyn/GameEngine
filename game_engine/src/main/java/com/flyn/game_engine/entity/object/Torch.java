package com.flyn.game_engine.entity.object;

import com.flyn.game_engine.basic.ExecuteInterface;
import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.basic.Texture;
import com.flyn.game_engine.basic.Window;
import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.utils.FileUtils;
import com.flyn.game_engine.utils.Loader;

public class Torch extends Entity implements ExecuteInterface {
	
	private static RawModel model = FileUtils.loadObjFile("src/main/java/model/poppy.obj");
	private static Texture texture = new Texture(Loader.loadTexture("src/main/java/texture/torch.png"), 1, 7);
	
	public Torch(Vector3f position, Vector3f rotation, Vector3f scale) {
		super(model, texture, position, rotation, scale);
		texture.setTransparency(true);
		texture.setUsedFakeLight(true);
		Window.addExecuteListener(this);
	}

	@Override
	public boolean execute(long time) {
		setTextureIndex((int) (time / 250) % 7);
		return ExecuteInterface.REPEAT_EXECUTE;
	}

}
