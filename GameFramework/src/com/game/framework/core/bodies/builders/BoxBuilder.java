package com.game.framework.core.bodies.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.core.bodies.BoxWorldBody;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.world.WorldManager;

public class BoxBuilder extends WorldBodyBuilder {

    private WorldManager _world;
    private String _id = null;
    private BodyDef.BodyType _type = BodyDef.BodyType.DynamicBody;
    private boolean _isSensor = false;
    private Vector2 _pos = new Vector2(0, 0);
    private Vector2 _size = new Vector2(1, 1);
    private float _density = 1;

    public BoxBuilder(WorldManager worldManager) {
        this._world = worldManager;
    }

    @Override
    public WorldBody build() {
        return super.finished(new BoxWorldBody(
            _world,
            _id == null ? _world.generateId() : _id,
            _type,
            _isSensor,
            _pos.x,
            _pos.y,
            _size.x,
            _size.y,
            _scale,
            _density
        ));
    }

    public BoxBuilder id(String id) {
        this._id = id;
        return this;
    }

    public BoxBuilder type(BodyDef.BodyType type) {
        this._type = type;
        return this;
    }

    public BoxBuilder isSensor(boolean isSensor) {
        this._isSensor = isSensor;
        return this;
    }

    public BoxBuilder pos(Vector2 pos) {
        this._pos = pos;
        return this;
    }

    public BoxBuilder pos(float x, float y) {
        this._pos.x = x;
        this._pos.y = y;
        return this;
    }

    public BoxBuilder posX(float x) {
        this._pos.x = x;
        return this;
    }

    public BoxBuilder posY(float y) {
        this._pos.y = y;
        return this;
    }

    public BoxBuilder size(Vector2 size) {
        this._size = size;
        return this;
    }

    public BoxBuilder size(float width, float height) {
        this._size.x = width;
        this._size.y = height;
        return this;
    }

    public BoxBuilder width(float width) {
        this._size.x = width;
        return this;
    }

    public BoxBuilder height(float height) {
        this._size.y = height;
        return this;
    }

    public BoxBuilder scale(Vector2 scale) {
        super.scale(scale);
        return this;
    }

    public BoxBuilder scaleX(float x) {
        super.scaleX(x);
        return this;
    }

    public BoxBuilder scaleY(float y) {
        super.scaleY(y);
        return this;
    }

    public BoxBuilder density(float density) {
        this._density = density;
        return this;
    }

    public BoxBuilder beginCollision(Function<WorldBody, Void> beginCollision) {
        super.beginCollision(beginCollision);
        return this;
    }

    public BoxBuilder endCollision(Function<WorldBody, Void> endCollision) {
        super.endCollision(endCollision);
        return this;
    }

}
