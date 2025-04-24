package org.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerAnimation {
    public static ImageView up;
    public static ImageView up1;
    public static ImageView up2;
    public static ImageView down;
    public static ImageView down1;
    public static ImageView down2;
    public static ImageView left;
    public static ImageView left1;
    public static ImageView left2;
    public static ImageView right;
    public static ImageView right1;
    public static ImageView right2;

    public void initialize() {
        // Khởi tạo các ImageView
        up = new ImageView();
        up1 = new ImageView();
        up2 = new ImageView();
        down = new ImageView();
        down1 = new ImageView();
        down2 = new ImageView();
        left = new ImageView();
        left1 = new ImageView();
        left2 = new ImageView();
        right = new ImageView();
        right1 = new ImageView();
        right2 = new ImageView();

        // Gán hình ảnh cho các ImageView
        loadImage(up, "/org/example/demo/sprites/player_up.png");
        loadImage(up1, "/org/example/demo/sprites/player_up_1.png");
        loadImage(up2, "/org/example/demo/sprites/player_up_2.png");
        loadImage(down, "/org/example/demo/sprites/player_down.png");
        loadImage(down1, "/org/example/demo/sprites/player_down_1.png");
        loadImage(down2, "/org/example/demo/sprites/player_down_2.png");
        loadImage(left, "/org/example/demo/sprites/player_left.png");
        loadImage(left1, "/org/example/demo/sprites/player_left_1.png");
        loadImage(left2, "/org/example/demo/sprites/player_left_2.png");
        loadImage(right, "/org/example/demo/sprites/player_right.png");
        loadImage(right1, "/org/example/demo/sprites/player_right_1.png");
        loadImage(right2, "/org/example/demo/sprites/player_right_2.png");

        // Log để kiểm tra
        logImageView("up", up);
        logImageView("up1", up1);
        logImageView("up2", up2);
        logImageView("down", down);
        logImageView("down1", down1);
        logImageView("down2", down2);
        logImageView("left", left);
        logImageView("left1", left1);
        logImageView("left2", left2);
        logImageView("right", right);
        logImageView("right1", right1);
        logImageView("right2", right2);
    }

    private void loadImage(ImageView imageView, String path) {
        try {
            // Kiểm tra xem tài nguyên có tồn tại không
            if (getClass().getResource(path) == null) {
                System.out.println("Resource not found: " + path);
                return;
            }
            Image image = new Image(getClass().getResource(path).toExternalForm());
            if (image.isError()) {
                System.out.println("Error loading image: " + path);
            } else {
                imageView.setImage(image);
            }
        } catch (Exception e) {
            System.out.println("Exception loading image " + path + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void logImageView(String name, ImageView imageView) {
        if (imageView == null) {
            System.out.println(name + " ImageView is null");
        } else {
            System.out.println(name + " ImageView initialized");
            if (imageView.getImage() == null) {
                System.out.println(name + " ImageView has no image");
            } else {
                System.out.println(name + " ImageView has image: " + imageView.getImage().getUrl());
            }
        }
    }
}