package com.game.framework.core.bodies.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.core.bodies.CustomWorldBody;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.world.WorldManager;

public class CustomBuilder extends WorldBodyBuilder {

    private WorldManager _world;
    private String _id = null;
    private String _shapePath = null;
    private BodyDef.BodyType _type = BodyDef.BodyType.DynamicBody;
    private Vector2 _pos = new Vector2(0, 0);
    private float _density = 1;

    public CustomBuilder(WorldManager worldManager, String shapePath) {
        this._world = worldManager;
        this._shapePath = shapePath;
    }

    @Override
    public WorldBody build() {
        return super.finished(new CustomWorldBody(
            _world,
            _id == null ? _world.generateId() : _id,
            _shapePath,
            _type,
            _pos.x,
            _pos.y,
            _scale,
            _density
        ));
    }

    public CustomBuilder id(String id) {
        this._id = id;
        return this;
    }

    public CustomBuilder type(BodyDef.BodyType type) {
        this._type = type;
        return this;
    }

    public CustomBuilder pos(Vector2 pos) {
        this._pos = pos;
        return this;
    }

    public CustomBuilder posX(float x) {
        this._pos.x = x;
        return this;
    }

    public CustomBuilder posY(float y) {
        this._pos.y = y;
        return this;
    }

    public CustomBuilder scale(Vector2 scale) {
        scale(scale);
        return this;
    }

    public CustomBuilder scaleX(float x) {
        scaleX(x);
        return this;
    }

    public CustomBuilder scaleY(float y) {
        scaleY(y);
        return this;
    }

    public CustomBuilder density(float density) {
        this._density = density;
        return this;
    }

    public CustomBuilder beginCollision(Function<WorldBody, Void> beginCollision) {
        super.beginCollision(beginCollision);
        return this;
    }

    public CustomBuilder endCollision(Function<WorldBody, Void> endCollision) {
        super.endCollision(endCollision);
        return this;
    }

}
