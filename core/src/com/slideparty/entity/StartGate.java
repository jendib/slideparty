package com.slideparty.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.slideparty.constant.ConfigConstants;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * The start gate entity.
 *
 * @author bgamard
 */
public class StartGate extends PhysicEntity {
    private Box2DSprite sprite; // start gate sprite

    public StartGate(World world, Ground ground) {
        // box 2d body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(ConfigConstants.GROUND_FLAT_WIDTH,
                ground.getHeightAt(ConfigConstants.GROUND_FLAT_WIDTH));
        body = world.createBody(bodyDef);

        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(2f, 30f, new Vector2(0, 30f), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;
        body.createFixture(fixtureDef);

        // sprite
        sprite = new Box2DSprite(new Texture(Gdx.files.internal("trunk.png")));
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        sprite.draw(batch, body);
    }
}
