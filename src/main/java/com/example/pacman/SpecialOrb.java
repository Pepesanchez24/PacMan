package com.example.pacman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SpecialOrb {
    private final Circle orb;

    public SpecialOrb(double x, double y) {
        orb = new Circle(x, y, 7);
        orb.setFill(Color.YELLOW);
    }

    public Circle getOrb() {
        return orb;
    }
}
