package org.example.demo;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
public class EndGame {
    public static void handleVictory() {
        Button btnHome = new Button("Home");
        btnHome.setOnAction(e -> {
            FXGL.getGameController().startNewGame(); // Quay lại màn chơi mới
        });
        Button btnExit = new Button("Exit Game");
        btnExit.setOnAction(e -> {
            FXGL.getGameController().exit(); // Thoát game
        });

        FXGL.getDialogService().showBox("\uD83C\uDFC6 CHIẾN THẮNG \uD83C\uDFC6", btnHome, btnExit);
        // Có thể thêm logic xử lý điểm số, hiển thị màn hình victory phức tạp hơn tại đây
        SoundManager.playVictory();
    }

    public static void handleGameOver() {
        Button btnHome = new Button("Home");
        btnHome.setOnAction(e -> {
            FXGL.getGameController().startNewGame(); // Quay lại màn chơi mới
        });

        Button btnExit = new Button("Exit Game");
        btnExit.setOnAction(e -> {
            FXGL.getGameController().exit(); // Thoát game
        });

        FXGL.getDialogService().showBox("\uD83D\uDC80 Thua rồi gà ơi \uD83D\uDC80", btnHome, btnExit);
        // Có thể thêm logic xử lý điểm số, hiển thị màn hình game over phức tạp hơn tại đây
        SoundManager.playGameOver();
    }

    // Bạn có thể thêm các phương thức khác liên quan đến việc kết thúc game ở đây

}