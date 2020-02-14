package com.game.framework.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.framework.core.world.WorldManager;

public class CustomWorldBody extends WorldBody {

    private Vector2 dimensions;
    private Vector2 origin;
    private String shapePath;

    public CustomWorldBody(WorldManager world, String id, String shapePath, BodyDef.BodyType type, float x, float y, Vector2 scale, float density) {
        super(world, id, createCustom(world.getWorld(), type, shapePath, x, y, scale, density));

        this.shapePath = shapePath;

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(shapePath));

        this.dimensions = scale;//loader.getDimensions("Name", scale);
        this.origin = new Vector2(loader.getOrigin("Name", scale));
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
        return createCustom(getWorld().getWorld(), type, shapePath, pos.x, pos.y, dim.scl(scale), density);
    }

    private static Body createCustom(World world, BodyDef.BodyType type, String shapePath, float x, float y, Vector2 scale, float density) {
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(shapePath));

        // body definition
        BodyDef playerDef = new BodyDef();
        playerDef.type = type;
        playerDef.fixedRotation = true;

        // fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;

        Body body = world.createBody(playerDef);
        body.setTransform(x, y, 0);

        loader.attachFixture(body, "Name", fixtureDef, scale);

        return body;
    }
}
