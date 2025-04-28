package org.example.demo;

import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import com.almasb.fxgl.entity.Entity;

import java.io.IOException;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static org.example.demo.BombermanApp.*;

public class GameInitializerMap {

//    private static final int TILE_SIZE = BombermanApp.TILE_SIZE;
//    private static final int MAP_WIDTH = BombermanApp.MAP_WIDTH;
//    private static final int MAP_HEIGHT = BombermanApp.MAP_HEIGHT;

    private static int[][] map;
    private static GamePlay controller;
    private static Entity player;

    private static int numOfOneals = 3;
    private static int numOfballooms = 5;
    private static int numOfDolls = 3;

    public static int getNumOfBallooms() {
        return numOfballooms;
    }

    public static int getNumOfOneals() {
        return numOfOneals;
    }

    public static int getNumOfDolls() {
        return numOfDolls;
    }

//    public GamePlay getController() {
//        return controller;
//    }
//
//    public Entity getPlayer() {
//        return player;
//    }
//
//    public int[][] getMap() {
//        return map;
//    }

    public static void initializeMap() {
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
        //BombermanApp.map[MAP_WIDTH - 2][MAP_HEIGHT - 2] = 4;
        int portalRow, portalCol;
        do {
            Random random = new Random();
            portalRow = random.nextInt(MAP_HEIGHT - 4) + 2; // Tránh khu vực khởi đầu
            portalCol = random.nextInt(MAP_WIDTH - 4) + 2;
        } while (BombermanApp.map[portalRow][portalCol] != 0);
        BombermanApp.map[portalRow][portalCol] = 4;
    }


//    public static void initUI() {
//        try {
//            FXMLLoader loader = new FXMLLoader(GameInitializerMap.class.getResource("/org/example/demo/game_play.fxml"));
//            Parent root = loader.load();
//            controller = loader.getController();
//        } catch (IOException e) {
//            e.printStackTrace(); // 👈 để xem lỗi gì
//            System.out.println("Không thể tải file game_play.fxml: " + e.getMessage());
//        }
//    }

    public static void spawnBalloom() {
//        try {
//            FXMLLoader loader = new FXMLLoader(GameInitializerMap.class.getResource("/org/example/demo/game_play.fxml"));
//            Parent root = loader.load();
//            controller = loader.getController();
//        } catch (IOException e) {
//            throw new RuntimeException("Không thể tải file game_play.fxml: " + e.getMessage());
//        }
        GamePlay controller = BombermanApp.getController();
        Random random = new Random();

        for (int i = 0; i < numOfballooms; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getBalloomImageView();
            Balloom balloom = new Balloom();
            balloom.initMap(map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);

            enemyView.setFitWidth(TILE_SIZE);
            enemyView.setFitHeight(TILE_SIZE);
            enemyView.setPreserveRatio(false);

            entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(y * TILE_SIZE, x * TILE_SIZE)
                    .zIndex(10)
                    .viewWithBBox(enemyView)
                    .with(new Balloom())
                    .buildAndAttach();
        }
    }

    public static void spawnOneal() {
        GamePlay controller = BombermanApp.getController();
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

    public static void spawnDolls () {
        GamePlay controller = BombermanApp.getController();
        Random random = new Random();

        for (int i = 0; i < numOfDolls; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getDollImageView();
            Doll doll = new Doll();
            doll.initMap(map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);

            enemyView.setFitWidth(TILE_SIZE);
            enemyView.setFitHeight(TILE_SIZE);
            enemyView.setPreserveRatio(false);

            entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(y * TILE_SIZE, x * TILE_SIZE)
                    .zIndex(10)
                    .viewWithBBox(enemyView)
                    .with(new Doll())
                    .buildAndAttach();
        }
    }

}
