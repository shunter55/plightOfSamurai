package com.game.framework.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.game.framework.renderer.Animated;
import com.game.framework.renderer.Renderable;
import com.game.framework.renderer.WorldBodyAnimation;
import com.game.framework.world.WorldManager;

import java.util.ArrayList;
import java.util.List;

public abstract class WorldBody implements Renderable, Animated {

    private WorldManager world;
    private Body body;

    // Renderable
    private WorldBodyAnimation animation = null;

    // Animated
    private float elapsedTime;
    private Function<Void, Void> animationCallback;

    // Unique identifier in WorldManager
    private String id;
    // Update function that will be called when the World updates.
    private Function<WorldBody, Void> updateFn;
    // InputAdapter function.
    private Function<WorldBody, InputAdapter> inputAdapterFn;

    WorldBody(WorldManager world, String id, Body body) {
        this.world = world;
        this.body = body;

        this.id = id;
    }

    /**
     * Updates the WorldBody.
     * Update Method is provided by setUpdate.
     */
    public void update(float elapsedTime) {
        if (updateFn != null) {
            try {
                updateFn.call(this);
            } catch (Exception e) {
                throw new RuntimeException("Could not update " + this + ". " + e);
            }
        }

        incrementTime(elapsedTime);
    }

    /**
     * Add a function that will be called every time the Body's World's Physics is updated.
     * @param updateFn
     */
    public void setUpdate(Function<WorldBody, Void> updateFn) {
        this.updateFn = updateFn;
    }

    /**
     * Add an InputAdapter to the WorldBody to receive input events.
     * @param inputAdapterFn Function that takes this WorldBody and returns a InputAdapter for it.
     */
    public void setInputAdapter(Function<WorldBody, InputAdapter> inputAdapterFn) {
        Gdx.input.setInputProcessor(inputAdapterFn.call(this));
    }

    public Body getBody() {
        return body;
    }


    // Renderable -----------------------------------------------------------------------------------

//    /**
//     * Sets the animation to use.
//     * @param animation The Animation to use.
//     */
//    public void setAnimation(WorldBodyAnimation animation) {
//        runAnimation(animation);
//    }
//
//    /**
//     * Use setAnimation(WorldBodyAnimation) with 1 Texture if you have many bodies using the same Texture.
//     * Creates a new WorldBodyAnimation with 1 image.
//     * @param imgPath The path to the image to load.
//     */
//    public void setImage(String imgPath) {
//        runAnimation(new WorldBodyAnimation(imgPath));
//    }

    @Override
    public Vector2 getWorldPos() {
        return body.getPosition();
    }

    @Override
    public float getRotationRadians() {
        return (float) (body.getAngle() % (2 * Math.PI));
    }
    // ---------------------------------------------------------------------------------------------

    // Animations ------------------------------------------------------------------------------------

    @Override
    public void runAnimation(WorldBodyAnimation animation) {
        runAnimation(animation, null);
    }

    @Override
    public void runAnimation(WorldBodyAnimation animation, Function<Void, Void> callback) {
        this.animation = animation;
        this.animationCallback = callback;

        elapsedTime = 0;
    }

    @Override
    public void incrementTime(float deltaTime) {
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

    @Override
    public Texture getFrame() {
        if (animation == null)
            return null;

        return animation.getFrame(elapsedTime);
    }

    @Override
    public WorldBodyAnimation getAnimation() {
        return animation;
    }

    // ---------------------------------------------------------------------------------------------

    public abstract void dispose();

}
