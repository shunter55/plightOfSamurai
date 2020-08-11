package com.game.framework.core2.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.framework.core.bodies.BoxWorldBody;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.world.WorldManager;

public class BoxBodyBuilder extends BodyBuilder {

    private BodyDef.BodyType _type = BodyDef.BodyType.DynamicBody;
    private boolean _isSensor = false;
    private Vector2 _pos = new Vector2(0, 0);
    private Vector2 _size = new Vector2(1, 1);
    private float _density = 1;

    public BoxBodyBuilder(WorldManager worldManager) {
        super(worldManager);
    }

    @Override
    public BodyObj build() {
        return createBox(
            world.getWorld(),
            _type,
            _isSensor,
            _pos.x,
            _pos.y,
            _size.x * Math.abs(_scale.x),
            _size.y * Math.abs(_scale.y),
            _density,
            _restitution,
            _radians
        );
    }

    @Override
    public BodyBuilder copy() {
        return new BoxBodyBuilder(world).type(_type).isSensor(_isSensor).pos(_pos).size(_size).scale(_scale).density(_density);
    }

    public BoxBodyBuilder type(BodyDef.BodyType type) {
        this._type = type;
        return this;
    }

    public BoxBodyBuilder isSensor(boolean isSensor) {
        this._isSensor = isSensor;
        return this;
    }

    public BoxBodyBuilder pos(Vector2 pos) {
        this._pos = pos;
        return this;
    }

    public BoxBodyBuilder pos(float x, float y) {
        this._pos.x = x;
        this._pos.y = y;
        return this;
    }

    public BoxBodyBuilder posX(float x) {
        this._pos.x = x;
        return this;
    }

    public BoxBodyBuilder posY(float y) {
        this._pos.y = y;
        return this;
    }

    public BoxBodyBuilder size(Vector2 size) {
        this._size = size;
        return this;
    }

    public BoxBodyBuilder size(float width, float height) {
        this._size.x = width;
        this._size.y = height;
        return this;
    }

    public BoxBodyBuilder width(float width) {
        this._size.x = width;
        return this;
    }

    public BoxBodyBuilder height(float height) {
        this._size.y = height;
        return this;
    }

    public BoxBodyBuilder scale(Vector2 scale) {
        super.scale(scale);
        return this;
    }

    public BoxBodyBuilder scaleX(float x) {
        super.scaleX(x);
        return this;
    }

    public BoxBodyBuilder scaleY(float y) {
        super.scaleY(y);
        return this;
    }

    public BoxBodyBuilder density(float density) {
        this._density = density;
        return this;
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
    private static BodyObj createBox(World world, BodyDef.BodyType type, boolean isSensor, float x, float y, float width, float height, float density, float restitution, float radians) {
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
        fixtureDef.restitution = restitution;
        fixtureDef.isSensor = isSensor;

        Body body = world.createBody(playerDef);
        Fixture f = body.createFixture(fixtureDef);
        body.setTransform(x, y, radians);

        boxShape.dispose();

        return new BodyObj(body, new Vector2(width / 2, height / 2), new Vector2(width, height));
    }

}
