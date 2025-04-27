package org.example.demo;
<<<<<<< HEAD
import com.almasb.fxgl.entity.Entity;
import javafx.scene.image.ImageView;
import java.util.Random;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
public class GameInitializerMap {
    private static final int TILE_SIZE = BombermanApp.TILE_SIZE;
    private static final int MAP_WIDTH = BombermanApp.MAP_WIDTH;
    private static final int MAP_HEIGHT = BombermanApp.MAP_HEIGHT;
    private static int numOfOneals = 3;
    private static int numOfBalloons = 5;

    public static int getNumOfBalloons() {
        return numOfBalloons;
    }

    public static int getNumOfOneals() {
        return numOfOneals;
    }

    public static void initializeMap() {
        BombermanApp.map = new int[MAP_HEIGHT][MAP_WIDTH]; // Khởi tạo map ở đây
        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                if (row == 0 || row == MAP_HEIGHT - 1 || col == 0 || col == MAP_WIDTH - 1) {
                    BombermanApp.map[row][col] = 1; // Tường xung quanh
                } else if (row % 2 == 0 && col % 2 == 0) {
                    BombermanApp.map[row][col] = 1; // Khối không thể phá hủy
                } else {
                    BombermanApp.map[row][col] = (Math.random() > 0.7) ? 2 : 0; // Ngẫu nhiên tường gạch hoặc đường đi
                }
            }
        }
        BombermanApp.map[1][1] = 0;
        BombermanApp.map[1][2] = 0;
        BombermanApp.map[2][1] = 0;
        BombermanApp.map[MAP_WIDTH - 2][MAP_HEIGHT - 2] = 4;
    }

    public static void spawnBalloom(GamePlay controller) { // Nhận controller làm tham số
        if (controller == null) {
            System.err.println("LỖI: GamePlay controller chưa được khởi tạo khi gọi spawnBalloom().");
            return;
        }
        Random random = new Random();

        for (int i = 0; i < numOfBalloons; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getBalloomImageView();
            if (enemyView != null) {
                enemyView.setFitWidth(TILE_SIZE);
                enemyView.setFitHeight(TILE_SIZE);
                enemyView.setPreserveRatio(false);

                entityBuilder()
                        .type(EntityType.ENEMY)
                        .at(y * TILE_SIZE, x * TILE_SIZE)
                        .zIndex(10)
                        .viewWithBBox(enemyView)
                        .with(new Balloon())
                        .buildAndAttach();
            } else {
                System.err.println("LỖI: balloomImageView từ controller là null.");
            }
        }
    }

    public static void spawnOneal(GamePlay controller) { // Nhận controller làm tham số
        if (controller == null) {
            System.err.println("LỖI: GamePlay controller chưa được khởi tạo khi gọi spawnOneal().");
            return;
        }
        Random random = new Random();

        for (int i = 0; i < numOfOneals; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getOnealImageView();
            if (enemyView != null) {
                enemyView.setFitWidth(TILE_SIZE);
                enemyView.setFitHeight(TILE_SIZE);
                enemyView.setPreserveRatio(false);

                entityBuilder()
                        .type(EntityType.ENEMY)
                        .at(y * TILE_SIZE, x * TILE_SIZE)
                        .zIndex(10)
                        .viewWithBBox(enemyView)
                        .with(new Oneal())
                        .buildAndAttach();
            } else {
                System.err.println("LỖI: onealImageView từ controller là null.");
            }
        }
    }
}
=======

import com.almasb.fxgl.entity.component.Component;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.io.IOException;
import java.util.Random;

public class GameInitializerMap {

    private static final int TILE_SIZE = 40;
    private static final int MAP_WIDTH = 15;
    private static final int MAP_HEIGHT = 13;

    private static int[][] map;
    private static GamePlay controller;
    private static Entity player;

    public GamePlay getController() {
        return controller;
    }

    public Entity getPlayer() {
        return player;
    }

    public int[][] getMap() {
        return map;
    }



    public static void initUI() {
        try {
            FXMLLoader loader = new FXMLLoader(GameInitializerMap.class.getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace(); // 👈 để xem lỗi gì
            System.out.println("Không thể tải file game_play.fxml: " + e.getMessage());
        }
    }

//////////sua init game
    public static void initGame() {
        if (controller == null) {
            System.out.println("⚠️ Controller is null! Bạn đã gọi initUI() chưa?");
            return;
        }

        FXGL.getGameScene().setBackgroundColor(Color.BLACK);

        map = new int[MAP_HEIGHT][MAP_WIDTH];
        initializeMap();

        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                ImageView view;
                EntityType type;

                if (map[row][col] == 1) {
                    view = controller.getWall();
                    type = EntityType.WALL;
                } else if (map[row][col] == 2) {
                    view = controller.getBrick();
                    type = EntityType.BRICK;
                } else {
                    view = controller.getGrass();
                    type = EntityType.GRASS;
                }

                FXGL.entityBuilder()
                        .type(type)
                        .at(col * TILE_SIZE, row * TILE_SIZE)
                        .viewWithBBox(view)
                        .buildAndAttach();
            }
        }

        ImageView playerView = controller.getPlayerImageView();
        playerView.setFitWidth(TILE_SIZE);
        playerView.setFitHeight(TILE_SIZE);
        playerView.setPreserveRatio(false);

        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(TILE_SIZE, TILE_SIZE)
                .viewWithBBox(playerView)
                .buildAndAttach();

        spawnEnemies();
    }

    public static void initializeMap() {
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
        map[1][1] = map[1][2] = map[2][1] = 0;
    }

/// //////////////

    private static void spawnEnemies() {
        Random random = new Random();
        int numOfBalloons = 5;

        for (int i = 0; i < numOfBalloons; i++) {
            spawnEnemy(random, new Balloon());
        }

        spawnEnemy(random, new Oneal());
    }

    private static void spawnEnemy(Random random, Object aiComponent) {
        int x, y;
        do {
            x = random.nextInt(MAP_HEIGHT);
            y = random.nextInt(MAP_WIDTH);
        } while (map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1));

        ImageView enemyView = controller.getEnemyImageView();
        enemyView.setFitWidth(TILE_SIZE);
        enemyView.setFitHeight(TILE_SIZE);
        enemyView.setPreserveRatio(false);

        FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(y * TILE_SIZE, x * TILE_SIZE)
                .viewWithBBox(enemyView)
                .with((Component) aiComponent)
                .buildAndAttach();
    }

}
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
