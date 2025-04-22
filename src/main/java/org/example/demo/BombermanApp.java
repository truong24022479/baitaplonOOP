package org.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.dsl.FXGL;

import java.io.IOException;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;


public class BombermanApp extends GameApplication {
    public static Entity player;
    private int[][] map;
    public static final int TILE_SIZE = 32;
    public static final int MAP_WIDTH = 15;
    public static final int MAP_HEIGHT = 15;
    private static final int PLAYER_SPEED = 1;
    private static final int MOVE_ERROR = 10;
    private KeyHandle keyH = new KeyHandle();
    private static GamePlay controller;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAP_WIDTH * TILE_SIZE);
        settings.setHeight(MAP_HEIGHT * TILE_SIZE);
        settings.setTitle("Bomberman Game");
        settings.setVersion("1.0");
    }

    protected void initUI() {
        GameInitializerMap.initUI();
    }

/// /////////////////
//    @Override
//    protected void initGame() {
//        GameInitializerMap.initUI();    // 👈 GỌI TRƯỚC để load FXML và gán controller
//        GameInitializerMap.initGame();  // 👈 Sau đó mới dùng controller
//    }
//    private void initializeMap(){
//        GameInitializerMap.initializeMap();
//    }

    protected void initGame() {
        getGameScene().setBackgroundColor(Color.BLACK);
        map = new int[MAP_HEIGHT][MAP_WIDTH];
        initializeMap();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải file game_play.fxml: " + e.getMessage());
        }


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


                entityBuilder()
                        .type(type)
                        .at(col * TILE_SIZE, row * TILE_SIZE)
                        .viewWithBBox(view)
                        .buildAndAttach();
            }
        }

        // Tao nhan vat nguoi choi
        ImageView playerView = controller.getPlayerImageView();

        playerView.setFitWidth(TILE_SIZE);
        playerView.setFitHeight(TILE_SIZE);
        playerView.setPreserveRatio(false);

        player = entityBuilder()
                .type(EntityType.PLAYER)
                .at(TILE_SIZE, TILE_SIZE)
                .viewWithBBox(playerView)
                .buildAndAttach();


        // Tao ke dich
        Random random = new Random();
        int numOfBalloons = 2;
        for (int i = 0; i < numOfBalloons; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot
            /// ////
            ImageView balloomView = controller.getBalloomImageView();
            Balloon balloon = new Balloon();
            balloon.initMap(map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);;

            balloomView.setFitWidth(TILE_SIZE);
            balloomView.setFitHeight(TILE_SIZE);
            balloomView.setPreserveRatio(true);
            balloomView.setSmooth(true);

            entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(y * TILE_SIZE, x * TILE_SIZE)
                    .viewWithBBox(balloomView)
                    .with(balloon)
                    .buildAndAttach();
        }

        int numOfOneals = 2;
        for (int i = 0; i < numOfOneals; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView onealView = controller.getOnealImageView();
            Oneal oneal = new Oneal();
            oneal.initMap(map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);

            onealView.setFitWidth(TILE_SIZE);
            onealView.setFitHeight(TILE_SIZE);
            onealView.setPreserveRatio(false);

            entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(y * TILE_SIZE, x * TILE_SIZE)
                    .viewWithBBox(onealView)
                    .with(new Oneal())
                    .buildAndAttach();
        }

//        int tileX = (int)(player.getX() / TILE_SIZE);
//        int tileY = (int)(player.getY() / TILE_SIZE);
//        ImageView bombView = new ImageView(getAssetLoader().loadImage("sprites/bomb.png"));
//
//
//        if (map[tileY][tileX] == 0 || (tileX != 1 && tileY != 1) || (tileX != 2 && tileY != 1) || (tileX != 1 && tileY != 2)) {
//            map[tileY][tileX] = 3;
//
//            Entity bomb = FXGL.entityBuilder()
//                    .type(EntityType.BOMB)
//                    .at(tileX * TILE_SIZE, tileY * TILE_SIZE)
//                    .view(bombView)
//                    .with(new Bomb(map, MAP_WIDTH, MAP_HEIGHT, controller))
//                    .buildAndAttach();
//        }



    }

    private void initializeMap() {
        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                if (row == 0 || row == MAP_HEIGHT - 1 || col == 0 || col == MAP_WIDTH - 1) {
                    map[row][col] = 1; // Tường xung quanh
                } else if (row % 2 == 0 && col % 2 == 0) {
                    map[row][col] = 1; // Khối không thể phá hủy
                } else {
                    //map[row][col] = 0;
                    map[row][col] = (Math.random() > 0.7) ? 2 : 0; // Ngẫu nhiên tường gạch hoặc đường đi
                }
            }
        }
        map[1][1] = 0;
        map[1][2] = 0;
        map[2][1] = 0;
    }

