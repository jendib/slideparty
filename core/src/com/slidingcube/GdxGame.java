package com.slidingcube;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.slidingcube.screen.BaseScreen;
import com.slidingcube.screen.GameScreen;
import com.slidingcube.screen.HomeScreen;

class GdxGame extends Game {
	private SpriteBatch batch;

	public void create() {
		batch = new SpriteBatch();

        // create screens
        final BaseScreen homeScreen = new HomeScreen();
        final BaseScreen gameScreen = new GameScreen(4);

        // play game after home screen
		homeScreen.setNext(new Runnable() {
			@Override
			public void run() {
				setScreen(gameScreen);
			}
		});

        // return to home screen after playing
        gameScreen.setNext(new Runnable() {
            @Override
            public void run() {
                setScreen(homeScreen);
            }
        });

        // start by the home screen
        setScreen(homeScreen);
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
	}
}
