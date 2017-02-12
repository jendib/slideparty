package com.slidingcube.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.flowpowered.noise.model.Line;
import com.flowpowered.noise.module.source.Perlin;
import com.slidingcube.constant.ConfigConstants;

public class Ground extends Entity {
    public Ground(World world) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // generate ground line
        ChainShape chainShape = new ChainShape();
        Line line = new Line(new Perlin());
        int width = 1000;
        int twidth = 1;
        float[] chain = new float[width / twidth * 2];
        for (int x = 0; x < width; x += twidth) {
            chain[x / twidth * 2] = x;
            chain[x / twidth * 2 + 1] = (float) line.getValue(x / (double) width) * 1000 - x / 10f - 1000;
        }
        chainShape.createChain(chain);

        fixtureDef.shape = chainShape;
        fixtureDef.restitution = 0;
        fixtureDef.friction = ConfigConstants.GROUND_FRICTION;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        chainShape.dispose();
    }

    @Override
    public void render(Camera camera, Batch batch, float delta) {

    }
}
