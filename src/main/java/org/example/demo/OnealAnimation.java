package org.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OnealAnimation {
    public static ImageView left1;
    public static ImageView left2;
    public static ImageView left3;
    public static ImageView right1;
    public static ImageView right2;
    public static ImageView right3;
    public static ImageView die;

    public void initializeImageView() {
        left1 = createImageView("/org/example/demo/sprites/oneal_left1 (1).png");
        left2 = createImageView("/org/example/demo/sprites/oneal_left2 (1).png");
        left3 = createImageView("/org/example/demo/sprites/oneal_left3 (1).png");
        right1 = createImageView("/org/example/demo/sprites/oneal_right1 (1).png");
        right2 = createImageView("/org/example/demo/sprites/oneal_right2 (1).png");
        right3 = createImageView("/org/example/demo/sprites/oneal_right3 (1).png");
        die = createImageView("/org/example/demo/sprites/oneal_dead (1).png");
    }

    public ImageView createImageView(String imageName) {
        try {
            Image image = new Image(getClass().getResource(imageName).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(32);
            imageView.setFitHeight(32);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (Exception e) {
            System.out.println("Error creating :" + imageName + " " + e.getMessage());
            return null;
        }
    }

}
