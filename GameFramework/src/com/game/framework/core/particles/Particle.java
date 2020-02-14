package com.game.framework.core.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.game.framework.core.bodies.WorldBody;

public class Particle {

    private WorldBody body;
    private ParticleEffect particleEffect;
    private Vector2 offset;
    private float scale;

    public Particle(WorldBody body, String filePath, float scale) {
        this.body = body;
        this.scale = scale;
        this.offset = new Vector2(0, 0);
        particleEffect = new ParticleEffect();

        particleEffect.load(Gdx.files.internal(filePath), Gdx.files.internal(""));
        particleEffect.scaleEffect(scale);
        update(0f);

        particleEffect.start();
    }

    public void update(float deltaTime) {
        particleEffect.setPosition(body.getWorldPos().x + offset.x, body.getWorldPos().y + offset.y);
        particleEffect.update(deltaTime);
    }

    public boolean isComplete() {
        return particleEffect.isComplete();
    }

    public void reset() {
        particleEffect.reset();
        particleEffect.scaleEffect(scale);
    }

    public ParticleEffect getEffect() {
        return particleEffect;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

}
