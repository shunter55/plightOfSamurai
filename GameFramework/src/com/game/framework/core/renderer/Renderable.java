package com.game.framework.core.renderer;

import com.badlogic.gdx.math.Vector2;
import com.game.framework.core.particles.Particle;

import java.util.List;

/**
 * Anything that has a Image that can be rendered.
 */
public interface Renderable extends Animated {

    /**
     * The World coordinates that the Image should be drawn.
     * @return World Coordinates.
     */
    public Vector2 getWorldPos();

    /**
     * Width and Height of the Object.
     * @return Width and Height of the Object.
     */
    public Vector2 getDimensions();

    /**
     * The Origin or center of the object in WorldCoordinates.
     * @return The Origin in World Coordinates.
     */
    public Vector2 getOrigin();

    /**
     * The current rotationAngle in radians.
     * @return Current rotationAngle in radians.
     */
    public float getRotationRadians();

    /**
     * The Animation that should be run if any.
     * @return The WorldBodyAnimation that should run, or null.
     */
    public WorldBodyAnimation getAnimation();

    /**
     * Any ParticleEffects that should be rendered.
     * @return The List of Particle effects that should be rendered.
     */
    public List<Particle> getParticles();

}
