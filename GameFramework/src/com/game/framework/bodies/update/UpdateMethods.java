package com.game.framework.bodies.update;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.game.framework.bodies.UpdateFunction;
import com.game.framework.bodies.WorldBody;

public class UpdateMethods {

    public static UpdateFunction<WorldBody> wasdMovement(final float speed) {
        return new UpdateFunction<WorldBody>() {
            @Override
            public void update(WorldBody body) {
                body.getBody().setLinearVelocity(0f, 0f);

                float vel = speed;

                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    vel *= 10;
                }

                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    body.getBody().setLinearVelocity(-vel, body.getBody().getLinearVelocity().y);
                }

                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    body.getBody().setLinearVelocity(vel, body.getBody().getLinearVelocity().y);
                }

                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    body.getBody().setLinearVelocity(body.getBody().getLinearVelocity().x, vel);
                }

                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    body.getBody().setLinearVelocity(body.getBody().getLinearVelocity().x, -vel);
                }
            }
        };
    }

}
