package com.game.framework.core2.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.framework.core.bodies.BodyEditorLoader;
import com.game.framework.core.world.WorldManager;

public class CustomBodyBuilder extends BodyBuilder<CustomBodyBuilder> {

    private String _shapePath;

    public CustomBodyBuilder(WorldManager worldManager, String shapePath) {
        super(worldManager);

        this._shapePath = shapePath;
    }

    @Override
    public BodyObj build() {
        return createCustom(
            world.getWorld(),
            _type,
            _shapePath,
            _pos.x,
            _pos.y,
            _scale,
            _angleRadians,
            _density,
            _restitution
        );
    }

    @Override
    public CustomBodyBuilder copy() {
        CustomBodyBuilder copy = new CustomBodyBuilder(world, _shapePath);
        return copy.copyFrom(this);
    }

    private static BodyObj createCustom(World world, BodyDef.BodyType type, String shapePath, float x, float y, Vector2 scale, float angle, float density, float restitution) {
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(shapePath));

        // body definition
        BodyDef playerDef = new BodyDef();
        playerDef.type = type;
        playerDef.fixedRotation = true;

        // fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = restitution;

        Body body = world.createBody(playerDef);
        body.setTransform(x, y, angle);

        loader.attachFixture(body, "Name", fixtureDef, scale);

        return new BodyObj(body, loader.getOrigin("Name", scale), scale);
    }

}
