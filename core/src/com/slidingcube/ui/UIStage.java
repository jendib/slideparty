package com.slidingcube.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slidingcube.constant.ConfigConstants;

import static com.badlogic.gdx.math.Interpolation.swingIn;
import static com.badlogic.gdx.math.Interpolation.swingOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * UI stage (unprojected).
 *
 * @author bgamard
 */
public class UIStage extends Stage {
    private long startTime = 0; // start time of the game
    private Label debugLabel; // debug label
    private Label startLabel; // countdown label

    /**
     * Create a new stage.
     *
     * @param viewport Viewport
     */
    public UIStage(Viewport viewport) {
        super(viewport);

        // countdown label
        Label.LabelStyle startStyle = new Label.LabelStyle();
        startStyle.font = new BitmapFont(Gdx.files.internal("font/countdown.fnt"));
        startStyle.fontColor = Color.valueOf("ff002aff");
        startLabel = new Label(null, startStyle);
        startLabel.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        startLabel.setPosition(- Gdx.graphics.getWidth(), 0);
        startLabel.setAlignment(Align.center);
        addActor(startLabel);

        if (ConfigConstants.DEBUG) {
            // debug label
            Label.LabelStyle label1Style = new Label.LabelStyle();
            label1Style.font = new BitmapFont(Gdx.files.internal("font/debug.fnt"));
            label1Style.fontColor = Color.WHITE;
            int rowHeight = Gdx.graphics.getWidth() / 8;
            debugLabel = new Label(null, label1Style);
            debugLabel.setSize(Gdx.graphics.getWidth(), rowHeight);
            debugLabel.setPosition(10, Gdx.graphics.getHeight() - rowHeight);
            debugLabel.setAlignment(Align.left);
            addActor(debugLabel);
        }
    }

    /**
     * Start the countdown if not started.
     */
    public void startCountdown() {
        if (startTime == 0) {
            startTime = TimeUtils.millis();
            startLabel.addAction(
                    sequence(
                            delay(1),
                            moveBy(Gdx.graphics.getWidth(), 0, 1.5f, swingOut)
                    )
            );
        }
    }

    /**
     * Return true if the countdown is over.
     *
     * @return True if over
     */
    public boolean isCountdownEnded() {
        return TimeUtils.millis() - startTime > ConfigConstants.COUNTDOWN;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        long countdown = (ConfigConstants.COUNTDOWN - TimeUtils.millis() + startTime) / 1000;
        startLabel.setText(countdown <= 0 ? "GO !" : String.valueOf(countdown));

        if (isCountdownEnded() && startLabel.getActions().size == 0) {
            startLabel.addAction(sequence(moveBy(Gdx.graphics.getWidth(), 0, 1.5f, swingIn), removeActor()));
        }

        if (ConfigConstants.DEBUG) {
            debugLabel.setText("FPS : " + Gdx.graphics.getFramesPerSecond());
        }
    }

    @Override
    public void draw() {
        if (!getRoot().isVisible()) return;

        // don't project the batch on the camera
        Batch batch = getBatch();
        batch.begin();
        getRoot().draw(batch, 1);
        batch.end();
    }
}
