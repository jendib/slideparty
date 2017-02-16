package com.slidingcube.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.slidingcube.constant.ConfigConstants;
import com.slidingcube.entity.Ground;
import com.slidingcube.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class GameScreen extends BaseScreen {
    private List<Player> playerList;
    private int playerCount;
    private Label scoreLabel;

    public GameScreen(int playerCount) {
        if (playerCount > 4) {
            playerCount = 4;
        }
        playerList = new ArrayList<>(playerCount);
        this.playerCount = playerCount;
    }

    @Override
    public void show() {
        super.show();
        playerList.clear();

        // the ground
        Ground ground = new Ground(world, ConfigConstants.GROUND_WIDTH);
        addEntity(ground);

        // add players
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player(world, i);
            playerList.add(player);
            addEntity(player);
        }

        // label
        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = new BitmapFont(Gdx.files.internal("font/debug.fnt"),
                Gdx.files.internal("font/debug.png"),
                false, true);
        label1Style.fontColor = Color.WHITE;

        int rowHeight = Gdx.graphics.getWidth() / 8;
        scoreLabel = new Label(null, label1Style);
        scoreLabel.setSize(Gdx.graphics.getWidth(), rowHeight);
        scoreLabel.setPosition(10, Gdx.graphics.getHeight() - rowHeight);
        scoreLabel.setAlignment(Align.left);
        addActor(scoreLabel);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // sort players
        float sumX = 0;
        float sumY = 0;
        NavigableMap<Float, Player> sortedPlayerIndex = new TreeMap<>();
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            Vector2 position = player.getPosition();
            sortedPlayerIndex.put(position.x, player);
            sumX += position.x;
            sumY += position.y;
        }

        // compute the first player
        Player firstPlayer = null;
        for (Map.Entry<Float, Player> entry : sortedPlayerIndex.entrySet()) {
            if (entry.getKey() < ConfigConstants.GROUND_WIDTH) {
                firstPlayer = entry.getValue();
            }
        }

        // all players are arrived
        if (firstPlayer == null) {
            next.run();
            return;
        }

        // help the latest players
        for (Map.Entry<Float, Player> entry : sortedPlayerIndex.entrySet()) {
            Player player = entry.getValue();
            player.setHelpForce((firstPlayer.getPosition().x - entry.getKey()) * ConfigConstants.HELP_FORCE);
        }

        // camera position
        camera.position.set(sumX / playerList.size(), sumY / playerList.size(), 0);

        // camera viewport
        float[] bbPlayers = getPlayerBoundingBox();
        float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth(); // 0.5625 for 16:9 screen
        float width = Math.abs(bbPlayers[0] - bbPlayers[2]) * 2f // base viewport
                + Gdx.graphics.getWidth() * ConfigConstants.CAMERA_MARGIN; // add some margin
        float height = Math.abs(bbPlayers[1] - bbPlayers[3]) * 2f // base viewport
                + Gdx.graphics.getHeight() * ConfigConstants.CAMERA_MARGIN; // add some margin
        if (width * aspectRatio > height) {
            camera.viewportWidth = width;
            camera.viewportHeight = width * aspectRatio;
        } else {
            camera.viewportWidth = height * (1 / aspectRatio);
            camera.viewportHeight = height;
        }
        camera.update();

        // debug info
        scoreLabel.setText("First player : " + (firstPlayer.getIndex() + 1) + " at " + firstPlayer.getPosition().x + "/" + firstPlayer.getPosition().y + "\n"
                + "Speed : " + firstPlayer.getBody().getLinearVelocity().len() + "\n"
                + "FPS : " + Gdx.graphics.getFramesPerSecond());
    }

    /**
     * Returns players bounding box array.
     *
     * @return array of the bounding box coordinates (x1,y1,x2,y2)
     */
    private float[] getPlayerBoundingBox() {
        float[] output = new float[] { Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
                Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY };
        for (Player player : playerList) {
            Vector2 position = player.getPosition();
            if (position.x < output[0]) {
                output[0] = position.x;
            }
            if (position.y < output[1]) {
                output[1] = position.y;
            }
            if (position.x > output[2]) {
                output[2] = position.x;
            }
            if (position.y > output[3]) {
                output[3] = position.y;
            }
        }
        return output;
    }
}
