package com.game.framework.core2.bodies.managers;

import com.game.framework.core.bodies.Collidable;
import com.game.framework.core.bodies.Function;
import com.game.framework.core2.bodies.WorldBody;

public class CollisionManager implements Collidable {

    private Function<WorldBody, Void> _beginCollision = null;
    private Function<WorldBody, Void> _endCollision = null;

    public void beginCollision(Function<WorldBody, Void> beginCollisionFn) {
        this._beginCollision = beginCollisionFn;
    }

    public void endCollision(Function<WorldBody, Void> endCollisionFn) {
        this._endCollision = endCollisionFn;
    }

    public void beginContact(WorldBody other) {
        if (_beginCollision != null) {
            _beginCollision.call((WorldBody) other);
        }
    }

    public void endContact(WorldBody other) {
        if (_endCollision != null) {
            _endCollision.call((WorldBody) other);
        }
    }

}
