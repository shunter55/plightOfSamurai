package com.game.framework.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.game.framework.renderer.Renderable;
import com.game.framework.world.WorldManager;

public abstract class WorldBody implements Renderable {

    private WorldManager world;
    private Body body;
    private Texture texture = null;

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
    public void update() {
        if (updateFn != null) {
            try {
                updateFn.call(this);
            } catch (Exception e) {
                throw new RuntimeException("Could not update " + this + ". " + e);
            }
        }
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

    public void setImage(String imgPath) {
        texture = new Texture(imgPath);
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Vector2 getWorldPos() {
        return body.getPosition();
    }

    @Override
    public float getRotationRadians() {
        return (float) (body.getAngle() % (2 * Math.PI));
    }

    public abstract void dispose();

}
