package com.slidingcube.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.slidingcube.constant.ConfigConstants;
import com.slidingcube.entity.Entity;

import java.util.ArrayList;
import java.util.List;

import box2dLight.DirectionalLight;
import box2dLight.RayHandler;

public class BaseScreen implements Screen, InputProcessor {
    private Box2DDebugRenderer debugRenderer;
    protected World world; // box 2d world
    protected SpriteBatch batch; // projected on the camera matrix
    protected SpriteBatch uiBatch; // not projected
    protected OrthographicCamera camera; // 2d camera
    private RayHandler rayHandler; // light rendering
    private List<Entity> entityList = new ArrayList<Entity>();
    private List<Actor> actorList = new ArrayList<Actor>();

    protected Entity addEntity(Entity entity) {
        entity.getBody().setUserData(entity);
        entityList.add(entity);
        return entity;
    }

    protected Actor addActor(Actor actor) {
        actorList.add(actor);
        return actor;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        debugRenderer = new Box2DDebugRenderer(true, true, false, true, false, false);
        world = new World(new Vector2(0, -10f), true);

        // lighting
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 0.1f);
        rayHandler.setBlurNum(2);
        DirectionalLight light = new DirectionalLight(rayHandler, 128, null, 225f);
        light.setColor(1f, 1f, 1f, 1f);

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
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1 / 60f, 8, 3);

        // draw world entities
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity entity : entityList) {
            entity.render(camera, batch, delta);
        }
        batch.end();

        // box 2d debug rendering
        debugRenderer.render(world, camera.combined);

        // render lights
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        // draw the UI
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
        rayHandler.dispose();
        batch.dispose();
        debugRenderer.dispose();
        world.dispose();
        Gdx.input.setInputProcessor(null);
    }

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
