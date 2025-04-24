package org.example.demo;

import com.almasb.fxgl.entity.Entity;
import javafx.scene.image.ImageView;

public class PlayerSpriteManager {
    private Entity player;
    private ImageView playerImageView;
    private String lastDirection = "down";
    private int frameIndex = 0;
    private double frameTimer = 0;
    private final double FRAME_DURATION = 0.2;

    private ImageView[] upFrames;
    private ImageView[] downFrames;
    private ImageView[] leftFrames;
    private ImageView[] rightFrames;

    public PlayerSpriteManager() {
        // Khởi tạo ImageView cho player
        this.playerImageView = new ImageView();

        // Khởi tạo các frame
        upFrames = new ImageView[]{PlayerAnimation.up, PlayerAnimation.up1, PlayerAnimation.up2};
        downFrames = new ImageView[]{PlayerAnimation.down, PlayerAnimation.down1, PlayerAnimation.down2};
        leftFrames = new ImageView[]{PlayerAnimation.left, PlayerAnimation.left1, PlayerAnimation.left2};
        rightFrames = new ImageView[]{PlayerAnimation.right, PlayerAnimation.right1, PlayerAnimation.right2};

        // Log các frame
        logFrames("upFrames", upFrames);
        logFrames("downFrames", downFrames);
        logFrames("leftFrames", leftFrames);
        logFrames("rightFrames", rightFrames);

        // Đặt hình ảnh mặc định
        if (downFrames[0] != null && downFrames[0].getImage() != null) {
            playerImageView.setImage(downFrames[0].getImage());
            System.out.println("Set initial image: " + downFrames[0].getImage().getUrl());
        } else {
            System.out.println("Cannot set initial image: downFrames[0] is null or has no image");
        }
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public ImageView getPlayerImageView() {
        return playerImageView;
    }

    private void logFrames(String name, ImageView[] frames) {
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == null) {
                System.out.println(name + "[" + i + "] is null");
            } else if (frames[i].getImage() == null) {
                System.out.println(name + "[" + i + "] has no image");
            } else {
                System.out.println(name + "[" + i + "] has image: " + frames[i].getImage().getUrl());
            }
        }
    }

    public void updateAnimation(double tpf, boolean upPressed, boolean downPressed, boolean leftPressed, boolean rightPressed) {
        String currentDirection = lastDirection;
        if (upPressed) {
            currentDirection = "up";
        } else if (downPressed) {
            currentDirection = "down";
        } else if (leftPressed) {
            currentDirection = "left";
        } else if (rightPressed) {
            currentDirection = "right";
        }

        if (!upPressed && !downPressed && !leftPressed && !rightPressed) {
            frameIndex = 0;
        } else {
            frameTimer += tpf;
            if (frameTimer >= FRAME_DURATION) {
                frameIndex = (frameIndex + 1) % 3;
                frameTimer = 0;
                System.out.println("Updated frameIndex to: " + frameIndex + " for direction: " + currentDirection);
            }
        }

        if (!currentDirection.equals(lastDirection)) {
            frameIndex = 0;
            lastDirection = currentDirection;
            System.out.println("Changed direction to: " + currentDirection);
        }

        ImageView[] currentFrames = switch (currentDirection) {
            case "up" -> upFrames;
            case "down" -> downFrames;
            case "left" -> leftFrames;
            case "right" -> rightFrames;
            default -> downFrames;
        };

        if (currentFrames[frameIndex] != null && currentFrames[frameIndex].getImage() != null) {
            playerImageView.setImage(currentFrames[frameIndex].getImage());
            System.out.println("Set image for " + currentDirection + " frame " + frameIndex + ": " + currentFrames[frameIndex].getImage().getUrl());
        } else {
            System.out.println("Cannot set image: " + currentDirection + " frame " + frameIndex + " is null or has no image");
        }
    }
}