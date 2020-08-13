package com.game.framework.core2.bodies;

import com.badlogic.gdx.math.Vector2;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.controller.InputProcessor;
import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.managers.BodyManager;
import com.game.framework.core2.bodies.managers.RenderManager;
import com.game.framework.core2.builders.BodyBuilder;
import com.game.framework.core2.builders.Buildable;

public class WorldBody {
    public WorldManager world;

    public BodyManager body;
    public RenderManager render;
    public ControlManager controller;

    private String _id;

    // Update function that will be called when the World updates.
    private Function<WorldBody, Void> updateFn;
    // When destroyed
    public Function<WorldBody, Void> _onDispose = null;

    public WorldBody(WorldManager world, Buildable builder, String idOverride) {
        this.world = world;
        this.body = new BodyManager(this, builder.getBodyBuilder());
        this.render = new RenderManager(this.body);
        this.controller = new ControlManager(this);

        this._id = idOverride;

        world.addBody(this);
    }

    public WorldBody(WorldManager world, Buildable builder) {
        this(world, builder, world.generateId());
    }

    public String id() {
        return _id;
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

        render.update(elapsedTime);
        controller.update();
        body.update(elapsedTime);
    }

    /**
     * Deletes the WorldBody from the world.
     */
    public void destroy() {
        world.remove(this);
        InputProcessor.getInputProcessor().removeInput(this);
    }

    public float dist(WorldBody other) {
        return new Vector2(body.getWorldPos().x - other.body.getWorldPos().x, body.getWorldPos().y - other.body.getWorldPos().y).len();
    }

    public void onDispose(Function<WorldBody, Void> fn) {
        this._onDispose = fn;
    }

    public void dispose() {
        if (_onDispose != null)
            _onDispose.call(this);

        body.dispose();
    }
}
