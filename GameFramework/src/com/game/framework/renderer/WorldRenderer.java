package com.game.framework.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.game.framework.utils.Utils;
import com.game.framework.world.WorldManager;

public class WorldRenderer {

    private Camera camera;

    private SpriteBatch spriteBatch;
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
        this(getCamera(mode, viewWidth, viewHeight));
    }

    public WorldRenderer(Camera camera) {
        this.camera = camera;

        spriteBatch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
    }

    public void render(WorldManager world) {
        render(world, camera);
    }

    public void render(WorldManager world, Camera camera) {
        // Render Box2d Body frames.
        debugRenderer.render(world.getWorld(), camera.combined);

        // Render WorldBody Images.
        for (Renderable renderable : world.getBodies()) {
            render(renderable);
        }
    }

    private float elapsedTime = 0;
    private void render(Renderable renderable) {

        WorldBodyAnimation animation = renderable.getAnimation();


        // Get Textures to render.
//        Animation<Texture> animation =
//            renderable.getFrameTime() != -1 && renderable.getFrames() != null && !renderable.getFrames().isEmpty() ?
//                new Animation<>(renderable.getFrameTime(), renderable.getFrames()) :
//                null;

        // Do not render if there is no sprite.
        if (animation == null)
            return;

        //Sprite sprite = new Sprite(renderable.getTexture());
        //Sprite sprite = new Sprite(animation.getKeyFrame(elapsedTime));
        Sprite sprite = new Sprite(renderable.getFrame());

        // Image width and height in terms of World Coordinates.
        Vector2 worldRatio = Utils.toWorldRatio(
                (int) sprite.getWidth(),
                (int) sprite.getHeight(),
                renderable.getDimensions().x,
                renderable.getDimensions().y);

        // Set Origin.
        sprite.setOrigin(
                renderable.getOrigin().x,
                renderable.getOrigin().y);

        // Set Rotation.
        sprite.rotate((float) Math.toDegrees(renderable.getRotationRadians()));

        // Set Position and Size
        sprite.setBounds(
                renderable.getWorldPos().x - renderable.getOrigin().x,
                renderable.getWorldPos().y - renderable.getOrigin().y,
                worldRatio.x,
                worldRatio.y);

        // Draw the Image.
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();

        //animation.incrementTime(1f / 60f);
//        elapsedTime += 1f / 60f;
//        elapsedTime %= renderable.getFrameTime() * renderable.getFrames().size;
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

}
