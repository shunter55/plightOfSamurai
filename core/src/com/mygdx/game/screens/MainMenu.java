package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.GameMain;

public class MainMenu extends ScreenAdapter {

    private static Vector2 worldSize = new Vector2(5f, 5f);

    private GameMain game;

    private Stage stage;

    TextButton startButton, restartButton;

    public MainMenu(GameMain game) {
        this.game = game;

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Skin skin = new Skin(Gdx.files.internal("buttonSkins/pix/skin/pixthulhu-ui.json"));
        startButton = new TextButton("Start", skin);
        startButton.setTransform(true);
        startButton.setScale(0.5f);
        startButton.setOrigin(-startButton.getWidth() / 2, -startButton.getHeight() / 2);
        startButton.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 50);
        startButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Touch Down!");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Touch Up!");
                game.startGame();
            }
        });
        stage.addActor(startButton);

        restartButton = new TextButton("Restart", skin);
        restartButton.setTransform(true);
        restartButton.setScale(0.5f);
        restartButton.setOrigin(-restartButton.getWidth() / 2, -restartButton.getHeight() / 2);
        restartButton.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 50);
        restartButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Touch Down!");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Touch Up!");
                game.restartGame();
            }
        });
        stage.addActor(restartButton);

    }

    public void setContinue() {
        startButton.setText("Continue");
        startButton.setSize(startButton.getPrefWidth(), startButton.getPrefHeight());
        startButton.setOrigin(-startButton.getWidth() / 2, -startButton.getHeight() / 2);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        stage.act(1/30f);
        stage.draw();
    }
}
