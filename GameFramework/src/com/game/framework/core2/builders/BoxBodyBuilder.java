package com.game.framework.core2.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.framework.core.bodies.BoxWorldBody;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.world.WorldManager;

import javax.swing.*;

public class BoxBodyBuilder extends BodyBuilder<BoxBodyBuilder> {

    private Vector2 _size = new Vector2(1, 1);

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
            _angleRadians,
            _density,
            _restitution
        );
    }

    @Override
    public BoxBodyBuilder copy() {
        BoxBodyBuilder copy = new BoxBodyBuilder(world).size(_size);
        return copy.copyFrom(this);
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
     * @param restitution The restitution of the box.
     * @return The newly created Box Body.
     */
    private static BodyObj createBox(World world, BodyDef.BodyType type, boolean isSensor, float x, float y, float width, float height, float angle, float density, float restitution) {
        // body definition
        BodyDef playerDef = new BodyDef();
        playerDef.type = type;

        // shape definition
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(width / 2, height / 2);

        // fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.isSensor = isSensor;

        Body body = world.createBody(playerDef);
        Fixture f = body.createFixture(fixtureDef);
        body.setTransform(x, y, angle);

        boxShape.dispose();

        return new BodyObj(body, new Vector2(width / 2, height / 2), new Vector2(width, height));
    }

}