//////////////////


    public int[][] getMap() {
    return map;
}

    @Override
    protected void initInput() {
        FXGL.getInput().clearAll(); // Xóa tất cả binding cũ

        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                keyH.upPressed = true;
            }

            @Override
            protected void onActionEnd() {
                keyH.upPressed = false;
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                keyH.downPressed = true;
            }

            @Override
            protected void onActionEnd() {
                keyH.downPressed = false;
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                keyH.leftPressed = true;
            }

            @Override
            protected void onActionEnd() {
                keyH.leftPressed = false;
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                keyH.rightPressed = true;
            }

            @Override
            protected void onActionEnd() {
                keyH.rightPressed = false;
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Place bomb"){
            @Override
            protected void onActionBegin() {
                Bomb.setBomb(player, map, 1, 2);
            }
        }, KeyCode.SPACE);

    }

    @Override
    protected void onUpdate(double tpf) {
        double dx = 0, dy = 0;
        if (keyH.upPressed) dy -= PLAYER_SPEED;
        if (keyH.downPressed) dy += PLAYER_SPEED;
        if (keyH.leftPressed) dx -= PLAYER_SPEED;
        if (keyH.rightPressed) dx += PLAYER_SPEED;
        movePlayer(dx, dy);

        int playerTileX = (int) (player.getX() / TILE_SIZE);
        int playerTileY = (int) (player.getY() / TILE_SIZE);



        FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemy -> {
            int enemyTileX = (int) (enemy.getX() / TILE_SIZE);
            int enemyTileY = (int) (enemy.getY() / TILE_SIZE);

            if (playerTileX == enemyTileX && playerTileY == enemyTileY) {
                removePlayer();
            }
        });
    }

    private void movePlayer(double dx, double dy) {
        double newX = player.getX();
        double newY = player.getY();

        if (dx != 0 && dy == 0) {
            double targetRow = Math.round(newY / TILE_SIZE);
            double alignedY = targetRow * TILE_SIZE;
            if (Math.abs(alignedY - newY) <= MOVE_ERROR) {
                newY = alignedY;
            }
            newX += dx;
        } else if (dy != 0 && dx == 0) {
            double targetCol = Math.round(newX / TILE_SIZE);
            double alignedX = targetCol * TILE_SIZE;
            if (Math.abs(alignedX - newX) <= MOVE_ERROR) {
                newX = alignedX;
            }
            newY += dy;
        } else {
            newX += dx;
            newY += dy;
        }

        int leftTile = (int) (newX / TILE_SIZE);
        int rightTile = (int) ((newX + TILE_SIZE - 1) / TILE_SIZE);
        int topTile = (int) (newY / TILE_SIZE);
        int bottomTile = (int) ((newY + TILE_SIZE - 1) / TILE_SIZE);

        if (leftTile < 0 || rightTile >= MAP_WIDTH || topTile < 0 || bottomTile >= MAP_HEIGHT)
            return;

        for (int ty = topTile; ty <= bottomTile; ty++) {
            for (int tx = leftTile; tx <= rightTile; tx++) {
                if (map[ty][tx] != 0) {
                    return;
                }
            }
        }

        player.setPosition(newX, newY);
    }

    public static void removePlayer() {
        FXGL.getGameWorld().removeEntity(player);
        FXGL.getDialogService().showMessageBox("\uD83D\uDC80 Game Over \uD83D\uDC80\n \uD83C\uDFC6  VICTORY  \uD83C\uDFC6", () -> {
            FXGL.getGameController().exit();
        });
    }

}