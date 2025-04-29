package org.example.demo;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;

public class ViewMenu extends FXGLMenu {

    public ViewMenu() {
        super(MenuType.MAIN_MENU);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/main_menu.fxml"));
            AnchorPane menuRoot = loader.load();

            // Tải ảnh programmatically
            Image backgroundImage = new Image(getClass().getResourceAsStream("/textures/view_logo.jpg"));
            if (backgroundImage.isError()) {
                System.err.println("Lỗi tải ảnh: " + backgroundImage.getException());
            } else {
                BackgroundSize backgroundSize = new BackgroundSize(400, 400, true, true, true, false);
                BackgroundImage backgroundImg = new BackgroundImage(backgroundImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        backgroundSize);
                menuRoot.setBackground(new Background(backgroundImg));
            }

            // Thêm root của FXML vào content root của FXGLMenu
            getContentRoot().getChildren().add(menuRoot);

            // Đảm bảo menu chiếm toàn bộ không gian bằng cách set giá trị
            getContentRoot().prefWidthProperty().setValue((double) FXGL.getAppWidth());
            getContentRoot().prefHeightProperty().setValue((double) FXGL.getAppHeight());

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không thể tải main_menu.fxml");
        }
    }
}