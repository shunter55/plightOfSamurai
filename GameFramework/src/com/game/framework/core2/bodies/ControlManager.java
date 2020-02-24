package com.game.framework.core2.bodies;

import com.badlogic.gdx.InputAdapter;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.controller.InputProcessor;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.bodies.managers.CollisionManager;

public class ControlManager {

    private WorldBody body;

    private Function<WorldBody, Void> updateFn;
    public CollisionManager collisions;

    public ControlManager(WorldBody body) {
        this.body = body;
        this.collisions = new CollisionManager();
    }

    public void update() {
        if (updateFn != null) {
            try {
                updateFn.call(body);
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
        InputProcessor.getInputProcessor().addInput(body, inputAdapterFn.call(body));
    }

    /**
     * Add an InputAdapter to the WorldBody to receive input events.
     */
    public void setInputAdapter(InputAdapter inputAdapter) {
        InputProcessor.getInputProcessor().addInput(body, inputAdapter);
    }

}
