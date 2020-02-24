package com.game.framework.core.bodies.joints;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.bodies.builders.WorldBodyBuilder;

public class Weld implements Joint {

    private WorldBodyBuilder builder;
    private Vector2 offset;
    private float offsetAngle;

    private WorldBody bodyB = null;

    public Weld(WorldBodyBuilder builder, Vector2 offset, float offsetAngle) {
        this.builder = builder;
        this.offset = offset;
        this.offsetAngle = offsetAngle;
    }

    public void buildOn(WorldBody body) {
        World world = body.getWorld().getWorld();

        WeldJointDef weld = new WeldJointDef();

        if (bodyB != null)
            builder.id(bodyB.getId());

        if (body.isFlippedX)
            builder.scaleX(-Math.abs(builder._scale.x));
        else
            builder.scaleX(Math.abs(builder._scale.x));
        if (body.isFlippedY)
            builder.scaleY(-Math.abs(builder._scale.y));
        else
            builder.scaleY(Math.abs(builder._scale.y));

        bodyB = builder.build();

        Vector2 offset = new Vector2(
            body.isFlippedX ? -this.offset.x : this.offset.x,
            body.isFlippedY ? -this.offset.y : this.offset.y
        );

        bodyB.setWorldPos(body.getWorldPos().add(offset));
        bodyB.setAngleRadians(offsetAngle);

        weld.initialize(body.getBody(), bodyB.getBody(), body.getWorldPos());

        world.createJoint(weld);
    }

    @Override
    public WorldBody getBody() {
        return bodyB;
    }

    public void dispose() {
        //bodyB.getWorld().remove(bodyB);
    }

}
