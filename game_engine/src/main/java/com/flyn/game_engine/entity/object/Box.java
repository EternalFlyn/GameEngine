package com.flyn.game_engine.entity.object;

import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.utils.Loader;

public class Box {
	
	private RawModel model;
	
	public Box(Vector3f p1, Vector3f p2) {
		float[] vertices = {        
			    p1.x(), p2.y(), p1.z(),
			    p1.x(), p1.y(), p1.z(),
			    p2.x(), p1.y(), p1.z(),
			    p2.x(), p1.y(), p1.z(),
			    p2.x(), p2.y(), p1.z(),
			    p1.x(), p2.y(), p1.z(),

			    p1.x(), p1.y(), p2.z(),
			    p1.x(), p1.y(), p1.z(),
			    p1.x(), p2.y(), p1.z(),
			    p1.x(), p2.y(), p1.z(),
			    p1.x(), p2.y(), p2.z(),
			    p1.x(), p1.y(), p2.z(),

			    p2.x(), p1.y(), p1.z(),
			    p2.x(), p1.y(), p2.z(),
			    p2.x(), p2.y(), p2.z(),
			    p2.x(), p2.y(), p2.z(),
			    p2.x(), p2.y(), p1.z(),
			    p2.x(), p1.y(), p1.z(),

			    p1.x(), p1.y(), p2.z(),
			    p1.x(), p2.y(), p2.z(),
			    p2.x(), p2.y(), p2.z(),
			    p2.x(), p2.y(), p2.z(),
			    p2.x(), p1.y(), p2.z(),
			    p1.x(), p1.y(), p2.z(),

			    p1.x(), p2.y(), p1.z(),
			    p2.x(), p2.y(), p1.z(),
			    p2.x(), p2.y(), p2.z(),
			    p2.x(), p2.y(), p2.z(),
			    p1.x(), p2.y(), p2.z(),
			    p1.x(), p2.y(), p1.z(),

			    p1.x(), p1.y(), p1.z(),
			    p1.x(), p1.y(), p2.z(),
			    p2.x(), p1.y(), p1.z(),
			    p2.x(), p1.y(), p1.z(),
			    p1.x(), p1.y(), p2.z(),
			    p2.x(), p1.y(), p2.z()
		};
		model = Loader.loadToVAO(vertices, 3);
	}
	
	public RawModel getModel() {
		return model;
	}

}
