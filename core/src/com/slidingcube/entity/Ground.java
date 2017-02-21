package com.slidingcube.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.RepeatablePolygonSprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.flowpowered.noise.model.Line;
import com.flowpowered.noise.module.source.Perlin;
import com.slidingcube.constant.ConfigConstants;

import java.util.Random;

/**
 * The ground entity.
 *
 * @author bgamard
 */
public class Ground extends PhysicEntity {
    private RepeatablePolygonSprite polySprite; // Sprite to draw the ground
    private Mesh surfaceMesh;
    private Texture texture;

    /**
     * Create a new ground.
     *
     * @param world Box 2D world
     * @param width Width of the ground
     */
    public Ground(World world, int width) {
        // generate ground line
        ChainShape chainShape = new ChainShape();
        Perlin perlin = new Perlin();
        perlin.setSeed(new Random().nextInt());
        Line line = new Line(perlin);
        float[] chain = new float[width * 2 + 4];
        chain[0] = 0;
        chain[1] = 0;
        chain[width * 2 + 2] = width;
        chain[width * 2 + 3] = 0;
        for (int x = 0; x < width; x++) {
            chain[x * 2 + 2] = x;
            if (x <= ConfigConstants.GROUND_FLAT_WIDTH) {
                // the beginning is flat
                chain[x * 2 + 3] = (float) line.getValue(0) * ConfigConstants.GROUND_SLOPE_FACTOR;
            } else if (width - x <= ConfigConstants.GROUND_FLAT_WIDTH) {
                // the end is flat
                int lastPosition = width - ConfigConstants.GROUND_FLAT_WIDTH * 2;
                chain[x * 2 + 3] = (float) line.getValue(lastPosition / (double) width)
                        * ConfigConstants.GROUND_SLOPE_FACTOR
                        - lastPosition * ConfigConstants.GROUND_SLOPE_MULTIPLIER;
            } else {
                float position = x - ConfigConstants.GROUND_FLAT_WIDTH; // position on the curvy part
                float slope = position * ConfigConstants.GROUND_SLOPE_MULTIPLIER; // force a slope ground
                chain[x * 2 + 3] = (float) line.getValue(position / (double) width)
                        * ConfigConstants.GROUND_SLOPE_FACTOR
                        - slope;
            }
        }
        chainShape.createChain(chain);

        // ground body
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        fixtureDef.shape = chainShape;
        fixtureDef.restitution = 0;
        fixtureDef.friction = ConfigConstants.GROUND_FRICTION;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        chainShape.dispose();

        // initialize surface mesh vertices
        float color = Color.WHITE.toFloatBits();
        int chainLinkCount = (chain.length - 4) / 2; // remove first and last chain vertices
        int verticesCount = chainLinkCount * 2; // 2 vertices by chain link
        float[] vertices = new float[verticesCount * 5]; // 5 data per vertice
        float maxChainX = chain[chain.length - 4]; // last used chain X value

        for (int i = 0; i < chainLinkCount; i++) { // assign surface vertices data 10 by 10
            float chainX = chain[2 + i * 2]; // skip first 2 coordinates (X1, Y1)
            float chainY = chain[3 + i * 2]; // skip first 3 coordinates (X1, Y1, X2)

            // first vertice (top one)
            vertices[i * 10] = chainX; // vertice X
            vertices[i * 10 + 1] = chainY + 3f; // vertice Y on the ground
            vertices[i * 10 + 2] = chainX / maxChainX * 20f; // texture coordinate X
            vertices[i * 10 + 3] = 0; // texture coordinate Y
            vertices[i * 10 + 4] = color; // vertice color

            // second vertice (bottom one)
            vertices[i * 10 + 5] = chainX; // vertice X
            vertices[i * 10 + 6] = chainY - 3f; // vertice Y below ground
            vertices[i * 10 + 7] = chainX / maxChainX * 20f; // texture coordinate X
            vertices[i * 10 + 8] = 1; // texture coordinate Y
            vertices[i * 10 + 9] = color; // vertice color
        }

        // initialize surface mesh indices
        short[] indices = new short[verticesCount * 3]; // mesh indices
        int j = 0;
        for (short i = 0; j < indices.length - 6; i += 2) {
            indices[j++] = i;
            indices[j++] = (short) (i + 2);
            indices[j++] = (short) (i + 1);
            indices[j++] = (short) (i + 1);
            indices[j++] = (short) (i + 2);
            indices[j++] = (short) (i + 3);
        }

        // create surface mesh
        surfaceMesh = new Mesh(Mesh.VertexDataType.VertexArray, true, vertices.length, indices.length,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0"),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color"));
        surfaceMesh.setVertices(vertices);
        surfaceMesh.setIndices(indices);

        // texture filling
        texture = new Texture(Gdx.files.internal("surface.png"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        Texture texture = new Texture(Gdx.files.internal("ground.png"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.MirroredRepeat);
        TextureRegion textureRegion = new TextureRegion(texture);
        polySprite = new RepeatablePolygonSprite();
        polySprite.setPolygon(textureRegion, chain, 40f); // ground texture density
    }

    @Override
    public void renderPolygon(PolygonSpriteBatch batch, float delta) {
        polySprite.draw(batch);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        texture.bind(0);
        surfaceMesh.render(batch.getShader(), GL20.GL_TRIANGLES);
    }
}
