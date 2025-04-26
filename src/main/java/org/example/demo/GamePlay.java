package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class GamePlay {
    //public String getBrick;
    @FXML
    private ImageView playerImageView;

    public ImageView getPlayerImageView() {
        return new ImageView(playerImageView.getImage());
    }

    @FXML
    private ImageView enemyImageView;

    public ImageView getEnemyImageView() {
        return new ImageView(enemyImageView.getImage());
    }

    @FXML
    private ImageView wall;

    public ImageView getWall() {
        ImageView view = new ImageView(wall.getImage());
        view.setFitWidth(Enemy.TILE_SIZE);
        view.setFitHeight(Enemy.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView brick;

    public ImageView getBrick() {
        ImageView view = new ImageView(brick.getImage());
        view.setFitWidth(Enemy.TILE_SIZE);
        view.setFitHeight(Enemy.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView grass;

    public ImageView getGrass() {
        ImageView view = new ImageView(grass.getImage());
        view.setFitWidth(Enemy.TILE_SIZE);
        view.setFitHeight(Enemy.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }


    @FXML
    public void initialize() {
        // Nếu cần xử lý gì khi FXML load xong thì viết ở đây
    }
}
