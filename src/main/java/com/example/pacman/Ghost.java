package com.example.pacman;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import java.util.*;

public class Ghost {
    private final Path ghost;
    private double x, y;
    private final int[][] maze;
    private final PacMan pacMan;

    public Ghost(double x, double y, Color color, int[][] maze, PacMan pacMan) {
        this.x = x;
        this.y = y;
        this.maze = maze;
        this.pacMan = pacMan;
        ghost = new Path();
        ghost.setFill(color);
        createGhostShape();
        ghost.setTranslateX(x * PacManGame.TILE_SIZE);
        ghost.setTranslateY(y * PacManGame.TILE_SIZE);
    }

    private void createGhostShape() {
        ghost.getElements().clear();
        double tileSize = PacManGame.TILE_SIZE;
        ghost.getElements().add(new MoveTo(0, tileSize / 2 * 0.75));
        ghost.getElements().add(new CubicCurveTo(tileSize / 8, -tileSize / 4, tileSize * 0.6, -tileSize / 4, tileSize * 0.75, tileSize / 2 * 0.75));
        ghost.getElements().add(new LineTo(tileSize * 0.75, tileSize * 0.75));
        ghost.getElements().add(new LineTo(0, tileSize * 0.75));
        ghost.getElements().add(new LineTo(0, tileSize / 2 * 0.75));
    }

    public Path getGhost() {
        return ghost;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void move(List<Ghost> ghosts) {
        int[] nextMove = findShortestPathToPacMan();
        if (nextMove != null) {
            if (!isPositionOccupied(nextMove[0], nextMove[1], ghosts)) {
                x = nextMove[0];
                y = nextMove[1];
            }
        }
        ghost.setTranslateX(x * PacManGame.TILE_SIZE);
        ghost.setTranslateY(y * PacManGame.TILE_SIZE);
        checkCollisionWithPacMan();
    }

    private boolean isPositionOccupied(int nx, int ny, List<Ghost> ghosts) {
        for (Ghost otherGhost : ghosts) {
            if (otherGhost != this && otherGhost.getX() == nx && otherGhost.getY() == ny) {
                return true;
            }
        }
        return false;
    }

    private int[] findShortestPathToPacMan() {
        int pacManX = pacMan.getX();
        int pacManY = pacMan.getY();
        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, int[]> parent = new HashMap<>();
        queue.add(new int[]{(int) x, (int) y});
        visited.add(x + "," + y);
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0];
            int cy = current[1];
            if (cx == pacManX && cy == pacManY) {
                return reconstructPath(parent, pacManX, pacManY);
            }
            for (int[] dir : directions) {
                int nx = cx + dir[0];
                int ny = cy + dir[1];
                String key = nx + "," + ny;
                if (isValidMove(nx, ny) && !visited.contains(key)) {
                    queue.add(new int[]{nx, ny});
                    visited.add(key);
                    parent.put(key, new int[]{cx, cy});
                }
            }
        }
        return null;
    }

    private int[] reconstructPath(Map<String, int[]> parent, int targetX, int targetY) {
        int[] step = {targetX, targetY};
        while (parent.containsKey(step[0] + "," + step[1])) {
            int[] prev = parent.get(step[0] + "," + step[1]);
            if (prev[0] == x && prev[1] == y) {
                return step;
            }
            step = prev;
        }
        return null;
    }

    private boolean isValidMove(int nx, int ny) {
        return nx >= 0 && ny >= 0 && nx < maze[0].length && ny < maze.length && maze[ny][nx] != 1;
    }

    public Bounds getBoundsInParent() {
        return ghost.getBoundsInParent();
    }

    private void checkCollisionWithPacMan() {
        if (pacMan.getBoundsInParent().intersects(ghost.getBoundsInParent())) {
            if (pacMan.isImmortal()) {
                die();
            } else {
                pacMan.die();
            }
        }
    }

    public void die() {
        x = 13;
        y = 15;
        ghost.setTranslateX(x * PacManGame.TILE_SIZE);
        ghost.setTranslateY(y * PacManGame.TILE_SIZE);
    }
}