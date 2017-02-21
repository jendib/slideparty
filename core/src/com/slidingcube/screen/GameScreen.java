package com.slidingcube.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.slidingcube.background.ParallaxBackground;
import com.slidingcube.background.ParallaxLayer;
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

        createBackground();

        // the ground
        Ground ground = new Ground(world, ConfigConstants.GROUND_WIDTH);
        addEntity(ground);

        // TODO Stop the players from going off scene (tree on left, house on right)

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

    /**
     * Create the parallax background.
     */
    private void createBackground() {
        Texture skyTexture = new Texture(Gdx.files.internal("sky.png"));
        skyTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        ParallaxLayer skyLayer = new ParallaxLayer(skyTexture, 0f, true);

        Texture mountainTexture = new Texture(Gdx.files.internal("mountain-2.png"));
        mountainTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        ParallaxLayer mountainLayer = new ParallaxLayer(mountainTexture, 0.0005f, false);

        Texture mountain2Texture = new Texture(Gdx.files.internal("mountain-1.png"));
        mountain2Texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        ParallaxLayer mountain2Layer = new ParallaxLayer(mountain2Texture, 0.001f, false);

        Texture cloudsTexture = new Texture(Gdx.files.internal("clouds.png"));
        cloudsTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        ParallaxLayer cloudsLayer = new ParallaxLayer(cloudsTexture, 0.0003f, true);

        setBackground(new ParallaxBackground(skyLayer, mountainLayer, mountain2Layer, cloudsLayer));
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
