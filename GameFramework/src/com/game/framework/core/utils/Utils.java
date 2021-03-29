package com.game.framework.core.utils;

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
        pixWidth = Math.abs(pixWidth);
        pixHeight = Math.abs(pixHeight);
        worldWidth = Math.abs(worldWidth);
        worldHeight = Math.abs(worldHeight);

        float rPixel = ((float) pixWidth) / ((float) pixHeight);
        float rWorld = worldWidth / worldHeight;

        if (rWorld <= rPixel) {
            return new Vector2(((float) pixWidth) * (worldHeight / ((float) pixHeight)), worldHeight);
        } else {
            return new Vector2(worldWidth, ((float) pixHeight) * (worldWidth / ((float) pixWidth)));
        }
    }

    /**
     * Convert WorldDimentions to PixelDimentions
     * @param worldWidth width in World Space.
     * @param worldHeight height in World Space
     * @param pixWidth width in pixels.
     * @param pixHeight height in pixels.
     * @return The width and height in Pixel Space, maintaining the world aspect ratio.
     */
    public static Vector2 toPixelRatio(int worldWidth, int worldHeight, int pixWidth, int pixHeight) {
        worldWidth = Math.abs(worldWidth);
        worldHeight = Math.abs(worldHeight);
        pixWidth = Math.abs(pixWidth);
        pixHeight = Math.abs(pixHeight);

        float rWorld = worldWidth / worldHeight;
        float rPixel = ((float) pixWidth) / ((float) pixHeight);

        if (rWorld <= rPixel) {
            return new Vector2(((float) pixHeight) * (worldWidth / worldHeight), (float) pixHeight);
        } else {
            return new Vector2((float) pixWidth, ((float) pixWidth) * (worldHeight / worldWidth));
        }
    }

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    public static float random(float min, float max) {
        return (float) (Math.random() * (max - min)) + min;
    }
}
