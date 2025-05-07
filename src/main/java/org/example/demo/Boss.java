package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.example.demo.Bomb.*;

public class Boss extends Enemy {
    private Entity boss;
    protected static int x;
    protected static int y;
    private static IntegerProperty health = new SimpleIntegerProperty(20);
    private static ProgressBar healthBar;

    private ImageView view;
    private ImageView[] leftFrames;
    private ImageView[] rightFrames;
    private boolean useLeftFrames = true;

    private int frameIndex = 0;
    private double frameTimer = 0;
    private final double FRAME_DURATION = 0.5;

    private static final double BOMB_SPAWN = 5;
    private double bombTimer = 0;

    public void onAdded() {
        boss = getEntity();
//        view = new ImageView(getClass().getResource("/org/example/demo/sprites/kondoria_dead.png").toExternalForm());
//        view.setFitHeight(3 * TILE_SIZE);
//        view.setFitWidth(3 * TILE_SIZE);
//        view.setPreserveRatio(true);
//        boss.getViewComponent().addChild(view);

        BossAnimation anim = new BossAnimation();
        anim.initializeImageView();
        leftFrames = new ImageView[]{BossAnimation.left1, BossAnimation.left2, BossAnimation.left3};
        rightFrames = new ImageView[]{BossAnimation.right1, BossAnimation.right2, BossAnimation.right3};

//        for (ImageView imgage : leftFrames) {
//            imgage.setFitWidth(TILE_SIZE * 3);
//            imgage.setFitHeight(TILE_SIZE * 3);
//        }
//        for (ImageView image : rightFrames) {
//            image.setFitWidth(TILE_SIZE * 3);
//            image.setFitHeight(TILE_SIZE * 3);
//        }

        view = new ImageView(leftFrames[0].getImage());
        view.setFitHeight(3 * TILE_SIZE);
        view.setFitWidth(3 * TILE_SIZE);
        view.setPreserveRatio(false);
        view.setSmooth(false);
        boss.getViewComponent().addChild(view);

        BombermanApp app = (BombermanApp) FXGL.getApp();
        initMap(app.getMap(), app.TILE_SIZE, app.MAP_WIDTH, app.MAP_HEIGHT);
    }

    public void onUpdate(double tpf) {
        frameTimer += tpf;
        if (frameTimer >= FRAME_DURATION) {
            frameIndex = (frameIndex + 1) % 3;
            frameTimer = 0;

            if (useLeftFrames) {
                view.setImage(leftFrames[frameIndex].getImage());
            } else {
                view.setImage(rightFrames[frameIndex].getImage());
            }
            if (frameIndex == 0) {
                useLeftFrames = !useLeftFrames;
            }
        }

        bombTimer += tpf;
        if (bombTimer >= BOMB_SPAWN) {
            spawnBomb();
            bombTimer = 0;
        }

        if (getHealth() <= 0) {
            bossDie();
        }
    }

    public void spawnBomb() {
        int x, y;
        Random random = new Random();
        do {
            x = random.nextInt(MAP_HEIGHT);
            y = random.nextInt(MAP_WIDTH);
        } while (BombermanApp.map[x][y] != 0);

        Bomb bomb = new Bomb(x, y, DELAY_BOMB_TIME, boss, map);
        bombAnimation.showBombAnimation(x * TILE_SIZE, y * TILE_SIZE);
        bomb.activate(DELAY_BOMB_TIME);
    }

    public static void spawnEnemy() {
        if (getHealth() <= 15) {
            GameInitializerMap.spawnBalloom(2);
        }
        if (getHealth() <= 10) {
            GameInitializerMap.spawnOneal(2);
        }
        if (getHealth() <= 5) {
            GameInitializerMap.spawnMinvo(2);
        }
    }

    //    public void bossDie() {
//        Image deadImage = new Image(getClass().getResource("/org/example/demo/sprites/kondoria_dead (1).png").toExternalForm());
//        view.setImage(deadImage);
//
//        // Cho hiệu ứng tồn tại 0.5s rồi xoá khỏi map
//        FXGL.getGameTimer().runOnceAfter(() -> {
//            getEntity().removeFromWorld();
//        }, Duration.seconds(0.5));
//    }
    public void bossDie() {
        Image deadImage = new Image(getClass().getResource("/org/example/demo/sprites/kondoria_dead (1).png").toExternalForm());
        view.setImage(deadImage);
        for (int i = 6; i <= 8; i++) {
            for (int j = 6; j <= 8; j++) {
                BombermanApp.map[i][j]=0;
            }
        }

        if (healthBar != null) {
            getGameScene().removeUINode(healthBar);
            healthBar = null; // Clear the reference
        }

        FXGL.getGameTimer().runOnceAfter(() -> {
            if (getEntity() != null && getEntity().isActive()) {
                getEntity().removeFromWorld();
            } else {
                System.err.println("Cảnh báo: Entity của Boss là null hoặc không hoạt động trong bossDie");
            }
        }, Duration.seconds(0.5));
    }


    // Thêm getter và setter cho sức khỏe
    public IntegerProperty healthProperty() {
        return health;
    }

    public static int getHealth() {
        return health.get();
    }

    public void setHealth(int value) {
        health.set(value);
    }

    public void decreaseHealth(int amount) {
        setHealth(getHealth() - amount);
    }

    public static void bossHealthBar() {
        // Thêm thanh máu cho boss
        healthBar = new ProgressBar();
        Entity bossEntity = FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).stream()
                .filter(e -> e.hasComponent(Boss.class))
                .findFirst()
                .orElse(null);
        if (bossEntity != null) {
            Boss boss = bossEntity.getComponent(Boss.class);
            if (boss != null) {
                // Sử dụng Bindings để tạo binding với healthProperty
                healthBar.progressProperty().bind(Bindings.createDoubleBinding(
                        () -> boss.getHealth() / 20.0,
                        boss.healthProperty()
                ));
                healthBar.setPrefWidth(200);
                healthBar.setPrefHeight(20);
                healthBar.setTranslateX((BombermanApp.getWidth() - 200) / 2);
                healthBar.setTranslateY(30); // Đặt ở trên cùng
                // Tùy chỉnh màu sắc (tùy chọn)
                healthBar.setStyle("-fx-accent: red;"); // Thanh máu màu đỏ
                getGameScene().addUINode(healthBar);
            } else {
                System.err.println("Boss component not found!");
            }
        } else {
            System.err.println("Boss entity not found!");
        }


    }
}
