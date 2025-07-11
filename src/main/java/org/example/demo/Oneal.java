package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Oneal extends Enemy {
    private Entity oneal;
    private Entity player;
    public static double ONEAL_SPEED = 30;

    private double vX = 0;
    private double vY = 0;

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
        oneal = getEntity();
        view = new ImageView(getClass().getResource("/org/example/demo/sprites/oneal_dead (1).png").toExternalForm());
        view.setFitWidth(TILE_SIZE);
        view.setFitHeight(TILE_SIZE);
        view.setPreserveRatio(false);
        getEntity().getViewComponent().addChild(view);

        OnealAnimation anim = new OnealAnimation();
        anim.initializeImageView();
        leftFrames = new ImageView[]{OnealAnimation.left1, OnealAnimation.left2, OnealAnimation.left3};
        rightFrames = new ImageView[]{OnealAnimation.right1, OnealAnimation.right2, OnealAnimation.right3};

        player = FXGL.getGameWorld().getSingleton(EntityType.PLAYER);
        BombermanApp app = (BombermanApp) FXGL.getApp();
        initMap(app.getMap(), app.TILE_SIZE, app.MAP_WIDTH, app.MAP_HEIGHT);
    }

    @Override
    public void onUpdate(double tpf) {
        if (isDead) {
            return;
        }

        if (isMoving) {
            oneal.translateX(vX * tpf);
            oneal.translateY(vY * tpf);

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

            if (Math.abs(oneal.getX() - moveTargetX) < 1 && Math.abs(oneal.getY() - moveTargetY) < 1) {
                oneal.setPosition(moveTargetX, moveTargetY); // snap to tile
                isMoving = false;
            }
        } else {
            moveOneal();
        }
    }

    public void moveOneal() {
        double distX = player.getX() - oneal.getX();
        double distY = player.getY() - oneal.getY();
        double distance = Math.sqrt(distX * distX + distY * distY);

        double onealSpeed = (distance < 5 * TILE_SIZE) ? ONEAL_SPEED : ENEMY_SPEED;

        int onealTileX = (int) (oneal.getX() / TILE_SIZE);
        int onealTileY = (int) (oneal.getY() / TILE_SIZE);

        int playerTileX = (int) (player.getX() / TILE_SIZE);
        int playerTileY = (int) (player.getY() / TILE_SIZE);

        int dx = Integer.compare(playerTileX, onealTileX);
        int dy = Integer.compare(playerTileY, onealTileY);

        int nextOnealTileX = onealTileX;
        int nextOnealTileY = onealTileY;
        if (dx != 0 && canMove((onealTileX + dx), onealTileY)) {
            nextOnealTileX = onealTileX + dx;
        } else if (dy != 0 && canMove(onealTileX, (onealTileY + dy))) {
            nextOnealTileY = onealTileY + dy;
        }

        if (canMove(nextOnealTileX, nextOnealTileY)) {
            moveTargetX = nextOnealTileX * TILE_SIZE;
            moveTargetY = nextOnealTileY * TILE_SIZE;

            double length = Math.sqrt((moveTargetX - oneal.getX()) * (moveTargetX - oneal.getX())
                    + (moveTargetY - oneal.getY()) * (moveTargetY - oneal.getY()));
            if (length == 0) return;
            vX = ((moveTargetX - oneal.getX()) / length) * onealSpeed;
            vY = ((moveTargetY - oneal.getY()) / length) * onealSpeed;
            isMoving = true;
        }
    }

    public void onealDie() {
        isMoving = false;
        if (isDead) return;
        isDead = true;

        Image deadImage = new Image(getClass().getResource("/org/example/demo/sprites/oneal_dead (1).png").toExternalForm());
        view.setImage(deadImage);

        FXGL.getGameTimer().runOnceAfter(() -> {
            if (getEntity().isActive()) {
                getEntity().removeFromWorld();
            }
        }, Duration.seconds(0.5));
    }
}