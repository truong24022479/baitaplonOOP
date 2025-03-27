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
//    private static final double BOMB_EXPLOSION_DELAY = 2.5;
//    private boolean canPlaceBomb = true;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAP_WIDTH * TILE_SIZE);
        settings.setHeight(MAP_HEIGHT * TILE_SIZE);
        settings.setTitle("Bomberman");
        //settings.setEnabledDefaultCameraControls(false);
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

//    private void movePlayer(int dx, int dy) {
//        double newX = player.getX() + dx * MOVE_DISTANCE;
//        double newY = player.getY() + dy * MOVE_DISTANCE;
//        // Cần đảm bảo kiểm tra va chạm và giới hạn map phù hợp với bước di chuyển nhỏ hơn
//        int mapX = (int) (newX / TILE_SIZE);
//        int mapY = (int) (newY / TILE_SIZE);
//
//        if (mapX >= 0 && mapX < MAP_WIDTH && mapY >= 0 && mapY < MAP_HEIGHT && map[mapY][mapX] == 0) {
//            player.setPosition(newX, newY);
//        }
//    }
private void movePlayer(double dx, double dy) {
    // Tính vị trí mới dựa trên lượng di chuyển dx, dy
    double newX = player.getX() + dx;
    double newY = player.getY() + dy;

    // Tính chỉ số ô (tile) tương ứng với vị trí mới
    int tileX = (int) (newX / TILE_SIZE);
    int tileY = (int) (newY / TILE_SIZE);

    // Kiểm tra ranh giới của map
    if (tileX < 0 || tileX >= MAP_WIDTH || tileY < 0 || tileY >= MAP_HEIGHT) {
        return; // Nếu ra ngoài map, không di chuyển
    }

    // Kiểm tra va chạm: di chuyển chỉ được khi ô mới có giá trị 0 (trống)
    if (map[tileY][tileX] == 0) {
        player.setPosition(newX, newY);
    }
}

    private static final int MOVE_DISTANCE = 8;
    private static final double PLAYER_SPEED = 100;// pixel/giây

//    @Override
//    protected void onUpdate(double tpf) {
//        // Giả sử bạn lưu trạng thái di chuyển từ input (ví dụ: dx, dy)
//        double dx = 0, dy = 0;
//        if (onKeyDown(KeyCode.W)) dy -= 1;
//        if (isKeyDown(KeyCode.S)) dy += 1;
//        if (isKeyDown(KeyCode.A)) dx -= 1;
//        if (isKeyDown(KeyCode.D)) dx += 1;
//
//        // Tính khoảng cách di chuyển dựa trên tpf
//        double moveX = dx * PLAYER_SPEED * tpf;
//        double moveY = dy * PLAYER_SPEED * tpf;
//
//        // Kiểm tra va chạm và cập nhật vị trí
//        double newX = player.getX() + moveX;
//        double newY = player.getY() + moveY;
//        // Thêm logic kiểm tra va chạm theo newX, newY nếu cần...
//        player.setPosition(newX, newY);
//    }

    /**
     * cnay dang dung
     */
    @Override
protected void initInput() {
    // Sử dụng onKeyDown để di chuyển nhân vật theo từng bước nhỏ
    onKeyDown(KeyCode.W, () -> movePlayer(0, -1));
    onKeyDown(KeyCode.S, () -> movePlayer(0, 1));
    onKeyDown(KeyCode.A, () -> movePlayer(-1, 0));
    onKeyDown(KeyCode.D, () -> movePlayer(1, 0));
}

    private void movePlayer(int dx, int dy) {
        // Tính toán vị trí mới dựa trên bước di chuyển đã định nghĩa
        double newX = player.getX() + dx * MOVE_DISTANCE;
        double newY = player.getY() + dy * MOVE_DISTANCE;
        // Xác định ô map tương ứng với vị trí mới
        int mapX = (int) (newX / TILE_SIZE);
        int mapY = (int) (newY / TILE_SIZE);

        // Kiểm tra xem vị trí mới có hợp lệ (trong map và không bị chướng ngại vật)
        if (mapX >= 0 && mapX < MAP_WIDTH && mapY >= 0 && mapY < MAP_HEIGHT && map[mapY][mapX] == 0) {
            player.setPosition(newX, newY);
        }
    }

    /**
     * 1
     * @param args
     */
//private boolean upPressed, downPressed, leftPressed, rightPressed;
//@Override
//protected void initInput() {
//    // Khi nhấn phím
//    onKeyDown(KeyCode.W, () -> upPressed = true);
//    onKeyDown(KeyCode.S, () -> downPressed = true);
//    onKeyDown(KeyCode.A, () -> leftPressed = true);
//    onKeyDown(KeyCode.D, () -> rightPressed = true);
//
//    // Khi thả phím
//    onKeyUp(KeyCode.W, () -> upPressed = false);
//    onKeyUp(KeyCode.S, () -> downPressed = false);
//    onKeyUp(KeyCode.A, () -> leftPressed = false);
//    onKeyUp(KeyCode.D, () -> rightPressed = false);
//}
//
//    @Override
//    protected void onUpdate(double tpf) {
//        // tpf: thời gian (giây) giữa các frame
//        double dx = 0, dy = 0;
//        if (upPressed)    dy -= PLAYER_SPEED * tpf;
//        if (downPressed)  dy += PLAYER_SPEED * tpf;
//        if (leftPressed)  dx -= PLAYER_SPEED * tpf;
//        if (rightPressed) dx += PLAYER_SPEED * tpf;
//
//        double newX = player.getX() + dx;
//        double newY = player.getY() + dy;
//
//        // Kiểm tra va chạm theo ô của map
//        int mapX = (int)(newX / TILE_SIZE);
//        int mapY = (int)(newY / TILE_SIZE);
//
//        if (mapX >= 0 && mapX < MAP_WIDTH && mapY >= 0 && mapY < MAP_HEIGHT && map[mapY][mapX] == 0) {
//            player.setPosition(newX, newY);
//        }
//    }

//    private boolean upPressed, downPressed, leftPressed, rightPressed;
//    @Override
//    protected void initInput() {
//        // Khi nhấn phím mũi tên
//        onKeyDown(KeyCode.I,    () -> upPressed    = true);
//        onKeyDown(KeyCode.K,  () -> downPressed  = true);
//        onKeyDown(KeyCode.J,  () -> leftPressed  = true);
//        onKeyDown(KeyCode.L, () -> rightPressed = true);
//
//        // Khi thả phím mũi tên
//        onKeyUp(KeyCode.H,    () -> upPressed    = false);
//        onKeyUp(KeyCode.K,  () -> downPressed  = false);
//        onKeyUp(KeyCode.J,  () -> leftPressed  = false);
//        onKeyUp(KeyCode.L, () -> rightPressed = false);
//    }
//
//    @Override
//    protected void onUpdate(double tpf) {
//        // Tính khoảng cách di chuyển dựa trên thời gian giữa 2 frame (tpf)
//        double dx = 0, dy = 0;
//        if (upPressed)    dy -= PLAYER_SPEED * tpf;
//        if (downPressed)  dy += PLAYER_SPEED * tpf;
//        if (leftPressed)  dx -= PLAYER_SPEED * tpf;
//        if (rightPressed) dx += PLAYER_SPEED * tpf;
//
//        movePlayer(dx, dy);
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
