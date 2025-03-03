package com.example.pacman;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class PacMan {
    private final Arc pacMan;
    private int x, y;
    private final int[][] maze;

    private int dx = 0, dy = 0;

    private boolean isImmortal = false;
    private long immortalStartTime = 0;
    public static final long IMMORTAL_DURATION = 10_000_000_000L;

    public PacMan(int x, int y, int[][] maze) {
        this.x = x;
        this.y = y;
        this.maze = maze;
        pacMan = new Arc(x * PacManGame.TILE_SIZE + (double) PacManGame.TILE_SIZE / 2, y * PacManGame.TILE_SIZE + (double) PacManGame.TILE_SIZE / 2, (double) PacManGame.TILE_SIZE / 2, (double) PacManGame.TILE_SIZE / 2, 45, 270);
        pacMan.setType(ArcType.ROUND);
        pacMan.setFill(Color.YELLOW);
    }

    public Arc getPacMan() {
        return pacMan;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(int newDx, int newDy) {
        int newX = x + newDx;
        int newY = y + newDy;

        if (maze[newY][newX] != 1) {
            dx = newDx;
            dy = newDy;
        }

        int currentX = x + dx;
        int currentY = y + dy;

        if (maze[currentY][currentX] != 1) {
            x = currentX;
            y = currentY;
            pacMan.setCenterX(x * PacManGame.TILE_SIZE + (double) PacManGame.TILE_SIZE / 2);
            pacMan.setCenterY(y * PacManGame.TILE_SIZE + (double) PacManGame.TILE_SIZE / 2);

            if (dx == 1) {
                pacMan.setStartAngle(45);
            } else if (dx == -1) {
                pacMan.setStartAngle(225);
            } else if (dy == 1) {
                pacMan.setStartAngle(315);
            } else if (dy == -1) {
                pacMan.setStartAngle(135);
            }
        }
    }

    public void die() {

    }

    public boolean isImmortal() {
        if (isImmortal && System.nanoTime() - immortalStartTime > IMMORTAL_DURATION) {
            isImmortal = false;
        }
        return isImmortal;
    }

    public void killGhost(Ghost ghost) {
        if (isImmortal) {
            ghost.die();
        }
    }

    public Bounds getBoundsInParent() {
        return pacMan.getBoundsInParent();
    }

    public void eatSpecialOrb() {
        isImmortal = true;
        immortalStartTime = System.nanoTime();
    }

    public long getImmortalStartTime() {
        return immortalStartTime;
    }

    public void setImmortal(boolean isImmortal) {
        this.isImmortal = isImmortal;
    }

}