package org.example.demo;

import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import static com.almasb.fxgl.dsl.FXGL.*;

public class Bomb extends Component {
    private boolean isActivated = false; // Trạng thái kích hoạt
    private boolean isBomberCanEscape = true; // Cho phép nhân vật thoát

    public Bomb(int bombX, int bombY, int tileSize, int tileSize1) {
    }

    public void startActivationTimer() {
        runOnce(() -> {
            isActivated = true;
            isBomberCanEscape = false;
            explode(); // Kích hoạt nổ
        }, Duration.seconds(2)); // Sau 2 giây thì nổ
    }

    private void explode() {
        getEntity().getViewComponent().clearChildren(); // Xóa hình ảnh bomb

        // Hiển thị hiệu ứng nổ
        ImageView explosionView = new ImageView(getAssetLoader().loadImage("sprites/explosion.png"));
        explosionView.setFitWidth(32);
        explosionView.setFitHeight(32);
        explosionView.setPreserveRatio(false);

        getEntity().getViewComponent().addChild(explosionView);

        // Xóa bomb khỏi thế giới sau hiệu ứng
        runOnce(() -> {
            getEntity().removeFromWorld();
        }, Duration.seconds(0.5)); // Hiệu ứng kéo dài 0.5 giây
    }
}
