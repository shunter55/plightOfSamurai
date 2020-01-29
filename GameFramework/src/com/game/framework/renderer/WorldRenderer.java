package com.game.framework.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.game.framework.world.WorldManager;

public class WorldRenderer {

    private Camera camera;
    private Box2DDebugRenderer debugRenderer;

    public enum CameraMode {
        Stretch, Zoom
    }

    public WorldRenderer(float viewWidth, float viewHeight) {
        this(new OrthographicCamera(
                viewWidth * ((float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight()),
                viewHeight * ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth())
        ));

        System.out.println((float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight());
    }

    public WorldRenderer(CameraMode mode) {
        this(getCamera(mode));
    }

    public WorldRenderer(Camera camera) {
        this.camera = camera;
        debugRenderer = new Box2DDebugRenderer();
    }

    public void render(WorldManager world) {
        render(world, camera);
    }

    public void render(WorldManager world, Camera camera) {
        debugRenderer.render(world.getWorld(), camera.combined);
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
