package com.slidingcube.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class HomeScreen extends BaseScreen {
    @Override
    public void show() {
        super.show();

        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = new BitmapFont(Gdx.files.internal("font/debug.fnt"),
                Gdx.files.internal("font/debug.png"),
                false, true);
        label1Style.fontColor = Color.WHITE;
        Label label = new Label("Touch to start", label1Style);
        label.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        label.setAlignment(Align.center);
        addActor(label);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        next.run();
        return true;
    }
}
