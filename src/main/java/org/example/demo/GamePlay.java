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
    private ImageView balloomImageView;

    public ImageView getBalloomImageView() {
        return new ImageView(balloomImageView.getImage());
    }

    @FXML
    private ImageView onealImageView;

    public ImageView getOnealImageView() {
        return new ImageView(onealImageView.getImage());
    }

    @FXML
    private ImageView dollImageView;

    public ImageView getDollImageView() {
        return new ImageView(dollImageView.getImage());
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
    private ImageView bombImageView;

    public ImageView getBombImageView() {
        ImageView view = new ImageView(bombImageView.getImage());
        view.setFitWidth(Enemy.TILE_SIZE);
        view.setFitHeight(Enemy.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView portal;

    public ImageView getPortal(){
        ImageView view = new ImageView(portal.getImage());
        view.setFitWidth(Enemy.TILE_SIZE);
        view.setFitHeight(Enemy.TILE_SIZE);
        view.setSmooth(false);
        return view;

    }

    @FXML
    private ImageView buffImage;

    public ImageView getBuffImage(){
        ImageView view = new ImageView(buffImage.getImage());
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