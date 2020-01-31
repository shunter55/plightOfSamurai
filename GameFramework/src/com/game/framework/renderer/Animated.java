package com.game.framework.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.game.framework.bodies.Function;
import com.game.framework.bodies.WorldBody;

public interface Animated {

    public void runAnimation(WorldBodyAnimation animation);

    public void runAnimation(WorldBodyAnimation animation, Function<Void, Void> callback);

    public Texture getFrame();

    public void incrementTime(float deltaTime);

}
