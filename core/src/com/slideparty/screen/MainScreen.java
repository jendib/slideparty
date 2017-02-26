package com.slideparty.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Main screen.
 *
 * @author bgamard
 */
public class MainScreen extends BaseScreen {
    private Stage stage;
    private Image logo;

    /**
     * Create a new main screen.
     *
     * @param game Calling game
     */
    public MainScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // logo group
        Group logoGroup = new Group();
        logoGroup.setRotation(- 10);
        stage.addActor(logoGroup);

        // surface
        Texture texture = new Texture(Gdx.files.internal("surface.png"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        Image surface = new Image(new TextureRegionDrawable(new TextureRegion(texture, texture.getWidth() * 10, texture.getHeight())), Scaling.fillY);
        surface.setSize(Gdx.graphics.getWidth() * 3, 100);
        logoGroup.addActor(surface);

        // ground
        texture = new Texture(Gdx.files.internal("ground.png"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Image ground = new Image(new TextureRegionDrawable(new TextureRegion(texture,
                texture.getWidth() * 12,
                texture.getHeight() * 8)));
        ground.setSize(Gdx.graphics.getWidth() * 3, Gdx.graphics.getWidth() * 2);
        ground.setPosition(0, - ground.getHeight());
        logoGroup.addActor(ground);

        // logo
        texture = new Texture(Gdx.files.internal("main/logo.png"));
        logo = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        logo.setPosition(Gdx.graphics.getWidth() / 2, logo.getHeight() / 2 + surface.getHeight() / 4, Align.center);
        logoGroup.addActor(logo);

        // logo animation
        logo.addAction(forever(
            sequence(
                moveBy(Gdx.graphics.getWidth(), 0, 4),
                moveBy(- Gdx.graphics.getWidth(), 0)
            )
        ));

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new ChoosePlayerScreen(game));
                return true;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);

        stage.act(delta);

        // center camera on logo
        Camera camera = stage.getCamera();
        Vector2 logoPosition = logo.localToStageCoordinates(new Vector2(logo.getWidth() / 2, logo.getHeight() / 2));
        camera.position.set(logoPosition, 0);
        camera.update();

        stage.draw();
    }
}
