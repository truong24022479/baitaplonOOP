package org.example.demo;

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
