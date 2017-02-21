package com.slidingcube.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.slidingcube.background.Background;
import com.slidingcube.constant.ConfigConstants;
import com.slidingcube.entity.Entity;
import com.slidingcube.renderer.Box2dDebugRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Base game screen.
 *
 * @author bgamard
 */
public class BaseScreen implements Screen, InputProcessor {
    private Box2dDebugRenderer debugRenderer; // box 2d debug renderer
    protected World world; // box 2d world
    private SpriteBatch batch; // batch projected on the camera matrix
    private PolygonSpriteBatch polyBatch; // batch for polygons
    private SpriteBatch uiBatch; // batch not projected
    protected OrthographicCamera camera; // 2d camera
    private List<Entity> entityList = new ArrayList<>(); // physic entities tracked in the scene
    private List<Actor> actorList = new ArrayList<>(); // actors tracked in the scene
    private Background background; // Screen background (optional)
    protected Runnable next; // Action to do when the scene ends. Subclasses are responsible for the call

    /**
     * Set the action to do when this screen ends.
     *
     * @param next Runnable
     */
    public void setNext(Runnable next) {
        this.next = next;
    }

    /**
     * Add a new physic entity to the scene.
     *
     * @param entity Physic entity
     * @return The entity
     */
    protected Entity addEntity(Entity entity) {
        entity.getBody().setUserData(entity);
        entityList.add(entity);
        return entity;
    }

    /**
     * Add a new actor to the scene.
     *
     * @param actor Actor
     * @return The actor
     */
    protected Actor addActor(Actor actor) {
        actorList.add(actor);
        return actor;
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
        actorList.clear();
        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();
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

                if (dataA instanceof Entity && dataB instanceof Entity) {
                    ((Entity) dataA).onBeginContact((Entity) dataB, contact.getFixtureA(), contact);
                    ((Entity) dataB).onBeginContact((Entity) dataA, contact.getFixtureB(), contact);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Object dataA = contact.getFixtureA().getBody().getUserData();
                Object dataB = contact.getFixtureB().getBody().getUserData();

                if (dataA instanceof Entity && dataB instanceof Entity) {
                    ((Entity) dataA).onEndContact((Entity) dataB, contact.getFixtureA(), contact);
                    ((Entity) dataB).onEndContact((Entity) dataA, contact.getFixtureB(), contact);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
        Gdx.input.setInputProcessor(this);
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
        for (Entity entity : entityList) {
            entity.renderPolygon(polyBatch, delta);
        }
        polyBatch.end();

        // draw world entities using the classic batch
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        batch.begin();
        for (Entity entity : entityList) {
            entity.render(batch, delta);
        }
        batch.end();

        if (ConfigConstants.DEBUG) {
            // box 2d debug rendering
            debugRenderer.render(world, camera.combined);
        }

        // draw the UI using the UI batch
        uiBatch.begin();
        for (Actor actor : actorList) {
            actor.draw(uiBatch, 1f);
        }
        uiBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / ConfigConstants.PPM, height / ConfigConstants.PPM);
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
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

    // InputProcessor

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean consumed = false;
        for (Entity entity : entityList) {
            if (entity.touchDown(screenX, screenY, pointer, button)) {
                consumed = true;
            }
        }
        return consumed;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
