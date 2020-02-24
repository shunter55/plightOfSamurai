package com.game.framework.core.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.game.framework.core2.bodies.WorldBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InputProcessor {
    private static InputProcessor instance = null;

    // WorldBody id -> InputAdapter.
    public Map<String, InputAdapter> inputAdapters;

    private InputProcessor() {
        inputAdapters = new HashMap<>();
    }

    public static InputProcessor getInputProcessor() {
        init();

        return instance;
    }

    public void addInput(WorldBody body, InputAdapter input) {
        inputAdapters.put(body.id(), input);
    }

    public void removeInput(WorldBody body) {
        inputAdapters.remove(body.id());
    }

    public InputAdapter getInput(WorldBody body) {
        return inputAdapters.get(body.id());
    }

    public Iterable<InputAdapter> getInputs() {
        return new ArrayList(inputAdapters.values());
    }

    private static void init() {
        if (instance == null) {
            instance = new InputProcessor();
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                for (InputAdapter input : instance.getInputs())
                    input.keyDown(keycode);
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                for (InputAdapter input : instance.getInputs())
                    input.keyUp(keycode);
                return true;
            }

            @Override
            public boolean keyTyped(char character) {
                for (InputAdapter input : instance.getInputs())
                    input.keyTyped(character);
                return super.keyTyped(character);
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                for (InputAdapter input : instance.getInputs())
                    input.touchDown(screenX, screenY, pointer, button);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                for (InputAdapter input : instance.getInputs())
                    input.touchUp(screenX, screenY, pointer, button);
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                for (InputAdapter input : instance.getInputs())
                    input.touchDragged(screenX, screenY, pointer);
                return true;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                for (InputAdapter input : instance.getInputs())
                    input.mouseMoved(screenX, screenY);
                return true;
            }

            @Override
            public boolean scrolled(int amount) {
                for (InputAdapter input : instance.getInputs())
                    input.scrolled(amount);
                return true;
            }
        });
    }

}
