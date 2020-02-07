package com.game.framework.core.bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.framework.core.world.WorldManager;

public class BoxWorldBody extends WorldBody {

    private Vector2 dimensions;
    private Vector2 origin;

    public BoxWorldBody(WorldManager world, String id, BodyDef.BodyType type, boolean isSensor, float x, float y, float width, float height, Vector2 scale, float density) {
        super(world, id, createBox(world.getWorld(), type, isSensor, x, y, width * Math.abs(scale.x), height * Math.abs(scale.y), density));

        dimensions = new Vector2(width, height);
        origin = new Vector2(width / 2, height / 2);
    }

    @Override
    public Vector2 getDimensions() {
        return dimensions;
    }

    @Override
    public Vector2 getOrigin() {
        return origin;
    }

    @Override
    public Body copyBody(BodyDef.BodyType type, boolean isSensor, Vector2 pos, Vector2 dim, Vector2 scale, float density) {
        return createBox(getWorld().getWorld(), type, isSensor, pos.x, pos.y, dim.x * Math.abs(scale.x), dim.y * Math.abs(scale.y), density);
    }

    /**
     * Create a Box2D Box Body.
     * @param world The world the Body will be created in.
     * @param type The BodyType of the Body
     * @param isSensor True if body is a sensor.
     * @param x The x position
     * @param y The y position
     * @param width The width of the box.
     * @param height The height of the box.
     * @param density The density of the box.
     * @return The newly created Box Body.
     */
    private static Body createBox(World world, BodyDef.BodyType type, boolean isSensor, float x, float y, float width, float height, float density) {
        // body definition
        BodyDef playerDef = new BodyDef();
        playerDef.type = type;

        // shape definition
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(width, height);

        // fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = density;
        fixtureDef.isSensor = isSensor;

        Body body = world.createBody(playerDef);
        Fixture f = body.createFixture(fixtureDef);
        body.setTransform(x, y, 0);

        boxShape.dispose();

        return body;
    }
}
