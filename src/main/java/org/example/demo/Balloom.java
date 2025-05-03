package org.example.demo;
/**
 * PHONG
 */

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.image.Image;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

public class Balloom extends Enemy {
    private double vX = 0;
    private double vY = 0;

    private Entity balloom;
    private Random random = new Random();

    private double moveTargetX;
    private double moveTargetY;

    private ImageView view;
    private ImageView[] leftFrames;
    private ImageView[] rightFrames;
    private int frameIndex = 0;
    private double frameTimer = 0;
    private final double FRAME_DURATION = 0.2;

    @Override
    public void onAdded() {
        balloom = getEntity();
        view = new ImageView (getClass().getResource("/org/example/demo/sprites/balloom_dead (1).png").toExternalForm());
        // hoặc image mặc định
        view.setFitWidth(TILE_SIZE);
        view.setFitHeight(TILE_SIZE);
        view.setPreserveRatio(false); // hoặc true nếu cần
        getEntity().getViewComponent().addChild(view); // rất quan trọng!

        BalloomAnimation anim = new BalloomAnimation();
        anim.initializeImageView();
        leftFrames = new ImageView[]{BalloomAnimation.left1, BalloomAnimation.left2, BalloomAnimation.left3};
        rightFrames = new ImageView[]{BalloomAnimation.right1, BalloomAnimation.right2, BalloomAnimation.right3};

        BombermanApp app = (BombermanApp) FXGL.getApp();
        if (app instanceof BombermanApp) {
            BombermanApp bom = (BombermanApp) app;
            this.initMap(bom.getMap(), bom.TILE_SIZE, bom.MAP_WIDTH, bom.MAP_HEIGHT);
            moveBalloom();
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (isDead) {
            return;
        }

        if (isMoving) {
            int targetTileX = (int) (moveTargetX / TILE_SIZE);
            int targetTileY = (int) (moveTargetY / TILE_SIZE);
            boolean targetStillValid = canMove(targetTileX, targetTileY);

            if (!targetStillValid) {
                // Dừng di chuyển nếu ô mục tiêu không hợp lệ (có bom, tường, v.v.)
                isMoving = false;
                vX = 0;
                vY = 0;
                // Đặt lại vị trí vào ô hiện tại
                balloom.setPosition(Math.round(balloom.getX() / TILE_SIZE) * TILE_SIZE,
                        Math.round(balloom.getY() / TILE_SIZE) * TILE_SIZE);
                return;
            }

            int nextTileX = (int) ((balloom.getX() + vX * tpf) / TILE_SIZE);
            int nextTileY = (int) ((balloom.getY() + vY * tpf) / TILE_SIZE);
            if (!canMove(nextTileX, nextTileY)) {
                // Dừng di chuyển nếu ô tiếp theo có bom hoặc chướng ngại
                isMoving = false;
                vX = 0;
                vY = 0;
                balloom.setPosition(Math.round(balloom.getX() / TILE_SIZE) * TILE_SIZE,
                        Math.round(balloom.getY() / TILE_SIZE) * TILE_SIZE);
                return;
            }

            // Tiếp tục di chuyển nếu mọi thứ hợp lệ
            balloom.translateX(vX * tpf);
            balloom.translateY(vY * tpf);

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

            if (Math.abs(balloom.getX() - moveTargetX) < 1 && Math.abs(balloom.getY() - moveTargetY) < 1) {
                balloom.setPosition(moveTargetX, moveTargetY); // snap to tile
                isMoving = false;
            }
        } else {
            moveBalloom();
        }
    }

    public void moveBalloom() {

        int tileX = (int) (balloom.getX() / TILE_SIZE);
        int tileY = (int) (balloom.getY() / TILE_SIZE);

        int direction = random.nextInt(4);
        int nextX = tileX;
        int nextY = tileY;

        switch (direction) {
            case 0:
                nextY--;
                break;// lên
            case 1:
                nextY++;
                break;// xuống
            case 2:
                nextX--;
                break;// trái
            case 3:
                nextX++;
                break;// phải
        }

        if (canMove(nextX, nextY)) {
            moveTargetX = nextX * TILE_SIZE;
            moveTargetY = nextY * TILE_SIZE;

            double dx = moveTargetX - balloom.getX();
            double dy = moveTargetY - balloom.getY();
            double length = Math.sqrt(dx * dx + dy * dy);

            vX = (dx / length) * ENEMY_SPEED;
            vY = (dy / length) * ENEMY_SPEED;

            isMoving = true;
        }
    }


    public void balloomDie() {
        isMoving = false; // dừng di chuyển
        if (isDead) return;
        isDead = true;

        // Cập nhật hình ảnh thành balloom_dead.png
        Image deadImage = new Image(getClass().getResource("/org/example/demo/sprites/balloom_dead (1).png").toExternalForm());
        view.setImage(deadImage);

        // Cho hiệu ứng tồn tại 0.5s rồi xoá khỏi map
        FXGL.getGameTimer().runOnceAfter(() -> {
            getEntity().removeFromWorld();
        }, Duration.seconds(0.5));

//        Timeline fadeOut = new Timeline();
//        fadeOut.getKeyFrames().add(
//                new KeyFrame(Duration.seconds(0.5),
//                        event -> {
//                            view.setOpacity(0); // Giảm opacity về 0
//                            if (getEntity().isActive()) {
//                                getEntity().removeFromWorld();
//                            }
//                        },
//                        new javafx.animation.KeyValue(view.opacityProperty(), 0)
//                )
//        );
//        fadeOut.play();
    }
}

