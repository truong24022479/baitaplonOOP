package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.example.demo.Bomb.*;

public class Doll extends Enemy {

    private Entity doll;
    private boolean isDead = false;
    private ImageView view;

    private static final double DOLL_SPEED = 15;
    private ImageView[] leftFrames;
    private ImageView[] rightFrames;
    private int frameIndex = 0;
    private double frameTimer = 0;
    private final double FRAME_DURATION = 0.2;

    private double vX = 0;
    private double vY = 0;
    private double moveTargetX;
    private double moveTargetY;
    private boolean isMoving = false;

    @Override
    public void onAdded() {
        doll = getEntity();

        // Thêm hình ảnh mặc định cho Doll
        view = new ImageView(getClass().getResource("/org/example/demo/sprites/doll_dead (1).png").toExternalForm());
        view.setFitWidth(TILE_SIZE);
        view.setFitHeight(TILE_SIZE);
        doll.getViewComponent().addChild(view);

        DollAnimation anim = new DollAnimation();
        anim.initializeImageView();
        leftFrames = new ImageView[]{DollAnimation.left1, DollAnimation.left2, DollAnimation.left3};
        rightFrames = new ImageView[]{DollAnimation.right1, DollAnimation.right2, DollAnimation.right3};

        // Khởi tạo bản đồ
        BombermanApp app = (BombermanApp) FXGL.getApp();
        initMap(app.getMap(), app.TILE_SIZE, app.MAP_WIDTH, app.MAP_HEIGHT);
    }

    @Override
    public void onUpdate(double tpf) {
        if (isDead) {
            return; // Khi đã chết thì không di chuyển nữa
        }

        if (isMoving) {
            doll.translateX(vX * tpf);
            doll.translateY(vY * tpf);

            frameTimer += tpf;
            if (frameTimer >= FRAME_DURATION) {
                frameIndex = (frameIndex + 1) % 2;
                frameTimer = 0;

                if (vX < 0) {
                    view.setImage(leftFrames[frameIndex].getImage());
                } else if (vX > 0) {
                    view.setImage(rightFrames[frameIndex].getImage());
                }
            }

            if (Math.abs(doll.getX() - moveTargetX) < 1 && Math.abs(doll.getY() - moveTargetY) < 1) {
                doll.setPosition(moveTargetX, moveTargetY);
                isMoving = false;
            }
        } else {
            moveDoll();
        }
    }

    private void moveDoll() {
        int tileX = (int) (doll.getX() / TILE_SIZE);
        int tileY = (int) (doll.getY() / TILE_SIZE);

        int direction = (int) (Math.random() * 4);
        int nextX = tileX;
        int nextY = tileY;

        switch (direction) {
            case 0 -> nextY--;
            case 1 -> nextY++;
            case 2 -> nextX--;
            case 3 -> nextX++;
        }

        if (canMove(nextX, nextY)) {
            moveTargetX = nextX * TILE_SIZE;
            moveTargetY = nextY * TILE_SIZE;

            double dx = moveTargetX - doll.getX();
            double dy = moveTargetY - doll.getY();
            double length = Math.sqrt(dx * dx + dy * dy);

            vX = (dx / length) * DOLL_SPEED;
            vY = (dy / length) * DOLL_SPEED;

            isMoving = true;
        }
    }

    // Gọi khi Doll bị tiêu diệt
    public void dollDie() {
        if (isDead) return;

        isDead = true;
        // Thay đổi hình ảnh chết nếu muốn (ví dụ doll_dead.png)
        Image deadImage = new Image(getClass().getResource("/org/example/demo/sprites/doll_dead (1).png").toExternalForm());
        view.setImage(deadImage);

        // Sau 0.5s đặt bomb và remove Doll
//        runOnce(() -> {
//            spawnBombOnDeath();
//            FXGL.getGameWorld().removeEntity(doll);
//        }, javafx.util.Duration.seconds(0.5));

        FXGL.getGameTimer().runOnceAfter(() -> {
            if (getEntity().isActive()) {
                spawnBombOnDeath();
                getEntity().removeFromWorld();
            }
        }, Duration.seconds(0.5));
    }

    private void spawnBombOnDeath() {
        int tileX = (int) Math.round(doll.getX() / TILE_SIZE);
        int tileY = (int) Math.round(doll.getY() / TILE_SIZE);

        Bomb bomb = new Bomb(tileX, tileY, DELAY_BOMB_TIME, explosionRadius, doll, map);
        bombAnimation.showBombAnimation(tileX * TILE_SIZE, tileY * TILE_SIZE);
        bomb.activate(DELAY_BOMB_TIME);
    }
}
