package org.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.IOException;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static org.example.demo.Buff.availableBuffs;
import static org.example.demo.Buff.resetBuff;

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

    public static int numOfOneals = 1;
    public static int numOfBallooms = 1;
    public static int numOfDolls = 1;
    public static int numOfMinvos = 1;

    public static int getNumOfBallooms() {
        return numOfBallooms;
    }

    public static int getNumOfOneals() {
        return numOfOneals;
    }

    public static int getNumOfDolls() {
        return numOfDolls;
    }

    public static int getNumOfMinvos() {
        return numOfMinvos;
    }

    public static int ENEMY_NUMBERS_LEFT = numOfBallooms + numOfMinvos + numOfDolls + numOfOneals;


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAP_WIDTH * TILE_SIZE);
        settings.setHeight(MAP_HEIGHT * TILE_SIZE);
        settings.setTitle("Bomberman Game");
        settings.setVersion("1.0");

        settings.setMainMenuEnabled(true); // Bật chức năng menu chính
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu() {
                return new ViewMenu();
            }
        });
    }

    protected void initUI() {
        //GameInitializerMap.initUI();
    }

    protected void initGame() {
        getGameScene().setBackgroundColor(Color.DEEPPINK);
        map = new int[MAP_HEIGHT][MAP_WIDTH];
        if (level == 2) {
            initializeBossMap();
        } else {
            initializeMap();
            resetBuff();
            Buff.createBuff();
        }
        //initializeMap();

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


        Bomb.bombAnimation = new BombAnimation(map);
        PlayerAnimation playerAnimation = new PlayerAnimation();
        playerAnimation.initialize();
//        player = new Player(TILE_SIZE, MAP_WIDTH, MAP_HEIGHT, Player.MOVE_ERROR);
        player.setMap(map);
        player.spawnPlayer();
//        player.initInput();
        if (level == 1) {
            GameInitializerMap.spawnBalloom(numOfBallooms);
            GameInitializerMap.spawnOneal(numOfOneals);
            GameInitializerMap.spawnDoll(numOfDolls);
            GameInitializerMap.spawnMinvo(numOfMinvos);
        } else {
           // spawnBoss();
            ENEMY_NUMBERS_LEFT = 1;
        }
    }

    private void initializeBossMap() {
        GameInitializerMap.initializeBossMap();
    }

    private void initializeMap() {
        GameInitializerMap.initializeMap();
    }

    protected void initInput() {
        // Khởi tạo input cho người chơi một lần duy nhất
        player = new Player(TILE_SIZE, MAP_WIDTH, MAP_HEIGHT, Player.MOVE_ERROR);
        player.initInput();
//        PlayerAnimation playerAnimation = new PlayerAnimation();
//        playerAnimation.initialize();
    }

    @Override
    protected void onUpdate(double tpf) {
        player.onUpdate(tpf);
    }
////////////////////////////////////////////////////////
    static int level = 1;
////////////////////////////////////////////////////////
    public static void GG() {
//        if (ENEMY_NUMBERS_LEFT <= 0 && Player.atPortal == true && availableBuffs.size() > 0) {
//            getDialogService().showMessageBox("You need to find " + availableBuffs.size() + " buffs left.", () -> {
//            });
//        }
        if (ENEMY_NUMBERS_LEFT <= 0 && Player.atPortal == true && availableBuffs.size() == 0 && level == 1) {
           // System.out.println("so quai con lai" + ENEMY_NUMBERS_LEFT);
            FXGL.getDialogService().showMessageBox("\uD83C\uDFC6 VICTORY \uD83C\uDFC6 \n BOSS", () -> {
            });
            level++;
            startNewLevel();

            System.out.println("level "+level);
        }
//        if (level == 2) {
//            FXGL.getDialogService().showMessageBox("\uD83C\uDFC6 VICTORY \uD83C\uDFC6 \n BOSS", () -> {
//                FXGL.getGameController().exit();
//            });
//        }
    }

    private static void startNewLevel() {
        // Gọi onRemoved()
        FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemy -> {
            if (enemy.hasComponent(Balloom.class)) {
                enemy.getComponent(Balloom.class).onRemoved();
            } else if (enemy.hasComponent(Oneal.class)) {
                enemy.getComponent(Oneal.class).onRemoved();
            } else if (enemy.hasComponent(Doll.class)) {
                enemy.getComponent(Doll.class).onRemoved();
            } else if (enemy.hasComponent(Minvo.class)) {
                enemy.getComponent(Minvo.class).onRemoved();
            }
        });
        // Xóa tất cả thực thể hiện tại
        FXGL.getGameWorld().removeEntities(FXGL.getGameWorld().getEntitiesByType(
                EntityType.PLAYER, EntityType.ENEMY, EntityType.BOMB, EntityType.BRICK,
                EntityType.GRASS, EntityType.WALL, EntityType.PORTAL, EntityType.BUFF
        ));

        // Khởi tạo lại game
        BombermanApp app = (BombermanApp) FXGL.getApp();
        app.initGame();
    }

    public static void removePlayer() {
        //SoundManager.playPlayerDeath();
//        getDialogService().showMessageBox("\uD83D\uDC80 Đồ ngu đồ ăn hại \uD83D\uDC80", () -> {
//            getGameController().exit();
//        });
        //handleGameOver();
    }
}