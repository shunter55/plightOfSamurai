package com.game.framework.core2.bodies.managers;

import com.game.framework.core.particles.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    private List<Particle> particles;

    public ParticleManager() {
        this.particles = new ArrayList<>();
    }

    public void attach(Particle particle) {
        this.particles.add(particle);
    }

    public List<Particle> get() {
        return particles;
    }

    public void update(float elapsedTime) {
        List<Particle> toRemove = new ArrayList<>();

        for (Particle particle : particles) {
            particle.update(elapsedTime);
            if (particle.isComplete()) {
                toRemove.add(particle);
            }
        }

        particles.removeAll(toRemove);
    }

}
