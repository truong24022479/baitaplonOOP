package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox; // Cần import VBox nếu dùng showBox với nhiều nút
import javafx.geometry.Pos; // Cần import Pos

public class EndGame {

    /**
     * Hiển thị màn hình chiến thắng cuối cùng.
     */
    public static void handleVictory() {
        Button btnHome = new Button("-HOME-"); // Đổi text cho rõ nghĩa
        btnHome.setOnAction(e -> {
            // Khi nhấn Home sau khi thắng, quay trở lại Menu chính
            FXGL.getGameController().gotoMainMenu();
        });

        Button btnExit = new Button("-EXIT GAME-"); // Đổi text cho rõ nghĩa
        btnExit.setOnAction(e -> {
            // Khi nhấn Exit, thoát game
            FXGL.getGameController().exit();
        });

        // Sử dụng showBox với nhiều nút
        FXGL.getDialogService().showBox("\uD83C\uDFC6 VICTORY \uD83C\uDFC6", btnHome, btnExit);

        // Có thể thêm logic xử lý điểm số, hiển thị màn hình victory phức tạp hơn tại đây
        // Nếu cần hiển thị UI phức tạp hơn, bạn có thể tạo một FXGLScene tùy chỉnh
    }

    /**
     * Hiển thị màn hình Game Over khi người chơi thua.
     */
    public static void handleGameOver() {
        // Nút CHƠI LẠI - bắt đầu game mới ngay lập tức
        Button btnReplay = new Button("-REPLAY-");
        btnReplay.setOnAction(e -> {
            FXGL.getGameController().startNewGame(); // Bắt đầu game mới từ level 1
        });
        // Nút quay về sảnh.
        Button btnHome = new Button("-HOME-"); // Đổi text cho rõ nghĩa
        btnHome.setOnAction(e -> {
            // Khi nhấn Home sau khi thua, quay trở lại Menu chính
            FXGL.getGameController().gotoMainMenu();
        });

        Button btnExit = new Button("-EXIT GAME-"); // Đổi text cho rõ nghĩa
        btnExit.setOnAction(e -> {
            // Khi nhấn Exit, thoát game
            FXGL.getGameController().exit();
        });

        // Sử dụng showBox với nhiều nút
        FXGL.getDialogService().showBox("\uD83D\uDC80 OVER-GAME \uD83D\uDC80", btnReplay, btnHome, btnExit);

        // Có thể thêm logic xử lý điểm số, hiển thị màn hình game over phức tạp hơn tại đây
        // Nếu cần hiển thị UI phức tạp hơn, bạn có thể tạo một FXGLScene tùy chỉnh
    }

    // Bạn có thể thêm các phương thức khác liên quan đến việc kết thúc game ở đây
}