package com.mygdx.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.character.Character;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.renderer.WorldBodyAnimation;
import com.game.framework.core.world.WorldManager;

import java.util.HashSet;
import java.util.Set;

public class SamuraiCharacter extends Character {

    private Set<String> toRemove = new HashSet<>();

    private boolean facingRight = true;

    public SamuraiCharacter(WorldManager world, Vector2 pos, Vector2 scale) {
        super(world, "samurai/idle1", BodyDef.BodyType.DynamicBody, pos, scale, 1f);

        addAnimations();

        attachBoxSensor(
                new Vector2(0.18f, -0.09f),
                new Vector2(0.15f, 0.17f),
                worldBody -> {
                    if (worldBody.getBody().getType() == BodyDef.BodyType.DynamicBody &&
                            !worldBody.getBody().getFixtureList().first().isSensor())
                        toRemove.add(worldBody.getId());
                    return null;
                },
                worldBody -> {
                    toRemove.remove(worldBody.getId());
                    return null;
                });

        addInputAdapter(world);
    }

    private void addAnimations() {

        WorldBodyAnimation idle = new WorldBodyAnimation(1f / 4f,
                "samurai/idle/samurai_idle_front_1_64.png",
                "samurai/idle/samurai_idle_front_2_64.png",
                "samurai/idle/samurai_idle_front_3_64.png",
                "samurai/idle/samurai_idle_front_4_64.png");
        addAnimation("idle", idle);

        addAnimation("attack",
            new WorldBodyAnimation(1f / 16f,
                "samurai/attack/samurai_attack_1_64.png",
                "samurai/attack/samurai_attack_2_64.png",
                "samurai/attack/samurai_attack_3_64.png",
                "samurai/attack/samurai_attack_4_64.png"));

        addAnimation("idle_left", new WorldBodyAnimation(idle).flipHorizontal());

        setDefaultAnimation("idle");
    }

    private void addInputAdapter(WorldManager world) {
        setInputAdapter((WorldBody worldBody) ->
            new InputAdapter() {
                @Override
                public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                    for (String id : toRemove)
                        world.remove(id);
                    runAnimation("attack", aNull -> {
                        System.out.println("done");
                        for (String id : toRemove)
                            world.remove(id);
                        return null;
                    });
                    return true;
                }

                @Override
                public boolean mouseMoved(int screenX, int screenY) {
                    if (screenX < Gdx.graphics.getWidth() / 2 && facingRight) {
                        flipX();
                        facingRight = !facingRight;
                    }
                    else if (screenX > Gdx.graphics.getWidth() / 2 && !facingRight) {
                        flipX();
                        facingRight = !facingRight;
                    }
                    return true;
                }
            });
    }

}
