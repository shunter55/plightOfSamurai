package com.game.framework.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.game.framework.renderer.Renderable;
import com.game.framework.world.WorldManager;

public class CustomWorldBody extends WorldBody implements Renderable {

    private Vector2 dimensions;
    private Vector2 origin;

    public CustomWorldBody(WorldManager world, String id, String shapePath, BodyDef.BodyType type, float x, float y, float scale, float density) {
        super(world, id, createCustom(world.getWorld(), type, shapePath, x, y, scale, density));

        this.dimensions = new Vector2(scale, scale);

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(shapePath));
        this.origin = loader.getOrigin("Name", scale);
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
    public void dispose() {

    }

    private static Body createCustom(World world, BodyDef.BodyType type, String shapePath, float x, float y, float scale, float density) {
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
