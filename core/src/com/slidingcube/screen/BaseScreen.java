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
import com.slidingcube.entity.Entity;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import java.util.ArrayList;
import java.util.List;

public class BaseScreen implements Screen, InputProcessor {
    private Box2DDebugRenderer debugRenderer;
    protected World world;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private List<Entity> entityList = new ArrayList<Entity>();

    protected Entity addEntity(Entity entity) {
        entity.getBody().setUserData(entity);
        entityList.add(entity);
        return entity;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        debugRenderer = new Box2DDebugRenderer(true, true, false, true, true, true);
        world = new World(new Vector2(0, -10f), true);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object dataA = contact.getFixtureA().getBody().getUserData();
                Object dataB = contact.getFixtureB().getBody().getUserData();

                if (dataA instanceof Entity && dataB instanceof Entity) {
                    ((Entity) dataA).onContact((Entity) dataB);
                    ((Entity) dataB).onContact((Entity) dataA);
                }
            }

            @Override
            public void endContact(Contact contact) {

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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1 / 60f, 8, 3);

        for (Entity entity : entityList) {
            entity.render(camera);
        }

        batch.begin();
        Box2DSprite.draw(batch, world);
        batch.end();

        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 25;
        camera.viewportHeight = height / 25;
        camera.update();

        batch.setProjectionMatrix(camera.combined);
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
