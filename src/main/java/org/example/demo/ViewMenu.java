package org.example.demo;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewMenu extends FXGLMenu {
    public ViewMenu() {
        super(MenuType.MAIN_MENU);
        getContentRoot().setStyle("-fx-background-color: black;");
        getContentRoot().setPrefWidth(FXGL.getAppWidth());
        getContentRoot().setPrefHeight(FXGL.getAppHeight());

        Text title = FXGL.getUIFactoryService().newText("Bomberman Game", 48);
        title.setFill(Color.WHITE);
        title.setStyle(
                "-fx-font-size: 60px; " + // Tăng kích thước font
                        "-fx-fill: white; " + // Màu chữ trắng (để thấy rõ hiệu ứng viền)
                        "-fx-stroke: #FFD700; " + // Màu viền vàng (vàng nhạt)
                        "-fx-stroke-width: 3; " + // Độ dày của viền
                        "-fx-effect: dropshadow(gaussian, #FFD700, 10, 0, 0, 0);" // Hiệu ứng bóng mờ (glow)
        );

        Button newGameButton = new Button("NEW GAME");
        newGameButton.setOnAction(e ->{
            BombermanApp.resetGame(); // Gọi resetGame() trước khi bắt đầu mới
            FXGL.getGameController().startNewGame();
        });
        styleButton(newGameButton, Color.RED);

        Button optionsButton = new Button("OPTIONS");
        optionsButton.setOnAction(e -> showGuide());
        styleButton(optionsButton, Color.RED);

        Button exitButton = new Button("EXIT");
        exitButton.setOnAction(e -> FXGL.getGameController().exit());
        styleButton(exitButton, Color.RED);

        VBox menuBox = new VBox(20, title, newGameButton, optionsButton, exitButton);
        menuBox.setAlignment(Pos.CENTER);
        VBox.setMargin(title, new Insets(0, 0, 50, 0));

        StackPane root = new StackPane(menuBox);
        root.setAlignment(Pos.CENTER);
        StackPane.setMargin(menuBox, new Insets(20));

        getContentRoot().getChildren().clear();
        getContentRoot().getChildren().add(root);
    }

    private void styleButton(Button button, Color backgroundColor) {
        button.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: " + toRgbString(backgroundColor) + "; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 2px; " +
                        "-fx-padding: 10px 20px;"
        );
    }

    private String toRgbString(Color c) {
        return String.format("rgba(%d, %d, %d, %f)",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255),
                c.getOpacity());
    }


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

        Label textLabel = new Label(guideText);
        textLabel.setWrapText(true);
        textLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        });

        VBox dialogVBox = new VBox(20, textLabel, okButton);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setPadding(new Insets(20));
        dialogVBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-border-color: white; -fx-border-width: 1px;");

        Scene dialogScene = new Scene(dialogVBox);
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(FXGL.getPrimaryStage());
        dialogStage.setTitle("Hướng dẫn chơi");
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }
}