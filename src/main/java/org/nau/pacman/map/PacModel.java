package org.nau.pacman.map;

import org.newdawn.slick.Color;
import org.nau.pacman.map.node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacModel {
    public static final int MIN_WIDTH = 4;
    public static final int MAX_WIDTH = 100;
    public static final int MIN_HEIGHT = 4;
    public static final int MAX_HEIGHT = 100;

    public static final int DEFAULT_LIFE_COUNT = 3;

    public static final int DEFAULT_DELAY = 3;

    private static final byte LEFT = 1;
    private static final byte RIGHT = 2;
    private static final byte TOP = 4;
    private static final byte BOTTOM = 8;

    private Random random = new Random();

    private int fps = 0;
    private long counter;

    private Node[][] map;

    private int width;
    private int height;

    private PacManUnit pacManUnit;
    private List<Unit> units;
    private List<Corn> corns;
    private boolean[][] wall;

    private int[][] routes;

    private Unit.Direction futureDirection = Unit.Direction.EAST;

    private int delay = DEFAULT_DELAY;
    private int score;
    private int lifeCount;
    private boolean win;
    private boolean died;

    public PacModel(Node[][] map) {
        height = map.length;
        width = map[0].length;

        if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT) {
            throw new IllegalArgumentException("Map size is illegal");
        }

        this.map = map;
        this.routes = new int[height][width];
        init();
    }

    private void init() {
        score = 0;
        counter = 0;
        lifeCount = DEFAULT_LIFE_COUNT;

        win = false;
        died = false;

        units = new ArrayList<>();
        corns = new ArrayList<>();
        wall = new boolean[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Node node = map[y][x];
                node.setX(x);
                node.setY(y);
                if (node instanceof Unit) {
                    Unit unit = (Unit)node;
                    if (node instanceof PacManUnit) {
                        pacManUnit = (PacManUnit) node;
                        pacManUnit.setDirection(futureDirection);
                    } else if (node instanceof GhostUnit) {
                        GhostUnit ghost = (GhostUnit)node;
                        switch (random.nextInt(3)) {
                            case 0:
                                ghost.setColor(Color.magenta);
                                break;
                            case 1:
                                ghost.setColor(Color.green);
                                break;
                            case 2:
                                ghost.setColor(Color.red);
                                break;
                        }
                    }
                    unit.setInitialX(x);
                    unit.setInitialY(y);
                    units.add((Unit) node);
                } else if (node instanceof Corn) {
                    corns.add((Corn)node);
                } else if (node instanceof Wall) {
                    wall[y][x] = true;
                }
                routes[y][x] = (getNode(x - 1, y) instanceof Wall ? 0 : LEFT) |
                        (getNode(x + 1, y) instanceof Wall ? 0 : RIGHT) |
                        (getNode(x, y - 1) instanceof Wall ? 0 : TOP) |
                        (getNode(x, y + 1) instanceof Wall ? 0 : BOTTOM);
            }
        }
    }

    private void respawn() {
        for(Unit unit: units) {
            unit.setX(unit.getInitialX());
            unit.setY(unit.getInitialY());
        }
    }

    public void restart() {
        init();
    }

    private void resetLevel() {
        score -= 100;
        win = false;
        died = false;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public Node getNode(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return map[y][x];
    }

    public void setDirection(Unit.Direction direction) {
        futureDirection = direction;
    }

    public int getScore() {
        return score;
    }

    public boolean isWin() {
        return win;
    }

    public boolean isDied() {
        return died;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public List<Corn> getCorns() {
        return this.corns;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public boolean isWall(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() && wall[y][x];
    }

    private int getRoute(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() ? routes[y][x] : 0;
    }

    public void tick() {
        counter += 1;
        if (counter % fps != 0) {
            return;
        }
        if (delay > 0) {
            delay -= 1;
            return;
        }

        if (isDied()) {
            if (lifeCount >= 0) {
                respawn();
                died = false;
            } else return;
        }

        for(Unit unit: units) {
            Unit.Direction direction = unit.getDirection();
            int x = unit.getX(), y = unit.getY();

            int route = getRoute(x, y);

            if (unit instanceof PacManUnit) {
                switch (futureDirection) {
                    case WEST:
                        if ((route &= LEFT) != 0) {
                            unit.setDirection(futureDirection);
                        }
                        break;
                    case EAST:
                        if ((route &= RIGHT) != 0) {
                            unit.setDirection(futureDirection);
                        }
                        break;
                    case NORTH:
                        if ((route &= TOP) != 0) {
                            unit.setDirection(futureDirection);
                        }
                        break;
                    case SOUTH:
                        if ((route &= BOTTOM) != 0) {
                            unit.setDirection(futureDirection);
                        }
                        break;
                }

                switch (unit.getDirection()) {
                    case NORTH:
                        if(!isWall(x, y - 1)) {
                            y -= 1;
                        }
                        break;
                    case SOUTH:
                        if(!isWall(x, y + 1)) {
                            y += 1;
                        }
                        break;
                    case WEST:
                        if(!isWall(x - 1, y)) {
                            x -= 1;
                        }
                        break;
                    case EAST:
                        if(!isWall(x + 1, y)) {
                            x += 1;
                        }
                        break;
                }

                Node node = getNode(x, y);
                // EAT CORNS
                if (node != null && corns.remove(node)) {
                    score += 1;
                }

                // WIN
                if (corns.size() == 0) {
                    win = true;
                }
            } else {
                int rndDirection = random.nextInt(Integer.bitCount(route));
                if ((route & TOP) != 0 && rndDirection-- == 0) {
                    y -= 1;
                } else if ((route & RIGHT) != 0 && rndDirection-- == 0){
                    x += 1;
                } else if ((route & BOTTOM) != 0 && rndDirection-- == 0){
                    y += 1;
                } else if ((route & LEFT) != 0 && rndDirection-- == 0){
                    x -= 1;
                }
            }

            unit.setX(x);
            unit.setY(y);

            // INTERSECTION GHOST
            for (Unit mob: units) {
                if (mob instanceof GhostUnit && mob.getX() == pacManUnit.getX() && mob.getY() == pacManUnit.getY()) {
                    died = true;
                    lifeCount -= 1;
                }
            }
        }
    }
}
