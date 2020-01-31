package com.game.framework.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.game.framework.bodies.WorldBody;

public class WorldBodyAnimation {

    private float elapsedTime;

    private Animation<Texture> animation;

    private float frameTime;
    private Array<Texture> frames;

    /**
     * Create a animation with 1 frame.
     * @param frameImgPath Path to Image to show.
     */
    public WorldBodyAnimation(String frameImgPath) {
        this(1f, frameImgPath);
    }

    /**
     * Create a animation with many frames.
     * @param frameTime Time in seconds to show each frame.
     * @param frameImgPaths Path to Image to show for each frame.
     */
    public WorldBodyAnimation(float frameTime, String... frameImgPaths) {
        this.frameTime = frameTime;

        Array<Texture> frames = new Array<>();
        for (String imgPath : frameImgPaths) {
            frames.add(new Texture(imgPath));
        }
        this.frames = frames;

        animation = new Animation<>(frameTime, frames);
    }

    public float getFrameTime() {
        return frameTime;
    }

    public int numFrames() {
        return frames.size;
    }

    public Texture getFrame(float elapsedTime) {
        return animation.getKeyFrame(elapsedTime);
    }

}
