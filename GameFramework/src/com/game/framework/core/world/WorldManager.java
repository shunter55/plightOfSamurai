package com.game.framework.core.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.framework.core.bodies.BoxWorldBody;
import com.game.framework.core.bodies.CustomWorldBody;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import javafx.util.Pair;

import java.util.*;

/**
 * Manages a single Box2d World.
 */
public class WorldManager {

    private final World world;
    private Map<String, WorldBody> worldBodies;

    private int nextObjectId = 0;

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
        this.bodiesToDestroy = new HashMap<>();

        world.setContactListener(getContactListener());
    }

    /**
     * Update the Worlds physics.
     * @param timeStep The amount of time to simulate.
     */
    public void updatePhysics(float timeStep) {
        world.step(timeStep, 6, 2);

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
        return remove(body.getId(), null);
    }

    public WorldBody remove(WorldBody body, Function<Void, Void> callback) {
        return remove(body.getId(), callback);
    }

    public void dispose() {
        for (WorldBody body : worldBodies.values()) {
            body.dispose();
        }

        world.dispose();
    }

    public void addBody(WorldBody body) {
        System.out.println("addBody");
        worldBodies.put(body.getId(), body);
    }

    public WorldBody getBody(String id) {
        return worldBodies.get(id);
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
    public WorldBody createBox(BodyDef.BodyType type, boolean isSensor, float x, float y, float width, float height, float density) {
        BoxWorldBody body = new BoxWorldBody(this, generateId(), type, isSensor, x, y, width, height, density);
        addBody(body);
        return body;
    }

    public WorldBody createCustom(BodyDef.BodyType type, String shapePath, float x, float y, Vector2 scale, float density) {
        CustomWorldBody body = new CustomWorldBody(this, generateId(), shapePath, type, x, y, scale, density);
        addBody(body);
        return body;
    }

    public Iterable<WorldBody> getBodies() {
        return worldBodies.values();
    }

    public String generateId() {
        return Integer.toString(++nextObjectId);
    }

    private void destroyBodies() {
        Set<Pair<WorldBody, Function<Void, Void>>> toDestroy = new HashSet<>(bodiesToDestroy.values());
        bodiesToDestroy.clear();

        for (Pair<WorldBody, Function<Void, Void>> body : toDestroy) {
            world.destroyBody(body.getKey().getBody());
            worldBodies.remove(body.getKey().getId());
            body.getKey().dispose();
            if (body.getValue() != null)
                body.getValue().call(null);
        }
    }

    private ContactListener getContactListener() {
        return new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                WorldBody o1 = (WorldBody) contact.getFixtureA().getBody().getUserData();
                WorldBody o2 = (WorldBody) contact.getFixtureB().getBody().getUserData();

                o1.beginContact(o2);
                o2.beginContact(o1);
            }

            @Override
            public void endContact(Contact contact) {
                WorldBody o1 = (WorldBody) contact.getFixtureA().getBody().getUserData();
                WorldBody o2 = (WorldBody) contact.getFixtureB().getBody().getUserData();

                o1.endContact(o2);
                o2.endContact(o1);
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
    }

}
