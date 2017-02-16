package com.slidingcube.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.slidingcube.camera.CameraHandler;
import com.slidingcube.constant.ConfigConstants;
import com.slidingcube.entity.Ground;
import com.slidingcube.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Main game screen.
 *
 * @author bgamard
 */
public class GameScreen extends BaseScreen {
    private List<Player> playerList; // List of active players
    private int playerCount; // Number of players
    private Label debugLabel; // Debug label
    private CameraHandler cameraHandler; // Camera handling

    private transient NavigableMap<Float, Player> sortedPlayerIndex = new TreeMap<>(); // Map of sorted players

    /**
     * Create a new game screen.
     *
     * @param playerCount Number of players to create
     */
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

        // camera handling
        cameraHandler = new CameraHandler(camera, playerList);

        if (ConfigConstants.DEBUG) {
            // debug label
            Label.LabelStyle label1Style = new Label.LabelStyle();
            label1Style.font = new BitmapFont(Gdx.files.internal("font/debug.fnt"),
                    Gdx.files.internal("font/debug.png"),
                    false, true);
            label1Style.fontColor = Color.WHITE;

            int rowHeight = Gdx.graphics.getWidth() / 8;
            debugLabel = new Label(null, label1Style);
            debugLabel.setSize(Gdx.graphics.getWidth(), rowHeight);
            debugLabel.setPosition(10, Gdx.graphics.getHeight() - rowHeight);
            debugLabel.setAlignment(Align.left);
            addActor(debugLabel);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // sort players
        sortedPlayerIndex.clear();
        for (Player player : playerList) {
            Vector2 position = player.getPosition();
            sortedPlayerIndex.put(position.x, player);
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

        // update the camera
        cameraHandler.update();

        if (ConfigConstants.DEBUG) {
            // debug info
            debugLabel.setText("First player : " + (firstPlayer.getIndex() + 1) + " at " + firstPlayer.getPosition().x + "/" + firstPlayer.getPosition().y + "\n"
                    + "Speed : " + firstPlayer.getBody().getLinearVelocity().len() + "\n"
                    + "FPS : " + Gdx.graphics.getFramesPerSecond());
        }
    }
}
