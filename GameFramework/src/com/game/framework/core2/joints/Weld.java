package com.game.framework.core2.joints;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core.bodies.builders.WorldBodyBuilder;
import com.game.framework.core2.builders.BodyBuilder;

public class Weld implements Joint {

    private Vector2 offset;
    private float offsetAngle;

    private BodyBuilder bodyBBuilder;
    private WorldBody bodyB = null;
    private String oldId = null;

    /**
     * @param builder Builder for the Body to add.
     * @param offset Offset for the body.
     * @param offsetRotation Offset rotation for the body.
     */
    public Weld(BodyBuilder builder, Vector2 offset, float offsetRotation) {
        this.bodyBBuilder = builder.copy();
        this.offset = offset;
        this.offsetAngle = offsetRotation;
    }

    public void buildOn(WorldBody body) {
        World world = body.world.getWorld();

        // Reset Scale of BodyBuilder.
        if (body.body.isFlippedX())
            bodyBBuilder.scaleX(-Math.abs(bodyBBuilder._scale.x));
        else
            bodyBBuilder.scaleX(Math.abs(bodyBBuilder._scale.x));
        if (body.body.isFlippedY())
            bodyBBuilder.scaleY(-Math.abs(bodyBBuilder._scale.y));
        else
            bodyBBuilder.scaleY(Math.abs(bodyBBuilder._scale.y));

        if (bodyB == null) {
            bodyB = new WorldBody(bodyBBuilder);
        } else {
            dispose();
            bodyB.body.buildBody(bodyBBuilder);
            bodyB.world.addBody(bodyB);
        }

        WeldJointDef weld = new WeldJointDef();

        Vector2 offset = new Vector2(
            body.body.isFlippedX() ? -this.offset.x : this.offset.x,
            body.body.isFlippedY() ? -this.offset.y : this.offset.y
        );

        bodyB.body.setWorldPos(body.body.getWorldPos().add(offset));
        bodyB.body.setRotationRadians(offsetAngle);

        weld.initialize(body.body.body, bodyB.body.body, body.body.getWorldPos());

        world.createJoint(weld);
    }

    @Override
    public WorldBody getBody() {
        return bodyB;
    }

    public void dispose() {
        bodyB.world.remove(bodyB);
    }

}
