package com.slidingcube.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.slidingcube.entity.Ground;
import com.slidingcube.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameScreen extends BaseScreen {
    private Map<Integer, Player> playerMap;
    private int playerCount;
    private Label scoreLabel;

    public GameScreen(int playerCount) {
        if (playerCount > 4) {
            playerCount = 4;
        }
        playerMap = new HashMap<Integer, Player>();
        this.playerCount = playerCount;
    }

    @Override
    public void show() {
        super.show();

        // the ground
        Ground ground = new Ground(world);
        addEntity(ground);

        // add players
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player(world, i);
            playerMap.put(i, player);
            addEntity(player);
        }

        // label
        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = new BitmapFont(Gdx.files.internal("font/debug.fnt"),
                Gdx.files.internal("font/debug.png"),
                false, true);
        label1Style.fontColor = Color.WHITE;

        int rowHeight = Gdx.graphics.getWidth() / 12;
        scoreLabel = new Label(null, label1Style);
        scoreLabel.setSize(Gdx.graphics.getWidth(), rowHeight);
        scoreLabel.setPosition(10, Gdx.graphics.getHeight() - rowHeight);
        scoreLabel.setAlignment(Align.left);
        addActor(scoreLabel);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // compute the first player
        Player firstPlayer = playerMap.entrySet().iterator().next().getValue();
        for (Map.Entry<Integer, Player> entry : playerMap.entrySet()) {
            Player player = entry.getValue();
            if (player.getPosition().x > firstPlayer.getPosition().x) {
                firstPlayer = player;
            }
        }

        // help the latest players
        for (Map.Entry<Integer, Player> entry : playerMap.entrySet()) {
            Player player = entry.getValue();
            player.setHelpForce(firstPlayer.getPosition().x - player.getPosition().x);
        }

        // show the first player
        scoreLabel.setText("First player : " + firstPlayer.getIndex() + " at " + firstPlayer.getPosition().x + "/" + firstPlayer.getPosition().y + "\n"
            + "Speed : " + firstPlayer.getBody().getLinearVelocity().len() + "\n"
            + "FPS : " + Gdx.graphics.getFramesPerSecond());

        // center the camera on the first player
        Vector3 cameraPosition = new Vector3(firstPlayer.getBody().getPosition(), 0);
        camera.position.lerp(cameraPosition, delta * 2f);
        camera.update();
    }
}
