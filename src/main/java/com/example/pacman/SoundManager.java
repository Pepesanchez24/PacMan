package com.example.pacman;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {
    private MediaPlayer eatSound;
    private MediaPlayer moveSound;
    private MediaPlayer gameOverSound;
    private MediaPlayer especialSound;
    private MediaPlayer ghostSound;

    public SoundManager() {
        try {
            URL eatSoundUrl = getClass().getResource("/sounds/comer.mp3");
            URL moveSoundUrl = getClass().getResource("/sounds/mover.mp3");
            URL gameOverSoundUrl = getClass().getResource("/sounds/gameOver.mp3");
            URL especialSoundUrl = getClass().getResource("/sounds/orbeEspecial.mp3");
            URL ghostSoundUrl = getClass().getResource("/sounds/comerFantasma.mp3");

            if (eatSoundUrl != null && moveSoundUrl != null && gameOverSoundUrl != null && especialSoundUrl != null && ghostSoundUrl != null) {
                eatSound = new MediaPlayer(new Media(eatSoundUrl.toString()));
                moveSound = new MediaPlayer(new Media(moveSoundUrl.toString()));
                gameOverSound = new MediaPlayer(new Media(gameOverSoundUrl.toString()));
                especialSound = new MediaPlayer(new Media(especialSoundUrl.toString()));
                ghostSound = new MediaPlayer(new Media(ghostSoundUrl.toString()));
                eatSound.setVolume(0.5);
                moveSound.setVolume(0.5);
                gameOverSound.setVolume(0.5);
                especialSound.setVolume(0.5);
                ghostSound.setVolume(0.5);
            } else {
                System.err.println("Error: No se encontraron los archivos de sonido.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar los sonidos: " + e.getMessage());
        }
    }

    public void playEatSound() {
        if (eatSound != null) {
            eatSound.stop();
            eatSound.play();
        } else {
            System.err.println("Error: El sonido de comer no está cargado.");
        }
    }

    public void playMoveSound() {
        if (moveSound != null) {
            moveSound.stop();
            moveSound.play();
        } else {
            System.err.println("Error: El sonido de mover no está cargado.");
        }
    }

    public void playGameOverSound() {
        if (gameOverSound != null) {
            gameOverSound.stop();
            gameOverSound.play();
        } else {
            System.err.println("Error: El sonido de fin de juego no está cargado.");
        }
    }

    public void playEspecialSound() {
        if (especialSound != null) {
            especialSound.stop();
            especialSound.play();
        } else {
            System.err.println("Error: El sonido especial no está cargado.");
        }
    }

    public void playGhostSound() {
        if (ghostSound != null) {
            ghostSound.stop();
            ghostSound.play();
        } else {
            System.err.println("Error: El sonido de fantasma no está cargado.");
        }
    }
}
