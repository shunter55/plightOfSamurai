package com.game.framework.core.bodies.builders;

import com.badlogic.gdx.math.Vector2;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;

public abstract class WorldBodyBuilder {

    public Vector2 _scale = new Vector2(1, 1);
    protected Function<WorldBody, Void> _beginCollision, _endCollision;

    public abstract WorldBody build();

    public WorldBody finished(WorldBody body) {
        body.beginCollision(_beginCollision);
        body.endCollision(_endCollision);
        return body;
    }

    public WorldBodyBuilder scale(Vector2 scale) {
        this._scale = scale;
        return this;
    }

    public WorldBodyBuilder scaleX(float x) {
        this._scale.x = x;
        return this;
    }

    public WorldBodyBuilder scaleY(float y) {
        this._scale.y = y;
        return this;
    }

    public WorldBodyBuilder beginCollision(Function<WorldBody, Void> beginCollision) {
        this._beginCollision = beginCollision;
        return this;
    }

    public WorldBodyBuilder endCollision(Function<WorldBody, Void> endCollision) {
        this._endCollision = endCollision;
        return this;
    }

}
