package com.slidingcube.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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

public class Player extends Entity {
    public int footContactCount;
    private long lastJump;
    private float helpForce = 0;
    private int index;
    private ParticleEffect effect;
    private Box2DSprite box2DSprite;
    private Label label;

    public Player(World world, int index) {
        this.index = index;

        // player body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(20f + 6 * index, 8);
        bodyDef.angularVelocity = 0;
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        // top shape
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(2.5f, 0.3f, new Vector2(0,3), 0);
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
        boxShape.setAsBox(2.5f, 0.3f, new Vector2(0, -3.3f), 0);
        fixtureDef.isSensor = true;
        Fixture footFixture = body.createFixture(fixtureDef);
        footFixture.setUserData(ConfigConstants.FIXTURE_FOOT);
        boxShape.dispose();

        // ball inside
        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(1);
        fixtureDef.isSensor = false;
        fixtureDef.density = 0;
        fixtureDef.shape = ballShape;
        Body ball = world.createBody(bodyDef);
        ball.createFixture(fixtureDef);

        // player effect
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particle/snow.p"), Gdx.files.internal("particle"));
        effect.allowCompletion();

        // player sprite
        box2DSprite = new Box2DSprite(new Texture(Gdx.files.internal("box.png")));

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

    public int getIndex() {
        return index;
    }

    public void setHelpForce(float helpForce) {
        this.helpForce = helpForce;
    }

    @Override
    public void onBeginContact(Entity entity, Fixture fixture, Contact contact) {
        if (ConfigConstants.FIXTURE_FOOT == fixture.getUserData()) {
            // something touched our foot
            footContactCount++;

            if (entity instanceof Ground) {
                effect.start();
            }
        }
    }

    @Override
    public void onEndContact(Entity entity, Fixture fixture, Contact contact) {
        if (ConfigConstants.FIXTURE_FOOT == fixture.getUserData()) {
            // something stopped touching our foot
            footContactCount--;

            if (entity instanceof Ground) {
                effect.allowCompletion();
            }
        }
    }

    @Override
    public void render(Camera camera, Batch batch, float delta) {
        // help the last
        body.applyForceToCenter(new Vector2(helpForce * ConfigConstants.HELP_FORCE_MULTIPLIER, 0), true);

        // apply air drag
        Vector2 velocity = body.getLinearVelocity();
        float sqrtVelocity = velocity.len2();
        body.applyForceToCenter(velocity.nor().scl(- 2f * sqrtVelocity), true);

        // render the sprite
        //box2DSprite.draw(batch, body);

        // render the label
        Vector2 position = body.getWorldPoint(new Vector2(2f, - 3f));
        label.setPosition(position.x - 4.5f, position.y);
        label.draw(batch, 1f);

        // render the effect
        effect.setPosition(position.x, position.y);
        effect.draw(batch, delta);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
        if (applyForce && footContactCount > 0 && lastJump + ConfigConstants.JUMP_INTERVAL < now) {
            lastJump = now;

            // the player jump
            body.applyLinearImpulse(new Vector2(ConfigConstants.JUMP_HORIZONTAL, ConfigConstants.JUMP_VERTICAL),
                    body.getWorldCenter(), true);

            // TODO push entities below us
            /*for (Entity entity : walkingOnEntitySet) {
                entity.getBody().applyLinearImpulse(new Vector2(0, ConfigConstants.JUMP_PUSH),
                        entity.getBody().getWorldCenter(), true);
            }*/
        }
        return applyForce;
    }
}
