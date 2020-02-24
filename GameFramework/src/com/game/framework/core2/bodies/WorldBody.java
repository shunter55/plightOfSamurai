package com.game.framework.core2.bodies;

import com.badlogic.gdx.InputAdapter;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.controller.InputProcessor;
import com.game.framework.core.particles.Particle;
import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.managers.BodyManager;
import com.game.framework.core2.bodies.managers.CollisionManager;
import com.game.framework.core2.bodies.managers.RenderManager;
import com.game.framework.core2.builders.BodyBuilder;

import java.util.ArrayList;
import java.util.List;

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

    public WorldBody(BodyBuilder builder, String idOverride) {
        this.world = builder.world;
        this.body = new BodyManager(world, this, builder);
        this.render = new RenderManager(this.body);
        this.controller = new ControlManager(this);

        this._id = idOverride;

        world.addBody(this);
    }

    public WorldBody(BodyBuilder builder) {
        this(builder, builder.world.generateId());
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
    }

    /**
     * Deletes the WorldBody from the world.
     */
    public void destroy() {
        world.remove(this);
        InputProcessor.getInputProcessor().removeInput(this);
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
