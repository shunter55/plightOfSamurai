package com.game.framework.core2.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.WorldBody;

public abstract class BodyBuilder<T extends BodyBuilder> implements Buildable<T> {

    public WorldManager world;

    protected Vector2 _pos = new Vector2(0, 0);
    public Vector2 _scale = new Vector2(1, 1);
    protected float _angleRadians = 0;
    protected float _density = 1;
    protected float _restitution = 0;

    protected BodyDef.BodyType _type = BodyDef.BodyType.DynamicBody;
    protected boolean _isSensor = false;

    protected short _categoryBits = 0;
    protected short _maskBits = 0;

    /**
     * Build the BodyObject.
     * @return A new BodyObject.
     */
    public abstract BodyObj build();

    /**
     * Implementation should use copyInto to set BodyBuilder fields.
     * @return Copy of the builder.
     */
    public abstract T copy();

    public BodyBuilder(WorldManager world) {
        this.world = world;
    }

    public T pos(Vector2 pos) {
        this._pos = pos;
        return self();
    }

    public T pos(float x, float y) {
        this._pos.x = x;
        this._pos.y = y;
        return self();
    }

    public T posX(float x) {
        this._pos.x = x;
        return self();
    }

    public T posY(float y) {
        this._pos.y = y;
        return self();
    }

    public T scale(Vector2 scale) {
        this._scale = scale;
        return self();
    }

    public T scale(float x, float y) {
        scale(new Vector2(x, y));
        return self();
    }

    public T scaleX(float x) {
        this._scale.x = x;
        return self();
    }

    public T scaleY(float y) {
        this._scale.y = y;
        return self();
    }

    public T angleRadian(float radians) {
        this._angleRadians = radians;
        return self();
    }

    public T angleDegree(float degrees) {
        return angleRadian((float) (degrees * (Math.PI / 180f)));
    }

    public T density(float density) {
        this._density = density;
        return self();
    }

    /**
     * Bounciness of the object. Between [0, 1].
     */
    public T restitution(float restitution) {
        this._restitution = restitution;
        return self();
    }

    public float getRestitution() {
        return this._restitution;
    }

    public T type(BodyDef.BodyType type) {
        this._type = type;
        return self();
    }

    public T isSensor(boolean isSensor) {
        this._isSensor = isSensor;
        return self();
    }

    public T categoryBits(short categoryBits) {
        this._categoryBits = categoryBits;
        return self();
    }

    public T maskBits(short maskBits) {
        this._maskBits = maskBits;
        return self();
    }

    public short getMaskBits() {
        return this._maskBits;
    }

    /**
     * Copy values from other BodyBuilder.
     */
    protected T copyFrom(BodyBuilder other) {
        _pos = other._pos;
        _scale = other._scale;
        _angleRadians = other._angleRadians;
        _density = other._density;
        _restitution = other._restitution;

        _type = other._type;
        _isSensor = other._isSensor;

        _categoryBits = other._categoryBits;
        _maskBits = other._maskBits;
        return self();
    }

    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }

    /**
     * @return The BodyBuilder that defines how the Body will be built.
     */
    @Override
    public T getBodyBuilder() {
        return self();
    }

}
