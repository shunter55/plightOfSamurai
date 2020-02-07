package com.game.framework.core.bodies;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.framework.core.bodies.joints.Joint;
import com.game.framework.core.controller.InputProcessor;
import com.game.framework.core.renderer.Renderable;
import com.game.framework.core.renderer.WorldBodyAnimation;
import com.game.framework.core.world.WorldManager;

import java.util.ArrayList;
import java.util.List;

public abstract class WorldBody implements Renderable, Collidable {

    private WorldManager world;
    private Body body;

    // Renderable
    private WorldBodyAnimation animation = null;

    // Animated
    private float elapsedTime;
    private Function<Void, Void> animationCallback;

    // Collidable
    private Function<WorldBody, Void> _beginCollision = null;
    private Function<WorldBody, Void> _endCollision = null;

    // Joints
    private List<Joint> joints;

    // Flip
    public boolean isFlippedX = false;
    public boolean isFlippedY = false;

    // When destroyed
    public Function<WorldBody, Void> _onDispose = null;

    // Unique identifier in WorldManager
    private String id;
    // Update function that will be called when the World updates.
    private Function<WorldBody, Void> updateFn;

    protected WorldBody(WorldManager world, String id, Body body) {
        this.world = world;
        this.body = body;

        this.joints = new ArrayList<>();

        this.id = id;

        this.body.setUserData(this);

        world.addBody(this);
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

        incrementTime(elapsedTime);
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
        InputProcessor.getInputProcessor().addInput(this, inputAdapterFn.call(this));
    }

    public Body getBody() {
        return body;
    }

    public String getId() {
        return id;
    }

    public WorldManager getWorld() {
        return world;
    }

    public void flipX() {
        isFlippedX = !isFlippedX;
        rebuildBody(new Vector2(-1, 1));
    }

    public void flipY() {
        isFlippedY = !isFlippedY;
        rebuildBody(new Vector2(1, -1));
    }

    public void rebuildBody(Vector2 scale) {
        BodyDef.BodyType type = BodyDef.BodyType.valueOf(body.getType().toString());
        boolean isSensor = body.getFixtureList().first().isSensor();
        Vector2 pos = getWorldPos().cpy();
        Vector2 dim = getDimensions();
        float density = body.getFixtureList().first().getDensity();

        world.remove(this, aVoid -> {
            this.body = copyBody(type, isSensor, pos, dim, scale, density);
            this.body.setUserData(this);
            world.addBody(this);
            for (Joint joint : joints) {
                joint.buildOn(this);
            }
            return null;
        });

//        for (Joint joint : joints) {
//            world.remove(joint.getBody(), aVoid -> {
//               joint.getBody().rebuildBody(scale);
//               return null;
//            });
//        }
    }

    public abstract Body copyBody(BodyDef.BodyType type, boolean isSensor, Vector2 pos, Vector2 dim, Vector2 scale, float density);


    // Renderable -----------------------------------------------------------------------------------
    public void setWorldPos(Vector2 pos) {
        body.setTransform(pos, body.getTransform().getRotation());
    }

    public void setAngleRadians(float radians) {
        body.setTransform(getWorldPos(), radians);
    }

    public void setAngleDegrees(float degrees) {
        setAngleRadians((float) (degrees * Math.PI / 180));
    }

    @Override
    public Vector2 getWorldPos() {
        return body.getPosition();
    }

    @Override
    public float getRotationRadians() {
        return (float) (body.getAngle() % (2 * Math.PI));
    }
    // ---------------------------------------------------------------------------------------------

    // Animations ------------------------------------------------------------------------------------
    @Override
    public void runAnimation(WorldBodyAnimation animation) {
        runAnimation(animation, null);
    }

    @Override
    public void runAnimation(WorldBodyAnimation animation, Function<Void, Void> callback) {
        this.animation = animation;
        this.animationCallback = callback;

        elapsedTime = 0;
    }

    @Override
    public void incrementTime(float deltaTime) {
        if (animation != null) {
            boolean shouldCallback = false;
            if (elapsedTime + deltaTime >= animation.getFrameTime() * animation.numFrames())
                shouldCallback = true;

            elapsedTime += deltaTime;
            elapsedTime %= animation.getFrameTime() * animation.numFrames();

            if (shouldCallback && animationCallback != null)
                animationCallback.call(null);
        }
    }

    @Override
    public Texture getFrame() {
        if (animation == null)
            return null;

        return animation.getFrame(elapsedTime);
    }

    @Override
    public WorldBodyAnimation getAnimation() {
        if (animation != null) {
            animation.flipHorizontal = isFlippedX;
            animation.flipVertical = isFlippedY;
        }
        return animation;
    }
    // ---------------------------------------------------------------------------------------------

    // Collidable ----------------------------------------------------------------------------------

    public void beginCollision(Function<WorldBody, Void> beginCollisionFn) {
        this._beginCollision = beginCollisionFn;
    }

    public void endCollision(Function<WorldBody, Void> endCollisionFn) {
        this._endCollision = endCollisionFn;
    }

    @Override
    public void beginContact(Collidable other) {
        if (_beginCollision != null) {
            _beginCollision.call((WorldBody) other);
        }
    }

    @Override
    public void endContact(Collidable other) {
        if (_endCollision != null) {
            _endCollision.call((WorldBody) other);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void attachJoint(Joint joint) {
        joint.buildOn(this);
        joints.add(joint);
    }

    public void disposeJoints() {
        for (Joint joint : joints) {
            joint.dispose();
        }
    }

    public void onDispose(Function<WorldBody, Void> fn) {
        this._onDispose = fn;
    }

    /**
     * Deletes the WorldBody from the world.
     */
    public void destroy() {
        world.remove(this);
        InputProcessor.getInputProcessor().removeInput(this);
    }

    public void dispose() {
        if (_onDispose != null)
            _onDispose.call(this);
    }

}
