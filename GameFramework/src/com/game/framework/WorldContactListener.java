package com.game.framework;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.framework.core.bodies.Function;
import com.game.framework.core2.bodies.WorldBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Facilitates calling the BeginContact and EndContact callbacks outside of the World's update step.
 * This allows begin and end callbacks to create and destroy bodies within the parameters of Box2d.
 */
public class WorldContactListener implements ContactListener {

    private List<Function<Void, Void>> beginContactCallbacks = new ArrayList<>();
    private List<Function<Void, Void>> endContactCallbacks = new ArrayList<>();
    private List<Function<Void, Void>> preSolveCallbacks = new ArrayList<>();
    private List<Function<Void, Void>> postSolveCallbacks = new ArrayList<>();

    /**
     * Calls all the begin and end contact callbacks since the last update.
     */
    public void update() {
        updateBeginContact();
        updateEndContact();
        updatePreSolve();
        updatePostSolve();
    }

    /**
     * Calls all the beginContact callbacks since the last update.
     */
    private void updateBeginContact() {
        for (Function<Void, Void> begContact : beginContactCallbacks) {
            begContact.call(null);
        }
        beginContactCallbacks.clear();
    }

    /**
     * Calls all the endContact callbacks since the last update.
     */
    private void updateEndContact() {
        for (Function<Void, Void> endContact : endContactCallbacks) {
            endContact.call(null);
        }
        endContactCallbacks.clear();
    }

    private void updatePreSolve() {
        for (Function<Void, Void> preSolve : preSolveCallbacks) {
            preSolve.call(null);
        }
        preSolveCallbacks.clear();
    }

    private void updatePostSolve() {
        for (Function<Void, Void> postSolve : postSolveCallbacks) {
            postSolve.call(null);
        }
        postSolveCallbacks.clear();
    }

    @Override
    public void beginContact(Contact contact) {
        WorldBody o1 = (WorldBody) contact.getFixtureA().getBody().getUserData();
        WorldBody o2 = (WorldBody) contact.getFixtureB().getBody().getUserData();

        beginContactCallbacks.add((Void) -> {
            o1.controller.collisions.beginContact(o2);
            o2.controller.collisions.beginContact(o1);

            return null;
        });
    }

    @Override
    public void endContact(Contact contact) {
        WorldBody o1 = (WorldBody) contact.getFixtureA().getBody().getUserData();
        WorldBody o2 = (WorldBody) contact.getFixtureB().getBody().getUserData();

        endContactCallbacks.add((Void) -> {
            o1.controller.collisions.endContact(o2);
            o2.controller.collisions.endContact(o1);

            return null;
        });
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        WorldBody o1 = (WorldBody) contact.getFixtureA().getBody().getUserData();
        WorldBody o2 = (WorldBody) contact.getFixtureB().getBody().getUserData();

        preSolveCallbacks.add((Void) -> {
            o1.controller.collisions.preSolve(o2);
            o2.controller.collisions.preSolve(o1);

            return null;
        });
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        WorldBody o1 = (WorldBody) contact.getFixtureA().getBody().getUserData();
        WorldBody o2 = (WorldBody) contact.getFixtureB().getBody().getUserData();

        postSolveCallbacks.add((Void) -> {
            o1.controller.collisions.postSolve(o2);
            o2.controller.collisions.postSolve(o1);

            return null;
        });
    }
}
