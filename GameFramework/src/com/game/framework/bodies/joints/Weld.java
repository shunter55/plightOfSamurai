package com.game.framework.bodies.joints;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.game.framework.bodies.Function;
import com.game.framework.bodies.WorldBody;
import com.game.framework.world.WorldManager;

public class Weld implements Joint {

    private Function<Void, WorldBody> bodyFn;
    private Vector2 offset;
    private float offsetAngle;

    private RopeJointDef def;

    public Weld(Function<Void, WorldBody> bodyFn, Vector2 offset, float offsetAngle) {
        this.bodyFn = bodyFn;
        this.offset = offset;
        this.offsetAngle = offsetAngle;
    }

    public void buildOn(WorldBody body) {
        World world = body.getWorld().getWorld();

        WeldJointDef weld = new WeldJointDef();

        WorldBody bodyB = bodyFn.call(null);
        bodyB.setWorldPos(body.getWorldPos().add(offset));
        bodyB.setAngleRadians(offsetAngle);

        weld.initialize(body.getBody(), bodyB.getBody(), body.getWorldPos());

        world.createJoint(weld);
    }

}
