package com.slideparty;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.slideparty.screen.ChoosePlayerScreen;

public class GdxGame extends Game {
	private SpriteBatch batch;

	public void create() {
		batch = new SpriteBatch();

        // Start by ChoosePlayerScreen
        this.setScreen(new ChoosePlayerScreen(this));

    }

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
	}
}
