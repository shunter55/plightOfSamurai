package com.game.framework.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.framework.bodies.BoxWorldBody;
import com.game.framework.bodies.Collidable;
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

    private Map<String, WorldBody> bodiesToDestroy;

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
     * @return The WorldBody that was removed.
     */
    public WorldBody remove(String id) {
        bodiesToDestroy.put(id, worldBodies.get(id));
        return worldBodies.get(id);
    }

    public WorldBody remove(WorldBody body) {
        return remove(body.getId());
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
    public WorldBody createBox(BodyDef.BodyType type, boolean isSensor, float x, float y, float width, float height, float density) {
        String id = getNextId();
        BoxWorldBody body = new BoxWorldBody(this, id, type, isSensor, x, y, width, height, density);
        worldBodies.put(id, body);

        return body;
    }

    public WorldBody createCustom(BodyDef.BodyType type, String shapePath, float x, float y, float scale, float density) {
        String id = getNextId();
        CustomWorldBody body = new CustomWorldBody(this, id, shapePath, type, x, y, scale, density);
        worldBodies.put(id, body);

        return body;
    }

    public Iterable<WorldBody> getBodies() {
        return worldBodies.values();
    }

    private String getNextId() {
        return Integer.toString(++nextObjectId);
    }

    private void destroyBodies() {
        for (WorldBody body : bodiesToDestroy.values()) {
            world.destroyBody(body.getBody());
            worldBodies.remove(body.getId());
            body.dispose();
        }

        bodiesToDestroy.clear();
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
