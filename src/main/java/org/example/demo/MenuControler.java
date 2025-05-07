package org.example.demo;

import javafx.fxml.FXML;
import static com.almasb.fxgl.dsl.FXGL.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class MenuControler {

    @FXML
    private void startGame() {
        getGameScene().clearUINodes(); // Xóa menu
        getGameController().startNewGame(); // Chuyển sang gameplay
    }

    @FXML
    private void showGuide() {
        String guideText = """
                Hướng dẫn chơi Bomberman:

                Mục tiêu: Tiêu diệt tất cả kẻ địch trên bản đồ.

                Điều khiển:
                - Sử dụng các phím mũi tên hoặc WASD để di chuyển.
                - Nhấn phím Space để đặt bom.

                Cách chơi:
                - Đặt bom để phá hủy các khối gạch (Brick) và tiêu diệt kẻ địch.
                - Thu thập các vật phẩm tăng sức mạnh ẩn sau các khối gạch bị phá hủy.
                - Cẩn thận tránh xa vụ nổ của bom bạn đặt và bom của kẻ địch.
                - Khi tất cả kẻ địch bị tiêu diệt và bạn chạm vào Cổng (Portal)
                 (xuất hiện sau khi phá hết Brick ở một số màn), bạn sẽ chiến thắng.

                Các loại kẻ địch:
                - Balloom
                - Oneal
                - Doll
                - Minvo

                Chúc bạn chơi game vui vẻ!
                """;

        // Sử dụng Alert để hiển thị hướng dẫn (đơn giản)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hướng dẫn chơi");
        alert.setHeaderText(null);

        Label content = new Label(guideText);
        content.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(400); // Giới hạn chiều cao nếu nội dung quá dài

        alert.getDialogPane().setContent(scrollPane);
        alert.showAndWait();

        // Hoặc bạn có thể sử dụng getDialogService().showCustomDialog() để tùy chỉnh giao diện hơn
        /*
        Label textLabel = new Label(guideText);
        textLabel.setWrapText(true);
        textLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> getDialogService().close());

        VBox dialogContent = new VBox(20, textLabel, okButton);
        dialogContent.setAlignment(javafx.geometry.Pos.CENTER);
        dialogContent.setPadding(new javafx.geometry.Insets(20));
        dialogContent.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 1px;");

        getDialogService().showCustomDialog(dialogContent);
        */
    }

    @FXML
    private void exitGame() {
        getGameController().exit(); // Thoát game
    }
}