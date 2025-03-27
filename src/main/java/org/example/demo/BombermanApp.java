package org.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.geometry.Point2D;

import javax.imageio.IIOParam;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import static com.almasb.fxgl.dsl.FXGL.*;

public class BombermanApp extends GameApplication {

    private Entity player;
    private int[][] map;
    private static final int TILE_SIZE = 32;
    private static final int MAP_WIDTH = 15;
    private static final int MAP_HEIGHT = 15;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAP_WIDTH * TILE_SIZE);
        settings.setHeight(MAP_HEIGHT * TILE_SIZE);
        settings.setTitle("Bomberman");
    }


    protected void initUI() {
        try {
            // Tải file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();

            // Lấy Controller
            GamePlay controller = loader.getController();

            player = entityBuilder()
                    .type(EntityType.PLAYER)
                    .at(TILE_SIZE, TILE_SIZE)
                    .viewWithBBox(controller.getPlayerImageView()) // Gán ImageView làm view của Entity
                    .buildAndAttach();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Không thể tải file game_play.fxml: " + e.getMessage());
        }
    }

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.BLACK);
        map = new int[MAP_HEIGHT][MAP_WIDTH];
        initializeMap();

        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                GamePlay controller = null;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/game_play.fxml"));
                try {
                    Parent root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                controller = loader.getController();
                if (map[row][col] == 1) {

                    entityBuilder()
                            .type(EntityType.WALL)
                            .at(col * TILE_SIZE, row * TILE_SIZE)
                            .viewWithBBox(controller.getWall())
                            .buildAndAttach();

                } else if (map[row][col] == 2) {
                    entityBuilder()
                            .type(EntityType.BRICK)
                            .at(col * TILE_SIZE, row * TILE_SIZE)
                            .viewWithBBox(controller.getBrick())
                            .buildAndAttach();
                }else {
                    entityBuilder()
                            .type(EntityType.GRASS)
                            .at(col * TILE_SIZE, row * TILE_SIZE)
                            .viewWithBBox(controller.getGrass())
                            .buildAndAttach();
                }
            }
        }

    }

    private void initializeMap() {
        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                if (row == 0 || row == MAP_HEIGHT - 1 || col == 0 || col == MAP_WIDTH - 1) {
                    map[row][col] = 1;
                } else if (row % 2 == 0 && col % 2 == 0) {
                    map[row][col] = 1;
                } else {
                    map[row][col] = (Math.random() > 0.7) ? 2 : 0;
                }
            }
        }
        map[1][1] = 0;
    }

private void movePlayer(double dx, double dy) {
    // Lấy kích thước bounding box của nhân vật
    // Giả sử nhân vật có kích thước 32×32 (trùng TILE_SIZE)
    // Nếu ảnh của bạn khác 32×32, hãy dùng view.getWidth() / getHeight() hoặc hằng số phù hợp
    double playerWidth = 32;
    double playerHeight = 32;

    // Tính tọa độ mới sau khi di chuyển
    double newX = player.getX() + dx;
    double newY = player.getY() + dy;

    // Tính các chỉ số tile cho 4 góc bounding box:
    // - top-left     (newX, newY)
    // - top-right    (newX + playerWidth - 1, newY)
    // - bottom-left  (newX, newY + playerHeight - 1)
    // - bottom-right (newX + playerWidth - 1, newY + playerHeight - 1)
    // Ở đây, -1 để tránh “văng” ra ngoài tile khi vị trí = bội số của TILE_SIZE
    int leftTile   = (int) (newX / TILE_SIZE);
    int rightTile  = (int) ((newX + playerWidth  - 1) / TILE_SIZE);
    int topTile    = (int) (newY / TILE_SIZE);
    int bottomTile = (int) ((newY + playerHeight - 1) / TILE_SIZE);

    // Kiểm tra ranh giới map
    if (leftTile < 0 || rightTile >= MAP_WIDTH || topTile < 0 || bottomTile >= MAP_HEIGHT) {
        // Nếu bounding box vượt ra ngoài map, không cho di chuyển
        return;
    }

    // Kiểm tra tất cả các tile trong vùng bounding box
    // Nếu bất kỳ tile nào != 0 (tức có vật cản), ta không cho di chuyển
    for (int ty = topTile; ty <= bottomTile; ty++) {
        for (int tx = leftTile; tx <= rightTile; tx++) {
            if (map[ty][tx] != 0) {
                return; // Gặp vật cản => không di chuyển
            }
        }
    }

    // Nếu mọi tile đều trống, cập nhật vị trí
    player.setPosition(newX, newY);
}

    @Override
    protected void initInput() {
        // Sử dụng onKeyDown để di chuyển nhân vật theo từng bước nhỏ
        onKeyDown(KeyCode.W, () -> movePlayer(0, -1));
        onKeyDown(KeyCode.S, () -> movePlayer(0, 1));
        onKeyDown(KeyCode.A, () -> movePlayer(-1, 0));
        onKeyDown(KeyCode.D, () -> movePlayer(1, 0));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
