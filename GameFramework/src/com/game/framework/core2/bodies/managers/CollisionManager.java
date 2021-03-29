package com.game.framework.core2.bodies.managers;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.core.bodies.Collidable;
import com.game.framework.core.bodies.Function;
import com.game.framework.core2.bodies.WorldBody;

public class CollisionManager implements Collidable {

    private WorldBody body;

    private Function<WorldBody, Void> _beginCollision = null;
    private Function<WorldBody, Void> _endCollision = null;
    private Function<WorldBody, Void> _preCollision = null;
    private Function<WorldBody, Void> _postCollision = null;

    public CollisionManager(WorldBody body) {
        this.body = body;
    }

    public void beginCollision(Function<WorldBody, Void> beginCollisionFn) {
        this._beginCollision = beginCollisionFn;
    }

    public void endCollision(Function<WorldBody, Void> endCollisionFn) {
        this._endCollision = endCollisionFn;
    }

    public void beginContact(WorldBody other) {
        if (other.body.body.getType() == BodyDef.BodyType.StaticBody) {
            body.body.move.cancelMove();
        }

        if (_beginCollision != null) {
            _beginCollision.call((WorldBody) other);
        }
    }

    public void endContact(WorldBody other) {
        if (_endCollision != null) {
            _endCollision.call((WorldBody) other);
        }
    }

    public void preCollision(Function<WorldBody, Void> preCollisionFn) {
        this._preCollision = preCollisionFn;
    }

    public void preSolve(WorldBody other) {
        if (_preCollision != null) {
            _preCollision.call((WorldBody) other);
        }
    }

    public void postCollision(Function<WorldBody, Void> postCollisionFn) {
        this._postCollision = postCollisionFn;
    }

    public void postSolve(WorldBody other) {
        if (_postCollision != null) {
            _postCollision.call((WorldBody) other);
        }
    }

}
