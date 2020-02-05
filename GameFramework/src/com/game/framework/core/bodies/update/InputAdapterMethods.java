package com.game.framework.core.bodies.update;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;

public class InputAdapterMethods {

    public static Function<WorldBody, InputAdapter> wasdInputAdapter(final float speed) {
        return new Function<WorldBody, InputAdapter>() {
            @Override
            public InputAdapter call(final WorldBody body) {
                return new InputAdapter() {
                    float vel = speed;

                    @Override
                    public boolean keyDown (int key) {
                        switch (key) {
                            case Input.Keys.A:
                                body.getBody().setLinearVelocity(-vel, body.getBody().getLinearVelocity().y);
                                break;
                            case Input.Keys.D:
                                body.getBody().setLinearVelocity(vel, body.getBody().getLinearVelocity().y);
                                break;
                            case Input.Keys.W:
                                body.getBody().setLinearVelocity(body.getBody().getLinearVelocity().x, vel);
                                break;
                            case Input.Keys.S:
                                body.getBody().setLinearVelocity(body.getBody().getLinearVelocity().x, -vel);
                                break;
                            case Input.Keys.SPACE:
                                body.getBody().setLinearVelocity(
                                        body.getBody().getLinearVelocity().x * 2.5f
                                        , body.getBody().getLinearVelocity().y * 2.5f);
                                break;
                        }
                        return true;
                    }

                    @Override
                    public boolean keyUp (int key) {
                        switch (key) {
                            case Input.Keys.A:
                                body.getBody().setLinearVelocity(0f, body.getBody().getLinearVelocity().y);
                                break;
                            case Input.Keys.D:
                                body.getBody().setLinearVelocity(0f, body.getBody().getLinearVelocity().y);
                                break;
                            case Input.Keys.W:
                                body.getBody().setLinearVelocity(body.getBody().getLinearVelocity().x, 0f);
                                break;
                            case Input.Keys.S:
                                body.getBody().setLinearVelocity(body.getBody().getLinearVelocity().x, 0f);
                                break;
                            case Input.Keys.SPACE:
                                body.getBody().setLinearVelocity(
                                        body.getBody().getLinearVelocity().x / 2.5f
                                        , body.getBody().getLinearVelocity().y / 2.5f);
                                break;
                        }
                        return true;
                    }

                    @Override
                    public boolean touchDown (int x, int y, int pointer, int button) {

                        return true;
                    }
                };
            }
        };
    }

}
