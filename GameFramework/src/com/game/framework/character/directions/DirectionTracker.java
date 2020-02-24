package com.game.framework.character.directions;

public class DirectionTracker<DirectionsEnum> {

    private DirectionsEnum[] directions;
    private DirectionsEnum currentDirection;

    public DirectionTracker(DirectionsEnum defaultDirection, DirectionsEnum[] directions) {
        this.directions = directions;

        currentDirection = defaultDirection;
    }

    public void setDirection(DirectionsEnum direction) {
        currentDirection = direction;
    }

    public DirectionsEnum getDirection() {
        return currentDirection;
    }

}
