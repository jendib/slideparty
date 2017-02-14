package com.slidingcube.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ShortArray;
import com.flowpowered.noise.model.Line;
import com.flowpowered.noise.module.source.Perlin;
import com.slidingcube.constant.ConfigConstants;

import java.util.Random;

public class Ground extends Entity {
    private PolygonSprite polySprite;

    public Ground(World world) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // generate ground line
        ChainShape chainShape = new ChainShape();
        Perlin perlin = new Perlin();
        perlin.setSeed(new Random().nextInt());
        Line line = new Line(perlin);
        int width = 1000;
        float[] chain = new float[width * 2 + 4];
        chain[0] = 0;
        chain[1] = -1200;
        chain[width * 2 + 2] = width;
        chain[width * 2 + 3] = -1200;
        for (int x = 0; x < width; x++) {
            chain[x * 2 + 2] = x;
            chain[x * 2 + 3] = (float) line.getValue(x / (double) width) * 1000 - x / 2f - 1000;
        }
        chainShape.createChain(chain);

        fixtureDef.shape = chainShape;
        fixtureDef.restitution = 0;
        fixtureDef.friction = ConfigConstants.GROUND_FRICTION;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        chainShape.dispose();


        // Creating the color filling (but textures would work the same way)
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0x282828FF); // DE is red, AD is green and BE is blue.
        pix.fill();
        Texture textureSolid = new Texture(pix);
        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray triangleIndices = triangulator.computeTriangles(chain);
        PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid), chain, triangleIndices.toArray());
        polySprite = new PolygonSprite(polyReg);
    }

    @Override
    public void renderPolygon(Camera camera, PolygonSpriteBatch batch, float delta) {
        polySprite.draw(batch);
    }
}
