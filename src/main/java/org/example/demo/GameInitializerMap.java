package org.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.io.IOException;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class GameInitializerMap {

    private static final int TILE_SIZE = 32;
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

    public static void spawnBalloom() {
        try {
            FXMLLoader loader = new FXMLLoader(GameInitializerMap.class.getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải file game_play.fxml: " + e.getMessage());
        }
        Random random = new Random();
        int numOfBalloons = 5;
        for (int i = 0; i < numOfBalloons; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getBalloomImageView();

            enemyView.setFitWidth(TILE_SIZE);
            enemyView.setFitHeight(TILE_SIZE);
            enemyView.setPreserveRatio(false);

            entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(y * TILE_SIZE, x * TILE_SIZE)
                    .viewWithBBox(enemyView)
                    .with(new Balloon() {{
                        initMap(map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);
                    }})
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
        int numOfOneals = 2;
        for (int i = 0; i < numOfOneals; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getOnealImageView();

            enemyView.setFitWidth(TILE_SIZE);
            enemyView.setFitHeight(TILE_SIZE);
            enemyView.setPreserveRatio(false);

            entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(y * TILE_SIZE, x * TILE_SIZE)
                    .viewWithBBox(enemyView)
                    .with(new Oneal() {{
                        initMap(map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);
                    }})
                    .buildAndAttach();
        }

    }
}
