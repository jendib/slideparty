package com.slideparty.screen;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Choose players screen.
 *
 * @author dtroncy
 */
public class ChoosePlayerScreen extends BaseScreen {
    private Stage stage;
    private int numberOfPlayer = 4;
    private SpriteDrawable punkSprite = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("players/punk.png"))));
    private SpriteDrawable bouleSprite = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("players/boule.png"))));
    private SpriteDrawable robotSprite = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("players/robot.png"))));
    private SpriteDrawable monsterSprite = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("players/monster.png"))));
    private SpriteDrawable playUpSprite = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("playUp.png"))));
    private SpriteDrawable playDownSprite = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("playDown.png"))));

    /**
     * Create a new choose player screen.
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

        //Player 1
        final ImageButton player1BtnImage = new ImageButton(punkSprite);
        player1BtnImage.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        player1BtnImage.setPosition(0,0);
        stage.addActor(player1BtnImage);

        //Player 2
        final ImageButton player2BtnImage = new ImageButton(bouleSprite);
        player2BtnImage.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        player2BtnImage.setPosition(Gdx.graphics.getWidth()*2/3,0);
        stage.addActor(player2BtnImage);

        //Player 3
        final ImageButton player3BtnImage = new ImageButton(robotSprite);
        player3BtnImage.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        player3BtnImage.setPosition(Gdx.graphics.getWidth()*2/3,Gdx.graphics.getHeight()*2/3);
        stage.addActor(player3BtnImage);

        //Player 4
        final ImageButton player4BtnImage = new ImageButton(monsterSprite);
        player4BtnImage.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        player4BtnImage.setPosition(0,Gdx.graphics.getHeight()*2/3);
        stage.addActor(player4BtnImage);


        //Play button
        final ImageButton playBtnImage = new ImageButton(playUpSprite, playDownSprite);
        playBtnImage.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        playBtnImage.setPosition(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        stage.addActor(playBtnImage);

        player1BtnImage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                switchPlayer(player1BtnImage);
                return true;
            }
        });

        player2BtnImage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                switchPlayer(player2BtnImage);
                return true;
            }
        });

        player3BtnImage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                switchPlayer(player3BtnImage);
                return true;
            }
        });

        player4BtnImage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                switchPlayer(player4BtnImage);
                return true;
            }
        });

        playBtnImage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                playBtnImage.setChecked(true);

                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                game.setScreen(new GameScreen(game, numberOfPlayer));
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
        stage.draw();
    }

    public void switchPlayer(ImageButton btn){

        if(btn.getStyle().imageUp.equals(punkSprite)){
            btn.getStyle().imageUp = bouleSprite;
        }else if(btn.getStyle().imageUp.equals(bouleSprite)){
            btn.getStyle().imageUp = robotSprite;
        }else if(btn.getStyle().imageUp.equals(robotSprite)){
            btn.getStyle().imageUp = monsterSprite;
        }else if(btn.getStyle().imageUp.equals(monsterSprite)){
            btn.getStyle().imageUp = punkSprite;
        }

    }
}
