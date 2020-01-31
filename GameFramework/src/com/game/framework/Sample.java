package com.game.framework;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.bodies.WorldBody;
import com.game.framework.bodies.update.InputAdapterMethods;
import com.game.framework.bodies.update.UpdateMethods;
import com.game.framework.renderer.WorldRenderer;
import com.game.framework.world.WorldManager;

/**
 * Does nothing.
 * Just sample code.
 */
public class Sample extends ApplicationAdapter {
    WorldManager worldManager;
    WorldRenderer worldRenderer;

    @Override
    public void create () {
        // WorldManager manages all objects.
        worldManager = new WorldManager();

        // Renders Box2d WireMeshes and Textures (imgPath).
        worldRenderer = new WorldRenderer(WorldRenderer.CameraMode.Zoom, 5, 5);

        // Create a BoxObject.
        WorldBody box = worldManager.createBox(BodyDef.BodyType.DynamicBody, -2.4f, -2.4f, 0.1f, 0.1f, 1);
        // Add input tracking to the box.
        box.setUpdate(UpdateMethods.wasdMovement(3f));
        //box.setInputAdapter(InputAdapterMethods.wasdInputAdapter(3f));

        // Create a CustomObject using Box2dEditor. (samurai/idle1
        worldManager.createCustom(
                BodyDef.BodyType.DynamicBody,
                "samurai/idle/samurai_idle_front_1_64.png",
                "samurai/idle1",
                0f,
                0f,
                1f,
                1);

        // Create Walls (Static Boxes)
        worldManager.createBox(BodyDef.BodyType.StaticBody, -2.5f, 0f, 0.01f, 5f, 1);
        worldManager.createBox(BodyDef.BodyType.StaticBody, 2.5f, 0f, 0.01f, 5f, 1);
        worldManager.createBox(BodyDef.BodyType.StaticBody, 0f, -2.5f, 5f, 0.01f, 1);
        worldManager.createBox(BodyDef.BodyType.StaticBody, 0f, 2.5f, 5f, 0.01f, 1);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            worldManager.createBox(BodyDef.BodyType.DynamicBody, 0, 0, 0.05f, 0.05f, 1);
        }

        worldManager.updatePhysics(1 / 60f);

        worldRenderer.render(worldManager);
    }

    @Override
    public void dispose () {
        worldManager.dispose();
        worldRenderer.dispose();
    }
}
