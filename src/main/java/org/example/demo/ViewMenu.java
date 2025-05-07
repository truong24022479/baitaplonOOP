package org.example.demo;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane; // Import Pane nếu bạn dùng trực tiếp rootPane
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color; // Vẫn cần nếu styleButton gốc dùng nó
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewMenu extends FXGLMenu {
    public ViewMenu() {
        super(MenuType.MAIN_MENU);
        // Giữ nguyên các cài đặt gốc của getContentRoot()
        getContentRoot().setStyle("-fx-background-color: black;");
        getContentRoot().setPrefWidth(FXGL.getAppWidth());
        getContentRoot().setPrefHeight(FXGL.getAppHeight());

        Text title = FXGL.getUIFactoryService().newText("Bomberman Game", 48);
        title.setFill(Color.WHITE); // Giữ lại setFill gốc
        title.setStyle(
                "-fx-font-size: 60px; " +
                        // "-fx-fill: white; " + // Đã có setFill ở trên
                        "-fx-stroke: #FFD700; " +
                        "-fx-stroke-width: 3; " +
                        "-fx-effect: dropshadow(gaussian, #FFD700, 10, 0, 0, 0);"
        );
        // Bạn có thể thêm animation cho title ở đây nếu muốn, như đã thảo luận trước đó.

        Button newGameButton = new Button("NEW GAME");
        newGameButton.setOnAction(e ->{
            FXGL.getGameController().startNewGame();
        });
        styleButtonLikeEndGame(newGameButton); // Sử dụng hàm style mới

        Button optionsButton = new Button("OPTIONS");
        optionsButton.setOnAction(e -> showGuide());
        styleButtonLikeEndGame(optionsButton); // Sử dụng hàm style mới

        Button exitButton = new Button("EXIT");
        exitButton.setOnAction(e -> FXGL.getGameController().exit());
        styleButtonLikeEndGame(exitButton); // Sử dụng hàm style mới

        // Giữ nguyên cấu trúc VBox và StackPane gốc của bạn
        VBox menuBox = new VBox(20, title, newGameButton, optionsButton, exitButton);
        menuBox.setAlignment(Pos.CENTER);
        VBox.setMargin(title, new Insets(0, 0, 50, 0));

        StackPane root = new StackPane(menuBox); // StackPane con này chứa VBox
        root.setAlignment(Pos.CENTER);
        StackPane.setMargin(menuBox, new Insets(20)); // Margin của VBox bên trong StackPane con

        getContentRoot().getChildren().clear();
        getContentRoot().getChildren().add(root); // Thêm StackPane con vào Pane gốc của menu
    }

    // Đổi tên hàm styleButton để phản ánh việc nó làm nút giống EndGame
    // và cập nhật nội dung CSS
    private void styleButtonLikeEndGame(Button button) {
        // Lấy style từ EndGame (màu nền đỏ, chữ trắng, viền đỏ sậm, bo góc)
        // Giữ nguyên font-size và padding từ styleButton gốc của ViewMenu để kích thước nút không đổi
        button.setStyle(
                "-fx-background-color: #B22222; " + // Màu đỏ Firebrick
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +         // Chữ đậm
                        "-fx-font-size: 16px; " +           // GIỮ NGUYÊN font-size từ styleButton gốc
                        "-fx-border-color: #8B0000; " +     // Màu viền đỏ sậm (DarkRed)
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 8px; " +        // Bo góc (giá trị 8px hoặc giá trị bạn thích)
                        "-fx-background-radius: 8px; " +    // Bo nền cho khớp viền
                        "-fx-padding: 10px 20px;"          // GIỮ NGUYÊN padding từ styleButton gốc
        );
        // Hiệu ứng khi hover (giống EndGame)
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #CD5C5C; " + // Màu đỏ IndianRed khi hover
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 16px; " +
                        "-fx-border-color: #8B0000; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 8px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 10px 20px;"
        ));
        // Khi chuột rời đi, đặt lại style gốc
        button.setOnMouseExited(e -> styleButtonLikeEndGame(button));
    }

    // Hàm toRgbString không còn được sử dụng trong styleButtonLikeEndGame nữa
    // vì chúng ta đã chuyển sang dùng mã màu HEX trực tiếp.
    /*
    private String toRgbString(Color c) {
        return String.format("rgba(%d, %d, %d, %f)",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255),
                c.getOpacity());
    }
    */

    private void showGuide() {
        // ... (code của showGuide giữ nguyên) ...
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
        // Áp dụng style mới cho nút OK trong dialog hướng dẫn
        styleButtonLikeEndGame(okButton);
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