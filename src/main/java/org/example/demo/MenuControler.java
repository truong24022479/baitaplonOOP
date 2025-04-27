package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import com.almasb.fxgl.dsl.FXGL;
import org.example.demo.BombermanApp;

public class MenuControler {
    @FXML
    public Button exitButton;
    @FXML
    public Button startButton;
    @FXML
    public ImageView logoImageView;
    @FXML
    private void startGame() {
        ((BombermanApp) FXGL.getApp()).startGame();
    }

    @FXML
    private void exitGame() {
        FXGL.getGameController().exit();
    }

}