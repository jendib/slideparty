package com.slidingcube;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class GdxGame extends Game {
	private SpriteBatch batch;

	public void create() {
		batch = new SpriteBatch();
		this.setScreen(new GameScreen());
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
	}
}
