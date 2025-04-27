package org.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.dsl.FXGL;
import java.io.IOException;
import static com.almasb.fxgl.dsl.FXGL.*;

public class BombermanApp extends GameApplication {
    public static Entity player;
    public static int[][] map;
    public static final int TILE_SIZE = 32;
    public static final int MAP_WIDTH = 15;
    public static final int MAP_HEIGHT = 15;
    private static final int PLAYER_SPEED = 1;
    public static final int MOVE_ERROR = 10;
    private final KeyHandle keyH = new KeyHandle();
    private static GamePlay controller;
    private boolean atPortal = false;
    private boolean gameStarted = false;

    public static GamePlay getController() {
        return controller;
    }

    public static void setController(GamePlay controller) {
        BombermanApp.controller = controller;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAP_WIDTH * TILE_SIZE);
        settings.setHeight(MAP_HEIGHT * TILE_SIZE);
        settings.setTitle("Bomberman Game");
        settings.setVersion("1.0");
        settings.setMainMenuEnabled(true);
    }

    @Override
    protected void initGame() {
        if (!gameStarted) {
            System.out.println("initGame() được gọi trước khi gameStarted là true. Bỏ qua.");
            return;
        }
        System.out.println("Bắt đầu initGame()...");
        getGameScene().setBackgroundColor(Color.DEEPPINK);
        getGameWorld().getEntities().forEach(entity -> getGameWorld().removeEntity(entity)); // Xóa tất cả entities hiện có
        getGameScene().clearUINodes(); // Xóa tất cả UI nodes
        // Khởi tạo bản đồ
        GameInitializerMap.initializeMap();

        // Tải game_play.fxml và lấy controller
        try {
            System.out.println("Đang cố gắng tải game_play.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            System.out.println("game_play.fxml tải thành công. Controller: " + controller);
            setController(controller);
        } catch (IOException e) {
            System.err.println("LỖI: Không thể tải file game_play.fxml: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (controller != null) {
            System.out.println("Controller không null. Tiến hành khởi tạo entities từ bản đồ...");
            for (int row = 0; row < MAP_HEIGHT; row++) {
                for (int col = 0; col < MAP_WIDTH; col++) {
                    ImageView view = null;
                    EntityType type = null;

                    if (map != null) {
                        if (map[row][col] == 1) {
                            view = controller.getWall();
                            type = EntityType.WALL;
                        } else if (map[row][col] == 2) {
                            view = controller.getBrick();
                            type = EntityType.BRICK;
                        } else if (map[row][col] == 4) {
                            view = controller.getPortal();
                            type = EntityType.PORTAL;
                        } else if (map[row][col] == 0) {
                            view = controller.getGrass();
                            type = EntityType.GRASS;
                        }

                        if (view != null && type != null) {
                            entityBuilder().type(type)
                                    .at(col * TILE_SIZE, row * TILE_SIZE)
                                    .viewWithBBox(view)
                                    .buildAndAttach();
                        }
                    }
                }
            }

            ImageView playerView = controller.getPlayerImageView();
            if (playerView != null) {
                playerView.setFitWidth(TILE_SIZE);
                playerView.setFitHeight(TILE_SIZE);
                playerView.setPreserveRatio(false);

                player = entityBuilder().type(EntityType.PLAYER)
                        .at(TILE_SIZE, TILE_SIZE)
                        .zIndex(10)
                        .viewWithBBox(playerView)
                        .buildAndAttach();
                System.out.println("Đã tạo người chơi.");
            } else {
                System.err.println("LỖI: playerImageView từ controller là null.");
            }

            System.out.println("Spawning enemies...");
            GameInitializerMap.spawnBalloom(controller);
            GameInitializerMap.spawnOneal(controller);
            System.out.println("Hoàn tất initGame().");
        } else {
            System.err.println("LỖI: GamePlay controller chưa được khởi tạo. Kiểm tra lỗi tải game_play.fxml.");
        }
    }

    private void initializeMap() {
        GameInitializerMap.initializeMap();
    }

    public int[][] getMap() {
        return map;
    }

    @Override
    protected void initInput() {
        FXGL.getInput().clearAll();
        if (!gameStarted) return;

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
        getInput().addAction(new UserAction("Đặt bom") {
            @Override
            protected void onActionBegin() {
                Bomb.setBomb(player, map, 1, Bomb.DELAY_BOMB_TIME);
            }
        }, KeyCode.SPACE);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (!gameStarted) return;
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
                System.out.println("bi cho can");
            }
        });
    }

    private void movePlayer(double dx, double dy) {
        if (!gameStarted) return;
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

        if (leftTile < 0 || rightTile >= MAP_WIDTH || topTile < 0 || bottomTile >= MAP_HEIGHT) return;

        if (map != null) {
            for (int ty = topTile; ty <= bottomTile; ty++) {
                for (int tx = leftTile; tx <= rightTile; tx++) {
                    if (map[ty][tx] != 0 && map[ty][tx] != 4) {
                        return;
                    }
                }
            }
        }

        if (newX / TILE_SIZE == MAP_WIDTH - 2 && newY / TILE_SIZE == MAP_HEIGHT - 2) {
            atPortal = true;
            GG();
        } else atPortal = false;
        player.setPosition(newX, newY);
    }

    public void GG() {
        if (!gameStarted) return;
        if (Bomb.ENEMY_NUMBERS == 0 && atPortal == true) {
            System.out.println("so quai con lai" + Bomb.ENEMY_NUMBERS);
            FXGL.getDialogService().showMessageBox("\uD83C\uDFC6 VICTORY \uD83C\uDFC6", () -> {
                FXGL.getGameController().exit();
            });
        }
    }

    public static void removePlayer() {
        if (!((BombermanApp) FXGL.getApp()).gameStarted) return;
        FXGL.getGameWorld().removeEntity(player);
        FXGL.getDialogService().showMessageBox("\uD83D\uDC80 Đồ ngu đồ ăn hại \uD83D\uDC80", () -> {
            FXGL.getGameController().exit();
        });
    }

    @Override
    protected void onPreInit() {
        FXGL.getSettings().setMainMenuEnabled(true);
    }

    protected Parent createMainMenu() {
        System.out.println("createMainMenu() is called.");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/main_menu.fxml"));
            System.out.println("FXML loaded successfully: " + root);
            return root;
        } catch (IOException e) {
            System.err.println("Error loading main_menu.fxml: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể tải main_menu.fxml");
        }
    }

    public void startGame() {
        System.out.println("startGame() được gọi.");
        gameStarted = true;
        getGameScene().clearUINodes();
        initGame();
        initInput();
    }

    public static void main(String[] args) {
        launch(args);
    }
}