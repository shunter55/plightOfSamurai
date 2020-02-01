package com.game.framework.bodies;

/**
 * Anything that can be collided with.
 */
public interface Collidable {

    /**
     * Contact started with object.
     * @param other The other object that was collided with.
     */
    public void beginContact(Collidable other);

    /**
     * Contact ended with object.
     * @param other The other object that was collided with.
     */
    public void endContact(Collidable other);

}
