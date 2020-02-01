package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.bodies.Function;
import com.game.framework.bodies.WorldBody;
import com.game.framework.bodies.update.InputAdapterMethods;
import com.game.framework.bodies.update.UpdateMethods;
import com.game.framework.renderer.WorldBodyAnimation;
import com.game.framework.renderer.WorldRenderer;
import com.game.framework.world.WorldManager;

import java.util.ArrayList;
import java.util.Vector;

public class MyGdxGame extends ApplicationAdapter {
	WorldManager worldManager;
	WorldRenderer worldRenderer;

	Vector2 worldSize = new Vector2(5, 5);

	WorldBody samurai;

	WorldBodyAnimation samuraiIdle;
	WorldBodyAnimation samuraiAttack;


	@Override
	public void create () {
		worldManager = new WorldManager();

		worldRenderer = new WorldRenderer(WorldRenderer.CameraMode.Zoom, worldSize.x, worldSize.y);

		WorldBody box = worldManager.createBox(BodyDef.BodyType.DynamicBody, -2.4f, -2.4f, 0.1f, 0.1f, 1);
		//box.setUpdate(UpdateMethods.wasdMovement(3f));
		//box.setInputAdapter(InputAdapterMethods.wasdInputAdapter(3f));
		box.runAnimation(new WorldBodyAnimation("samurai/idle/samurai_idle_front_1_64.png"));

		samurai = buildSamurai();

//		samurai.setFrames(1f / 4f,
//			"samurai/idle/samurai_idle_front_1_64.png",
//			"samurai/idle/samurai_idle_front_2_64.png",
//			"samurai/idle/samurai_idle_front_3_64.png",
//			"samurai/idle/samurai_idle_front_4_64.png"
//		);

		//samurai.setImage("samurai/idle/samurai_idle_front_1_64.png");
		samurai.setUpdate(UpdateMethods.wasdMovement(3f));

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

		worldManager.updatePhysics(1f / 60f);

		worldRenderer.render(worldManager);
		//worldRenderer.renderAnimated(samurai);
	}

	@Override
	public void dispose () {
		worldManager.dispose();
		worldRenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		if (worldRenderer != null)
			worldRenderer.dispose();
		worldRenderer = new WorldRenderer(WorldRenderer.CameraMode.Zoom, worldSize.x, worldSize.y);
	}

	private WorldBody buildSamurai() {
		WorldBody samurai = worldManager.createCustom(
				BodyDef.BodyType.DynamicBody,
				"samurai/idle1", 0f, 0f, 0.51f, 1);

		samuraiIdle = new WorldBodyAnimation(1f / 4f,
				"samurai/idle/samurai_idle_front_1_64.png",
				"samurai/idle/samurai_idle_front_2_64.png",
				"samurai/idle/samurai_idle_front_3_64.png",
				"samurai/idle/samurai_idle_front_4_64.png");
		samuraiAttack = new WorldBodyAnimation(1f / 16f,
				"samurai/attack/samurai_attack_1_64.png",
				"samurai/attack/samurai_attack_2_64.png",
				"samurai/attack/samurai_attack_3_64.png",
				"samurai/attack/samurai_attack_4_64.png");

		samurai.runAnimation(samuraiIdle);

		samurai.beginCollision(new Function<WorldBody, Void>() {
			@Override
			public Void call(WorldBody worldBody) {
				if (worldBody.getBody().getType() == BodyDef.BodyType.DynamicBody)
					worldManager.remove(worldBody);
				return null;
			}
		});

		samurai.setInputAdapter(new Function<WorldBody, InputAdapter>() {
			@Override
			public InputAdapter call(final WorldBody worldBody) {
				return new InputAdapter() {
					@Override
					public boolean touchUp(int screenX, int screenY, int pointer, int button) {
						worldBody.runAnimation(samuraiAttack, new Function<Void, Void>() {
							@Override
							public Void call(Void aVoid) {
								worldBody.runAnimation(samuraiIdle);
								return null;
							}
						});
						return true;
					}
				};
			}
		});

		return samurai;
	}
}
