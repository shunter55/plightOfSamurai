package com.game.framework.core2.character.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.OrderedMap;
import com.game.framework.core.bodies.Function;
import com.game.framework.core2.bodies.WorldBody;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Creates a setUpdate function that manages a Set of Directions.
 * movement.move will be called for all the registered directions when the Input.Key is pressed.
 */
public class DirectionInputManager {
    private CharacterMovementManager movement;

    private Map<Enum, Set<Integer>> directionInputKeyMap;
    private Enum stopEnum;

    private Enum lastDir = null;

    public DirectionInputManager(WorldBody body, CharacterMovementManager movement) {
        this.movement = movement;

        directionInputKeyMap = new HashMap<>();

        body.controller.setUpdate(worldBody -> {
            int numKeys = 0;
            Enum bestMatch = null;

            for (Enum dir : directionInputKeyMap.keySet()) {
                if (matchesInput(dir) && numKeys < directionInputKeyMap.get(dir).size()) {
                    numKeys = directionInputKeyMap.get(dir).size();
                    bestMatch = dir;
                }
            }

            if (bestMatch == null) {
                if (stopEnum != null)
                    movement.move(stopEnum);
            } else {
                movement.move(bestMatch);
            }

            return null;
        });
    }

    /**
     * Add a Mapping from inputKey to movement Direction.
     * @param dir Direction that the Character should move when Set of inputKeys are pressed.
     *            Must be registered with CharacterMovementManager.
     * @param inputKey Set of Input.Keys. When pressed Character will move in given direction.
     */
    public void addDirectionInput(Enum dir, Integer inputKey, Integer... otherInputKeys) {
        movement.verifyDirection(dir);

        Set<Integer> inputKeys = new HashSet<>();
        inputKeys.add(inputKey);
        for (Integer ik : otherInputKeys) {
            inputKeys.add(ik);
        }
        directionInputKeyMap.put(dir, inputKeys);
    }

    /**
     * Sets the direction that will be mapped to Stop.
     * No buttons are pressed.
     * @param dir The stop direction.
     */
    public void setStopEnum(Enum dir) {
        stopEnum = dir;
    }

    private boolean matchesInput(Enum dir) {
        for (Integer inputKey : directionInputKeyMap.get(dir)) {
            if (!Gdx.input.isKeyPressed(inputKey)) {
                return false;
            }
        }
        return true;
    }

}
