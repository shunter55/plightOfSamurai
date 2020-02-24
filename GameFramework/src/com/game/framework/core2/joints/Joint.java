package com.game.framework.core2.joints;

import com.game.framework.core2.bodies.WorldBody;

public interface Joint {

    public void buildOn(WorldBody body);

    public WorldBody getBody();

    public void dispose();

}
