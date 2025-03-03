package com.example.pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;

public class Maze {
    private int[][] maze;
    private final Pane root;
    private final List<Circle> orbs = new ArrayList<>();
    private final List<SpecialOrb> specialOrbs = new ArrayList<>();
    public int orbsEaten = 0;
    public Text scoreText;

    public Maze(int width, int height) {
        this.maze = new int[height][width];
        this.root = new Pane();

        this.maze = new int[][] {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,2,1},
                {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1},
                {1,0,0,0,0,0,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,0,1,1,0,0,0,0,0,2,0,0,0,0,1,1,0,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,2,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
                {1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1},
                {1,0,1,1,1,1,0,1,1,0,1,1,1,0,0,1,1,1,0,1,1,0,1,1,1,1,0,1},
                {1,0,1,1,1,1,0,1,1,0,1,0,0,0,0,0,0,1,0,1,1,0,1,1,1,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,2,1,0,0,0,0,0,0,0,0,0,1},
                {1,0,1,1,1,1,0,1,1,1,1,0,0,0,0,0,0,1,1,1,1,0,1,1,1,1,0,1},
                {1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0,1,1,1,1,1,1},
                {1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
                {1,0,1,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0,1,1,1,1,2,1},
                {1,0,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1},
                {1,0,0,0,0,2,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };

        createMaze();
        createOrbs();
        createScoreDisplay();
    }

    private void createMaze() {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (maze[y][x] == 1) {
                    Rectangle wall = new Rectangle(x * PacManGame.TILE_SIZE, y * PacManGame.TILE_SIZE, PacManGame.TILE_SIZE, PacManGame.TILE_SIZE);
                    wall.setFill(Color.DARKBLUE);
                    root.getChildren().add(wall);
                } else {
                    Rectangle path = new Rectangle(x * PacManGame.TILE_SIZE, y * PacManGame.TILE_SIZE, PacManGame.TILE_SIZE, PacManGame.TILE_SIZE);
                    path.setFill(Color.BLACK);
                    root.getChildren().add(path);
                }
            }
        }
    }

    private void createOrbs() {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (maze[y][x] == 0) {
                    Circle orb = new Circle(x * PacManGame.TILE_SIZE + (double) PacManGame.TILE_SIZE / 2, y * PacManGame.TILE_SIZE + (double) PacManGame.TILE_SIZE / 2, 3);
                    orb.setFill(Color.WHITE);
                    root.getChildren().add(orb);
                    orbs.add(orb);
                } else if (maze[y][x] == 2) {
                    SpecialOrb specialOrb = new SpecialOrb(x * PacManGame.TILE_SIZE + (double) PacManGame.TILE_SIZE / 2, y * PacManGame.TILE_SIZE + (double) PacManGame.TILE_SIZE / 2);
                    root.getChildren().add(specialOrb.getOrb());
                    specialOrbs.add(specialOrb);
                }
            }
        }
    }

    private void createScoreDisplay() {
        scoreText = new Text(10, 20, "Puntuación: 0");
        scoreText.setFont(new Font("Arial", 20));
        scoreText.setLayoutY(8);
        scoreText.setFill(Color.YELLOW);
        root.getChildren().add(scoreText);
    }

    void updateScore() {
        scoreText.setText("Puntuación: " + orbsEaten);
    }

    public void eatOrb(Circle orb) {
        if (orbs.contains(orb)) {
            orbs.remove(orb);
            root.getChildren().remove(orb);
            orbsEaten++;
            updateScore();
        }
    }

    public void eatSpecialOrb(SpecialOrb specialOrb) {
        specialOrbs.remove(specialOrb);
        root.getChildren().remove(specialOrb.getOrb());
        orbsEaten += 5;
        updateScore();
    }

    public Pane getRoot() {
        return root;
    }

    public int[][] getMaze() {
        return maze;
    }

    public List<Circle> getOrbs() {
        return orbs;
    }

    List<SpecialOrb> getSpecialOrbs() {
        return specialOrbs;
    }
}

