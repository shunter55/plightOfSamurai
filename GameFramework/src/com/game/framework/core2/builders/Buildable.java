package com.game.framework.core2.builders;

/**
 * Builds an object that can be added to World.
 */
public interface Buildable<T extends BodyBuilder> {

    /**
     * @return The BodyBuilder that defines how the Body will be built.
     */
    public T getBodyBuilder();

}
