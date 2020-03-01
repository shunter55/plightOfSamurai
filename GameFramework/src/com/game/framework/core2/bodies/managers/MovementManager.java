package com.game.framework.core2.bodies.managers;

import com.badlogic.gdx.math.Vector2;

public class MovementManager {
    private BodyManager body;

    private Vector2 _moveTo;
    float vel;
    boolean shouldMove;

    public MovementManager(BodyManager body) {
        this.body = body;
        this.shouldMove = false;
    }

    public void update(float deltaTime) {
        // Stop if reached moveTo position.
        if (_moveTo == null || _moveTo.epsilonEquals(body.getWorldPos(), 0.1f)) {
            shouldMove = false;
        }

        if (shouldMove) {
            velocity(calcMoveToVelocity(_moveTo, vel));
        }
    }

    /**
     * @param pos The position to move to.
     * @param vel The speed to travel.
     * @param keepUpdating Keep recalculating velocity until reaching positing, or canceling.
     */
    public void moveTo(Vector2 pos, float vel, boolean keepUpdating) {
        _moveTo = new Vector2(pos);
        this.vel = vel;
        shouldMove = keepUpdating;

        velocity(calcMoveToVelocity(_moveTo, vel));
    }

    public void cancelMove() {
        this.shouldMove = false;
    }

    public void velocity(Vector2 vel) {
        body.body.setLinearVelocity(vel);
    }

    public void velocity(float x, float y) {
        velocity(new Vector2(x, y));
    }

    public void velocityX(float x) {
        velocity(x, body.body.linVelLoc.y);
    }

    public void velocityY(float y) {
        velocity(body.body.linVelLoc.x, y);
    }

    private static final float dTime = 1 / 60f;
    private Vector2 calcMoveToVelocity(Vector2 moveTo, float speed) {
        if (body.getWorldPos().epsilonEquals(_moveTo, dTime * vel)) {
            speed = new Vector2(_moveTo).sub(body.getWorldPos()).len() / dTime;
        }
        return new Vector2(moveTo).sub(body.getWorldPos()).setLength(1f).scl(speed);
    }

}
