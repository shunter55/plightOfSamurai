package com.game.framework.core.bodies.joints;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;

public class Weld implements Joint {

    private Function<Void, WorldBody> bodyFn;
    private Vector2 offset;
    private float offsetAngle;

    private WorldBody bodyB = null;

    public Weld(Function<Void, WorldBody> bodyFn, Vector2 offset, float offsetAngle) {
        this.bodyFn = bodyFn;
        this.offset = offset;
        this.offsetAngle = offsetAngle;
    }

    public void buildOn(WorldBody body) {
        World world = body.getWorld().getWorld();

        WeldJointDef weld = new WeldJointDef();

        bodyB = bodyFn.call(null);
        bodyB.setWorldPos(body.getWorldPos().add(offset));
        bodyB.setAngleRadians(offsetAngle);

        weld.initialize(body.getBody(), bodyB.getBody(), body.getWorldPos());

        world.createJoint(weld);
    }

    public void dispose() {
        bodyB.getWorld().remove(bodyB);
    }

}
