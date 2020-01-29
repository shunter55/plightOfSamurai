package com.game.framework.bodies;

import com.badlogic.gdx.physics.box2d.Body;
import com.game.framework.world.WorldManager;

import java.util.concurrent.Callable;

public abstract class WorldBody {

    private WorldManager world;
    private Body body;

    // Unique identifier in WorldManager
    private String id;
    // Update function that will be called when the World updates.
    private UpdateFunction<WorldBody> updateFn;

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
                updateFn.update(this);
            } catch (Exception e) {
                throw new RuntimeException("Could not update " + this + ". " + e);
            }
        }
    }

    /**
     * Add a function that will be called every time the Body's World's Physics is updated.
     * @param updateFn
     */
    public void setUpdate(UpdateFunction<WorldBody> updateFn) {
        this.updateFn = updateFn;
    }

    public Body getBody() {
        return body;
    }

    public abstract void dispose();

}
