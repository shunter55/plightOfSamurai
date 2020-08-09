package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.particles.Particle;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core.bodies.builders.BoxBuilder;
import com.game.framework.core.bodies.update.UpdateMethods;
import com.game.framework.core.controller.InputProcessor;
import com.game.framework.core.renderer.WorldBodyAnimation;
import com.game.framework.core.renderer.WorldRenderer;
import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.builders.BoxBodyBuilder;
import com.game.framework.core2.builders.CustomBodyBuilder;
import com.game.framework.core2.joints.Weld;
import com.mygdx.game.GameMain;
import com.mygdx.game.characters.BladeMaster;
import com.mygdx.game.characters.Samurai2;

public class GameScreen implements Screen {

    private static Vector2 worldSize = new Vector2(5, 5);

    private GameMain game;

    private WorldManager worldManager;
    private WorldRenderer worldRenderer;

//    SamuraiCharacter samurai;
    Samurai2 samurai;

    public GameScreen(GameMain game) {
        this.game = game;

        start();
    }

    public void start() {
        if (worldManager != null)
            worldManager.dispose();
        if (worldRenderer != null)
            worldRenderer.dispose();
        worldManager = new WorldManager();
        worldRenderer = new WorldRenderer(WorldRenderer.CameraMode.Zoom, worldSize.x, worldSize.y);

//        // Box
//        WorldBody box = new BoxBuilder(worldManager).pos(-2.4f, -2.4f).size(0.1f, 0.1f).scaleX(-1f).build();
//        box.runAnimation(new WorldBodyAnimation("samurai/idle/samurai_idle_front_1_64.png"));
//
//        // Character
//        samurai = new SamuraiCharacter(worldManager, new Vector2(0, 0), new Vector2(0.5f, 0.5f));
//        samurai.setUpdate(UpdateMethods.wasdMovement(3f));
//        samurai.addInputAdapter(worldRenderer);

//        samurai = new WorldBody(new CustomBodyBuilder(worldManager, "samurai/idle1").scale(0.5f, 0.5f));
//        samurai.body.joints.attach(
//            new Weld(new WorldBody(new BoxBodyBuilder(worldManager).isSensor(true).density(0.0001f)),
//            new Vector2(0, 0),
//            0));
//        samurai.controller.setUpdate(UpdateMethods.wasdMovement(3f));
//        samurai.render.animations.addAnimation("idleFront", new WorldBodyAnimation(1/4f,
//            "samurai/idle/samurai_idle_front_1_64.png",
//                "samurai/idle/samurai_idle_front_2_64.png",
//                "samurai/idle/samurai_idle_front_3_64.png",
//                "samurai/idle/samurai_idle_front_4_64.png"));
//        samurai.render.animations.setDefaultAnimation("idleFront");
//        samurai.render.particles.attach(new Particle(samurai, "particle1.party", 0.005f));

        samurai = new Samurai2(worldManager, worldRenderer);



        // Walls
        worldManager.addBody(new BoxBodyBuilder(worldManager).type(BodyDef.BodyType.StaticBody).pos(-2.5f, 0f).size(0.01f, 5f));
        worldManager.addBody(new BoxBodyBuilder(worldManager).type(BodyDef.BodyType.StaticBody).pos(2.5f, 0f).size(0.01f, 5f));
        worldManager.addBody(new BoxBodyBuilder(worldManager).type(BodyDef.BodyType.StaticBody).pos(0f, 2.5f).size(5f, 0.01f));
        worldManager.addBody(new BoxBodyBuilder(worldManager).type(BodyDef.BodyType.StaticBody).pos(0f, -2.5f).size(5f, 0.01f));
//        new BoxBuilder(worldManager).type(BodyDef.BodyType.StaticBody).pos(-2.5f, 0f).size(0.01f, 5f).build();
//        new BoxBuilder(worldManager).type(BodyDef.BodyType.StaticBody).pos(2.5f, 0f).size(0.01f, 5f).build();
//        new BoxBuilder(worldManager).type(BodyDef.BodyType.StaticBody).pos(0f, -2.5f).size(5f, 0.1f).build();
//        new BoxBuilder(worldManager).type(BodyDef.BodyType.StaticBody).pos(0f, 2.5f).size(5f, 0.1f).build();
    }

    @Override
    public void show() {
        InputProcessor.getInputProcessor();
    }

    @Override
    public void render(float delta) {
        // Background
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Spawn
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            //new Samurai2(worldManager, worldRenderer).controller.setUpdate(null);
            //new WorldBody(new BoxBodyBuilder(worldManager).size(0.1f, 0.1f).density(0.5f));
            new BladeMaster(worldManager, worldRenderer);
        }

        // Rotation
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            samurai.body.body.setAngularVelocity(-3f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            samurai.body.body.setAngularVelocity(3f);
        }

        // Camera Movement
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            worldRenderer.moveBy(new Vector2(1f / 20f, 0f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            worldRenderer.moveBy(new Vector2(-1f / 20f, 0f));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.mainMenu();
        }

        // Update the world.
        worldManager.updatePhysics(1f / 60f);

        // Render the world.
        worldRenderer.render(worldManager);
    }

    @Override
    public void resize(int width, int height) {
        if (worldRenderer != null)
            worldRenderer.dispose();

        //worldRenderer.resetCamera(WorldRenderer.CameraMode.Zoom, worldSize.x, worldSize.y);
        worldRenderer = new WorldRenderer(WorldRenderer.CameraMode.Zoom, worldSize.x, worldSize.y);
        samurai.resize(worldRenderer);
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
