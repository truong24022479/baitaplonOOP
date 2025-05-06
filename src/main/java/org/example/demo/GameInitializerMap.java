package org.example.demo;

import javafx.scene.image.ImageView;
import com.almasb.fxgl.entity.Entity;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static org.example.demo.BombermanApp.*;

public class GameInitializerMap {

    private static int[][] map;
    private static GamePlay controller;
    private static Entity player;


    public static void initializeMap() {
        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                if (row == 0 || row == MAP_HEIGHT - 1 || col == 0 || col == MAP_WIDTH - 1) {
                    BombermanApp.map[row][col] = 1; // Tường xung quanh
                } else if (row % 2 == 0 && col % 2 == 0) {
                    BombermanApp.map[row][col] = 1; // Khối không thể phá hủy
                } else {
                    BombermanApp.map[row][col] = (Math.random() > 0.7) ? 2 : 0; // Ngẫu nhiên tường gạch hoặc đường đi
                    if (BombermanApp.map[row][col] == 2) Bomb.BRICK_NUMS++;
                }
            }
        }
        BombermanApp.map[1][1] = 0;
        BombermanApp.map[1][2] = 0;
        BombermanApp.map[2][1] = 0;
    }

    public static void spawnBalloom(int numOfBallooms) {
        GamePlay controller = BombermanApp.getController();
        Random random = new Random();

        for (int i = 0; i < numOfBallooms; i++) {
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

    public static void spawnOneal(int numOfOneals) {
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

    public static void spawnDoll(int numOfDolls) {
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

    public static void spawnMinvo(int numOfMinvos) {
        GamePlay controller = BombermanApp.getController();
        Random random = new Random();

        for (int i = 0; i < numOfMinvos; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getMinvoImageView();
            Minvo minvo = new Minvo();
            minvo.initMap(BombermanApp.map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);

            enemyView.setFitWidth(TILE_SIZE);
            enemyView.setFitHeight(TILE_SIZE);
            enemyView.setPreserveRatio(false);

            entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(y * TILE_SIZE, x * TILE_SIZE)
                    .zIndex(10)
                    .viewWithBBox(enemyView)
                    .with(new Minvo())
                    .buildAndAttach();
        }
    }

    public static void spawnPortal(int x, int y, GamePlay controller) {
        ImageView portal = controller.getPortal();
        portal.setFitWidth(TILE_SIZE);
        portal.setFitHeight(TILE_SIZE);
        portal.setPreserveRatio(false);
        BombermanApp.map[y][x] = 4; // Đảo ngược x, y để phù hợp với map (hàng, cột)
        entityBuilder()
                .type(EntityType.PORTAL)
                .at(x * TILE_SIZE, y * TILE_SIZE) // Tọa độ pixel (cột, hàng)
                .zIndex(1) // Đặt zIndex lớn hơn ô cỏ
                .viewWithBBox(portal)
                .buildAndAttach();
        Bomb.portalSpawned = true; // Đánh dấu portal đã xuất hiện
        System.out.println("Portal spawned at: [" + y + "][" + x + "]");
    }

    public static void spawnBuff(int x, int y, GamePlay controller) {
        if (Bomb.remainingBuffsToSpawn <= 0) return;
        ImageView buffImage = controller.getBuffImage();
        buffImage.setFitWidth(TILE_SIZE);
        buffImage.setFitHeight(TILE_SIZE);
        buffImage.setPreserveRatio(false);
        BombermanApp.map[y][x] = 5;
        entityBuilder()
                .type(EntityType.BUFF)
                .at(x * TILE_SIZE, y * TILE_SIZE)
                .zIndex(1)
                .viewWithBBox(buffImage)
                .buildAndAttach();
        Bomb.remainingBuffsToSpawn--;
        new Buff( x, y, null);
    }

    public static void initializeBossMap() {
        Bomb.BRICK_NUMS = 0;
        // Bản đồ đơn giản hơn, ít gạch, không portal
        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                if (row == 0 || row == MAP_HEIGHT - 1 || col == 0 || col == MAP_WIDTH - 1) {
                    BombermanApp.map[row][col] = 1; // Tường viền
                } else {
                    BombermanApp.map[row][col] = 0;
                }
            }
        }
        BombermanApp.map[1][1] = 0;
        BombermanApp.map[1][2] = 0;
        BombermanApp.map[2][1] = 0;
    }
    public static void spawnBoss() {
        GamePlay controller = BombermanApp.getController();
        Random random = new Random();
        int x = 6, y = 6;


//        ImageView bossView = new ImageView(GameInitializerMap.class.getResource("/org/example/demo/sprites/kondoria_dead (1).png").toExternalForm());
//        Boss boss = new Boss();
//        boss.initMap(BombermanApp.map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);
//        bossView.setFitWidth(3 * TILE_SIZE);
//        bossView.setFitHeight(3 * TILE_SIZE);
//        bossView.setPreserveRatio(false);

        entityBuilder()
                .type(EntityType.ENEMY)
                .at(y * TILE_SIZE, x * TILE_SIZE)
                .zIndex(10)
//                .viewWithBBox(bossView)
                .collidable()
                .with(new Boss())
                .buildAndAttach();

        for (int i = 6; i <= 8; i++) {
            for (int j = 6; j <= 8; j++) {
                BombermanApp.map[i][j]=6;
            }
        }
    }
}
