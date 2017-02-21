package com.slidingcube.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
 * Choose player screen.
 *
 * @author dtroncy
 */

public class NumberOfPlayerScreen implements Screen {
    private Stage stage;
    private int numberOfPlayer = 4;
    private Game game;

    public NumberOfPlayerScreen(Game aGame) {
        game = aGame;
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

        Label title = new Label("Number of player", label1Style);
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

        TextButton more=new TextButton("More",styleButton);
        more.setSize(200,200);
        more.setPosition(Gdx.graphics.getWidth()*2/3, Gdx.graphics.getHeight()/2);
        stage.addActor(more);

        TextButton less=new TextButton("Less",styleButton);
        less.setSize(200,200);
        less.setPosition(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/2);
        stage.addActor(less);

        TextButton play=new TextButton("Play",styleButton);
        play.setPosition(Gdx.graphics.getWidth()/2, 0);
        stage.addActor(play);

        more.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                if(numberOfPlayer<4){
                    numberOfPlayer++;
                }
                number.setText(String.valueOf(numberOfPlayer));
                return true;
            }
        });

        less.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(numberOfPlayer>2) {
                    numberOfPlayer--;
                }
                number.setText(String.valueOf(numberOfPlayer));
                return true;
            }
        });

        play.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                game.setScreen(new GameScreen(numberOfPlayer));

                return true;
            }
        });

    }

    public void resize (int width, int height) {
        // See below for what true means.
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
        stage.dispose();
    }
}
