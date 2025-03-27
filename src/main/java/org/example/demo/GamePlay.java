package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class GamePlay {
    //public String getBrick;
    @FXML
    private ImageView playerImageView;

    public ImageView getPlayerImageView() {
        return playerImageView;
    }

    @FXML
    private ImageView wall;

    public ImageView getWall() {
        return wall;
    }

    @FXML
    private ImageView brick;

    public ImageView getBrick() {
        return brick;
    }

    @FXML
    private ImageView grass;

    public ImageView getGrass() {
        return grass;
    }

    @FXML
    public void initialize() {
        // Nếu cần xử lý gì khi FXML load xong thì viết ở đây
    }
}
