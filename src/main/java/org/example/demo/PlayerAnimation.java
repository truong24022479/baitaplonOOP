package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class PlayerAnimation {
    @FXML
    public static ImageView up;
    @FXML
    public static ImageView up1;
    @FXML
    public static ImageView up2;
    @FXML
    public static ImageView down;
    @FXML
    public static ImageView down1;
    @FXML
    public static ImageView down2;
    @FXML
    public static ImageView left;
    @FXML
    public static ImageView left1;
    @FXML
    public static ImageView left2;
    @FXML
    public static ImageView right;
    @FXML
    public static ImageView right1;
    @FXML
    public static ImageView right2;
    @FXML
    private ImageView playerImageView;

    public ImageView getPlayerImageView() {
        return playerImageView;
    }
    public void initialize() {
        if (down == null) {
            System.out.println("down ImageView is null");
        } else {
            System.out.println("down ImageView initialized");
        }
    }
}
