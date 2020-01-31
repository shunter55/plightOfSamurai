package com.game.framework.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.game.framework.utils.Utils;
import com.game.framework.world.WorldManager;

public class WorldRenderer {

    private Camera camera;

    private SpriteBatch spriteBatch;
    private Box2DDebugRenderer debugRenderer;

    public enum CameraMode {
        Stretch, Zoom
    }

    public WorldRenderer(float viewWidth, float viewHeight) {
//        this(new OrthographicCamera(
//                viewWidth * ((float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight()),
//                viewHeight * ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth())
//        ));

        this(new OrthographicCamera(viewWidth, viewHeight));
    }

    public WorldRenderer(CameraMode mode) {
        this(getCamera(mode));
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

    private void render(Renderable renderable) {
        // Do not render if there is no sprite.
        if (renderable.getTexture() == null)
            return;

        Sprite sprite = new Sprite(renderable.getTexture());

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
    }

    public void dispose() {
        debugRenderer.dispose();
    }

    private static Camera getCamera(CameraMode mode) {
        switch (mode) {
            case Stretch: return new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            //case Zoom: return new PerspectiveCamera(180f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        throw new RuntimeException("Invalid camera mode: " + mode);
    }

}
