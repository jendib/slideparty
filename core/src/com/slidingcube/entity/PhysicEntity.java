package com.slidingcube.entity;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Base class for physic entities.
 *
 * @author bgamard
 */
public abstract class PhysicEntity {
    protected Body body; // box 2D body

    /**
     * Called when it's time to render the entity using the polygon batch.
     *
     * @param batch Polygon batch
     * @param delta Time passed
     */
    public void renderPolygon(PolygonSpriteBatch batch, float delta) {}

    /**
     * Called when it's time to render the entity using the sprite batch.
     *
     * @param batch Sprite batch
     * @param delta Time passed
     */
    public void render(SpriteBatch batch, float delta) {}

    /**
     * Returns the underlying box 2D body.
     *
     * @return Box 2D body
     */
    public Body getBody() {
        return body;
    }

    /**
     * Returns the entity position.
     *
     * @return PhysicEntity position
     */
    public Vector2 getPosition() {
        return body.getPosition();
    }

    /**
     * Called when an entity touches this entity.
     *
     * @param entity Colliding entity
     * @param fixture Fixture from this entity which collided
     * @param contact Contact points informations
     */
    public void onBeginContact(PhysicEntity entity, Fixture fixture, Contact contact) {}

    /**
     * Called when an entity stops touching this entity.
     *
     * @param entity Colliding entity
     * @param fixture Fixture from this entity which collided
     * @param contact Contact points informations
     */
    public void onEndContact(PhysicEntity entity, Fixture fixture, Contact contact) {}
}
