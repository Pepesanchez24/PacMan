package com.example.pacman;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PacManGame extends Application {

    public static final int TILE_SIZE = 20;
    public static final int WIDTH = 28;
    public static final int HEIGHT = 31;
    private GameManager gameManager;

    @Override
    public void start(Stage primaryStage) {
        gameManager = new GameManager();
        Scene scene = new Scene(gameManager.getRoot(), WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        scene.setOnKeyPressed(event -> gameManager.handleKeyPress(event.getCode()));
        primaryStage.setTitle("Pac-Man");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        Platform.runLater(() -> gameManager.getRoot().requestFocus());
        gameManager.showMainMenu();
        gameManager.getRoot().setOnMouseClicked(event -> {
            if (gameManager.getRoot().getChildren().size() == 1) {
                gameManager.startGame();
            }
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
