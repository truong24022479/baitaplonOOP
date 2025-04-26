package org.example.demo;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
public class GamePlay {
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
    private ImageView wall;

    public ImageView getWall() {
        ImageView view = new ImageView(wall.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE); // Sử dụng TILE_SIZE từ BombermanApp
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView brick;

    public ImageView getBrick() {
        ImageView view = new ImageView(brick.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView grass;

    public ImageView getGrass() {
        ImageView view = new ImageView(grass.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView bombImageView;

    public ImageView getBombImageView() {
        ImageView view = new ImageView(bombImageView.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView portal;

    public ImageView getPortal() {
        ImageView view = new ImageView(portal.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    public void initialize() {
        // Nếu cần xử lý gì khi FXML load xong thì viết ở đây
    }
}