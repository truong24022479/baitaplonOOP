package org.example.demo;

import javafx.scene.input.KeyCode;

public class KeyHandle {
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public void handleKeyPress(KeyCode code) {
        if (code == KeyCode.W) upPressed = true;
        if (code == KeyCode.S) downPressed = true;
        if (code == KeyCode.A) leftPressed = true;
        if (code == KeyCode.D) rightPressed = true;
    }

    public void handleKeyRelease(KeyCode code) {
        if (code == KeyCode.W) upPressed = false;
        if (code == KeyCode.S) downPressed = false;
        if (code == KeyCode.A) leftPressed = false;
        if (code == KeyCode.D) rightPressed = false;
    }
}
