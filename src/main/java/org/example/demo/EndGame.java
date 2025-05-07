package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;    // Cần cho màu sắc (cho màu nền game và có thể cho nút nếu muốn)
import javafx.util.Duration;     // Cần cho TimerAction

// Các import không cần thiết nếu chỉ dùng các tính năng cơ bản của showBox
// import javafx.scene.layout.VBox;
// import javafx.geometry.Pos;
// import javafx.scene.text.Text;      // KHÔNG CẦN NỮA
// import javafx.scene.text.Font;      // KHÔNG CẦN NỮA
// import javafx.scene.text.FontWeight;// KHÔNG CẦN NỮA


public class EndGame {

    // Màu nền mặc định của game mà EndGame sẽ khôi phục về.
    private static final Color GAME_MAIN_BACKGROUND_COLOR = Color.DEEPPINK;

    // Hàm style chung cho các nút trong dialog kết thúc game (giữ nguyên)
    private static void styleEndGameButtonRed(Button button) {
        button.setStyle(
                "-fx-background-color: #B22222; " + // Màu đỏ Firebrick
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: #8B0000; " +     // Màu viền đỏ sậm (DarkRed)
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #CD5C5C; " + // Màu đỏ IndianRed khi hover
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: #8B0000; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        ));
        button.setOnMouseExited(e -> styleEndGameButtonRed(button));
    }

    /**
     * Chơi hiệu ứng nhấp nháy màn hình chiến thắng.
     */
    private static void playVictoryFlashEffect(Runnable onFinished) {
        var gameScene = FXGL.getGameScene();
        FXGL.getGameTimer().runOnceAfter(() -> {
            gameScene.setBackgroundColor(Color.GOLD);
            FXGL.getGameTimer().runOnceAfter(() -> {
                gameScene.setBackgroundColor(Color.LIGHTYELLOW);
                FXGL.getGameTimer().runOnceAfter(() -> {
                    gameScene.setBackgroundColor(Color.WHITE);
                    FXGL.getGameTimer().runOnceAfter(() -> {
                        gameScene.setBackgroundColor(GAME_MAIN_BACKGROUND_COLOR);
                        if (onFinished != null) {
                            onFinished.run();
                        }
                    }, Duration.seconds(0.15));
                }, Duration.seconds(0.15));
            }, Duration.seconds(0.15));
        }, Duration.seconds(0.1));
    }


    /**
     * Hiển thị màn hình chiến thắng cuối cùng.
     */
    public static void handleVictory() {
        playVictoryFlashEffect(() -> {
            Button btnHome = new Button("-HOME-");
            styleEndGameButtonRed(btnHome); // Style nút
            btnHome.setOnAction(e -> {
                FXGL.getGameScene().setBackgroundColor(GAME_MAIN_BACKGROUND_COLOR);
                FXGL.getGameController().gotoMainMenu();
            });

            Button btnExit = new Button("-EXIT GAME-");
            styleEndGameButtonRed(btnExit); // Style nút
            btnExit.setOnAction(e -> {
                FXGL.getGameController().exit();
            });

            // Sử dụng String cho thông báo VICTORY như ban đầu
            FXGL.getDialogService().showBox("\uD83C\uDFC6 VICTORY \uD83C\uDFC6", btnHome, btnExit);
        });
    }

    /**
     * Hiển thị màn hình Game Over khi người chơi thua.
     */
    public static void handleGameOver() {
        Button btnReplay = new Button("-REPLAY-");
        styleEndGameButtonRed(btnReplay); // Style nút
        btnReplay.setOnAction(e -> {
            FXGL.getGameScene().setBackgroundColor(GAME_MAIN_BACKGROUND_COLOR);
            FXGL.getGameController().startNewGame();
        });

        Button btnHome = new Button("-HOME-");
        styleEndGameButtonRed(btnHome); // Style nút
        btnHome.setOnAction(e -> {
            FXGL.getGameScene().setBackgroundColor(GAME_MAIN_BACKGROUND_COLOR);
            FXGL.getGameController().gotoMainMenu();
        });

        Button btnExit = new Button("-EXIT GAME-");
        styleEndGameButtonRed(btnExit); // Style nút
        btnExit.setOnAction(e -> {
            FXGL.getGameController().exit();
        });

        // Sử dụng String cho thông báo GAME OVER như ban đầu
        // Emoji có thể hơi khác so với phiên bản bạn gửi, bạn có thể điều chỉnh lại nếu cần
        FXGL.getDialogService().showBox("\uD83D\uDC79 GAME OVER \uD83D\uDC79", btnReplay, btnHome, btnExit);
    }
}