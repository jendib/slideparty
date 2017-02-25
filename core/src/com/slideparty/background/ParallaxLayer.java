package com.slideparty.background;

import com.badlogic.gdx.graphics.Texture;

public class ParallaxLayer {
    Texture texture;
    boolean fill = false;
    public float ratio;

    public ParallaxLayer(Texture texture, float ratio, boolean fill) {
        this.texture = texture;
        this.ratio = ratio;
        this.fill = fill;
    }
}