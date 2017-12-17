package org.nau;

import org.nau.pacman.map.PacModel;
import org.nau.pacman.map.loader.Models;
import org.nau.pacman.map.node.*;
import org.newdawn.slick.*;


public class Pac extends BasicGame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int TARGET_FPS = 59;

    private PacModel model;

    private float cellSize;
    private float cornSize;
    private float pacSize;
    private float ghostSize;
    private float wallLineWidth;

    private boolean exit = false;

    private Color debugGridLineColor = new Color(255, 255, 255,16);

    public Pac() {
        super("PacManUnit");
        model = Models.load("maps/classic.level");
        model.setFps(TARGET_FPS / 3);

        setCellSize((float) Math.min(WIDTH, HEIGHT) / Math.max(model.getWidth(), model.getHeight()));
    }

    private void setCellSize(float value) {
        cellSize = value;
        cornSize = value * 0.16F;
        pacSize = value * 1.3F;
        ghostSize = value *1.3F;
        wallLineWidth = value * 0.06F;
    }

    private void debug(Graphics g) {
        final double offset = cellSize / 2;
        Color oldColor = g.getColor();
        g.setColor(debugGridLineColor);
        for (int i = 0; i < Math.max(model.getWidth(), model.getHeight()); ++i) {
            float x = (float) (offset + i * cellSize);
                g.drawLine(x, 0, x, (float) Math.min(WIDTH, HEIGHT));
                g.drawLine(0, x, (float) Math.min(WIDTH, HEIGHT), x);
        }
        g.setColor(oldColor);
    }

    @Override
    public void init(GameContainer container) throws SlickException {

    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        if (model.isDied()) {
            g.setColor(Color.darkGray);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

//        debug(g);

        float offset = (float) cellSize / 2;

        g.setColor(Color.blue);
        g.setLineWidth(wallLineWidth);
        for (int y = 0; y < model.getHeight(); ++y) {
            for (int x = 0; x < model.getWidth(); ++x) {
                boolean n, e, s, w;
                if (model.isWall(x, y)) {
                    w = model.isWall(x - 1, y);
                    e = model.isWall(x + 1, y);
                    n = model.isWall(x, y - 1);
                    s = model.isWall(x, y + 1);

                    float x1 = (cellSize * x) + offset;
                    float y1 = (cellSize * y) + offset;

                    if (w) {
                        g.drawLine(x1, y1, x1 - offset, y1);
                    }
                    if (e) {
                        g.drawLine(x1, y1, x1 + offset, y1);
                    }
                    if (n) {
                        g.drawLine(x1, y1, x1, y1 - offset);
                    }
                    if (s) {
                        g.drawLine(x1, y1, x1, y1 + offset);
                    }
                }
            }
        }
        g.setColor(Color.white);
        g.setLineWidth(1);

        for (Corn corn: model.getCorns()) {
            float x = (float)(cellSize * corn.getX()) + offset;
            float y = (float)(cellSize * corn.getY()) + offset;

            g.fillOval(x - (float) (cornSize / 2), y - (float) (cornSize / 2), (float) cornSize, (float) cornSize);
        }

        for(Unit unit: model.getUnits()) {
            if (unit instanceof PacManUnit) {
                drawPac(g, (PacManUnit) unit);
            } else if (unit instanceof GhostUnit) {
                drawGhost(g, (GhostUnit) unit);
            }
        }

        drawInfo(g);
    }

    private void drawInfo(Graphics g) {
        final int offset = 20;
        int x = Math.min(WIDTH, HEIGHT) + offset;
        int y = offset;

        g.setColor(Color.yellow);

        if (model.isWin()) {
            g.drawString("WIN", WIDTH / 2, HEIGHT / 2);
        }

        if (model.isDied() && model.getLifeCount() < 0) {
            g.drawString("GAME OVER (to restart press 'R', to exit 'Esc')", WIDTH / 2 - 150, HEIGHT / 2);
        }

        g.drawString("Score: " + model.getScore(), x, y);
        y += offset * 2;
        g.drawString("Life count: " + model.getLifeCount(), x, y);
    }

    private void drawPac(Graphics g, PacManUnit unit) {
        float offset = (cellSize - pacSize) / 2;
        float x = (float)(cellSize * unit.getX());
        float y = (float)(cellSize * unit.getY());

        g.setColor(Color.yellow);
        g.fillOval(x + offset, y + offset, pacSize, pacSize);
    }

    private void drawGhost(Graphics g, GhostUnit unit) {
        float offset = (cellSize - ghostSize) / 2;
        float x = (float)(cellSize * unit.getX());
        float y = (float)(cellSize * unit.getY());

        g.setColor(unit.getColor());
        g.fillOval(x + offset, y + offset, ghostSize, ghostSize);
        g.fillRect(x + offset, y - 2.8f * offset, ghostSize, ghostSize / 2.1f);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if (exit) {
            container.exit();
        }
        model.tick();
    }

    @Override
    public void keyPressed(int key, char c) {
        System.out.println(key);
        switch (key) {
            case 200:   // top button
                model.setDirection(Unit.Direction.NORTH);
                break;
            case 203:   // left button
                model.setDirection(Unit.Direction.WEST);
                break;
            case 205:   // right button
                model.setDirection(Unit.Direction.EAST);
                break;
            case 208:   // bottom button
                model.setDirection(Unit.Direction.SOUTH);
                break;
            case 1:     // escape
                exit = true;
                break;
            case 19:    // R: restart
                model.restart();
                break;
        }
    }
    
    public static void main(String[] args) throws SlickException {
        try {
            AppGameContainer app = new AppGameContainer(new Pac());
            app.setDisplayMode(WIDTH, HEIGHT, false);
            app.setTargetFrameRate(TARGET_FPS);
            app.setForceExit(false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

}
