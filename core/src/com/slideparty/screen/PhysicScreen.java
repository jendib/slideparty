package com.slideparty.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.slideparty.background.Background;
import com.slideparty.constant.ConfigConstants;
import com.slideparty.entity.PhysicEntity;
import com.slideparty.renderer.Box2dDebugRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Base game screen.
 *
 * @author bgamard
 */
public class PhysicScreen extends BaseScreen {
    private Box2dDebugRenderer debugRenderer; // box 2d debug renderer
    protected World world; // box 2d world
    private SpriteBatch batch; // batch projected on the camera matrix
    private PolygonSpriteBatch polyBatch; // batch for polygons
    protected OrthographicCamera camera; // 2d camera
    private List<PhysicEntity> entityList = new ArrayList<>(); // physic entities tracked in the scene
    private Background background; // Screen background (optional)

    /**
     * Create a new screen.
     *
     * @param game Calling game
     */
    public PhysicScreen(Game game) {
        super(game);
    }

    /**
     * Add a new physic entity to the scene.
     *
     * @param entity Physic entity
     * @return The entity
     */
    protected PhysicEntity addEntity(PhysicEntity entity) {
        entity.getBody().setUserData(entity);
        entityList.add(entity);
        return entity;
    }

    /**
     * Remove a physic entity from the scene.
     *
     * @param entity Physic entity to remove
     */
    protected void removeEntity(PhysicEntity entity) {
        world.destroyBody(entity.getBody());
        entityList.remove(entity);
    }

    /**
     * Set the background.
     *
     * @param background Screen background
     */
    protected void setBackground(Background background) {
        this.background = background;
    }

    @Override
    public void show() {
        entityList.clear();
        batch = new SpriteBatch();
        polyBatch = new PolygonSpriteBatch();
        camera = new OrthographicCamera();
        if (ConfigConstants.DEBUG) {
            debugRenderer = new Box2dDebugRenderer(true, true, false, true, false, false);
        }
        world = new World(new Vector2(0, ConfigConstants.GRAVITY), true);

        // collision listener
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object dataA = contact.getFixtureA().getBody().getUserData();
                Object dataB = contact.getFixtureB().getBody().getUserData();

                if (dataA instanceof PhysicEntity && dataB instanceof PhysicEntity) {
                    ((PhysicEntity) dataA).onBeginContact((PhysicEntity) dataB, contact.getFixtureA(), contact);
                    ((PhysicEntity) dataB).onBeginContact((PhysicEntity) dataA, contact.getFixtureB(), contact);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Object dataA = contact.getFixtureA().getBody().getUserData();
                Object dataB = contact.getFixtureB().getBody().getUserData();

                if (dataA instanceof PhysicEntity && dataB instanceof PhysicEntity) {
                    ((PhysicEntity) dataA).onEndContact((PhysicEntity) dataB, contact.getFixtureA(), contact);
                    ((PhysicEntity) dataB).onEndContact((PhysicEntity) dataA, contact.getFixtureB(), contact);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    @Override
    public void render(float delta) {
        // clear the screen and step the world
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        world.step(1 / 60f, 8, 8);

        // draw background and world entities using the polygon batch
        polyBatch.setProjectionMatrix(camera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        polyBatch.enableBlending();
        polyBatch.begin();

        // draw the background first
        if (background != null) {
            background.draw(camera, polyBatch);
        }

        // draw entities
        for (PhysicEntity entity : entityList) {
            entity.renderPolygon(polyBatch, delta);
        }
        polyBatch.end();

        // draw world entities using the classic batch
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        batch.begin();
        for (PhysicEntity entity : entityList) {
            entity.render(batch, delta);
        }
        batch.end();

        if (ConfigConstants.DEBUG) {
            // box 2d debug rendering
            debugRenderer.render(world, camera.combined);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (ConfigConstants.DEBUG) {
            debugRenderer.dispose();
        }
        world.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
