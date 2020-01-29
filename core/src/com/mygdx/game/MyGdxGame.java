package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.bodies.WorldBody;
import com.game.framework.bodies.update.UpdateMethods;
import com.game.framework.renderer.WorldRenderer;
import com.game.framework.world.WorldManager;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	WorldManager worldManager;
	WorldRenderer worldRenderer;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		worldManager = new WorldManager();

		WorldBody box = worldManager.createBox(BodyDef.BodyType.DynamicBody, 0, 0, 0.1f, 0.1f, 1);
		box.setUpdate(UpdateMethods.wasdMovement(1.5f));

		worldRenderer = new WorldRenderer(5, 5);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();

		worldManager.updatePhysics(1 / 60f);

		worldRenderer.render(worldManager);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();

		worldManager.dispose();
		worldRenderer.dispose();
	}
}
