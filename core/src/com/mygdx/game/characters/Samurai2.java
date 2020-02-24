package com.mygdx.game.characters;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.game.framework.core.bodies.update.UpdateMethods;
import com.game.framework.core.particles.Particle;
import com.game.framework.core.renderer.WorldBodyAnimation;
import com.game.framework.core.renderer.WorldRenderer;
import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.builders.BodyBuilder;
import com.game.framework.core2.builders.BoxBodyBuilder;
import com.game.framework.core2.builders.CustomBodyBuilder;
import com.game.framework.core2.joints.Joint;
import com.game.framework.core2.joints.Weld;

import java.util.HashMap;
import java.util.Map;

public class Samurai2 extends WorldBody {

    private class SamuraiInputAdapter extends InputAdapter {
        private WorldRenderer renderer;
        public Map<String, WorldBody> toRemove;
        private boolean facingRight = true;

        public SamuraiInputAdapter(WorldRenderer renderer) {
            this.renderer = renderer;
            this.toRemove = new HashMap<>();
        }

        @Override
        public boolean touchUp (int screenX, int screenY, int pointer, int button) {
            // Destroy bodies at beginning of click.
            for (WorldBody body : toRemove.values())
                body.destroy();

            // Run attack animation.
            render.animations.runAnimation("attackFront", aNull -> {
                // Destroy bodies at end of attack animation.
                System.out.println("done");
                for (WorldBody body : toRemove.values())
                    body.destroy();
                return null;
            });
            return true;
        }

        @Override
        public boolean mouseMoved (int screenX, int screenY) {
            Vector2 worldPos = renderer.unproject(new Vector2(screenX, screenY));
//            System.out.println(worldPos + " : " + body.getWorldPos());

            if (worldPos.x < body.getWorldPos().x && facingRight) {
                body.flipX();
                facingRight = false;
            } else if (worldPos.x > body.getWorldPos().x && !facingRight) {
                body.flipX();
                facingRight = true;
            }

//            if (worldPos.y < getWorldPos().y) {
//                setDefaultAnimation("idle");
//            } else {
//                setDefaultAnimation("idleBack");
//            }
            return true;
        }
    }

    public Samurai2(WorldManager world, WorldRenderer renderer) {
        super(new CustomBodyBuilder(world, "samurai/idle1").scale(0.5f, 0.5f));

        SamuraiInputAdapter clickInput = new SamuraiInputAdapter(renderer);

        Joint attackBox = new Weld(new BoxBodyBuilder(world).isSensor(true).size(0.15f, 0.17f).density(0.0001f),
            new Vector2(0.18f, -0.09f),
            0);

        body.joints.attach(attackBox);
        controller.setUpdate(UpdateMethods.wasdMovement(3f));
        controller.setInputAdapter(clickInput);
        render.animations.addAnimation("idleFront", new WorldBodyAnimation(1/4f,
            "samurai/idle/samurai_idle_front_1_64.png",
            "samurai/idle/samurai_idle_front_2_64.png",
            "samurai/idle/samurai_idle_front_3_64.png",
            "samurai/idle/samurai_idle_front_4_64.png"));
        render.animations.addAnimation("attackFront", new WorldBodyAnimation(1/16f,
                "samurai/attack/samurai_attack_1_64.png",
                "samurai/attack/samurai_attack_2_64.png",
                "samurai/attack/samurai_attack_3_64.png",
                "samurai/attack/samurai_attack_4_64.png"));
        render.animations.setDefaultAnimation("idleFront");
        render.particles.attach(new Particle(this, "particle1.party", 0.005f));
        onDispose(body -> {
            body.body.joints.disposeAll();
            return null;
        });

        // Edit after Joint WorldBodies have been built.
        attackBox.getBody().controller.collisions.beginCollision(body -> {
            if (body.body.body.getType() == BodyDef.BodyType.DynamicBody &&
                !body.body.body.getFixtureList().first().isSensor())
            clickInput.toRemove.put(body.id(), body);
            return null;
        });
        attackBox.getBody().controller.collisions.endCollision(body -> {
            clickInput.toRemove.remove(body.id());
            return null;
        });
    }


}
