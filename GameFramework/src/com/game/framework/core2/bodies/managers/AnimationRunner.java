package com.game.framework.core2.bodies.managers;

import com.badlogic.gdx.graphics.Texture;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.renderer.WorldBodyAnimation;

public class AnimationRunner {

    private WorldBodyAnimation animation;

    private float elapsedTime;
    private Function<Void, Void> animationCallback;

    public void runAnimation(WorldBodyAnimation animation, Function<Void, Void> callback) {
        this.animation = animation;
        this.animationCallback = callback;

        elapsedTime = 0;
    }

    public void runAnimation(WorldBodyAnimation animation) {
        runAnimation(animation, null);
    }

    public void update(float deltaTime) {
        if (animation != null) {
            boolean shouldCallback = false;
            if (elapsedTime + deltaTime >= animation.getFrameTime() * animation.numFrames())
                shouldCallback = true;

            elapsedTime += deltaTime;
            elapsedTime %= animation.getFrameTime() * animation.numFrames();

            if (shouldCallback && animationCallback != null)
                animationCallback.call(null);
        }
    }

    public Texture getFrame() {
        if (animation == null)
            return null;

        return animation.getFrame(elapsedTime);
    }

    public WorldBodyAnimation getAnimation(BodyManager body) {
        if (animation != null) {
            animation.flipHorizontal = body.isFlippedX();
            animation.flipVertical = body.isFlippedY();
        }
        return animation;
    }

}
