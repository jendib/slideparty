package com.slidingcube;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.util.HashMap;
import java.util.Map;

class GameScreen extends BaseScreen {
    private Map<Integer, Player> playerMap = new HashMap<Integer, Player>();

    @Override
    public void show() {
        super.show();

        // the ground
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(-50, 10, 5000, -2000);
        fixtureDef.shape = groundShape;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0f;
        world.createBody(bodyDef).createFixture(fixtureDef);
        groundShape.dispose();

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
