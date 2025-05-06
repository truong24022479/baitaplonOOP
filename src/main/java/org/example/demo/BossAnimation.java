package org.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BossAnimation {
    public static ImageView left1;
    public static ImageView left2;
    public static ImageView left3;
    public static ImageView right1;
    public static ImageView right2;
    public static ImageView right3;
    public static ImageView die;

    public void initializeImageView() {
        left1 = createImageView("/org/example/demo/sprites/kondoria_left1 (1).png");
        left2 = createImageView("/org/example/demo/sprites/kondoria_left2 (1).png");
        left3 = createImageView("/org/example/demo/sprites/kondoria_left3 (1).png");
        right1 = createImageView("/org/example/demo/sprites/kondoria_right1 (1).png");
        right2 = createImageView("/org/example/demo/sprites/kondoria_right2 (1).png");
        right3 = createImageView("/org/example/demo/sprites/kondoria_right3 (1).png");
        die = createImageView("/org/example/demo/sprites/kondoria_dead (1).png");
    }

    public ImageView createImageView(String imageName) {
        try {
            Image image = new Image(getClass().getResource(imageName).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(96);
            imageView.setFitHeight(96);
            imageView.setPreserveRatio(false);
            imageView.setSmooth(false);
            return imageView;
        } catch (Exception e) {
            System.out.println("Error creating :" + imageName + " " + e.getMessage());
            return null;
        }
    }

}

//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//
//public class BossAnimation {
//    public static ImageView left1;
//    public static ImageView left2;
//    public static ImageView left3;
//    public static ImageView right1;
//    public static ImageView right2;
//    public static ImageView right3;
//    public static ImageView die;
//
//    public void initializeImageView() {
//        left1 = loadImageView("/org/example/demo/sprites/kondoria_left1 (1).png");
//        left2 = loadImageView("/org/example/demo/sprites/kondoria_left2 (1).png");
//        left3 = loadImageView("/org/example/demo/sprites/kondoria_left3 (1).png");
//        right1 = loadImageView("/org/example/demo/sprites/kondoria_right1 (1).png");
//        right2 = loadImageView("/org/example/demo/sprites/kondoria_right2 (1).png");
//        right3 = loadImageView("/org/example/demo/sprites/kondoria_right3 (1).png");
//        die = loadImageView("/org/example/demo/sprites/kondoria_dead (1).png");
//    }
//
//    private ImageView loadImageView(String path) {
//        try {
//            return new ImageView(getClass().getResource(path).toExternalForm());
//        } catch (Exception e) {
//            System.out.println("Error loading image: " + path + " " + e.getMessage());
//            return null;
//        }
//    }
//}

