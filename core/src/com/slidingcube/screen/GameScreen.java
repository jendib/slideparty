package com.slidingcube.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slidingcube.background.ParallaxBackground;
import com.slidingcube.background.ParallaxLayer;
import com.slidingcube.camera.CameraHandler;
import com.slidingcube.constant.ConfigConstants;
import com.slidingcube.entity.Ground;
import com.slidingcube.entity.Player;
import com.slidingcube.entity.StartGate;
import com.slidingcube.ui.GameStage;

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
public class GameScreen extends PhysicScreen {
    private List<Player> playerList; // list of active players
    private StartGate startGate; // start gate blocking players at the beginning
    private int playerCount; // number of players
    private GameStage stage; // stage for the UI
    private CameraHandler cameraHandler; // camera handling
    private NavigableMap<Float, Player> sortedPlayerIndex = new TreeMap<>(); // map of sorted players

    /**
     * Create a new game screen.
     *
     * @param game Calling game
     * @param playerCount Number of players to create
     */
    public GameScreen(Game game, int playerCount) {
        super(game);

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

        // TODO Stop the players from going off scene (rock on left, house on right)

        // add players
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player(world, i);
            playerList.add(player);
            addEntity(player);
        }

        // add the start gate
        startGate = new StartGate(world, ground);
        addEntity(startGate);

        // camera handling
        cameraHandler = new CameraHandler(camera, ground, playerList);

        // UI
        stage = new GameStage(new ScreenViewport(), playerList);
        Gdx.input.setInputProcessor(stage);
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
        ParallaxLayer mountainLayer = new ParallaxLayer(mountainTexture, 0.001f, false);

        Texture mountain2Texture = new Texture(Gdx.files.internal("mountain-1.png"));
        mountain2Texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        ParallaxLayer mountain2Layer = new ParallaxLayer(mountain2Texture, 0.002f, false);

        Texture cloudsTexture = new Texture(Gdx.files.internal("clouds.png"));
        cloudsTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        ParallaxLayer cloudsLayer = new ParallaxLayer(cloudsTexture, 0.0006f, true);

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

        if (firstPlayer == null) {
            // all players are arrived
            for (Player player : playerList) {
                player.getBody().setType(BodyDef.BodyType.StaticBody);
            }
            stage.startEndSequence(game, sortedPlayerIndex.values());
        } else {
            // help the latest players
            for (Map.Entry<Float, Player> entry : sortedPlayerIndex.entrySet()) {
                Player player = entry.getValue();
                player.setHelpForce((firstPlayer.getPosition().x - entry.getKey()) * ConfigConstants.HELP_FORCE);
            }
        }

        // the first drawn frame is the game start
        stage.startCountdown();

        // 5 seconds after the start, remove the start gate
        if (stage.isCountdownEnded() && startGate != null) {
            removeEntity(startGate);
            startGate = null;
        }

        // update the camera
        cameraHandler.update();

        // render the UI
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }
}
