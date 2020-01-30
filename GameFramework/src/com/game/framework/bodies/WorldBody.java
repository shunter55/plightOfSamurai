package com.game.framework.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.physics.box2d.Body;
import com.game.framework.world.WorldManager;

import java.util.concurrent.Callable;

public abstract class WorldBody {

    private WorldManager world;
    private Body body;

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

    public abstract void dispose();

}
