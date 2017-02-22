package com.slidingcube.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Choose players screen.
 *
 * @author dtroncy
 */
public class ChoosePlayerScreen extends BaseScreen {
    private Stage stage;
    private int numberOfPlayer = 4;

    /**
     * Create a new game screen.
     *
     * @param game Calling game
     */
    public ChoosePlayerScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        final TextButton.TextButtonStyle styleButton = new TextButton.TextButtonStyle();

        styleButton.fontColor = Color.WHITE;
        styleButton.font = new BitmapFont(Gdx.files.internal("font/debug.fnt"),
                Gdx.files.internal("font/debug.png"),
                false, true);

        SpriteDrawable punkSprite = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("players/punk.png"))));
        SpriteDrawable bouleSprite = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("players/boule.png"))));
        SpriteDrawable robotSprite = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("players/robot.png"))));
        SpriteDrawable monsterSprite = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("players/monster.png"))));


        //Player 1
        ImageButton player1BtnImage = new ImageButton(punkSprite);
        player1BtnImage.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        player1BtnImage.setPosition(0,0);
        stage.addActor(player1BtnImage);

        //Player 2
        ImageButton player2BtnImage = new ImageButton(bouleSprite);
        player2BtnImage.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        player2BtnImage.setPosition(Gdx.graphics.getWidth()*2/3,0);
        stage.addActor(player2BtnImage);

        //Player 3
        ImageButton player3BtnImage = new ImageButton(robotSprite);
        player3BtnImage.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        player3BtnImage.setPosition(Gdx.graphics.getWidth()*2/3,Gdx.graphics.getHeight()*2/3);
        stage.addActor(player3BtnImage);

        //Player 4
        ImageButton player4BtnImage = new ImageButton(monsterSprite);
        player4BtnImage.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        player4BtnImage.setPosition(0,Gdx.graphics.getHeight()*2/3);
        stage.addActor(player4BtnImage);

        TextButton playBtn = new TextButton("Play",styleButton);
        playBtn.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        playBtn.setPosition(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        stage.addActor(playBtn);

        player1BtnImage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });

        player2BtnImage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });

        player3BtnImage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });

        player4BtnImage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

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
