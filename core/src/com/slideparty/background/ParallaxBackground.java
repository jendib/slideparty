package com.slideparty.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ParallaxBackground extends Background {
	private Array<ParallaxLayer> layers;

	public ParallaxBackground(ParallaxLayer... layers){
        this.layers = new Array<>();
		this.layers.addAll(layers);
	}

    @Override
	public void draw(OrthographicCamera camera, Batch batch) {
        Vector2 origin = new Vector2(camera.position.x - camera.viewportWidth * 0.5f, camera.position.y - camera.viewportHeight * 0.5f);

        for (ParallaxLayer layer : layers) {
            float textureHeight = camera.viewportHeight;
            if (!layer.fill) {
                textureHeight = (float) layer.texture.getHeight() / (float) layer.texture.getWidth() * camera.viewportWidth;
            }
            float parallaxRatio = camera.position.x * layer.ratio;
            batch.draw(layer.texture, origin.x, origin.y, camera.viewportWidth, textureHeight, parallaxRatio, 1, parallaxRatio + 1, 0);
        }
	}
}