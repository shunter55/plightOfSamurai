package com.game.framework.core.bodies.joints;

import com.game.framework.core.bodies.WorldBody;

public interface Joint {

    public void buildOn(WorldBody body);

    public WorldBody getBody();

    public void dispose();

}
