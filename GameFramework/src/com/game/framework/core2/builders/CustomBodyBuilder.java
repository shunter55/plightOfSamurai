package com.game.framework.core2.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.framework.core.bodies.BodyEditorLoader;
import com.game.framework.core.bodies.CustomWorldBody;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.world.WorldManager;

public class CustomBodyBuilder extends BodyBuilder {

    private String _shapePath;
    private BodyDef.BodyType _type = BodyDef.BodyType.DynamicBody;
    private Vector2 _pos = new Vector2(0, 0);
    private float _density = 1;

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
            _density
        );
    }

    @Override
    public CustomBodyBuilder copy() {
        return new CustomBodyBuilder(world, _shapePath).type(_type).pos(_pos).scale(_scale).density(_density);
    }

    public CustomBodyBuilder type(BodyDef.BodyType type) {
        this._type = type;
        return this;
    }

    public CustomBodyBuilder pos(Vector2 pos) {
        this._pos = pos;
        return this;
    }

    public CustomBodyBuilder posX(float x) {
        this._pos.x = x;
        return this;
    }

    public CustomBodyBuilder posY(float y) {
        this._pos.y = y;
        return this;
    }

    public CustomBodyBuilder scale(Vector2 scale) {
        super.scale(scale);
        return this;
    }

    public CustomBodyBuilder scale(float x, float y) {
        super.scale(x, y);
        return this;
    }

    public CustomBodyBuilder scaleX(float x) {
        super.scaleX(x);
        return this;
    }

    public CustomBodyBuilder scaleY(float y) {
        super.scaleY(y);
        return this;
    }

    public CustomBodyBuilder density(float density) {
        this._density = density;
        return this;
    }

    private static BodyObj createCustom(World world, BodyDef.BodyType type, String shapePath, float x, float y, Vector2 scale, float density) {
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

        return new BodyObj(body, loader.getOrigin("Name", scale), scale);
    }

}
