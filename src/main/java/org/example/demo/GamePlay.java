package org.example.demo;

<<<<<<< HEAD
import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.example.demo.BombermanApp;

public class GamePlay {
    @FXML
    private AnchorPane root;
=======
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class GamePlay {
    //public String getBrick;
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
    @FXML
    private ImageView playerImageView;

    public ImageView getPlayerImageView() {
<<<<<<< HEAD
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
=======
        return new ImageView(playerImageView.getImage());
    }

    @FXML
    private ImageView enemyImageView;

    public ImageView getEnemyImageView() {
        return new ImageView(enemyImageView.getImage());
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
    }

    @FXML
    private ImageView wall;

    public ImageView getWall() {
<<<<<<< HEAD
        if (wall == null) {
            System.err.println("LỖI: wall chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(wall.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
=======
        ImageView view = new ImageView(wall.getImage());
        view.setFitWidth(Enemy.TILE_SIZE);
        view.setFitHeight(Enemy.TILE_SIZE);
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView brick;

    public ImageView getBrick() {
<<<<<<< HEAD
        if (brick == null) {
            System.err.println("LỖI: brick chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(brick.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
=======
        ImageView view = new ImageView(brick.getImage());
        view.setFitWidth(Enemy.TILE_SIZE);
        view.setFitHeight(Enemy.TILE_SIZE);
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
        view.setSmooth(false);
        return view;
    }

    @FXML
    private ImageView grass;

    public ImageView getGrass() {
<<<<<<< HEAD
        if (grass == null) {
            System.err.println("LỖI: grass chưa được inject!");
            return null;
        }
        ImageView view = new ImageView(grass.getImage());
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
=======
        ImageView view = new ImageView(grass.getImage());
        view.setFitWidth(Enemy.TILE_SIZE);
        view.setFitHeight(Enemy.TILE_SIZE);
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
        view.setSmooth(false);
        return view;
    }

<<<<<<< HEAD
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
=======

    @FXML
    public void initialize() {
        // Nếu cần xử lý gì khi FXML load xong thì viết ở đây
    }
}
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
