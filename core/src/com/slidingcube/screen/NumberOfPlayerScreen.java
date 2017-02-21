package com.slidingcube.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Choose players screen.
 *
 * @author dtroncy
 */
public class NumberOfPlayerScreen extends BaseScreen {
    private Stage stage;
    private int numberOfPlayer = 4;

    /**
     * Create a new game screen.
     *
     * @param game Calling game
     */
    public NumberOfPlayerScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = new BitmapFont(Gdx.files.internal("font/debug.fnt"),
                Gdx.files.internal("font/debug.png"),
                false, true);
        label1Style.fontColor = Color.WHITE;

        Label title = new Label("Number of players", label1Style);
        title.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        title.setAlignment(Align.top);
        stage.addActor(title);

        final Label number = new Label(String.valueOf(numberOfPlayer), label1Style);
        number.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        number.setAlignment(Align.center);
        stage.addActor(number);

        final TextButton.TextButtonStyle styleButton = new TextButton.TextButtonStyle();

        styleButton.fontColor = Color.WHITE;
        styleButton.font = new BitmapFont(Gdx.files.internal("font/debug.fnt"),
                Gdx.files.internal("font/debug.png"),
                false, true);

        TextButton moreBtn = new TextButton("More",styleButton);
        moreBtn.setSize(200,200);
        moreBtn.setPosition(Gdx.graphics.getWidth()*2/3, Gdx.graphics.getHeight()/2);
        stage.addActor(moreBtn);

        TextButton lessBtn = new TextButton("Less",styleButton);
        lessBtn.setSize(200,200);
        lessBtn.setPosition(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/2);
        stage.addActor(lessBtn);

        TextButton playBtn = new TextButton("Play",styleButton);
        playBtn.setPosition(Gdx.graphics.getWidth()/2, 0);
        stage.addActor(playBtn);

        moreBtn.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                if(numberOfPlayer<4){
                    numberOfPlayer++;
                }
                number.setText(String.valueOf(numberOfPlayer));
                return true;
            }
        });

        lessBtn.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(numberOfPlayer>2) {
                    numberOfPlayer--;
                }
                number.setText(String.valueOf(numberOfPlayer));
                return true;
            }
        });

        playBtn.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                game.setScreen(new GameScreen(game, numberOfPlayer));

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
        stage.act(delta);
        stage.draw();
    }
}
