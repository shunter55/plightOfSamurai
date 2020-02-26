package com.game.framework.core2.character.managers;

import com.game.framework.core.bodies.Function;
import java.util.HashMap;
import java.util.Set;

public class DirectionManager {

    private class DirFuncs {
        private Function<Void, Void> _onDir, _fromDir;

        public void setOnDir(Function<Void, Void> fn) {
            _onDir = fn;
        }

        public void setFromDir(Function<Void, Void> fn) {
            _fromDir = fn;
        }

        public void onDir() {
            if (_onDir != null)
                _onDir.call(null);
        }

        public void fromDir() {
            if (_fromDir != null)
                _fromDir.call(null);
        }
    }

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

    /**
     * Character begins facing a direction.
     * @param dir The direction to face.
     */
    public void face(String dir) {
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

}
