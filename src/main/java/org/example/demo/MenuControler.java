package org.example.demo;

import javafx.fxml.FXML;
import static com.almasb.fxgl.dsl.FXGL.*;

public class MenuControler {

    @FXML
    private void startGame() {
        getGameScene().clearUINodes(); // Xóa menu
        getGameController().startNewGame(); // Chuyển sang gameplay
    }

    @FXML
    private void exitGame() {
        getGameController().exit(); // Thoát game
    }
}