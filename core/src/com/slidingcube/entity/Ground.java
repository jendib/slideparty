package com.slidingcube.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.RepeatablePolygonSprite;
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

/**
 * The ground entity.
 *
 * @author bgamard
 */
public class Ground extends Entity {
    private RepeatablePolygonSprite polySprite; // Sprite to draw the ground

    /**
     * Create a new ground.
     *
     * @param world Box 2D world
     * @param width Width of the ground
     */
    public Ground(World world, int width) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // generate ground line
        float height = 1200;
        ChainShape chainShape = new ChainShape();
        Perlin perlin = new Perlin();
        perlin.setSeed(new Random().nextInt());
        Line line = new Line(perlin);
        float[] chain = new float[width * 2 + 4];
        chain[0] = 0;
        chain[1] = - height;
        chain[width * 2 + 2] = width;
        chain[width * 2 + 3] = - height;
        for (int x = 0; x < width; x++) {
            chain[x * 2 + 2] = x;
            chain[x * 2 + 3] = (float) line.getValue(x / (double) width) * 1000 - x / 2f - 1000;
        }
        chainShape.createChain(chain);

        // ground body
        fixtureDef.shape = chainShape;
        fixtureDef.restitution = 0;
        fixtureDef.friction = ConfigConstants.GROUND_FRICTION;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        chainShape.dispose();

        // texture filling
        Texture texture = new Texture(Gdx.files.internal("snow.jpg"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(texture);
        polySprite = new RepeatablePolygonSprite();
        polySprite.setPolygon(textureRegion, chain, 20f);
    }

    @Override
    public void renderPolygon(PolygonSpriteBatch batch, float delta) {
        polySprite.draw(batch);
    }
}
