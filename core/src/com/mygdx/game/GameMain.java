package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.PhysicsBalls;

public class GameMain extends Game {

    private PhysicsBalls physicsBalls;

    @Override
    public void create() {
        physicsBalls = new PhysicsBalls(this);

        setScreen(physicsBalls);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
