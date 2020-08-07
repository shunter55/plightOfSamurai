package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.core.bodies.builders.BoxBuilder;
import com.game.framework.core.bodies.update.UpdateMethods;
import com.game.framework.core.renderer.WorldBodyAnimation;
import com.game.framework.core.renderer.WorldRenderer;
import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.builders.BoxBodyBuilder;
import com.game.framework.core2.builders.CircleBodyBuilder;
import com.mygdx.game.GameMain;
import com.mygdx.game.shapes.Ball;

public class PhysicsBalls implements Screen {

    private static Vector2 worldSize = new Vector2(5, 10);
    private static float velocity = 0;

    private WorldManager worldManager;
    private WorldRenderer worldRenderer;

    public PhysicsBalls(GameMain game) {
        start();
    }

    public void start() {
        if (worldManager != null)
            worldManager.dispose();
        if (worldRenderer != null)
            worldRenderer.dispose();
        worldManager = new WorldManager();
        worldRenderer = new WorldRenderer(WorldRenderer.CameraMode.Zoom, worldSize.x, worldSize.y);

        new WorldBody(
           new BoxBodyBuilder(worldManager)
               .type(BodyDef.BodyType.StaticBody)
               .pos(-3.0f, 0f)
               .size(0.01f, 5f)
        );

        new WorldBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(3.0f, 0f)
                .size(0.01f, 5f)
        );

        new WorldBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(-1.6f, 3.5f)
                .size(1.5f, 0.01f)
                .rotateRad(-(float)Math.PI/8)
        );

        new WorldBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(1.6f, 3.5f)
                .size(1.5f, 0.01f)
                .rotateRad((float)Math.PI/8)
        );

        new WorldBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(-1.7f, 3f)
                .size(0.01f, 1.3f)
                .rotateRad((float)Math.PI/2)
        );

        new WorldBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(1.7f, 3f)
                .size(0.01f, 1.3f)
                .rotateRad((float)Math.PI/2)
        );

        WorldBody stopper = new WorldBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(0f, 2.7f)
                .size(0.2f, 0.2f)
                .isSensor(true)
        );

        WorldBody ball = new Ball(
            new CircleBodyBuilder(worldManager)
        );

        ball.body.body.setLinearVelocity(0f, velocity);

        stopper.controller.collisions.beginCollision(body -> {
            return null;
        });

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the world.
        worldManager.updatePhysics(1f / 60f);

        // Render the world.
        worldRenderer.render(worldManager);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        worldManager.dispose();
        worldRenderer.dispose();
    }
}
