package com.game.framework.core.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.framework.WorldContactListener;
import com.game.framework.core.bodies.Function;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core.bodies.builders.BoxBuilder;
import com.game.framework.core.bodies.builders.CustomBuilder;
import com.game.framework.core2.builders.Buildable;

import java.util.*;

/**
 * Manages a single Box2d World.
 */
public class WorldManager {

    private final World world;
    public Map<String, WorldBody> worldBodies;

    private int nextObjectId = 0;

    private Set<Buildable> bodiesToCreate;
    private WorldContactListener worldContactListener;
    private Map<String, Pair<WorldBody, Function<Void, Void>>> bodiesToDestroy;

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

        this.bodiesToCreate = new HashSet<>();
        this.bodiesToDestroy = new HashMap<>();

        this.worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);
    }

    // CREATE OBJECTS -------------------------------------------------------------------------------------------

    public BoxBuilder boxBody() {
        return new BoxBuilder(this);
    }

    public CustomBuilder customBody(String shapePath) {
        return new CustomBuilder(this, shapePath);
    }

    // ----------------------------------------------------------------------------------------------------------


    /**
     * Update the Worlds physics.
     * @param timeStep The amount of time to simulate.
     */
    public void updatePhysics(float timeStep) {
        addBodies();

        world.step(timeStep, 6, 2);

        worldContactListener.update();

        for (WorldBody body : worldBodies.values()) {
            body.update(timeStep);
        }

        destroyBodies();
    }

    public World getWorld() {
        return world;
    }

    /**
     * Remove a WorldBody from the world.
     * @param id The WorldBody's id.
     * @return The WorldBody that was removed. null if it does not exist.
     */
    public WorldBody remove(String id, Function<Void, Void> callback) {
        if (worldBodies.containsKey(id))
            bodiesToDestroy.put(id, new Pair<>(worldBodies.get(id), callback));
        return worldBodies.get(id);
    }

    public WorldBody remove(String id) {
        return remove(id, null);
    }

    public WorldBody remove(WorldBody body) {
        return remove(body.id(), null);
    }

    public WorldBody remove(WorldBody body, Function<Void, Void> callback) {
        return remove(body.id(), callback);
    }

    public void dispose() {
        for (WorldBody body : worldBodies.values()) {
            body.dispose();
        }

        world.dispose();
    }

    public void addBody(WorldBody body) {
        if (body.id() == null)
            throw new RuntimeException("Body Id cannot be null.");
        worldBodies.put(body.id(), body);
    }

    /**
     * Add a body to the world.
     * @param builder The BodyBuilder that defines the body to add.
     */
    public WorldBody addBody(Buildable builder) {
//        bodiesToCreate.add(builder);

        WorldBody newBody = new WorldBody(this, builder);
        worldBodies.put(newBody.id(), newBody);

        return newBody;
    }

    public WorldBody getBody(String id) {
        return worldBodies.get(id);
    }

    public Iterable<WorldBody> getBodies() {
        return worldBodies.values();
    }

    public String generateId() {
        return Integer.toString(++nextObjectId);
    }

    private void addBodies() {
        for (Buildable builder : bodiesToCreate) {
            WorldBody newBody = new WorldBody(this, builder);
            worldBodies.put(newBody.id(), newBody);
        }
        bodiesToCreate.clear();
    }

    private void destroyBodies() {
        Set<Pair<WorldBody, Function<Void, Void>>> toDestroy = new HashSet<>(bodiesToDestroy.values());
        bodiesToDestroy.clear();

        for (Pair<WorldBody, Function<Void, Void>> body : toDestroy) {
            System.out.println("Destroy: " + body.getKey().id());
            world.destroyBody(body.getKey().body.body);
            worldBodies.remove(body.getKey().id());
            body.getKey().dispose();
        }

        if (!bodiesToDestroy.isEmpty())
            destroyBodies();

        for (Pair<WorldBody, Function<Void, Void>> body : toDestroy) {
            if (body.getValue() != null)
                body.getValue().call(null);
        }
    }

    public class Pair<T, T1> {

        private T key;
        private T1 value;

        public Pair(T key, T1 value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return key;
        }

        public T1 getValue() {
            return value;
        }
    }
}
