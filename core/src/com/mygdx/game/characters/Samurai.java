package com.mygdx.game.characters;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.game.framework.core.bodies.CustomWorldBody;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.bodies.builders.BoxBuilder;
import com.game.framework.core.bodies.joints.Weld;
import com.game.framework.core.renderer.WorldBodyAnimation;
import com.game.framework.core.world.WorldManager;

import java.util.HashSet;
import java.util.Set;

public class Samurai extends CustomWorldBody {

    private WorldBodyAnimation samuraiIdle, samuraiAttack;

    private Set<String> toRemove = new HashSet<>();

    public Samurai(WorldManager worldManager) {
        super(worldManager, worldManager.generateId(), "samurai/idle1", BodyDef.BodyType.DynamicBody, 0f, 0f, new Vector2(0.5f, 0.5f), 1f);
        worldManager.addBody(this);

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

        this.attachJoint(new Weld(
            new BoxBuilder(worldManager).isSensor(true).pos(getWorldPos()).width(0.15f).height(0.15f).density(0.0000001f)
                .beginCollision(worldBody -> {
                    if (worldBody.getBody().getType() == BodyDef.BodyType.DynamicBody &&
                            !worldBody.getBody().getFixtureList().first().isSensor())
                        toRemove.add(worldBody.getId());
                    return null;
                })
                .endCollision(worldBody -> {
                    toRemove.remove(worldBody.getId());
                    return null;
                }),
            new Vector2(0.18f, -0.09f),
            0f));

        this.runAnimation(samuraiIdle);

        this.beginCollision((WorldBody worldBody) -> {
//				if (worldBody.getBody().getType() == BodyDef.BodyType.DynamicBody)
//					worldManager.remove(worldBody);
            return null;
        });

        this.setInputAdapter((WorldBody worldBody) ->
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
    }

}
