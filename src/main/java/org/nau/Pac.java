package org.nau;

import org.newdawn.slick.*;


public class Pac extends BasicGame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int TARGET_FPS = 59;

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
        

    }

    private void drawInfo(Graphics g) {
       
    }

    private void drawPac(Graphics g, PacManUnit unit) {

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {

    }

    @Override
    public void keyPressed(int key, char c) {
    	
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
