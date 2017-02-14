package com.slidingcube;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.slidingcube.screen.GameScreen;
import com.slidingcube.screen.HomeScreen;

class GdxGame extends Game {
	private SpriteBatch batch;

	public void create() {
		batch = new SpriteBatch();
        HomeScreen homeScreen = new HomeScreen(new Runnable() {
            @Override
            public void run() {
		        setScreen(new GameScreen(4));
            }
        });

        setScreen(homeScreen);
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
	}
}
