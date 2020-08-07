package com.game.framework.core2.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.WorldBody;

public abstract class BodyBuilder {

    public WorldManager world;
    public Vector2 _scale = new Vector2(1, 1);
    protected float _radians = 0;

    public abstract BodyObj build();
    public abstract BodyBuilder copy();

    public BodyBuilder(WorldManager world) {
        this.world = world;
    }

    public BodyBuilder scale(Vector2 scale) {
        this._scale = scale;
        return this;
    }

    public BodyBuilder scale(float x, float y) {
        scale(new Vector2(x, y));
        return this;
    }

    public BodyBuilder scaleX(float x) {
        this._scale.x = x;
        return this;
    }

    public BodyBuilder scaleY(float y) {
        this._scale.y = y;
        return this;
    }

    public BodyBuilder rotateRad(float radians) {
        this._radians = radians;
        return this;
    }

    public BodyBuilder rotateDeg(float degrees) {
        this._radians = (float)(degrees * Math.PI / 180);
        return this;
    }

}
