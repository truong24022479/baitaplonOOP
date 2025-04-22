package org.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import com.almasb.fxgl.entity.Entity;

import java.io.IOException;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class GameInitializerMap {

    private static final int TILE_SIZE = BombermanApp.TILE_SIZE;
    private static final int MAP_WIDTH = BombermanApp.MAP_WIDTH;
    private static final int MAP_HEIGHT = BombermanApp.MAP_HEIGHT;

    private static int[][] map;
    private static GamePlay controller;
    private static Entity player;

    private static int numOfOneals = 3;
    private static int numOfBalloons = 5;

    public static int getNumOfBalloons() {
        return numOfBalloons;
    }

    public static int getNumOfOneals() {
        return numOfOneals;
    }

    public GamePlay getController() {
        return controller;
    }

    public Entity getPlayer() {
        return player;
    }

    public int[][] getMap() {
        return map;
    }

    public static void initializeMap() {
        for (int row = 0; row < BombermanApp.MAP_HEIGHT; row++) {
            for (int col = 0; col < BombermanApp.MAP_WIDTH; col++) {
                if (row == 0 || row == BombermanApp.MAP_HEIGHT - 1 || col == 0 || col == BombermanApp.MAP_WIDTH - 1) {
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

    public static void spawnBalloom() {
        try {
            FXMLLoader loader = new FXMLLoader(GameInitializerMap.class.getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải file game_play.fxml: " + e.getMessage());
        }
        Random random = new Random();

        for (int i = 0; i < numOfBalloons; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getBalloomImageView();
            Balloon balloon = new Balloon();
            balloon.initMap(map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);

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
        }
    }

    public static void spawnOneal() {
        try {
            FXMLLoader loader = new FXMLLoader(GameInitializerMap.class.getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải file game_play.fxml: " + e.getMessage());
        }
        Random random = new Random();

        for (int i = 0; i < numOfOneals; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getOnealImageView();
            Oneal oneal = new Oneal();
            oneal.initMap(map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);

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
        }

    }
}
