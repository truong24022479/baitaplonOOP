//package org.example.demo;
//
//import com.almasb.fxgl.app.scene.FXGLMenu;
//import com.almasb.fxgl.app.scene.MenuType;
//import com.almasb.fxgl.dsl.FXGL;
//import com.almasb.fxgl.ui.FXGLButton;
//import javafx.geometry.Pos;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Text;
//
//public class GameOverMenu extends FXGLMenu {
//
//    public GameOverMenu() {
//        super(MenuType.GAME_OVER);
//
//        Text gameOverText = new Text("Game Over");
//        gameOverText.setStyle("-fx-font-size: 48px; -fx-fill: white;");
//
//        FXGLButton playAgainBtn = new FXGLButton("Chơi Lại");
//        playAgainBtn.setOnAction(e -> FXGL.getGameController().startNewGame());
//
//        FXGLButton exitBtn = new FXGLButton("Thoát Game");
//        exitBtn.setOnAction(e -> FXGL.getGameController().exitApp());
//
//        VBox menuBox = new VBox(20, gameOverText, playAgainBtn, exitBtn);
//        menuBox.setAlignment(Pos.CENTER);
//        menuBox.setPrefWidth(FXGL.getAppWidth());
//        menuBox.setPrefHeight(FXGL.getAppHeight());
//
//        getContentRoot().getChildren().addAll(menuBox);
//    }
//}