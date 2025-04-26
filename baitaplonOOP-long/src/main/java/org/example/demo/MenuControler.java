package org.example.demo;
import javafx.fxml.FXML;
import static com.almasb.fxgl.dsl.FXGL.*;
public class MenuControler {
    private BombermanApp getBombermanApp() {
        return (BombermanApp) getApp(); // Gọi getApp() không có đối số và ép kiểu
    }

    @FXML
    private void startGame() {
        getBombermanApp().startGame(); // Gọi phương thức startGame trong BombermanApp
    }

    @FXML
    private void exitGame() {
        getGameController().exit(); // Thoát game
    }
}