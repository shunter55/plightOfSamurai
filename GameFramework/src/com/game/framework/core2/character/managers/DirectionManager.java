package com.game.framework.core2.character.managers;

import com.game.framework.core.bodies.Function;
import com.game.framework.core2.character.managers.directions.DirFuncs;

import java.util.HashMap;
import java.util.Set;

/**
 * Manages the Direction a Character faces.
 * Allows adding functionality for whenever the character faces a direction.
 */
public class DirectionManager {

    private HashMap<String, DirFuncs> directions;
    private String currentDirection;

    public DirectionManager() {
        directions = new HashMap<>();
    }

    public void addDirection(String dir) {
        directions.put(dir, new DirFuncs());
    }

    public void addDirections(Enum[] enums) {
        for (Enum e : enums) {
            addDirection(e.toString());
        }
    }

    public void removeDirection(String dir) {
        directions.remove(dir);
    }

    public void removeDirections(Enum[] enums) {
        for (Enum e : enums) {
            removeDirection(e.toString());
        }
    }

    public Set<String> getDirections() {
        return directions.keySet();
    }

    public boolean containsDirection(String dir) {
        return getDirections().contains(dir);
    }

    public boolean containsDirection(Enum dir) {
        return containsDirection(dir.toString());
    }

    /**
     * Character begins facing a direction.
     * @param dir The direction to face.
     */
    public void face(String dir) {
        verifyDirection(dir);

        if (!getDirections().contains(dir))
            throw new RuntimeException(dir + " is not in " + getDirections());

        if (!dir.equals(currentDirection)) {
            directions.get(dir).onDir();

            if (currentDirection != null)
                directions.get(currentDirection).fromDir();
        }

        currentDirection = dir;
    }

    public void face(Enum dir) {
        face(dir.toString());
    }

    /**
     * @return The current direction the character is facing.
     */
    public String current() {
        return currentDirection;
    }

    public boolean isAny(Enum... enums) {
        for (Enum e : enums) {
            if (currentDirection.equals(e.toString()))
                return true;
        }
        return false;
    }

    /**
     * Run function whenever character starts facing a direction. When `face` is called.
     * @param dir Direction when fn should be called.
     * @param fn The function to call.
     */
    public void onDirection(String dir, Function<Void, Void> fn) {
        if (!directions.containsKey(dir))
            throw new RuntimeException(dir + " not in " + getDirections() + " addDirection first!");

        directions.get(dir).setOnDir(fn);
    }

    public void onDirection(Enum dir, Function<Void, Void> fn) {
        onDirection(dir.toString(), fn);
    }

    /**
     * Run function whenever character stops facing a direction. When `face` is called.
     * @param dir Direction when fn should be called.
     * @param fn The function to call.
     */
    public void fromDirection(String dir, Function<Void, Void> fn) {
        if (!directions.containsKey(dir))
            throw new RuntimeException(dir + " not in " + getDirections() + " addDirection first!");

        directions.get(dir).setFromDir(fn);
    }

    /**
     * Throws an error if the direction is not valid.
     */
    private void verifyDirection(String dir) {
        if (!containsDirection(dir)) {
            throw new RuntimeException(dir + " is not in " + getDirections());
        }
    }

    private void verifyDirection(Enum dir) {
        verifyDirection(dir.toString());
    }

}
