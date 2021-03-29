package com.game.framework.core2.bodies.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.game.framework.core2.bodies.WorldBody;

import com.game.framework.core2.builders.BodyBuilder;
import com.game.framework.core2.builders.BodyObj;
import com.game.framework.core2.joints.Joint;

public class BodyManager {
    private WorldBody worldBody;

    public MovementManager move;
    public JointManager joints;

    BodyBuilder bodyBuilder;
    public Body body;
    private Vector2 origin;
    private Vector2 dimensions;

    private boolean _isFlippedX = false;
    private boolean _isFlippedY = false;

    /**
     * Manages a WorldBody. Adds additional functionality.
     * @param worldBody The WorldBody that is being managed.
     * @param bodyBuilder The Builder for the WorldBody.
     */
    public BodyManager(WorldBody worldBody, BodyBuilder bodyBuilder) {
        this.worldBody = worldBody;

        this.move = new MovementManager(this);
        this.joints = new JointManager(worldBody);

        this.bodyBuilder = bodyBuilder.copy();

        buildBody(this.bodyBuilder);
    }

    public void update(float deltaTime) {
        move.update(deltaTime);
    }

    // World Position
    public void setWorldPos(Vector2 pos) {
        body.setTransform(pos, body.getTransform().getRotation());
    }

    public Vector2 getWorldPos() {
        return body.getPosition();
    }

    // World Origin
    public Vector2 getWorldOrigin() {
        return origin;
    }

    // World Dimensions
    public Vector2 getWorldDimensions() {
        return dimensions;
    }

    // World Rotation
    public void setRotationRadians(float radians) {
        body.setTransform(getWorldPos(), radians);
    }

    public void setRotationDegrees(float degrees) {
        setRotationRadians((float) (degrees * Math.PI / 180));
    }

    public float getRotationRadians() {
        return (float) (body.getAngle() % (2 * Math.PI));
    }

    public float getRotationDegrees() {
        return (float) (getRotationRadians() * (180d / Math.PI));
    }

    // Flip Body
    public void flipX() {
        _isFlippedX = !_isFlippedX;
        rebuildBody(new Vector2(-1, 1));
    }

    public void setFlipX(boolean flipX) {
        if (_isFlippedX != flipX) {
            flipX();
        }
    }

    public void flipY() {
        _isFlippedY = !_isFlippedY;
        rebuildBody(new Vector2(1, -1));
    }

    public void setFlipY(boolean flipY) {
        if (_isFlippedY != flipY) {
            flipY();
        }
    }

    public boolean isFlippedX() {
        return _isFlippedX;
    }

    public boolean isFlippedY() {
        return _isFlippedY;
    }

    // Rebuild Body for Flipping
    public void rebuildBody(Vector2 scale, float restitution, short maskBits) {
        Vector2 pos = getWorldPos().cpy();

        Vector2 velocity = worldBody.body.body.getLinearVelocity();
        worldBody.world.remove(worldBody, aVoid -> {
            bodyBuilder._scale.scl(scale);
            bodyBuilder.restitution(restitution);
            bodyBuilder.maskBits(maskBits);

            BodyObj bodyObj = bodyBuilder.build();
            this.body = bodyObj.body;
            this.dimensions = new Vector2(Math.abs(bodyObj.dimensions.x), Math.abs(bodyObj.dimensions.y));
            this.body.setUserData(worldBody);
            setWorldPos(pos);
            this.body.setLinearVelocity(velocity);

            worldBody.world.addBody(worldBody);
            for (Joint joint : joints.joints()) {
                joint.buildOn(worldBody);
            }
            return null;
        });
    }

    public void rebuildBody(Vector2 scale, float restitution) {
        rebuildBody(scale, restitution, bodyBuilder.getMaskBits());
    }

    public void rebuildBody(Vector2 scale) {
        rebuildBody(scale, bodyBuilder.getRestitution(), bodyBuilder.getMaskBits());
    }

    public void buildBody(BodyBuilder builder) {
        BodyObj bodyObj = builder.build();
        this.body = bodyObj.body;
        this.origin = bodyObj.origin;
        this.dimensions = new Vector2(Math.abs(bodyObj.dimensions.x), Math.abs(bodyObj.dimensions.y));
        this.body.setUserData(worldBody);
    }

    public Vector2 getDimensions() {
        return this.dimensions;
    }

    public void dispose() {
        joints.disposeAll();
    }

}
