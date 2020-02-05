package com.game.framework.core.bodies.update;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;

public class UpdateMethods {

    public static Function<WorldBody, Void> wasdMovement(final float speed) {
        return new Function<WorldBody, Void>() {
            @Override
            public Void call(WorldBody body) {
                body.getBody().setLinearVelocity(0f, 0f);

                float vel = speed;

                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    vel *= 2.5;
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

                return null;
            }
        };
    }

}
