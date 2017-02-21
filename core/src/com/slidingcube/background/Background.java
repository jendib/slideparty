package com.slidingcube.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Base background class.
 *
 * @author bgamard.
 */
public abstract class Background {
    /**
     * Draw the background.
     *
     * @param camera Scene camera
     * @param batch Camera projected batch
     */
    public abstract void draw(OrthographicCamera camera, Batch batch);
}
