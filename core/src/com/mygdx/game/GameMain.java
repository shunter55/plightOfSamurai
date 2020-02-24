package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainMenu;

public class GameMain extends Game {

    private MainMenu mainMenu;
    private GameScreen gameScreen;

    @Override
    public void create() {
        mainMenu = new MainMenu(this);
        gameScreen = new GameScreen(this);

        setScreen(mainMenu);
    }

    public void mainMenu() {
        mainMenu.setContinue();
        setScreen(mainMenu);
    }

    public void startGame() {
        setScreen(gameScreen);
    }

    public void restartGame() {
        gameScreen.dispose();
        gameScreen = new GameScreen(this);
        startGame();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
