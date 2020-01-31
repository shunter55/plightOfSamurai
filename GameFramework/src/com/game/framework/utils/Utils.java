package com.game.framework.utils;

import com.badlogic.gdx.math.Vector2;

public class Utils {

    /**
     * Convert PixelDimentions to WorldDimentions
     * @param pixWidth width in pixels.
     * @param pixHeight height in pixels.
     * @param worldWidth width in World Space.
     * @param worldHeight height in World Space.
     * @return The width and height in World Space, maintaining the pixel aspect ratio.
     */
    public static Vector2 toWorldRatio(int pixWidth, int pixHeight, float worldWidth, float worldHeight) {

        float rPixel = pixWidth / pixHeight;
        float rWorld = worldWidth / worldHeight;

        //if (rWorld > rPixel) {
        //    return new Vector2(pixWidth * (worldHeight / pixHeight), worldHeight);
        //} else {
            return new Vector2(worldWidth, pixHeight * (worldWidth / pixWidth));
        //}
    }

}
