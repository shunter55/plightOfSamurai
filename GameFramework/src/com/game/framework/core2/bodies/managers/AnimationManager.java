package com.game.framework.core2.bodies.managers;

import com.badlogic.gdx.graphics.Texture;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.renderer.WorldBodyAnimation;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class AnimationManager {
    public AnimationRunner runner;

    private Map<String, WorldBodyAnimation> animations;
    private String defaultAnimation;
    private Queue<WorldBodyAnimation> animationQueue;

    public AnimationManager() {
        this.runner = new AnimationRunner();

        this.animations = new HashMap<>();
        this.animationQueue = new LinkedBlockingQueue<>();
    }

    public void addAnimation(String id, WorldBodyAnimation animation) {
        animations.put(id, animation);
    }

    public void removeAnimation(String id) {
        animations.remove(id);
    }

    public Set<String> getAnimationKeys() {
        return animations.keySet();
    }

    public void update(float elapsedTime) {
        runner.update(elapsedTime);
    }

    /**
      * The Default animation that will play when no other animations to play. This will loop. Must be added.
      * @param id id of the animation to play.
      */
    public void setDefaultAnimation(String id) {
        defaultAnimation = id;
        updateAnimations(null);
    }

    public String getDefaultAnimation() {
        return defaultAnimation;
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

    public void queueAnimation(String id) {
        queueAnimation(id, null);
    }

     // Update the animations that should be playing.
    private void updateAnimations(Function<Void, Void> callback) {
        // No Animations avaliable.
        if (animationQueue.isEmpty() && defaultAnimation == null) {
            return;
        }
        // Run Default Animation.
        else if (animationQueue.isEmpty() && defaultAnimation != null) {
            runner.runAnimation(animations.get(defaultAnimation), callback);
        }
        // Run other Animation.
        else {
            runner.runAnimation(animationQueue.remove(), aVoid -> {
                if (callback != null)
                    callback.call(null);
                updateAnimations(null);
                return null;
            });
        }
    }

}
