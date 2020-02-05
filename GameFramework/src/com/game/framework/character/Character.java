package com.game.framework.character;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.game.framework.core.bodies.CustomWorldBody;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.bodies.joints.Weld;
import com.game.framework.core.renderer.WorldBodyAnimation;
import com.game.framework.core.world.WorldManager;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Represent a game character.
 * Higher abstraction from WorldBodies.
 */
public class Character extends CustomWorldBody {

    private WorldManager world;

    private Map<String, WorldBodyAnimation> animations;
    private String defaultAnimation;
    private Queue<WorldBodyAnimation> animationQueue;

    public Character(WorldManager world, String shapePath, BodyDef.BodyType type, Vector2 pos, Vector2 scale, float density) {
        super(world, world.generateId(), shapePath, type, pos.x, pos.y, scale, density);
        world.addBody(this);

        this.world = world;

        animations = new HashMap<>();
        animationQueue = new LinkedBlockingQueue<>();
    }

    // Animations -----------------------------------------------------------------------------------------------------

    public void addAnimation(String id, WorldBodyAnimation animation) {
        animations.put(id, animation);
    }

    public void removeAnimation(String id) {
        animations.remove(id);
    }

    public Set<String> getAnimationKeys() {
        return animations.keySet();
    }

    /**
     * Run animation immediately clearing all other animations.
     * @param id Id of the animation to run.
     */
    public void runAnimation(String id, Function<Void, Void> callback) {
        if (!animations.containsKey(id)) {
            throw new RuntimeException("Add " + id + " animation first using addAnimation!");
        }

        animationQueue.clear();

        // Run default animation.
        if (id.equals(defaultAnimation)) {
            updateAnimations(callback);
        }
        // Run other Animation.
        else {
            queueAnimation(id, callback);
        }
    }

    public void runAnimation(String id) {
        runAnimation(id, null);
    }

    /**
     * Queue an animation to run after other animations finish.
     * @param id Id of the animation to queue.
     */
    public void queueAnimation(String id, Function<Void, Void> callback) {
        if (!animations.containsKey(id)) {
            throw new RuntimeException("Add " + id + " animation first using addAnimation!");
        }
        if (id.equals(defaultAnimation)) {
            throw new RuntimeException("Do run the defaultAnimation. This will play automatically after all other animations finish.");
        }

        animationQueue.add(animations.get(id));
        updateAnimations(callback);
    }

    public void queueAnimation(String id) {
        queueAnimation(id, null);
    }

    /**
     * The Default animation that will play when no other animations to play. This will loop. Must be added.
     * @param id id of the animation to play.
     */
    public void setDefaultAnimation(String id) {
        defaultAnimation = id;
        updateAnimations(null);
    }

    // update the animations that should be playing.
    private void updateAnimations(Function<Void, Void> callback) {
        // No Animations avaliable.
        if (animationQueue.isEmpty() && defaultAnimation == null) {
            return;
        }
        // Run Default Animation.
        else if (animationQueue.isEmpty() && defaultAnimation != null) {
            runAnimation(animations.get(defaultAnimation), callback);
        }
        // Run other Animation.
        else {
            runAnimation(animationQueue.remove(), aVoid -> {
                if (callback != null)
                    callback.call(null);
                updateAnimations(null);
                return null;
            });
        }
    }

    public String getDefaultAnimation() {
        return defaultAnimation;
    }

    // Sensors --------------------------------------------------------------------------------------------------------

    /**
     * Attach a sensor to the Character.
     * @param pos Relative position from the main Character body.
     * @param size Size of the Sensor.
     */
    public void attachBoxSensor(Vector2 pos,
                                Vector2 size,
                                Function<WorldBody, Void> beginCollision,
                                Function<WorldBody, Void> endCollision) {
        this.attachJoint(new Weld(aVoid -> {
            WorldBody body = world.createBox(
                BodyDef.BodyType.DynamicBody,
                true,
                this.getWorldPos().x, this.getWorldPos().y,
                size.x, size.y,
                0.000000001f);

            if (beginCollision != null) {
                body.beginCollision(beginCollision);
            }
            if (endCollision != null) {
                body.endCollision(endCollision);
            }

            return body;
        },
        pos,
        0f));
    }



}
