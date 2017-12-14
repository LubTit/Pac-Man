package org.nau.pacman.map.node;

public class Unit extends Node {
    private int initialX;
    private int initialY;

    private Direction direction;


    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    public Unit() {

    }

    public Unit(int x, int y) {
        super(x, y);
        initialX = x;
        initialY = y;
    }

    public int getInitialX() {
        return initialX;
    }

    public void setInitialX(int initialX) {
        this.initialX = initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public void setInitialY(int initialY) {
        this.initialY = initialY;
    }

    public void resetPosition() {
        this.setX(initialX);
        this.setY(initialY);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
