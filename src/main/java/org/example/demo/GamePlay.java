package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.example.demo.BombermanApp;

public class GamePlay {
    @FXML
    private AnchorPane root;
    @FXML
    private ImageView playerImageView;

    public ImageView getPlayerImageView() {
        if (playerImageView == null) {
            System.err.println("LỖI: playerImageView chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(playerImageView.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setPreserveRatio(false);
        return view;
    }

    @FXML
    private ImageView balloomImageView;

    public ImageView getBalloomImageView() {
        if (balloomImageView == null) {
            System.err.println("LỖI: balloomImageView chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(balloomImageView.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setPreserveRatio(false);
        return view;
    }

    @FXML
    private ImageView onealImageView;

    public ImageView getOnealImageView() {
        if (onealImageView == null) {
            System.err.println("LỖI: onealImageView chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(onealImageView.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setPreserveRatio(false);
        return view;
    }

    @FXML
    private ImageView wall;

    public ImageView getWall() {
        if (wall == null) {
            System.err.println("LỖI: wall chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(wall.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView brick;

    public ImageView getBrick() {
        if (brick == null) {
            System.err.println("LỖI: brick chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(brick.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView grass;

    public ImageView getGrass() {
        if (grass == null) {
            System.err.println("LỖI: grass chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(grass.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView bombImageView;

    public ImageView getBombImageView() {
        if (bombImageView == null) {
            System.err.println("LỖI: bombImageView chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(bombImageView.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView portal;

    public ImageView getPortal() {
        if (portal == null) {
            System.err.println("LỖI: portal chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(portal.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setSmooth(false);
        return view;
    }

    @FXML
    public void initialize() {
        System.out.println("GamePlay controller đã được khởi tạo.");
    }

    public void updateScore(int score) {
        System.out.println("Score: " + score);
    }

    public void handlePowerUp(String powerUpType) {
        System.out.println("Nhặt được power-up: " + powerUpType);
    }

}