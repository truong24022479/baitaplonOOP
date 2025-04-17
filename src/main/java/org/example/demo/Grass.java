package org.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Grass {
    private int x, y; // Tọa độ của cỏ
    private int size; // Kích thước của ô cỏ
    private ImageView grassView; // Hiển thị hình ảnh cỏ

    /**
     * Constructor có tham số, khởi tạo tọa độ và kích cỡ của cỏ từ giá trị ban đầu.
     * @param x tọa độ của vật
     * @param y tọa độ của vật
     * @param size kích cỡ cở grass
     */
    public Grass(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;

        // Tải hình ảnh cỏ từ file.
        Image grassImage = new Image("/org/example/demo/assets/grass.png");
        grassView = new ImageView(grassImage);

        // Cài đặt kích thước và vị trí cho cỏ
        grassView.setFitWidth(size);
        grassView.setFitHeight(size);
        grassView.setX(x);
        grassView.setY(y);
    }

    // Phương thức render cỏ trên màn hình
    public void render() {
        System.out.println("Hiển thị cỏ tại: " + x + ", " + y);
    }

    // Getter để lấy ImageView của cỏ (nếu cần dùng cho FXGL)
    public ImageView getGrassView() {
        return grassView;
    }


}
