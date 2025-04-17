package org.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;



public class BombermanApp extends GameApplication {
    private Entity player;
    private int[][] map;
    public static final int TILE_SIZE = 32;
    public static final int MAP_WIDTH = 15;
    public static final int MAP_HEIGHT = 15;
    private static final int PLAYER_SPEED = 1;



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
        try {
            // Tải file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();

            // Lấy Controller
            GamePlay controller = loader.getController();
            ImageView playerImageView = controller.getPlayerImageView();
            ImageView enemyImageView = controller.getEnemyImageView();

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
        int numOfBalloons = 5;
        for (int i = 0; i < numOfBalloons; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getEnemyImageView();

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

        int numOfOneals = 1;
        for (int i = 0; i < numOfOneals; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getEnemyImageView();

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

    private void initializeMap() {
        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                if (row == 0 || row == MAP_HEIGHT - 1 || col == 0 || col == MAP_WIDTH - 1) {
                    map[row][col] = 1; // Tường xung quanh
                } else if (row % 2 == 0 && col % 2 == 0) {
                    map[row][col] = 1; // Khối không thể phá hủy
                } else {
                    map[row][col] = (Math.random() > 0.7) ? 2 : 0; // Ngẫu nhiên tường gạch hoặc đường đi
                }
            }
        }
        map[1][1] = 0;
        map[1][2] = 0;
        map[2][1] = 0;
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
    }


    @Override
    protected void onUpdate(double tpf) {
        double dx = 0, dy = 0;
        if (keyH.upPressed) dy -= PLAYER_SPEED;
        if (keyH.downPressed) dy += PLAYER_SPEED;
        if (keyH.leftPressed) dx -= PLAYER_SPEED;
        if (keyH.rightPressed) dx += PLAYER_SPEED;
        movePlayer(dx, dy);
    }

    private void movePlayer(double dx, double dy) {
        double newX = player.getX() + dx;
        double newY = player.getY() + dy;

        int leftTile = (int) (newX / TILE_SIZE);
        int rightTile = (int) ((newX + TILE_SIZE - 1) / TILE_SIZE);
        int topTile = (int) (newY / TILE_SIZE);
        int bottomTile = (int) ((newY + TILE_SIZE - 1) / TILE_SIZE);

        if (leftTile < 0 || rightTile >= MAP_WIDTH || topTile < 0 || bottomTile >= MAP_HEIGHT) {
            return;
        }

        for (int ty = topTile; ty <= bottomTile; ty++) {
            for (int tx = leftTile; tx <= rightTile; tx++) {
                if (map[ty][tx] != 0) {
                    return;
                }
            }
        }

        player.setPosition(newX, newY);
    }

    public int[][] getMap() {
        return map;
    }


    public static void main(String[] args) {
        launch(args);
    }
}