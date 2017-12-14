package org.nau.pacman.map.node;

import org.newdawn.slick.Color;

public class GhostUnit extends Unit {
    private Color color;

    public GhostUnit() {
        super();
    }

    public GhostUnit(int x, int y, Color color) {
        super(x, y);
        setColor(color);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
