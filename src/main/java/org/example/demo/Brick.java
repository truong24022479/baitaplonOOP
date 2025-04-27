package org.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick {
    private int x, y; // Tọa độ của gạch
    private boolean isDestroyed; // Trạng thái gạch
    private boolean hasItem; // Gạch có chứa vật phẩm hay không
    private ImageView brickView; // Hiển thị hình ảnh gạch

    public Brick(int x, int y, boolean hasItem) {
        this.x = x;
        this.y = y;
        this.isDestroyed = false;
        this.hasItem = hasItem;

        // Tải hình ảnh gạch lên.
        Image brickImage = new Image("/org/example/demo/assets/brick.png");
        brickView = new ImageView(brickImage);

        // Cài đặt kích thước và vị trí cho gạch
        brickView.setFitWidth(32); // Kích thước ô gạch (ví dụ: TILE_SIZE)
        brickView.setFitHeight(32);
        brickView.setX(x);
        brickView.setY(y);
    }

    // Phương thức phá hủy gạch
    public void destroy() {
        if (!isDestroyed) {
            isDestroyed = true;
            System.out.println("Gạch bị phá hủy tại: " + x + ", " + y);
            if (hasItem) {
                revealItem(); // Hiển thị vật phẩm nếu có
            }
        }
    }

    // Hiển thị vật phẩm khi gạch bị phá hủy
    private void revealItem() {
        System.out.println("Vật phẩm được phát hiện tại: " + x + ", " + y);
        // Logic để thêm vật phẩm vào trò chơi
    }

    // Phương thức hiển thị gạch trên màn hình
    public void render() {
        if (!isDestroyed) {
            System.out.println("Hiển thị gạch tại: " + x + ", " + y);
        }
    }

    // Getter để lấy trạng thái gạch
    public boolean isDestroyed() {
        return isDestroyed;
    }

    // Getter để lấy hình ảnh gạch
    public ImageView getBrickView() {
        return brickView;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
