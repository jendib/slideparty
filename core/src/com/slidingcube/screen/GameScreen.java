package com.slidingcube.screen;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.flowpowered.noise.model.Line;
import com.flowpowered.noise.module.source.Perlin;
import com.slidingcube.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameScreen extends BaseScreen {
    private Map<Integer, Player> playerMap = new HashMap<Integer, Player>();

    @Override
    public void show() {
        super.show();

        // the ground
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        EdgeShape groundShape = new EdgeShape();
        ChainShape chainShape = new ChainShape();

        Line line = new Line(new Perlin());
        int width = 1000;
        int twidth = 1;
        float[] chain = new float[width / twidth * 2];
        for (int x = 0; x < width; x += twidth) {
            chain[x / twidth * 2] = x;
            chain[x / twidth * 2 + 1] = (float) line.getValue(x / (double) width) * 1000 - x / 10f - 1000;
        }
        chainShape.createChain(chain);

        groundShape.set(-50, 10, 5000, -2000);
        fixtureDef.shape = chainShape;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0f;
        world.createBody(bodyDef).createFixture(fixtureDef);
        groundShape.dispose();
        chainShape.dispose();

        // add players
        for (int i = 0; i < 4; i++) {
            Player player = new Player(world, i);
            playerMap.put(i, player);
            addEntity(player);
        }
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
    }
}
