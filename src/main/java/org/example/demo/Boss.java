package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.example.demo.Bomb.*;

public class Boss extends Enemy {
    private Entity boss;
    protected static int x;
    protected static int y;
    protected static int health = 20;

    private ImageView view;
    private ImageView[] leftFrames;
    private ImageView[] rightFrames;
    private boolean useLeftFrames = true;

    private int frameIndex = 0;
    private double frameTimer = 0;
    private final double FRAME_DURATION = 0.5;

    private static final double BOMB_SPAWN = 5;
    private double bombTimer = 0;

    public void onAdded () {
        boss = getEntity();
//        view = new ImageView(getClass().getResource("/org/example/demo/sprites/kondoria_dead.png").toExternalForm());
//        view.setFitHeight(3 * TILE_SIZE);
//        view.setFitWidth(3 * TILE_SIZE);
//        view.setPreserveRatio(true);
//        boss.getViewComponent().addChild(view);

        BossAnimation anim = new BossAnimation();
        anim.initializeImageView();
        leftFrames =new ImageView[]{BossAnimation.left1, BossAnimation.left2, BossAnimation.left3};
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

    public void onUpdate (double tpf) {
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

        if (health == 0) {
            bossDie();
        }
    }

    public void spawnBomb() {
        int x, y;
        Random random = new Random();
        do {
            x = random.nextInt(MAP_HEIGHT);
            y = random.nextInt(MAP_WIDTH);
        } while(BombermanApp.map[x][y] != 0);

        Bomb bomb = new Bomb(x, y, DELAY_BOMB_TIME, boss, map);
        bombAnimation.showBombAnimation(x * TILE_SIZE, y * TILE_SIZE);
        bomb.activate(DELAY_BOMB_TIME);
    }

    public void spawnEnemy() {
        if (health == 15) {
            GameInitializerMap.spawnBalloom(3);
        } else if (health == 10) {
            GameInitializerMap.spawnOneal(3);
        } else if (health == 5) {
            GameInitializerMap.spawnMinvo(2);
        }
    }

    public void bossDie() {
        Image deadImage = new Image(getClass().getResource("/org/example/demo/sprites/kondoria_dead (1).png").toExternalForm());
        view.setImage(deadImage);

        // Cho hiệu ứng tồn tại 0.5s rồi xoá khỏi map
        FXGL.getGameTimer().runOnceAfter(() -> {
            getEntity().removeFromWorld();
        }, Duration.seconds(0.5));
    }
}
