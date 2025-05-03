package org.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.entity.Entity;

import com.almasb.fxgl.dsl.FXGL;

import java.io.IOException;

import static com.almasb.fxgl.dsl.FXGL.*;
import static org.example.demo.Bomb.explosionRadius;

/// ///////////////////long
public class BombermanApp extends GameApplication {
    public static Player player;
    public static int[][] map;
    public static final int TILE_SIZE = 32;
    public static final int MAP_WIDTH = 15;
    public static final int MAP_HEIGHT = 15;
    private static GamePlay controller;

    public int[][] getMap() {
        return map;
    }

    public static GamePlay getController() {
        return controller;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAP_WIDTH * TILE_SIZE);
        settings.setHeight(MAP_HEIGHT * TILE_SIZE);
        settings.setTitle("Bomberman Game");
        settings.setVersion("1.0");
    }

    protected void initUI() {
        //GameInitializerMap.initUI();
    }

    protected void initGame() {
        getGameScene().setBackgroundColor(Color.DEEPPINK);
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
                } else if (map[row][col] == 4) {
                    view = controller.getPortal();
                    type = EntityType.PORTAL;
                } else {
                    view = controller.getGrass();
                    type = EntityType.GRASS;
                }

                entityBuilder().type(type)
                        .at(col * TILE_SIZE, row * TILE_SIZE)
                        .viewWithBBox(view)
                        .buildAndAttach();
            }
        }

        PlayerAnimation playerAnimation = new PlayerAnimation();
        playerAnimation.initialize();

//        BombAnimation bombAnimation = new BombAnimation();
//        bombAnimation.initialize();
        Bomb.bombAnimation = new BombAnimation(map, explosionRadius);

        player = new Player(TILE_SIZE, MAP_WIDTH, MAP_HEIGHT, Player.MOVE_ERROR);
        player.setMap(map);
        player.spawnPlayer();
        player.initInput();

        GameInitializerMap.spawnBalloom(GameInitializerMap.getNumOfBallooms());
        GameInitializerMap.spawnOneal(GameInitializerMap.getNumOfOneals());
        GameInitializerMap.spawnDoll(GameInitializerMap.getNumOfDolls());
        GameInitializerMap.spawnMinvo(GameInitializerMap.getNumOfMinvos());
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.ENEMY, EntityType.BOMB) {

            protected void onCollisionBegin(Entity enemy, Entity bomb) {
                Enemy enemyComponent = enemy.getComponent(Enemy.class);
                if (enemyComponent != null) {
                    enemyComponent.isMoving = false; // Dừng di chuyển
                    // Đặt lại vị trí vào ô hiện tại
                    enemy.setPosition(Math.round(enemy.getX() / TILE_SIZE) * TILE_SIZE,
                            Math.round(enemy.getY() / TILE_SIZE) * TILE_SIZE);
                    // Kích hoạt chọn hướng mới
                    if (enemyComponent instanceof Balloom) {
                        ((Balloom) enemyComponent).moveBalloom();
                    } else if (enemyComponent instanceof Doll) {
                        ((Doll) enemyComponent).moveDoll();
                    } else if (enemyComponent instanceof Oneal) {
                        ((Oneal) enemyComponent).moveOneal();
                    } else if (enemyComponent instanceof Minvo) {
                        ((Minvo) enemyComponent).moveMinvo();
                    }
                }
            }
        });
    }

    private void initializeMap() {
        GameInitializerMap.initializeMap();
    }

    @Override
    protected void onUpdate(double tpf) {
        player.onUpdate(tpf);
    }

    public static void GG() {
        if (Bomb.ENEMY_NUMBERS_LEFT == 0 && Player.atPortal == true) {
            System.out.println("so quai con lai" + Bomb.ENEMY_NUMBERS_LEFT);
            FXGL.getDialogService().showMessageBox("\uD83C\uDFC6 VICTORY \uD83C\uDFC6", () -> {
                FXGL.getGameController().exit();
            });
        }
    }

    public static void removePlayer() {
        //FXGL.getGameWorld().removeEntity(player);
//        FXGL.getDialogService().showMessageBox("\uD83D\uDC80 Đồ ngu đồ ăn hại \uD83D\uDC80", () -> {
//            FXGL.getGameController().exit();
//        });
//        SoundManager.playPlayerDeath();
        getDialogService().showMessageBox("\uD83D\uDC80 Đồ ngu đồ ăn hại \uD83D\uDC80", () -> {
            getGameController().exit();
        });
    }
}