package com.slidingcube.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.slidingcube.constant.ConfigConstants;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * The player entity.
 *
 * @author bgamard
 */
public class Player extends PhysicEntity {
    private int footContactCount; // Number of contact with the ground
    private long lastJumpTime; // Last jump time
    private float helpForce = 0; // Help force
    private int index; // Index of this player
    private ParticleEffect effect; // Ground particles effect
    private Vector2 groundContactPosition; // Contact between the player and the ground
    private Box2DSprite sprite; // Player sprite
    private Label label; // Index label

    private transient Vector2 helpForceVector = new Vector2(); // Help force vector
    private transient Vector2 labelPositionVector = new Vector2(2f, - 3f); // Position of the label

    /**
     * Create a new player.
     *
     * @param world Box 2D world
     * @param index Player index
     */
    public Player(World world, int index) {
        this.index = index;

        // player body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // TODO Get the real ground initial position
        bodyDef.position.set(5f + 6 * index, 1000);
        bodyDef.angularVelocity = 0;
        body = world.createBody(bodyDef);

        // top shape
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(2.5f, 0.3f, new Vector2(0, 3), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = ConfigConstants.PLAYER_DENSITY;
        fixtureDef.friction = 0;
        fixtureDef.restitution = .2f;
        body.createFixture(fixtureDef);

        // left shape
        boxShape.setAsBox(2.7f, 0.3f, new Vector2(2.2f, 0), MathUtils.degreesToRadians * 90);
        body.createFixture(fixtureDef);

        // right shape
        boxShape.setAsBox(2.7f, 0.3f, new Vector2(-2.2f, 0), MathUtils.degreesToRadians * 90);
        body.createFixture(fixtureDef);

        // bottom shape
        boxShape.setAsBox(2.5f, 0.3f, new Vector2(0, -3), 0);
        body.createFixture(fixtureDef);

        // player foot
        boxShape.setAsBox(3.6f, 4.2f);
        fixtureDef.isSensor = true;
        fixtureDef.density = 0; // the foot don't weight anything
        Fixture footFixture = body.createFixture(fixtureDef);
        footFixture.setUserData(ConfigConstants.FIXTURE_FOOT_ID);
        boxShape.dispose();

        // ball inside
        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(1);
        fixtureDef.isSensor = false;
        fixtureDef.density = 0; // the ball don't weight anything
        fixtureDef.shape = ballShape;
        Body ball = world.createBody(bodyDef);
        ball.createFixture(fixtureDef);

        // player effect
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particle/snow.p"), Gdx.files.internal("particle"));
        effect.allowCompletion();

        // player sprite
        sprite = new Box2DSprite(new Texture(Gdx.files.internal("box.png")));

        // label
        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = new BitmapFont(Gdx.files.internal("font/debug.fnt"),
                Gdx.files.internal("font/debug.png"),
                false, false);
        label1Style.fontColor = Color.WHITE;
        label = new Label(null, label1Style);
        label.setSize(5f, 6f);
        label.setFontScale(0.05f);
        label.setAlignment(Align.center);
        label.setText(Integer.toString(index + 1));

    }

    /**
     * Returns the player index.
     *
     * @return Player index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set the base help force.
     *
     * @param helpForce Help force
     */
    public void setHelpForce(float helpForce) {
        this.helpForce = helpForce;
    }

    @Override
    public void onBeginContact(PhysicEntity entity, Fixture fixture, Contact contact) {
        if (ConfigConstants.FIXTURE_FOOT_ID == fixture.getUserData()) {
            // something touched our foot
            footContactCount++;
        } else {
            if (entity instanceof Ground) {
                // the actual player is touching the ground, show some particles
                effect.start();
                if (contact.getWorldManifold().getNumberOfContactPoints()> 0) {
                    groundContactPosition = contact.getWorldManifold().getPoints()[0];
                }
            }
        }
    }

    @Override
    public void onEndContact(PhysicEntity entity, Fixture fixture, Contact contact) {
        if (ConfigConstants.FIXTURE_FOOT_ID == fixture.getUserData()) {
            // something stopped touching our foot
            footContactCount--;
        } else {
            // the actual player stopped touching the ground
            if (entity instanceof Ground) {
                effect.allowCompletion();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        // help the last
        helpForceVector.x = helpForce * ConfigConstants.HELP_FORCE_MULTIPLIER;
        body.applyForceToCenter(helpForceVector, true);

        // apply air drag
        Vector2 velocity = body.getLinearVelocity();
        float sqrtVelocity = velocity.len2();
        body.applyForceToCenter(velocity.nor().scl(- 2f * sqrtVelocity), true);

        // render the sprite
        // sprite.draw(batch, body);

        // render the label
        Vector2 position = body.getWorldPoint(labelPositionVector);
        label.setPosition(position.x - 4.5f, position.y);
        label.draw(batch, 1f);

        // render the effect
        if (groundContactPosition != null) {
            effect.setPosition(groundContactPosition.x, groundContactPosition.y);
            effect.draw(batch, delta);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO GameScreen should handle touch events
        boolean applyForce = false;
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        if (screenX < width / 2 && screenY < height / 2 && index == 0) {
            applyForce = true;
        } else if (screenX > width / 2 && screenY < 500 && index == 1) {
            applyForce = true;
        } else if (screenX < width / 2 && screenY > 500 && index == 2) {
            applyForce = true;
        } else if (screenX > width / 2 && screenY > 500 && index == 3) {
            applyForce = true;
        }

        long now = TimeUtils.millis();
        if (applyForce && footContactCount > 0 && lastJumpTime + ConfigConstants.JUMP_INTERVAL < now) {
            lastJumpTime = now;

            // the player jump
            body.applyLinearImpulse(new Vector2(ConfigConstants.JUMP_HORIZONTAL, ConfigConstants.JUMP_VERTICAL),
                    body.getWorldCenter(), true);

            // TODO push entities below us
            /*for (PhysicEntity entity : walkingOnEntitySet) {
                entity.getBody().applyLinearImpulse(new Vector2(0, ConfigConstants.JUMP_PUSH),
                        entity.getBody().getWorldCenter(), true);
            }*/
        }
        return applyForce;
    }
}
