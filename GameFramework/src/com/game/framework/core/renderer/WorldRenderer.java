package com.game.framework.core.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core.particles.Particle;
import com.game.framework.core.utils.Utils;
import com.game.framework.core.world.WorldManager;

public class WorldRenderer {

    private Camera camera;

    public SpriteBatch spriteBatch;
    private Box2DDebugRenderer debugRenderer;

    public enum CameraMode {
        // Objects will be stretched to fill the screen.
        Stretch,
        // Aspect ratio of Objects will be preserved. Screen will have sidebars.
        Zoom
    }

    /**
     * Create a World Renderer.
     * @param mode The mode to render.
     * @param viewWidth
     * @param viewHeight
     */
    public WorldRenderer(CameraMode mode, float viewWidth, float viewHeight) {
        this(getCamera(mode, viewWidth, viewHeight), getWorldRatio(mode, viewWidth, viewHeight));
    }

    public WorldRenderer(Camera camera, Vector2 worldRatio) {
        this.camera = camera;

        spriteBatch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
    }

    public void resetCamera(CameraMode mode, float viewWidth, float viewHeight) {
        this.camera = getCamera(mode, viewWidth, viewHeight);
    }

    /**
     * Move the Camera by deltaPos.
     * @param deltaPos Move the camera by deltaPos.
     */
    public void moveBy(Vector2 deltaPos) {
        camera.translate(deltaPos.x, deltaPos.y, camera.position.z);
        camera.update();
    }

    public Vector2 getWorldPos() {
        return new Vector2(camera.position.x, camera.position.y);
    }

    public void render(WorldManager world) {
        render(world, camera);
    }

    public void render(WorldManager world, Camera camera) {
        // Render Box2d Body frames.
        debugRenderer.render(world.getWorld(), camera.combined);

        // Render WorldBody Images.
        for (WorldBody body : world.getBodies()) {
            render(body.render);
        }
    }

    /**
     * Convert Pixel Coordinate to World Coordinate.
     * @param pixCoord Pixel coordinate on the screen.
     * @return World coordinate.
     */
    public Vector2 unproject(Vector2 pixCoord) {
         Vector3 worldCoord = camera.unproject(new Vector3(pixCoord, 0));
         return new Vector2(worldCoord.x, worldCoord.y);
         //return new Vector2(worldCoord.x * worldRatio.x, worldCoord.y * worldRatio.y);
    }

    private void render(Renderable renderable) {
        WorldBodyAnimation animation = renderable.getAnimation();

        // Do not render if there is no sprite.
        if (animation == null)
            return;

        Sprite sprite = new Sprite(renderable.getFrame());

        sprite.setFlip(animation.flipHorizontal, animation.flipVertical);
        Vector2 origin = new Vector2(renderable.getOrigin().x, renderable.getOrigin().y);
        if (animation.flipHorizontal) {
            origin.x = Math.abs(renderable.getDimensions().x) - renderable.getOrigin().x;
        //    origin.y = Math.abs(renderable.getDimensions().y) - renderable.getOrigin().y;
        }

        // Image width and height in terms of World Coordinates.
        Vector2 worldRatio = Utils.toWorldRatio(
            (int) sprite.getWidth(),
            (int) sprite.getHeight(),
            renderable.getDimensions().x,
            renderable.getDimensions().y);

        // Set Origin.
        sprite.setOrigin(origin.x, origin.y);

        // Set Rotation.
        sprite.rotate((float) Math.toDegrees(renderable.getRotationRadians()));

        // Set Position and Size
        sprite.setBounds(
            renderable.getWorldPos().x - origin.x,
            renderable.getWorldPos().y - origin.y,
            worldRatio.x,
            worldRatio.y);

        // Draw the Image.
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Particle particle : renderable.getParticles()) {
            particle.getEffect().draw(spriteBatch);
        }
        sprite.draw(spriteBatch);
        spriteBatch.end();

        for (Particle particle : renderable.getParticles()) {
            if (particle.isComplete())
                particle.reset();
        }
    }

    public void dispose() {
        debugRenderer.dispose();
        spriteBatch.dispose();
    }

    private static Camera getCamera(CameraMode mode, float worldWidth, float worldHeight) {
        switch (mode) {
            case Stretch: return new OrthographicCamera(worldWidth, worldHeight);
            case Zoom: {
                Vector2 worldRatio = Utils.toWorldRatio(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), worldWidth, worldHeight);
                return new OrthographicCamera(worldRatio.x, worldRatio.y);
            }
        }
        throw new RuntimeException("Invalid camera mode: " + mode);
    }

    private static Vector2 getWorldRatio(CameraMode mode, float worldWidth, float worldHeight) {
        switch (mode) {
            case Stretch: return new Vector2(1, 1);
            case Zoom: {
                Vector2 viewportSize = Utils.toWorldRatio(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), worldWidth, worldHeight);
                return new Vector2(viewportSize.x / worldHeight, viewportSize.y / worldHeight);
            }
        }
        throw new RuntimeException("Invalid camera mode: " + mode);
    }

}
