package com.game.framework.core2.bodies.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.particles.Particle;
import com.game.framework.core.renderer.Renderable;
import com.game.framework.core.renderer.WorldBodyAnimation;

import java.util.List;

public class RenderManager implements Renderable {
    public AnimationManager animations;
    public ParticleManager particles;
    public TextManager text;

    private BodyManager body;

    public RenderManager(BodyManager body) {
        this.body = body;

        this.animations = new AnimationManager();
        this.particles = new ParticleManager();
        this.text = new TextManager();
    }

    public void update(float elapsedTime) {
        animations.update(elapsedTime);
        particles.update(elapsedTime);
    }

    @Override
    public Vector2 getWorldPos() {
        return body.getWorldPos();
    }

    @Override
    public Vector2 getDimensions() {
        return body.getWorldDimensions();
    }

    @Override
    public Vector2 getOrigin() {
        return body.getWorldOrigin();
    }

    @Override
    public float getRotationRadians() {
        return body.getRotationRadians();
    }

    @Override
    public WorldBodyAnimation getAnimation() {
        return animations.runner.getAnimation(body);
    }

    @Override
    public List<Particle> getParticles() {
        return particles.get();
    }

    @Override
    public Texture getFrame() {
        return animations.runner.getFrame();
    }

    @Override
    public TextManager getTextManager() { return text; }
}
