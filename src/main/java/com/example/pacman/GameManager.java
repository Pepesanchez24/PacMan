package com.example.pacman;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.example.pacman.PacMan.IMMORTAL_DURATION;

public class GameManager {
    private final Pane root;
    private Maze maze;
    private PacMan pacMan;
    private List<Ghost> ghosts;
    private SoundManager soundManager;
    private AnimationTimer gameTimer;
    private Timeline spawnTimer;

    private long lastUpdate = 0;
    private static final long UPDATE_INTERVAL = 300_000_000;
    private long lastMoveTime = 0;
    private static final long MOVE_INTERVAL = 280_000_000;

    private Label immortalityLabel;

    private int dx = 0, dy = 0;

    public GameManager() {
        root = new Pane();
        showMainMenu();
    }

    public void showMainMenu() {
        StackPane mainMenu = new StackPane();
        mainMenu.setPrefSize(root.getWidth(), root.getHeight());
        mainMenu.setStyle("-fx-background-color: #1e1e1e;");

        VBox layout = new VBox(60);
        layout.setAlignment(Pos.CENTER);
        layout.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label titleLabel = new Label("PAC-MAN");
        titleLabel.setFont(new Font("Arial", 100));
        titleLabel.setTextFill(Color.YELLOW);
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.5), 10, 0.0, 2, 2);");

        Button playButton = new Button("Jugar");
        playButton.setStyle("-fx-font-size: 36px; -fx-background-color: #ffcc00; -fx-text-fill: #000000; -fx-font-weight: bold; -fx-border-radius: 20; -fx-background-radius: 20;");
        playButton.setPrefSize(300, 80);
        playButton.setOnAction(e -> startGame());

        Button exitButton = new Button("Salir");
        exitButton.setStyle("-fx-font-size: 36px; -fx-background-color: #ff6666; -fx-text-fill: #000000; -fx-font-weight: bold; -fx-border-radius: 20; -fx-background-radius: 20;");
        exitButton.setPrefSize(300, 80);
        exitButton.setOnAction(e -> System.exit(0));
        layout.getChildren().addAll(titleLabel, playButton, exitButton);
        mainMenu.getChildren().add(layout);
        root.getChildren().add(mainMenu);
    }

    public void startGame() {
        root.getChildren().clear();
        maze = new Maze(15, 15);
        pacMan = new PacMan(1, 2, maze.getMaze());
        ghosts = new ArrayList<>();
        soundManager = new SoundManager();
        root.getChildren().add(maze.getRoot());
        root.getChildren().add(pacMan.getPacMan());
        ghosts.add(new Ghost(13, 15, Color.YELLOWGREEN, maze.getMaze(), pacMan));

        for (Ghost ghost : ghosts) {
            root.getChildren().add(ghost.getGhost());
        }

        immortalityLabel = new Label("Inmortalidad: 0");
        immortalityLabel.setFont(new Font("Arial", 20));
        immortalityLabel.setTextFill(Color.YELLOW);
        immortalityLabel.setLayoutX(root.getWidth() - 150);
        immortalityLabel.setLayoutY(8);
        root.getChildren().add(immortalityLabel);
        spawnTimer = new Timeline(new KeyFrame(Duration.seconds(7), e -> spawnGhost()));
        spawnTimer.setCycleCount(Timeline.INDEFINITE);
        spawnTimer.play();
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (now - lastMoveTime >= MOVE_INTERVAL) {
                    pacMan.move(dx, dy);
                    lastMoveTime = now;
                }

                if (now - lastUpdate >= UPDATE_INTERVAL) {
                    for (Ghost ghost : ghosts) {
                        ghost.move(ghosts);
                    }
                    lastUpdate = now;
                }

                if (checkPacManGhostCollision()) {
                    stopGame();
                }

                checkOrbCollision(pacMan);
                updateImmortalityCounter();
            }
        };
        gameTimer.start();
    }


    private void spawnGhost() {
        if (ghosts.size() == 1) {
            Ghost newGhost = new Ghost(13, 15,Color.PINK, maze.getMaze(), pacMan);
            ghosts.add(newGhost);
            root.getChildren().add(newGhost.getGhost());
        }
        else if (ghosts.size() == 2) {
            Ghost newGhost = new Ghost(13, 15,Color.RED, maze.getMaze(), pacMan);
            ghosts.add(newGhost);
            root.getChildren().add(newGhost.getGhost());
        }
        else if (ghosts.size() == 3) {
            Ghost newGhost = new Ghost(13, 15,Color.CYAN, maze.getMaze(), pacMan);
            ghosts.add(newGhost);
            root.getChildren().add(newGhost.getGhost());
        }
    }

    private void checkOrbCollision(PacMan pacMan) {
        for (Circle orb : maze.getOrbs()) {
            double distance = Math.sqrt(Math.pow(pacMan.getPacMan().getCenterX() - orb.getCenterX(), 2) +
                    Math.pow(pacMan.getPacMan().getCenterY() - orb.getCenterY(), 2));

            if (distance < (double) PacManGame.TILE_SIZE / 2 + orb.getRadius()) {
                maze.eatOrb(orb);
                soundManager.playEatSound();
                break;
            }
        }

        for (SpecialOrb specialOrb : maze.getSpecialOrbs()) {
            double distance = Math.sqrt(Math.pow(pacMan.getPacMan().getCenterX() - specialOrb.getOrb().getCenterX(), 2) +
                    Math.pow(pacMan.getPacMan().getCenterY() - specialOrb.getOrb().getCenterY(), 2));

            if (distance < (double) PacManGame.TILE_SIZE / 2 + specialOrb.getOrb().getRadius()) {
                pacMan.eatSpecialOrb();
                maze.eatSpecialOrb(specialOrb);
                soundManager.playEspecialSound();
                break;
            }
        }
    }

    private void updateImmortalityCounter() {
        if (pacMan.isImmortal()) {
            long remainingTime = (IMMORTAL_DURATION - (System.nanoTime() - pacMan.getImmortalStartTime())) / 1_000_000_000;
            immortalityLabel.setText("Inmortalidad: " + remainingTime);

            if (remainingTime <= 0) {
                pacMan.setImmortal(false);
                immortalityLabel.setText("Inmortalidad: 0");
            }
        } else {
            immortalityLabel.setText("Inmortalidad: 0");
        }
    }

    private boolean checkPacManGhostCollision() {
        for (Ghost ghost : ghosts) {
            if (pacMan.getPacMan().getBoundsInParent().intersects(ghost.getBoundsInParent())) {
                if (!pacMan.isImmortal()) {
                    soundManager.playGameOverSound();
                    stopGame();
                    return true;
                } else {
                    pacMan.killGhost(ghost);
                    soundManager.playGhostSound();
                    maze.orbsEaten += 100;
                    maze.updateScore();
                    maze.scoreText.setText("Puntuación: " + maze.orbsEaten);
                }
            }
        }
        return false;
    }

    public void handleKeyPress(KeyCode code) {
        switch (code) {
            case UP:
                dx = 0;
                dy = -1;
                soundManager.playMoveSound();
                break;
            case DOWN:
                dx = 0;
                dy = 1;
                soundManager.playMoveSound();
                break;
            case LEFT:
                dx = -1;
                dy = 0;
                soundManager.playMoveSound();
                break;
            case RIGHT:
                dx = 1;
                dy = 0;
                soundManager.playMoveSound();
                break;
        }
    }

    public void stopGame() {
        gameTimer.stop();
        ghosts.clear();
        showGameOverMenu();
    }

    private void showGameOverMenu() {
        StackPane gameOverMenu = new StackPane();
        gameOverMenu.setPrefSize(root.getWidth(), root.getHeight());
        gameOverMenu.setStyle("-fx-background-color: #1e1e1e;");
        VBox layout = new VBox(60);
        layout.setAlignment(Pos.CENTER);
        layout.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label gameOverLabel = new Label("¡Fin de la partida!");
        gameOverLabel.setFont(new Font("Arial", 48));
        gameOverLabel.setTextFill(Color.WHITE);
        gameOverLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.5), 10, 0.0, 2, 2);");

        Label scoreLabel = new Label("Puntuación: " + maze.orbsEaten);
        scoreLabel.setFont(new Font("Arial", 36));
        scoreLabel.setTextFill(Color.WHITE);

        Button restartButton = new Button("Reiniciar");
        restartButton.setStyle("-fx-font-size: 36px; -fx-background-color: #ffcc00; -fx-text-fill: #000000; -fx-font-weight: bold; -fx-border-radius: 20; -fx-background-radius: 20;");
        restartButton.setPrefSize(300, 80);
        restartButton.setOnAction(e -> restartGame());

        Button exitButton = new Button("Salir");
        exitButton.setStyle("-fx-font-size: 36px; -fx-background-color: #ff6666; -fx-text-fill: #000000; -fx-font-weight: bold; -fx-border-radius: 20; -fx-background-radius: 20;");
        exitButton.setPrefSize(300, 80);
        exitButton.setOnAction(e -> System.exit(0));
        layout.getChildren().addAll(gameOverLabel, scoreLabel, restartButton, exitButton);
        gameOverMenu.getChildren().add(layout);
        root.getChildren().add(gameOverMenu);
    }

    private void restartGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }

        if (spawnTimer != null) {
            spawnTimer.stop();
        }

        root.getChildren().clear();
        maze.orbsEaten = 0;
        maze = new Maze(15, 15);
        pacMan = new PacMan(1, 2, maze.getMaze());
        ghosts.clear();
        ghosts.add(new Ghost(13, 15, Color.RED, maze.getMaze(), pacMan));
        root.getChildren().add(maze.getRoot());
        root.getChildren().add(pacMan.getPacMan());
        for (Ghost ghost : ghosts) {
            root.getChildren().add(ghost.getGhost());
        }

        dx = 0;
        dy = 0;
        startGame();
    }

    public Pane getRoot() {
        return root;
    }
}
