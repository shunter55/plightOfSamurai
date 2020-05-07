package com.game.framework.core2.character.managers;

import com.game.framework.core.bodies.Function;
import com.game.framework.core2.character.managers.directions.DirFuncs;

import java.util.HashMap;
import java.util.Set;

/**
 * Manages a Character's Movement.
 * Character's movement direction functions should probably call out to WorldObject's MovementManager.
 * Allows assigning functionality based off the direction the Character is moving.
 */
public class CharacterMovementManager {

    private DirectionManager directionManager;

    private HashMap<Enum, DirFuncs> directions;
    private Enum currentDir = null;

    public CharacterMovementManager(DirectionManager directionManager) {
        this.directionManager = directionManager;

        this.directions = new HashMap<>();
    }

    public void move(Enum dir) {
        verifyDirection(dir);

        if (directions.containsKey(currentDir)) {
            directions.get(currentDir).fromDir();

            if (currentDir == dir) {
                directions.get(dir).whileDir();
            } else {
                directions.get(dir).onDir();
            }
        } else {
            directions.get(dir).onDir();
        }

        currentDir = dir;
    }

    public void addDirections(Enum[] dirs) {
        for (Enum dir : dirs) {
            directions.put(dir, new DirFuncs());
        }
    }

    public Set<Enum> getDirections() {
        return directions.keySet();
    }

    public DirFuncs direction(Enum dir) {
        verifyDirection(dir);
        return directions.get(dir);
    }

    public Enum lastDirection() {
        return currentDir;
    }

//    /**
//     * Add functionality for when Character moves in a direction.
//     * null is the stop direction.
//     */
//    public void onDirection(Enum dir, Function<Void, Void> fn) {
//        verifyDirection(dir);
//
//        DirFuncs funcs = directions.containsKey(dir) ? directions.get(dir) : new DirFuncs();
//        funcs.setOnDir(fn);
//
//        directions.put(dir, funcs);
//    }

//    /**
//     * Add functionality for when Character changes direction from a direction.
//     * null is the stop direction.
//     */
//    public void fromDirection(Enum dir, Function<Void, Void> fn) {
//        verifyDirection(dir);
//
//        DirFuncs funcs = directions.containsKey(dir) ? directions.get(dir) : new DirFuncs();
//        funcs.setFromDir(fn);
//
//        directions.put(dir, funcs);
//    }

    void verifyDirection(Enum dir) {
        if (!directions.containsKey(dir)) {
            throw new RuntimeException(dir + " is not in " + getDirections());
        }
    }

}
