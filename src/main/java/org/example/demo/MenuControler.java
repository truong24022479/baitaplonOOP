package org.example.demo;

import javafx.fxml.FXML;
<<<<<<< HEAD
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
=======
import static com.almasb.fxgl.dsl.FXGL.*;

public class MenuControler {

    @FXML
    private void startGame() {
        getGameScene().clearUINodes(); // Xóa menu
        getGameController().startNewGame(); // Chuyển sang gameplay
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
    }

    @FXML
    private void exitGame() {
<<<<<<< HEAD
        FXGL.getGameController().exit();
    }

=======
        getGameController().exit(); // Thoát game
    }
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
}