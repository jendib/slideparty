package com.slidingcube.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slidingcube.constant.ConfigConstants;
import com.slidingcube.entity.Player;

import java.util.Collection;
import java.util.List;

import static com.badlogic.gdx.math.Interpolation.swingIn;
import static com.badlogic.gdx.math.Interpolation.swingOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * UI stage.
 *
 * @author bgamard
 */
public class GameStage extends Stage {
    private long startTime = 0; // start time of the game
    private Label debugLabel; // debug label
    private Label startLabel; // countdown label

    /**
     * Create a new stage.
     *
     * @param viewport Viewport
     */
    public GameStage(Viewport viewport, final List<Player> playerList) {
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

        // player controls
        Drawable jumpBtnDrawable = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/jump-btn.png"))));
        for (final Player player : playerList) {
            // create the jump button
            final ImageButton jumpBtn = new ImageButton(jumpBtnDrawable);
            jumpBtn.setSize(200, 200);
            switch(playerList.indexOf(player)) {
                case 0:
                    jumpBtn.setPosition(20, 20);
                    break;
                case 1:
                    jumpBtn.setPosition(Gdx.graphics.getWidth() - jumpBtn.getWidth() - 20, 20);
                    break;
                case 2:
                    jumpBtn.setPosition(Gdx.graphics.getWidth() - jumpBtn.getWidth() - 20,
                            Gdx.graphics.getHeight() - jumpBtn.getHeight() - 20);
                    break;
                case 3:
                    jumpBtn.setPosition(20, Gdx.graphics.getHeight() - jumpBtn.getHeight() - 20);
                    break;
            }
            addActor(jumpBtn);

            // jump on touch
            jumpBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (player.jump()) {
                        jumpBtn.addAction(sequence(
                                alpha(0.5f, 0.1f),
                                delay(ConfigConstants.JUMP_INTERVAL / 1000f),
                                alpha(1f, 0.1f)
                        ));
                    }
                    return true;
                }
            });
        }

        // fps counter
        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = new BitmapFont(Gdx.files.internal("font/debug.fnt"));
        label1Style.fontColor = Color.WHITE;
        debugLabel = new Label(null, label1Style);
        debugLabel.setSize(Gdx.graphics.getWidth(), 30);
        debugLabel.setPosition(10, Gdx.graphics.getHeight() - 30);
        debugLabel.setAlignment(Align.left);
        debugLabel.setTouchable(Touchable.disabled);
        addActor(debugLabel);
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

    /**
     * Start the ending sequence.
     *
     * @param playerList Ordered player list
     */
    public void startEndSequence(Collection<Player> playerList) {

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        long countdown = (ConfigConstants.COUNTDOWN - TimeUtils.millis() + startTime) / 1000;
        startLabel.setText(countdown <= 0 ? "GO !" : String.valueOf(countdown));

        if (isCountdownEnded() && startLabel.getActions().size == 0) {
            startLabel.addAction(sequence(moveBy(Gdx.graphics.getWidth(), 0, 1.5f, swingIn), removeActor()));
        }

        debugLabel.setText("FPS : " + Gdx.graphics.getFramesPerSecond());
    }
}
