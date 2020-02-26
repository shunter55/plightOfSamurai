//package com.mygdx.game.characters;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.InputAdapter;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.game.framework.character.Character;
//import com.game.framework.core.bodies.Function;
//import com.game.framework.core.bodies.WorldBody;
//import com.game.framework.core.particles.Particle;
//import com.game.framework.core.renderer.WorldBodyAnimation;
//import com.game.framework.core.renderer.WorldRenderer;
//import com.game.framework.core.utils.Utils;
//import com.game.framework.core.world.WorldManager;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//public class SamuraiCharacter extends Character {
//
//    private Map<String, WorldBody> toRemove = new HashMap<>();
//
//    private boolean facingRight = true;
//
//    public SamuraiCharacter(WorldManager world, Vector2 pos, Vector2 scale) {
//        super(world, "samurai/idle1", BodyDef.BodyType.DynamicBody, pos, scale, 1f);
//
//        addAnimations();
//
//        attachBoxSensor(
//            new Vector2(0.18f, -0.09f),
//            new Vector2(0.15f, 0.17f),
//            worldBody -> {
//                if (worldBody.getBody().getType() == BodyDef.BodyType.DynamicBody &&
//                        !worldBody.getBody().getFixtureList().first().isSensor())
//                    toRemove.put(worldBody.getId(), worldBody);
//                return null;
//            },
//            worldBody -> {
//                toRemove.remove(worldBody.getId());
//                return null;
//            });a
//
//        //addInputAdapter(world, new Vector2(5, 5));
//
//        onDispose(aVoid -> {
//            disposeJoints();
//            return null;
//        });
//    }
//
//    private void addAnimations() {
//        WorldBodyAnimation idle = new WorldBodyAnimation(1f / 4f,
//                "samurai/idle/samurai_idle_front_1_64.png",
//                "samurai/idle/samurai_idle_front_2_64.png",
//                "samurai/idle/samurai_idle_front_3_64.png",
//                "samurai/idle/samurai_idle_front_4_64.png");
//        addAnimation("idle", idle);
//
//        addAnimation("idleBack",
//            new WorldBodyAnimation(1f / 4f,
//                "samurai/idle_back/samurai_idle_back_1_64.png",
//                "samurai/idle_back/samurai_idle_back_2_64.png",
//                "samurai/idle_back/samurai_idle_back_3_64.png",
//                "samurai/idle_back/samurai_idle_back_4_64.png"));
//
//        addAnimation("attack",
//            new WorldBodyAnimation(1f / 16f,
//                "samurai/attack/samurai_attack_1_64.png",
//                "samurai/attack/samurai_attack_2_64.png",
//                "samurai/attack/samurai_attack_3_64.png",
//                "samurai/attack/samurai_attack_4_64.png"));
//        addAnimation("attackBack",
//            new WorldBodyAnimation(1f / 16f,
//                "samurai/attack_back/samurai_attack_1.png",
//                "samurai/attack_back/samurai_attack_2.png",
//                "samurai/attack_back/samurai_attack_3.png",
//                "samurai/attack_back/samurai_attack_4.png"));
//
//        addAnimation("idle_left", new WorldBodyAnimation(idle).flipHorizontal());
//
//        setDefaultAnimation("idle");
//
//        Particle particle = new Particle(this, "particle1.party", 0.005f);
//        Particle blood = new Particle(this, "blood.p", 0.005f);
//
//        attachParticle(particle);
//        attachParticle(blood);
//    }
//
//    public SamuraiCharacter addInputAdapter(WorldRenderer renderer) {
//        setInputAdapter((WorldBody worldBody) ->
//            new InputAdapter() {
//                @Override
//                public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//                    // Destroy bodies at beginning of click.
//                    for (WorldBody body : toRemove.values())
//                        body.destroy();
//
//                    // Run attack animation.
//                    runAnimation("attack", aNull -> {
//                        // Destroy bodies at end of attack animation.
//                        System.out.println("done");
//                        for (WorldBody body : toRemove.values())
//                            body.destroy();
//                        return null;
//                    });
//                    return true;
//                }
//
//                @Override
//                public boolean mouseMoved(int screenX, int screenY) {
//                    Vector2 worldPos = renderer.unproject(new Vector2(screenX, screenY));
//                    System.out.println(worldPos + " : " + getWorldPos());
//
//                    if (worldPos.x < getWorldPos().x && facingRight) {
//                        flipX();
//                        facingRight = false;
//                    } else if (worldPos.x > getWorldPos().x && !facingRight) {
//                        flipX();
//                        facingRight = true;
//                    }
//
//                    if (worldPos.y < getWorldPos().y) {
//                        setDefaultAnimation("idle");
//                    } else {
//                        setDefaultAnimation("idleBack");
//                    }
//
//
//
////                    if (screenX < Gdx.graphics.getWidth() / 2 && facingRight) {
////                        flipX();
////                        facingRight = !facingRight;
////                    }
////                    else if (screenX > Gdx.graphics.getWidth() / 2 && !facingRight) {
////                        flipX();
////                        facingRight = !facingRight;
////                    }
//                    return true;
//                }
//            });
//        return this;
//    }
//
//}
