package com.slidingcube.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * Base screen for all game screens.
 *
 * @author bgamard
 */
public abstract class BaseScreen implements Screen {
    protected Game game; // calling game

    /**
     * Create a new screen.
     *
     * @param game Calling game
     */
    public BaseScreen(Game game) {
        this.game = game;
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
    }
}
