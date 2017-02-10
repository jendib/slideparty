package com.slidingcube;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * @author bgamard.
 */

class Player extends Entity {
    private boolean onGround = false;
    private long lastJump;
    private float helpForce = 0;
    private int index;

    Player(World world, int index) {
        this.index = index;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(4f, 8);
        bodyDef.angularVelocity = 0;
        body = world.createBody(bodyDef);

        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(2.5f, 3);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = 5;
        fixtureDef.friction = 0;
        fixtureDef.restitution = .2f;
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        // box.setUserData(new Box2DSprite(someImage));

        boxShape.dispose();
    }

    void setHelpForce(float helpForce) {
        this.helpForce = helpForce;
    }

    @Override
    public void onContact(Entity entity) {
        // the player is on the ground
        // TODO the ground is below the player
        onGround = true;
    }

    @Override
    public void render(Camera camera) {
        // center on me
        camera.position.set(body.getPosition(), 0);

        // help the last
        body.applyForceToCenter(new Vector2(helpForce * 100, 0), true);

        // apply air drag
        Vector2 v = body.getLinearVelocity();
        float vSqrd = v.len2();
        body.applyForceToCenter(v.nor().scl(- 2f * vSqrd), true);

        camera.update();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean applyForce = false;
        if (screenX < 1000 && screenY < 500 && index == 0) {
            applyForce = true;
        } else if (screenX > 1000 && screenY < 500 && index == 1) {
            applyForce = true;
        } else if (screenX < 1000 && screenY > 500 && index == 2) {
            applyForce = true;
        } else if (screenX > 1000 && screenY > 500 && index == 3) {
            applyForce = true;
        }

        if (applyForce && onGround) {
            onGround = false;
            lastJump = TimeUtils.millis();
            body.applyLinearImpulse(new Vector2(3000, 1500), body.getWorldCenter(), true);
        }
        return applyForce;
    }
}
