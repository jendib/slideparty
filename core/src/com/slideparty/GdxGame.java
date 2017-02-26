package com.slideparty;

import com.badlogic.gdx.Game;
import com.slideparty.screen.MainScreen;

/**
 * The game.
 */
public class GdxGame extends Game {
    @Override
	public void create() {
        this.setScreen(new MainScreen(this));
    }
}
