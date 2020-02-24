package com.game.framework.core2.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class BodyObj {

    public Body body;
    public Vector2 origin;
    public Vector2 dimensions;

    public BodyObj(Body body, Vector2 origin, Vector2 dimensions) {
        this.body = body;
        this.origin = origin;
        this.dimensions = dimensions;
    }

}
