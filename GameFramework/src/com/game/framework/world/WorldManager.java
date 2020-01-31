package com.game.framework.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.framework.bodies.BoxWorldBody;
import com.game.framework.bodies.CustomWorldBody;
import com.game.framework.bodies.WorldBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages a single Box2d World.
 */
public class WorldManager {

    private final World world;
    private Map<String, WorldBody> worldBodies;

    private int nextObjectId = 0;

    public WorldManager() {
        this(new Vector2(0, 0), true);
    }

    /**
     * Creates a World object that will be managed.
     * @param gravity the world gravity vector.
     * @param doSleep improve performance by not simulating inactive bodies.
     */
    public WorldManager(Vector2 gravity, boolean doSleep) {
        this.world = new World(gravity, doSleep);
        this.worldBodies = new HashMap<>();
    }

    /**
     * Update the Worlds physics.
     * @param timeStep The amount of time to simulate.
     */
    public void updatePhysics(float timeStep) {
        world.step(timeStep, 6, 2);

        for (WorldBody body : worldBodies.values()) {
            body.update();
        }
    }

    public World getWorld() {
        return world;
    }

    public void dispose() {
        for (WorldBody body : worldBodies.values()) {
            body.dispose();
        }

        world.dispose();
    }

    /**
     * Add a Box object to the World.
     * @param type
     * @param x
     * @param y
     * @param width
     * @param height
     * @param density
     * @return
     */
    public WorldBody createBox(BodyDef.BodyType type, float x, float y, float width, float height, float density) {
        String id = getNextId();
        BoxWorldBody body = new BoxWorldBody(this, id, type, x, y, width, height, density);
        worldBodies.put(id, body);

        return body;
    }

    public WorldBody createCustom(BodyDef.BodyType type, String imgPath, String shapePath, float x, float y, float scale, float density) {
        String id = getNextId();
        CustomWorldBody body = new CustomWorldBody(this, id, imgPath, shapePath, type, x, y, scale, density);
        worldBodies.put(id, body);

        return body;
    }

    public Iterable<WorldBody> getBodies() {
        return worldBodies.values();
    }

    private String getNextId() {
        return Integer.toString(++nextObjectId);
    }

}
