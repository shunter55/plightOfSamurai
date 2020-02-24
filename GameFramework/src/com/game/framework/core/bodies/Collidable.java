package com.game.framework.core.bodies;

import com.game.framework.core2.bodies.WorldBody;

/**
 * Anything that can be collided with.
 */
public interface Collidable {

    /**
     * Contact started with object.
     * @param other The other object that was collided with.
     */
    public void beginContact(WorldBody other);

    /**
     * Contact ended with object.
     * @param other The other object that was collided with.
     */
    public void endContact(WorldBody other);

}
