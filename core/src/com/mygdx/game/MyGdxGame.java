package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.character.Character;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.bodies.joints.Weld;
import com.game.framework.core.bodies.update.UpdateMethods;
import com.game.framework.core.renderer.WorldBodyAnimation;
import com.game.framework.core.renderer.WorldRenderer;
import com.game.framework.core.world.WorldManager;
import com.mygdx.game.characters.Samurai;
import com.mygdx.game.characters.SamuraiCharacter;

import java.util.HashSet;
import java.util.Set;

public class MyGdxGame extends ApplicationAdapter {
	WorldManager worldManager;
	WorldRenderer worldRenderer;

	Vector2 worldSize = new Vector2(5, 5);

	WorldBody samurai;

	WorldBodyAnimation samuraiIdle;
	WorldBodyAnimation samuraiAttack;

	boolean isAttack = false;
	Set<String> toRemove = new HashSet<>();

	@Override
	public void create () {
		worldManager = new WorldManager();

		worldRenderer = new WorldRenderer(WorldRenderer.CameraMode.Stretch, worldSize.x, worldSize.y);

		WorldBody box = worldManager.createBox(BodyDef.BodyType.DynamicBody, false, -2.4f, -2.4f, 0.1f, 0.1f, 1);
		//box.setUpdate(UpdateMethods.wasdMovement(3f));
		//box.setInputAdapter(InputAdapterMethods.wasdInputAdapter(3f));
		box.runAnimation(new WorldBodyAnimation("samurai/idle/samurai_idle_front_1_64.png"));

		//samurai = new Samurai(worldManager);
		samurai = new SamuraiCharacter(worldManager, new Vector2(0, 0), new Vector2(0.5f, 0.5f));

		samurai.setUpdate(UpdateMethods.wasdMovement(3f));

		worldManager.createBox(BodyDef.BodyType.StaticBody, false, -2.5f, 0f, 0.01f, 5f, 1);
		worldManager.createBox(BodyDef.BodyType.StaticBody, false, 2.5f, 0f, 0.01f, 5f, 1);
		worldManager.createBox(BodyDef.BodyType.StaticBody, false, 0f, -2.5f, 5f, 0.01f, 1);
		worldManager.createBox(BodyDef.BodyType.StaticBody, false, 0f, 2.5f, 5f, 0.01f, 1);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			worldManager.createBox(BodyDef.BodyType.DynamicBody, false, 0, 0, 0.05f, 0.05f, 1);
			new Samurai(worldManager);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			samurai.getBody().setAngularVelocity(-3f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			samurai.getBody().setAngularVelocity(3f);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			worldRenderer.moveBy(new Vector2(1f / 20f, 0f));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			worldRenderer.moveBy(new Vector2(-1f / 20f, 0f));
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
		final WorldBody samurai = worldManager.createCustom(
				BodyDef.BodyType.DynamicBody,
				"samurai/idle1", 0f, 0f, new Vector2(0.5f, 0.5f), 1);

		samurai.setWorldPos(new Vector2(1.1f, 1.5f));

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

		samurai.attachJoint(new Weld(
			new Function<Void, WorldBody>() {
				@Override
				public WorldBody call(Void aVoid) {
					WorldBody body = worldManager.createBox(
						BodyDef.BodyType.DynamicBody,
						true,
						samurai.getWorldPos().x, samurai.getWorldPos().y,
						0.15f, 0.17f,
						0.000001f);

					body.beginCollision(new Function<WorldBody, Void>() {
						@Override
						public Void call(WorldBody worldBody) {
							if (worldBody.getBody().getType() == BodyDef.BodyType.DynamicBody)
								toRemove.add(worldBody.getId());
							return null;
						}
					});

					body.endCollision(new Function<WorldBody, Void>() {
						@Override
						public Void call(WorldBody worldBody) {
							toRemove.remove(worldBody.getId());
							return null;
						}
					});

					return body;
				}
			},
			new Vector2(0.18f, -0.09f),
			0f));

		samurai.runAnimation(samuraiIdle);

		samurai.beginCollision((WorldBody worldBody) -> {
//				if (worldBody.getBody().getType() == BodyDef.BodyType.DynamicBody)
//					worldManager.remove(worldBody);
			return null;
		});

		samurai.setInputAdapter((WorldBody worldBody) ->
			new InputAdapter() {
				@Override
				public boolean touchUp(int screenX, int screenY, int pointer, int button) {
					for (String id : toRemove)
						worldManager.remove(id);
					worldBody.runAnimation(samuraiAttack, aVoid -> {
						for (String id : toRemove)
							worldManager.remove(id);
						worldBody.runAnimation(samuraiIdle);
						return null;
					});
					return true;
				}
			});

		return samurai;
	}
}
